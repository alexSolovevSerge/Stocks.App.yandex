package com.example.stocksappyandex.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.stocksappyandex.Data.Company;
import com.example.stocksappyandex.MainActivity;
import com.example.stocksappyandex.R;
import com.example.stocksappyandex.SectionsStagePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

//Главный фрагмент программы. С поиском и двумя списками тикеров.

public class MainFragment extends Fragment {

    public static int pos = 3;

    private SectionsStagePagerAdapter sectionsStagePagerAdapter;

    private ViewPager2 viewPager;

    public static Company selectedCompany;

    public static EditText editTextSearch;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_page_layout,container,false);

        editTextSearch = view.findViewById(R.id.editTextTextPersonName);


        sectionsStagePagerAdapter = new SectionsStagePagerAdapter(getFragmentManager(),getLifecycle());


        viewPager = view.findViewById(R.id.containerStocksFav);

        setupViewPager(viewPager);
        List<String> names = new ArrayList<>();
        names.add("Stocks");
        names.add("Favourites");

        TabLayout tabLayout = view.findViewById(R.id.tabLayoutStockAndFavourite);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        new TabLayoutMediator(tabLayout, viewPager, false, true, new TabLayoutMediator.TabConfigurationStrategy() {

            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(names.get(position));


            }
        }).attach();


        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String search = "%" + s.toString() + "%";
                MainActivity.getDataStock(getViewLifecycleOwner(),MainActivity.viewModel.getSearchedCompanys(search));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = "%" + s.toString() + "%";
                MainActivity.getDataStock(getViewLifecycleOwner(),MainActivity.viewModel.getSearchedCompanys(search));

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search = "%" + s.toString() + "%";
                MainActivity.getDataStock(getViewLifecycleOwner(),MainActivity.viewModel.getSearchedCompanys(search));
            }
        });

        return view;


    }

    private void setupViewPager(ViewPager2 viewPager){
        SectionsStagePagerAdapter adapter = new SectionsStagePagerAdapter(getFragmentManager(),getLifecycle());
        adapter.addFragment(new RecyclerViewStocksFragment());
        adapter.addFragment(new RecyclerViewFaouriteFragment());
        adapter.createFragment(0);
        adapter.createFragment(1);


        viewPager.setAdapter(adapter);

    }
}
