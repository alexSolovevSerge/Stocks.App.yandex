package com.example.stocksappyandex.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocksappyandex.Data.CompaniesAdapter;
import com.example.stocksappyandex.Data.Company;
import com.example.stocksappyandex.MainActivity;
import com.example.stocksappyandex.R;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

import static com.example.stocksappyandex.Fragments.RecyclerViewStocksFragment.viewModel;

public class RecyclerViewFaouriteFragment extends Fragment {


    public static List<Company> listFavourites = new ArrayList<>();
    public static CompaniesAdapter adapter;
    public static RecyclerView recyclerViewFavouriteCompanies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_view_favourite,container,false);

        recyclerViewFavouriteCompanies = view.findViewById(R.id.recycleViewFavourite);

        adapter = new CompaniesAdapter(listFavourites);


        recyclerViewFavouriteCompanies.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewFavouriteCompanies.setAdapter(adapter);

        getData(viewModel.getFavouriteCompanys());

        adapter.setOnCompanyClickListener(new CompaniesAdapter.OnCompanyClickListener() {
            @Override
            public void onNoteClick(int position) {
                setSelectedFragment(position);
            }
        });

        return view;


    }
    private void setSelectedFragment(int position){
        MainFragment.selectedCompany = adapter.getCompany(position);
        MainActivity.adapter.addFragment(new SelectedCompanyFragment());
        MainActivity.adapter.createFragment(1);
        MainActivity.adapter.notifyDataSetChanged();
        MainActivity.viewPager.setCurrentItem(1);
    }

    private boolean getData(LiveData<List<Company>> data ){
        LiveData<List<Company>> companiesFromDB = data;
        List<Company> comp = new ArrayList<>();
        companiesFromDB.observe(this,new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesFromLiveData) {
                listFavourites.clear();
                listFavourites.addAll(companiesFromLiveData);
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
}
