package com.habibfr.budget_buddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
        return saldoList.get(i).getUser_id();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.row_listvector, viewGroup, false);
        Saldo currenSaldo = (Saldo) getItem(i);

        TextView txtAmountMasukKeluar = view.findViewById(R.id.txtAmountMasukKeluar);
        TextView txtIncomeOrExpensive = view.findViewById(R.id.txtIncomeOrExpensive);

        txtAmountMasukKeluar.setText("Rp. "+currenSaldo.getSaldo());
        txtIncomeOrExpensive.setText(currenSaldo.getFullname());
        return view;
    }
}
