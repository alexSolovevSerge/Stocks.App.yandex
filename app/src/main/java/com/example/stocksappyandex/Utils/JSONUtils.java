package com.example.stocksappyandex.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;


import com.example.stocksappyandex.Data.CompaniesDatabase;
import com.example.stocksappyandex.Data.Company;
import com.example.stocksappyandex.Fragments.GraphFragment;
import com.example.stocksappyandex.Fragments.MainFragment;
import com.example.stocksappyandex.Fragments.RecyclerViewStocksFragment;
import com.example.stocksappyandex.MainActivity;
import com.github.mikephil.charting.data.CandleEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.security.auth.login.LoginException;

import static com.example.stocksappyandex.MainActivity.handler;
import static java.lang.Thread.sleep;

public class JSONUtils {

    private static final String KEY = "c0nq0h748v6u2iq11630";


    public static class GetListCompanyObj {

        private static Context context;
        private Activity activity;

        public static Thread thread;
        public static Thread thread2;

        public GetListCompanyObj(Context context,Activity activity) {

            this.activity = activity;
            this.context = context;
        }

        //Загрузка информации о тикерах с API

        public void getList(String ticker) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Log.i("thread", Thread.currentThread().getName());
                    String urltt = "https://finnhub.io/api/v1/stock/profile2?symbol=%s&token=%s";
                    List<Company> list = new ArrayList<>();
                    URL url = null;
                    HttpsURLConnection urlConnection = null;
                    StringBuilder builder = new StringBuilder();
                    URL url1 = null;
                    HttpsURLConnection urlConnection1 = null;
                    Bitmap bitmap = null;

                    try {
                        url = new URL(String.format(urltt, ticker, KEY));
                        urlConnection = (HttpsURLConnection) url.openConnection();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                        reader.close();
                        JSONObject jsonObject = new JSONObject(builder.toString());
                        builder.setLength(0);
                        String ticker = jsonObject.getString("ticker");
                        String name = jsonObject.getString("name");
                        boolean favourite = false;
                        String logopath;
                        if(ticker.equals("GS")){
                            logopath = "https://www.goldmansachs.com/";
                        }else {
                            logopath = jsonObject.getString("weburl");
                        }
                        URL urlogo = new URL(logopath);
                        logopath = urlogo.getHost();
                        long currentprice = 0;
                        long deltaprice = 0;

                        url = new URL(String.format("https://logo.clearbit.com/%s",logopath).replace("www.",""));
                        urlConnection1 = (HttpsURLConnection) url.openConnection();
                        bitmap = BitmapFactory.decodeStream(urlConnection1.getInputStream());
                        String bitmapencode = BitMapToString(bitmap);
                        Company company =new Company(ticker, name, favourite, logopath, currentprice, deltaprice,bitmapencode,"","");
                        getPrice(company);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                        if(urlConnection1!=null){
                            urlConnection1.disconnect();
                        }
                    }


                }
            };
            thread = new Thread(runnable);
            thread.start();
        }
        public String BitMapToString(Bitmap bitmap) {
            if(bitmap!=null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();
                String temp = Base64.encodeToString(b, Base64.DEFAULT);
                return temp;
            }else{
                return null;
            }
        }

        // Загрузка цен на акции с API

        private CompaniesDatabase database;
        public  void getPrice(Company company) {


            String urlttt = "https://finnhub.io/api/v1/quote?symbol=%s&token=%s";
            Runnable runnable1 = new Runnable() {
                @Override
                public void run() {
                    Log.i("thread", Thread.currentThread().getName());
                    StringBuilder builder = new StringBuilder();
                    URL url = null;
                    HttpsURLConnection urlConnection = null;
                    try {
                        String uri = String.format(urlttt, company.getTicker(), KEY);
                        url = new URL(uri);
                        urlConnection = (HttpsURLConnection) url.openConnection();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                        reader.close();
                        JSONObject jsonObject = new JSONObject(builder.toString());
                        builder.setLength(0);
                        Double currentPrice = jsonObject.getDouble("c");
                        Double lastdayPrice = jsonObject.getDouble("pc");;
                        company.setCurrentprice(currentPrice);
                        company.setDeltaprice(currentPrice - lastdayPrice);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.adapterStock.setNewCompany(company);
                            }
                        });
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }

                }
            };
            thread2 = new Thread(runnable1);
            thread2.start();
        }


        //Загрузка данных для чарта с API

        public static String day = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=%s&interval=5min&apikey=8CQ70Y2Z0I959N5C";
        public static String days = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=%s&apikey=8CQ70Y2Z0I959N5C";
        public static String year = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol=%s&apikey=8CQ70Y2Z0I959N5C";

        public static void getChart(String range, Context context){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String[] rangeIn = {"Day","Week","Month","Year","All"};
                    if(range.equals(rangeIn[0])){
                        String uri = String.format(day, MainFragment.selectedCompany.getTicker());
                        try {
                            StringBuilder builder = jsonConnect(uri);
                            JSONObject jsonObject = new JSONObject(builder.toString());
                            JSONObject jsonArray = jsonObject.getJSONObject("Time Series (5min)");
                            Iterator x = jsonArray.keys();
                            List<JSONObject> results = new ArrayList<>();
                            while (x.hasNext()){
                                results.add((JSONObject)jsonArray.get(x.next().toString()));
                            }
                            setListEntries(results,null);
                            setData();
                        } catch (JSONException e) {
                            e.printStackTrace();

                            connectionTimaOutAction(rangeIn[0]);
                        }catch (NullPointerException e){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context,"Не удалось подключиться",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }else if(range.equals(rangeIn[1])){
                        String uri = String.format(days, MainFragment.selectedCompany.getTicker());
                        try {
                            StringBuilder builder = jsonConnect(uri);
                            JSONObject jsonObject = new JSONObject(builder.toString());
                            JSONObject jsonArray = jsonObject.getJSONObject("Time Series (Daily)");
                            Iterator x = jsonArray.keys();
                            List<JSONObject> results = new ArrayList<>();
                            while (x.hasNext()){
                                results.add((JSONObject)jsonArray.get(x.next().toString()));
                            }
                            setListEntries(results,7);
                            setData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            connectionTimaOutAction(rangeIn[1]);
                        }
                    }else if(range.equals(rangeIn[2])){
                        String uri = String.format(days, MainFragment.selectedCompany.getTicker());
                        try {
                            StringBuilder builder = jsonConnect(uri);
                            JSONObject jsonObject = new JSONObject(builder.toString());
                            JSONObject jsonArray = jsonObject.getJSONObject("Time Series (Daily)");
                            Iterator x = jsonArray.keys();
                            List<JSONObject> results = new ArrayList<>();
                            while (x.hasNext()){
                                results.add((JSONObject)jsonArray.get(x.next().toString()));
                            }
                            setListEntries(results,30);
                            setData();
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }else if(range.equals(rangeIn[3])) {
                        String uri = String.format(year, MainFragment.selectedCompany.getTicker());
                        try {
                            StringBuilder builder = jsonConnect(uri);
                            JSONObject jsonObject = new JSONObject(builder.toString());
                            JSONObject jsonArray = jsonObject.getJSONObject("Monthly Time Series");
                            Iterator x = jsonArray.keys();
                            List<JSONObject> results = new ArrayList<>();
                            while (x.hasNext()) {
                                results.add((JSONObject) jsonArray.get(x.next().toString()));
                            }
                            setListEntries(results, 12);

                            setData();
                        } catch (JSONException e) {

                            e.printStackTrace();
                            connectionTimaOutAction(rangeIn[3]);
                        }
                    }else if(range.equals(rangeIn[4])){
                        String uri = String.format(year, MainFragment.selectedCompany.getTicker());
                        try {
                            StringBuilder builder = jsonConnect(uri);
                            JSONObject jsonObject = new JSONObject(builder.toString());
                            JSONObject jsonArray = jsonObject.getJSONObject("Monthly Time Series");
                            Iterator x = jsonArray.keys();
                            List<JSONObject> results = new ArrayList<>();
                            while (x.hasNext()){
                                results.add((JSONObject)jsonArray.get(x.next().toString()));
                            }
                            setListEntries(results,null);
                            setData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            connectionTimaOutAction(rangeIn[4]);
                        }
                    }
                }
            }).start();
        }
    }


    //Действие при слишком частом запросе к API(TimeOutError)

    private static void connectionTimaOutAction(String range){
        try {
            sleep(2000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        if(MainActivity.adapter.getItemCount()==2&&GraphFragment.spinnerrange!=null&&GraphFragment.spinnerrange.getSelectedItem().toString().equals(range)){
            JSONUtils.GetListCompanyObj.getChart(GraphFragment.spinnerrange.getSelectedItem().toString(), GetListCompanyObj.context);
        }
    }

    //Загрузка JSON для Chart

    public static StringBuilder jsonConnect(String uri){
        URL url = null;
        HttpsURLConnection urlConnection = null;
        StringBuilder builder = new StringBuilder();
        try {
            url = new URL(uri);
            urlConnection = (HttpsURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            return builder;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
        }
        return null;
    }

    //Присваивание значений в Chart

    private static void setData(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                GraphFragment.setData();
            }
        });
    }

    //Заполнение List для Chart

    private static void setListEntries(List<JSONObject> results,Integer countOfValues) throws JSONException {
        int i = 0;
        int i2 = 0;
        if(countOfValues==null){
            i2 = results.size()-1;
        }else{
            i2 = countOfValues;
        }
        MainActivity.candleEntries.clear();
        for(JSONObject a : results){
            float shadowH = Float.parseFloat(a.getString("2. high"));
            float shadowL = Float.parseFloat(a.getString("3. low"));
            float open = Float.parseFloat(a.getString("1. open"));
            float close = Float.parseFloat(a.getString("4. close"));
            MainActivity.candleEntries.add(new CandleEntry(i,shadowH,shadowL,open,close));
            i++;
            if (i==i2){break;}
        }
    }

}