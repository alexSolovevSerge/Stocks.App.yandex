package com.example.stocksappyandex.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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

import javax.security.auth.login.LoginException;

import static androidx.core.content.ContextCompat.getSystemService;

public class RecyclerViewStocksFragment extends Fragment {

    public static List<Company> list = new ArrayList<>();

    private static int fistVisibleView;


    public static RecyclerView recyclerViewCompanies;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_view_stocks,container,false);
        recyclerViewCompanies = view.findViewById(R.id.recycleViewStocks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewCompanies.setLayoutManager(layoutManager);
        recyclerViewCompanies.setAdapter(MainActivity.adapterStock);

        TransitionManager.beginDelayedTransition(MainFragment.root);
        int viewIdFirst = layoutManager.findFirstVisibleItemPosition();
        recyclerViewCompanies.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int viewIdSecond = layoutManager.findFirstVisibleItemPosition();
                if(viewIdSecond<3){
                    MainActivity.startScrollAnimation.applyTo(MainFragment.root);
                }else if(viewIdSecond>3) {
                    MainActivity.endScrollAnimation.applyTo(MainFragment.root);
                }
            }
        });

    return view;


    }
}
