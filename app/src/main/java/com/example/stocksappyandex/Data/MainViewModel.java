package com.example.stocksappyandex.Data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {

    private static int count;

    private static CompaniesDatabase database;

    private LiveData<List<Company>> companies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = CompaniesDatabase.getInstance(getApplication());
        companies = database.companiesDao().getAllCompanies();
    }


    public LiveData<List<Company>> getCompanies(){
        return companies;
    }


    public LiveData<List<Company>> getFavouriteCompanys(){
        return database.companiesDao().getFavouriteCompanies();
    }

    public LiveData<List<Company>> getSearchedCompanys(String search){
        return database.companiesDao().getSearchedCompanies(search);
    }

    public void updateCompany(Company company){
        new CompanyUpdater().execute(company);
    }

    public int getCompaniesCount() throws ExecutionException, InterruptedException {
        return new CountCompaniesGetter().execute().get();
    }
    public void deleteAllCompanies(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                database.companiesDao().deleteAllcompanies();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public synchronized void insertCompany(Company company){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                database.companiesDao().insertCompany(company);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public Company getCompanyId(int id){
        return database.companiesDao().getCompanyById(id);
    }
    public Company getCompanyByTicker(String ticker){
        return database.companiesDao().getCompanyByTicker(ticker);
    }
    public void setCompanies(List<Company> companies){
        deleteAllCompanies();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                database.companiesDao().insertList(companies);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static class CountCompaniesGetter extends AsyncTask<Void,Void,Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {
            return database.companiesDao().countAllCompanies();
        }
    }

    public static class CompanyUpdater extends AsyncTask<Company,Void,Void>{


        @Override
        protected Void doInBackground(Company... companies) {
            database.companiesDao().updateCompany(companies[0]);
            return null;
        }
    }
}