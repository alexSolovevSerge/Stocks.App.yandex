package com.example.stocksappyandex.Fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocksappyandex.MainActivity;
import com.example.stocksappyandex.R;
import com.example.stocksappyandex.Utils.JSONUtils;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;


public class GraphFragment extends Fragment {


    public static RecyclerView recyclerViewFavouriteCompanies;
    private static CandleStickChart candleStickChart;
    private TextView textViewPrice;
    private Spinner spinnerrange;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selected_company_graph,container,false);

        textViewPrice = view.findViewById(R.id.textViewPrice);
        textViewPrice.setText("$" + MainFragment.selectedCompany.getCurrentprice());

        spinnerrange = view.findViewById(R.id.spinnerrange);

        candleStickChart = view.findViewById(R.id.candleStickChart);
        candleStickChart.setHighlightPerDragEnabled(true);

        candleStickChart.setDrawBorders(true);
        candleStickChart.setBorderColor(Color.GRAY);
        YAxis yAxis = candleStickChart.getAxisLeft();
        YAxis rightAxis = candleStickChart.getAxisRight();
        yAxis.setDrawGridLines(false);
        rightAxis.setDrawGridLines(false);
        candleStickChart.requestDisallowInterceptTouchEvent(true);
        XAxis xAxis = candleStickChart.getXAxis();

        xAxis.setDrawGridLines(true);// disable x axis grid lines
        xAxis.setDrawLabels(false);
        rightAxis.setTextColor(Color.WHITE);
        yAxis.setDrawLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);

        Legend l = candleStickChart.getLegend();
        l.setEnabled(false);

        JSONUtils.GetListCompanyObj.getChart(spinnerrange.getSelectedItem().toString(),getContext());

        spinnerrange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JSONUtils.GetListCompanyObj.getChart(spinnerrange.getSelectedItem().toString(),getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        return view;
    }
    public static void setData(){
        CandleDataSet set1 = new CandleDataSet(MainActivity.candleEntries, "DataSet 1");
        set1.setColor(Color.rgb(80, 80, 80));
        set1.setShadowColor(Color.parseColor("#8A847F"));
        set1.setShadowWidth(0.8f);
        set1.setDecreasingColor(Color.parseColor("#783535"));
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(Color.parseColor("#2B602B"));
        set1.setIncreasingPaintStyle(Paint.Style.FILL);
        set1.setNeutralColor(Color.LTGRAY);
        set1.setDrawValues(false);


        CandleData data = new CandleData(set1);

        candleStickChart.setData(data);
        candleStickChart.invalidate();
    }
}
