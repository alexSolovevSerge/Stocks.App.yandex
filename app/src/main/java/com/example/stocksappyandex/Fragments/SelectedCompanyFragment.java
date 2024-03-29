package com.example.stocksappyandex.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.stocksappyandex.Data.Company;
import com.example.stocksappyandex.MainActivity;
import com.example.stocksappyandex.R;
import com.example.stocksappyandex.SectionsStagePagerAdapter;
import com.example.stocksappyandex.Utils.JSONUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

public class SelectedCompanyFragment extends Fragment implements View.OnClickListener {
    public static TextView textViewCompanyTitle;
    public static ImageView imageViewFavourite;
    private ViewPager2 viewPagerInfo;
    private Button buttonBack;

    private SectionsStagePagerAdapter sectionsStagePagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chosenlayout,container,false);

        viewPagerInfo = view.findViewById(R.id.containerSelectedInfo);

        sectionsStagePagerAdapter = new SectionsStagePagerAdapter(getFragmentManager(),getLifecycle());



        setupViewPager(viewPagerInfo);
        Log.i("TAG",MainActivity.listStock.size()+"");


        List<String> names = new ArrayList<>();
        names.add("Chart");
        names.add("Summary");
        TabLayout tabLayout = view.findViewById(R.id.tabLayoutInfo);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        new TabLayoutMediator(tabLayout, viewPagerInfo, false, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(names.get(position));
            }
        }).attach();


        textViewCompanyTitle = view.findViewById(R.id.textViewCompanyName);
        imageViewFavourite = view.findViewById(R.id.imageViewFavouriteChoice);
        buttonBack = view.findViewById(R.id.buttonBack);

        imageViewFavourite.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        if(MainFragment.selectedCompany!=null){
            textViewCompanyTitle.setText(MainFragment.selectedCompany.getName());
            boolean fav = MainFragment.selectedCompany.isFavourite();
            if(fav){
                imageViewFavourite.setImageResource(R.drawable.favourite);
            }
            else{
                imageViewFavourite.setImageResource(R.drawable.nonfavourite);
            }
            if(MainFragment.selectedCompany.getSummary().length()<2){
                JSONUtils.GetListCompanyObj.getSummary(MainFragment.selectedCompany);
            }
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                destroySelectedCompanyFragment();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(callback);



        return view;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonBack:
                destroySelectedCompanyFragment();
                break;
            case R.id.imageViewFavouriteChoice:
                favoutiteSetterButton();
                break;
        }

    }

    private void destroySelectedCompanyFragment(){
       MainActivity.viewPager.setCurrentItem(0);
       MainActivity.adapter.removeFragment(1);
       MainActivity.viewPager.getAdapter().notifyDataSetChanged();
       MainFragment.selectedCompany=null;
    }
    private void favoutiteSetterButton(){
        if(MainFragment.selectedCompany.isFavourite()){
            MainFragment.selectedCompany.setFavourite(false);
            MainActivity.viewModel.updateCompany(MainFragment.selectedCompany);
            imageViewFavourite.setImageResource(R.drawable.nonfavourite);

        }else{
            MainFragment.selectedCompany.setFavourite(true);
            MainActivity.viewModel.updateCompany(MainFragment.selectedCompany);
            imageViewFavourite.setImageResource(R.drawable.favourite);


        }
    }

    private void setupViewPager(ViewPager2 viewPager){
        SectionsStagePagerAdapter adapter = new SectionsStagePagerAdapter(getFragmentManager(),getLifecycle());
        adapter.addFragment(new GraphFragment());
        adapter.addFragment(new DescriptionFragment());
        adapter.createFragment(0);
        adapter.createFragment(1);




        viewPager.setAdapter(adapter);

    }

}
