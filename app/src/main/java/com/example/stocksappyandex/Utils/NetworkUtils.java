package com.example.stocksappyandex.Utils;

import android.app.Activity;
import android.content.Context;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {


    public static class GetArray {
        private Context context;
        private Activity activity;

        public GetArray(Context context, Activity activity) {

            this.activity = activity;
            this.context = context;
        }

        public Thread thread;

        public void getArr() {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    String URL1 = "https://www.slickcharts.com/dowjones";
                    StringBuilder builder = new StringBuilder();

                    URL url = null;
                    HttpsURLConnection urlConnection = null;
                    LinkedHashSet<String> set = new LinkedHashSet<>();
                    try {
                        url = new URL(URL1);
                        urlConnection = (HttpsURLConnection) url.openConnection();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                        reader.close();
                        String result = builder.toString();
                        Pattern pattern = Pattern.compile("<td><a href=\"/symbol/(.*?)\">");
                        Matcher matcher = pattern.matcher(result);
                        while (matcher.find()) {
                            String a = matcher.group().replace("<td><a href=\"/symbol/", "");
                            String b = a.substring(0, a.length() - 2);
                            set.add(b);
                        }
                        JSONUtils.GetListCompanyObj getListCompanyObj = new JSONUtils.GetListCompanyObj(context, activity);
                        List<String> res = new ArrayList<>();
                        for (String a : set) {
                            res.add(a);
                        }
                        for (int i = 0; i < res.size() - 1; i = i + 2) {
                            getListCompanyObj.getList(res.get(i));
                            getListCompanyObj.getList(res.get(i + 1));
                        }


                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                }
            };
            thread = new Thread(runnable);
            thread.start();
        }


    }
}