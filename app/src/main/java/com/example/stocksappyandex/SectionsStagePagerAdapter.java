package com.example.stocksappyandex;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsStagePagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentManager mFragmentManager;

    public SectionsStagePagerAdapter(FragmentManager fm, Lifecycle lc){
        super(fm,lc);
        mFragmentManager = fm;
    }

    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);
    }
    public Fragment getFragment(int position){
        return mFragmentList.get(position);
    }
    public void removeFragment(int position){
        mFragmentList.remove(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }
}
