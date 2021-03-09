package com.example.stocksappyandex.Utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.stocksappyandex.Data.Company;
import com.example.stocksappyandex.MainActivity;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Provider;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSoketUtils {
    String socketURL = "{\"type\":\"subscribe\",\"symbol\":\"%s\"}";

    public void getTickerPrice(List<Company> list){
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebSocket ws = null;
                try {
                    ws = connect();
                    while(true){
                        if(list.size()==26) {
                            try {
                                ws = connect();
                                for (Company a : list) {
                                    ws.sendText(String.format(socketURL, a.getTicker()));
                                }
                                while (true){

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private WebSocket connect() throws Exception {
        return new WebSocketFactory()
                .setConnectionTimeout(1000)
                .createSocket("wss://ws.finnhub.io?token=c0nq0h748v6u2iq11630")
                .addListener(new WebSocketAdapter() {
                    @Override
                    public void onTextMessage(WebSocket websocket, String text) throws Exception {
                        if(text.length()<120){setPrice(text);}
                    }
                })
                .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
                .connect();

    }

    private BufferedReader getInput() throws IOException {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    private void setPrice(String text){
        if (text.contains("trade")) {
            Pattern patternTicker = Pattern.compile(",\"s\":\"(.*?)\",");
            Matcher matcherTicker = patternTicker.matcher(text);
            Pattern patternPrice = Pattern.compile("\"p\":(.*?),");
            Matcher matcherPrice = patternPrice.matcher(text);
            while (matcherTicker.find()) {
                String a = matcherTicker.group().substring(6).replace("\",", "");
                Company company = MainActivity.viewModel.getCompanyByTicker(a);
                while (matcherPrice.find()) {
                    Double b = Double.parseDouble(matcherPrice.group().substring(4).replace(",", ""));
                    if(company.getCurrentprice()!=b){
                        company.setDeltaprice(company.getCurrentprice()-b);
                        company.setCurrentprice(b);
                        MainActivity.viewModel.updateCompany(company);
                    }
                }
            }
        }
    }
}
