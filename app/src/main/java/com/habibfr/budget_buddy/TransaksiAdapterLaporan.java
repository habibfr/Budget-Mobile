package com.habibfr.budget_buddy;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransaksiAdapterLaporan extends RecyclerView.Adapter<TransaksiAdapterLaporan.TransaksiViewHolder> {
    private List<Transaksi> transaksiList;

    public TransaksiAdapterLaporan(List<Transaksi> transaksiList) {
        this.transaksiList = transaksiList;
    }

    @NonNull
    @Override
    public TransaksiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_laporan, parent, false);
        return new TransaksiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiViewHolder holder, int position) {
        Transaksi transaksi = transaksiList.get(position);

        String txtjudul = transaksi.getTransaction_id() + " " + transaksi.getTitle();
        holder.judul.setText(txtjudul);
        holder.amount.setText(String.valueOf(transaksi.getAmount()));
        holder.date.setText(transaksi.getDate());
    }

    @Override
    public int getItemCount() {
        return transaksiList.size();
    }

    public static class TransaksiViewHolder extends RecyclerView.ViewHolder {
        TextView judul, amount, date;

        public TransaksiViewHolder(View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.txtShopping);
            amount = itemView.findViewById(R.id.txtAmountLaporan);
            date = itemView.findViewById(R.id.txtTanggalLaporan);
        }
    }
}
