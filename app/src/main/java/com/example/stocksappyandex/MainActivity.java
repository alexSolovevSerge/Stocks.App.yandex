package com.example.stocksappyandex;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.stocksappyandex.Data.CompaniesAdapter;
import com.example.stocksappyandex.Data.Company;
import com.example.stocksappyandex.Data.MainViewModel;
import com.example.stocksappyandex.Fragments.MainFragment;
import com.example.stocksappyandex.Fragments.SelectedCompanyFragment;
import com.example.stocksappyandex.Utils.JSONUtils;
import com.example.stocksappyandex.Utils.NetworkUtils;
import com.example.stocksappyandex.Utils.WebSoketUtils;
import com.github.mikephil.charting.data.CandleEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

//Главное активити программы. Содержит вью пейджер для двух фрагментов: Main Fragment и Selected Company Fragment

public class MainActivity extends AppCompatActivity {



    public static Handler handler = new Handler();

    public static SectionsStagePagerAdapter adapter;

    public static ViewPager2 viewPager;

    public static CompaniesAdapter adapterStock;
    public static CompaniesAdapter adapterFavourites;
    public static MainViewModel viewModel;

    public static List<Company> listStock = new ArrayList<>();
    public static List<Company> listFavourites = new ArrayList<>();
    public static List<CandleEntry> candleEntries = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        viewPager = findViewById(R.id.container);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        NetworkUtils.GetArray getArray = new NetworkUtils.GetArray(this,this);
        adapterStock = new CompaniesAdapter(listStock);
        adapterFavourites = new CompaniesAdapter(listFavourites);
        getDataStock(this,viewModel.getCompanies());
        getDataFavourite(viewModel.getFavouriteCompanys());
        new WebSoketUtils().getTickerPrice(listStock);
        try {
            if(viewModel.getCompaniesCount()>0){}
            else{
                getArray.getArr();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter = new SectionsStagePagerAdapter(getSupportFragmentManager(),getLifecycle());
        adapter.addFragment(new MainFragment());
        adapter.createFragment(0);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);



        adapterStock.setOnCompanyClickListener(new CompaniesAdapter.OnCompanyClickListener() {
            @Override
            public void onNoteClick(int position) {
                setSelectedFragmentStock(position);

            }
        });
        adapterFavourites.setOnCompanyClickListener(new CompaniesAdapter.OnCompanyClickListener() {
            @Override
            public void onNoteClick(int position) {
                viewPager.setCurrentItem(0);
                setSelectedFragmentFavourites(position);

            }
        });


    }

    private void setupViewPager(ViewPager viewPager){}

    private void setSelectedFragmentStock(int position){
        MainFragment.selectedCompany = adapterStock.getCompany(position);
        MainActivity.adapter.addFragment(new SelectedCompanyFragment());
        MainActivity.adapter.createFragment(1);
        MainActivity.adapter.notifyDataSetChanged();
        MainActivity.viewPager.setCurrentItem(1);
    }

    private void setSelectedFragmentFavourites(int position){
        MainFragment.selectedCompany = adapterFavourites.getCompany(position);
        MainActivity.adapter.addFragment(new SelectedCompanyFragment());
        MainActivity.adapter.createFragment(1);
        MainActivity.adapter.notifyDataSetChanged();
        MainActivity.viewPager.setCurrentItem(1);
    }

    public static boolean getDataStock(LifecycleOwner owner,LiveData<List<Company>> data ){
        LiveData<List<Company>> companiesFromDB = data;
        List<Company> comp = new ArrayList<>();
        companiesFromDB.observe(owner, new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesFromLiveData) {
                listStock.clear();
                listStock.addAll(companiesFromLiveData);
                adapterStock.notifyDataSetChanged();
            }
        });
        if(comp.size()>0){
            return true;
        }
        else{
            return false;
        }
    }
    private boolean getDataFavourite(LiveData<List<Company>> data ){
        LiveData<List<Company>> companiesFromDB = data;
        List<Company> comp = new ArrayList<>();
        companiesFromDB.observe(this,new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesFromLiveData) {
                listFavourites.clear();
                listFavourites.addAll(companiesFromLiveData);
                adapterFavourites.notifyDataSetChanged();
                adapterStock.notifyDataSetChanged();
            }
        });
        if(comp.size()>0){
            return true;
        }
        else{
            return false;
        }
    }

    public static void backButtonOnSearch(){

    }

}