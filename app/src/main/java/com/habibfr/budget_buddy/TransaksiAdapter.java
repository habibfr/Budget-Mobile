package com.habibfr.budget_buddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView txtTglTglTrxHome = view.findViewById(R.id.tglTglTrxHome);
        TextView txtAmountTrxHome = view.findViewById(R.id.txtAmountTrxHome);
        TextView txtTypeTrxHome = view.findViewById(R.id.txtTypeTrxHome);

        txtShopping.setText(currentTrx.getTitle());
        txtTglTglTrxHome.setText(currentTrx.getDate());
        txtAmountTrxHome.setText("Rp. " + currentTrx.getAmount());
        txtTypeTrxHome.setText(currentTrx.getType());
        if(currentTrx.getType().equals("Masuk")){
            btnCar.setImageResource(R.drawable.img_calendar);
        }

        return view;
    }
}
