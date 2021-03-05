package com.example.stocksappyandex.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.stocksappyandex.MainActivity;
import com.example.stocksappyandex.R;



public class RecyclerViewFaouriteFragment extends Fragment {

    public static RecyclerView recyclerViewFavouriteCompanies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_view_favourite,container,false);
        recyclerViewFavouriteCompanies = view.findViewById(R.id.recycleViewFavourite);
        recyclerViewFavouriteCompanies.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFavouriteCompanies.setAdapter(MainActivity.adapterFavourites);
        return view;
    }
}
