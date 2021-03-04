package com.example.stocksappyandex.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocksappyandex.Data.CompaniesAdapter;
import com.example.stocksappyandex.Data.Company;
import com.example.stocksappyandex.Data.MainViewModel;
import com.example.stocksappyandex.MainActivity;
import com.example.stocksappyandex.R;
import com.example.stocksappyandex.SectionsStagePagerAdapter;
import com.example.stocksappyandex.Utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static androidx.core.content.ContextCompat.getSystemService;

public class RecyclerViewStocksFragment extends Fragment {

    public static List<Company> list = new ArrayList<>();
    public static Handler handler = new Handler();

    public static CompaniesAdapter adapter;
    public static RecyclerView recyclerViewCompanies;
    public static MainViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_view_stocks,container,false);

        recyclerViewCompanies = view.findViewById(R.id.recycleViewStocks);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);


        NetworkUtils.GetArray getArray = new NetworkUtils.GetArray(getContext(),getActivity());


        adapter = new CompaniesAdapter(list);


        recyclerViewCompanies.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewCompanies.setAdapter(adapter);

        getData(this,viewModel.getCompanies());


        try {
            if(viewModel.getCompaniesCount()>0){


            }else{
                getArray.getArr();

            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        adapter.setOnCompanyClickListener(new CompaniesAdapter.OnCompanyClickListener() {
            @Override
            public void onNoteClick(int position) {
               setSelectedFragment(position);

            }
        });


    return view;


    }
    public static boolean getData(LifecycleOwner owner,LiveData<List<Company>> data ){
        LiveData<List<Company>> companiesFromDB = data;
        List<Company> comp = new ArrayList<>();
        companiesFromDB.observe(owner, new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesFromLiveData) {
                list.clear();
                list.addAll(companiesFromLiveData);
                adapter.notifyDataSetChanged();
            }
        });
        if(comp.size()>0){
            return true;
        }
        else{
            return false;
        }
    }
    private void setSelectedFragment(int position){
        MainFragment.selectedCompany = adapter.getCompany(position);
        MainActivity.adapter.addFragment(new SelectedCompanyFragment());
        MainActivity.adapter.createFragment(1);
        MainActivity.adapter.notifyDataSetChanged();
        MainActivity.viewPager.setCurrentItem(1);
    }
}
