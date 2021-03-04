package com.example.stocksappyandex;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.stocksappyandex.Fragments.MainFragment;

//Главное активити программы. Содержит вью пейджер для двух фрагментов: Main Fragment и Selected Company Fragment

public class MainActivity extends AppCompatActivity {

    public static SectionsStagePagerAdapter adapter;

    public static ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        viewPager = findViewById(R.id.container);


        adapter = new SectionsStagePagerAdapter(getSupportFragmentManager(),getLifecycle());
        adapter.addFragment(new MainFragment());
        adapter.createFragment(0);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);

    }

    private void setupViewPager(ViewPager viewPager){






    }
}