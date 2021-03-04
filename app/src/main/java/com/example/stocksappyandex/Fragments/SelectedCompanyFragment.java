package com.example.stocksappyandex.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stocksappyandex.Data.Company;
import com.example.stocksappyandex.MainActivity;
import com.example.stocksappyandex.R;

public class SelectedCompanyFragment extends Fragment implements View.OnClickListener {
    public static TextView textViewCompanyTitle;
    public static ImageView imageViewFavourite;
    private Button buttonBack;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chosenlayout,container,false);

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
        }

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
    }
    private void favoutiteSetterButton(){
        if(MainFragment.selectedCompany.isFavourite()){
            MainFragment.selectedCompany.setFavourite(false);
            RecyclerViewStocksFragment.viewModel.updateCompany(MainFragment.selectedCompany);
            imageViewFavourite.setImageResource(R.drawable.nonfavourite);
            RecyclerViewStocksFragment.adapter.notifyDataSetChanged();
        }else{
            MainFragment.selectedCompany.setFavourite(true);
            RecyclerViewStocksFragment.viewModel.updateCompany(MainFragment.selectedCompany);
            imageViewFavourite.setImageResource(R.drawable.favourite);
            RecyclerViewStocksFragment.adapter.notifyDataSetChanged();
        }
    }
}
