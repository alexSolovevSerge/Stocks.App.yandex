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
import com.example.stocksappyandex.Fragments.RecyclerViewStocksFragment;
import com.example.stocksappyandex.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static com.example.stocksappyandex.Fragments.RecyclerViewStocksFragment.handler;

public class JSONUtils {

    private static final String KEY = "c0nq0h748v6u2iq11630";


    public static class GetListCompanyObj {

        private Context context;
        private Activity activity;

        public static Thread thread;
        public static Thread thread2;

        public GetListCompanyObj(Context context,Activity activity) {

            this.activity = activity;
            this.context = context;
        }

        public void getList(String ticker) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
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
                        String logopath = jsonObject.getString("weburl");
                        long currentprice = 0;
                        long deltaprice = 0;

                        url = new URL(String.format("https://logo.clearbit.com/%s",logopath).replace("https://www.",""));
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
        private CompaniesDatabase database;
        public  void getPrice(Company company) {


            String urlttt = "https://finnhub.io/api/v1/quote?symbol=%s&token=%s";
            Runnable runnable1 = new Runnable() {
                @Override
                public void run() {
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
                                RecyclerViewStocksFragment.adapter.setNewCompany(company);
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
    }

}