package com.habibfr.budget_buddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class SaldoAdapter extends BaseAdapter {
    Context context;
    List<Saldo> saldoList;

    public SaldoAdapter(Context context, List<Saldo> saldoList) {
        this.context = context;
        this.saldoList = saldoList;
    }

    @Override
    public int getCount() {
        return saldoList.size();
    }

    @Override
    public Object getItem(int i) {
        return saldoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return saldoList.indexOf(saldoList.get(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.row_listvector, viewGroup, false);
        Saldo currentSaldo = (Saldo) getItem(i);

        TextView txtAmountMasukKeluar = view.findViewById(R.id.txtAmountMasukKeluar);
        TextView txtIncomeOrExpensive = view.findViewById(R.id.txtIncomeOrExpensive);
        LinearLayout bgSaldoTengah = view.findViewById(R.id.bgSaldoTengah);
        if (currentSaldo.getType().equals("Income")) {
            txtAmountMasukKeluar.setText("Rp. " + currentSaldo.getSaldo());
            txtIncomeOrExpensive.setText(currentSaldo.getType());
        } else {
            bgSaldoTengah.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.rectangle_bg_gray_301_radius_24));
            txtAmountMasukKeluar.setText("Rp. " + currentSaldo.getSaldo());
            txtIncomeOrExpensive.setText(currentSaldo.getType());
        }

        return view;
    }
}
