package com.habibfr.budget_buddy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.List;

public class TransaksiAdapter extends BaseAdapter {

    Context context;
    List<Transaksi> transaksiList;

    public TransaksiAdapter(Context context, List<Transaksi> transaksiList) {
        this.context = context;
        this.transaksiList = transaksiList;
    }

    @Override
    public int getCount() {
        return transaksiList.size();
    }

    @Override
    public Object getItem(int i) {
        return transaksiList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return transaksiList.get(i).getUser_id();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_transaksi, viewGroup, false);
        Transaksi currentTrx = (Transaksi) getItem(i);
        ImageView btnCar = view.findViewById(R.id.btnCar);
        TextView txtShopping = view.findViewById(R.id.txtShopping);
        TextView txtAmountTrxHome = view.findViewById(R.id.txtAmountTrxHome);
        LinearLayout layoutItemTransaksi = view.findViewById(R.id.layoutItemTransaksi);

        txtShopping.setText(currentTrx.getTitle());
        String amount = String.valueOf(currentTrx.getAmount());
        txtAmountTrxHome.setText(String.format(amount));
        if(currentTrx.getType().equals("Keluar")){

            btnCar.setImageResource(R.drawable.img_calendar);
            layoutItemTransaksi.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.rectangle_bg_gray_301_radius_20));
//            btnCar.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.rectangle_bg_gray_301_radius_24));
        }

        layoutItemTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToDetail = new Intent(view.getContext(), DetailActivity.class);
                intentToDetail.putExtra("transaction_id", currentTrx.getTransaction_id());
                intentToDetail.putExtra("user_id", currentTrx.getUser_id());
                intentToDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intentToDetail);
            }
        });

        return view;
    }
}
