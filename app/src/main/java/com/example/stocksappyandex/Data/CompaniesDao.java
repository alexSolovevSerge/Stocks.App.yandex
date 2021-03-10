package com.example.stocksappyandex.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CompaniesDao {
    @Query("SELECT * FROM companies ORDER BY ticker")
    LiveData<List<Company>> getAllCompanies();

    @Query("SELECT * FROM companies WHERE favourite")
    LiveData<List<Company>> getFavouriteCompanies();

    @Query("Select * FROM companies where name LIKE :call ORDER BY ticker")
    LiveData<List<Company>> getSearchedCompanies(String call);

    @Insert
    void insertCompany(Company company);

    @Query("SELECT COUNT(*) FROM companies")
    int countAllCompanies();

    @Query("SELECT * FROM companies WHERE id=:companyId")
    Company getCompanyById(int companyId);

    @Query("SELECT * FROM companies WHERE ticker=:ticker")
    Company getCompanyByTicker(String ticker);

    @Update
    void updateCompany(Company company);


    @Insert
    void insertList(List<Company> a );

    @Query("DELETE FROM companies")
    void deleteAllcompanies();


}
