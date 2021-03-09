package com.example.stocksappyandex.Data;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocksappyandex.R;

import java.util.List;

import static com.example.stocksappyandex.MainActivity.viewModel;

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.CompaniesViewHolder> {


    private List<Company> companies;

    public OnCompanyClickListener onCompanyClickListener;

    public void setOnCompanyClickListener(OnCompanyClickListener onCompanyClickListener) {
        this.onCompanyClickListener = onCompanyClickListener;
    }

    public CompaniesAdapter(List<Company> companies) {
        this.companies = companies;
    }

    @NonNull
    @Override
    public CompaniesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_card_layout, parent, false);
        return new CompaniesViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CompaniesViewHolder holder, int position) {
        Company company = companies.get(position);
        holder.textViewname.setText(company.getName());
        holder.textViewticker.setText(company.getTicker());
        Double currentprice = company.getCurrentprice();
        Double deltaprice = company.getDeltaprice();
        holder.textViewcurrentPrice.setText(String.format("$%.3f", company.getCurrentprice()));
        if(deltaprice<0) {
            holder.textViewDeltaPrice.setText(String.format("-$%.3f", Math.abs(company.getDeltaprice())));
            holder.textViewDeltaPrice.setTextColor(Color.RED);
        }else if (deltaprice>0){
            holder.textViewDeltaPrice.setText(String.format("+$%.3f", Math.abs(company.getDeltaprice())));
            holder.textViewDeltaPrice.setTextColor(Color.GREEN);
        }
        String bitmap = company.getBitmap();
        if(bitmap!=null) {
            holder.imageViewLogo.setImageBitmap(StringToBitMap(bitmap));
        }
        if(company.isFavourite()==true){
            holder.imageViewFavourite.setImageResource(R.drawable.favourite);
        }else{

            holder.imageViewFavourite.setImageResource(R.drawable.nonfavourite);
        }
    }

    public interface OnCompanyClickListener {
        void onNoteClick(int position);
    }




    @Override
    public int getItemCount() {
        return companies.size();
    }

    public void setCompanies(List<Company> companies) {

        this.companies = companies;
    }

    public Company getCompany(int id){
        return companies.get(id);
    }

    public List<Company> getCompanies(){
        return companies;
    }

    public synchronized void setNewCompany(Company company) {

        viewModel.insertCompany(company);
        this.companies.add(company);
        notifyItemInserted(companies.size()-1);
    }


    class CompaniesViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewname;
        private TextView textViewticker;
        private TextView textViewcurrentPrice;
        private TextView textViewDeltaPrice;
        private ImageView imageViewLogo;
        private ImageView imageViewFavourite;


        public CompaniesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewname = itemView.findViewById(R.id.textViewName);
            textViewticker = itemView.findViewById(R.id.textViewTicker);
            textViewcurrentPrice = itemView.findViewById(R.id.textViewCurrentPrice);
            textViewDeltaPrice = itemView.findViewById(R.id.textViewDeltaPrice);
            imageViewLogo = itemView.findViewById(R.id.imageViewLogo);
            imageViewFavourite = itemView.findViewById(R.id.imageViewFavore);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onCompanyClickListener != null){
                        onCompanyClickListener.onNoteClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public Bitmap StringToBitMap(String encodedString) {
        if(encodedString!=null) {
            try {
                byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                        encodeByte.length);
                return bitmap;
            } catch (Exception e) {
                e.getMessage();
                return null;
            }
        }else{
            return null;
        }
    }
}