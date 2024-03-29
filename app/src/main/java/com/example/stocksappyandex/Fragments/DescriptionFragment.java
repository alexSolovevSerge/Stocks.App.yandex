package com.example.stocksappyandex.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocksappyandex.R;


public class DescriptionFragment extends Fragment {


    public static RecyclerView recyclerViewFavouriteCompanies;

    public static TextView textViewDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selected_company_description,container,false);

        textViewDescription = view.findViewById(R.id.textViewDescription);
        textViewDescription.setText(MainFragment.selectedCompany.getSummary());

        return view;
    }
}
