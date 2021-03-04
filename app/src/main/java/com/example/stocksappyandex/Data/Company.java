package com.example.stocksappyandex.Data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "companies")
public class Company {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String ticker;
    private String name;
    private boolean favourite;
    private String logopath;
    private double currentprice;
    private double deltaprice;
    private String bitmap;
    private String news;
    private String summary;

    @Ignore
    public Company(String ticker, String name, boolean favourite, String logopath, double currentprice, double deltaprice, String bitmap,String news,String summary) {
        this.ticker = ticker;
        this.name = name;
        this.favourite = favourite;
        this.logopath = logopath;
        this.currentprice = currentprice;
        this.deltaprice = deltaprice;
        this.bitmap = bitmap;
        this.news = news;
        this.summary = summary;
    }

    public Company(int id, String ticker, String name, boolean favourite, String logopath, double currentprice, double deltaprice, String bitmap,String news,String summary) {
        this.id = id;
        this.ticker = ticker;
        this.name = name;
        this.favourite = favourite;
        this.logopath = logopath;
        this.currentprice = currentprice;
        this.deltaprice = deltaprice;
        this.bitmap = bitmap;
        this.news = news;
        this.summary = summary;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getLogopath() {
        return logopath;
    }

    public void setLogopath(String logopath) {
        this.logopath = logopath;
    }

    public double getCurrentprice() {
        return currentprice;
    }

    public void setCurrentprice(double currentprice) {
        this.currentprice = currentprice;
    }

    public double getDeltaprice() {
        return deltaprice;
    }

    public void setDeltaprice(double deltaprice) {
        this.deltaprice = deltaprice;
    }
}