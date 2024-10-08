package com.example.appbanhangonline.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.service.chooser.ChooserAction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhangonline.Interface.ItemDeleteClickListener;
import com.example.appbanhangonline.model.DonHang;
import com.example.appbanhangonline.Interface.ItemClickListener;
import com.example.appbanhangonline.R;

import java.text.DecimalFormat;
import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    Context context;
    List<DonHang> list;
    ItemDeleteClickListener itemDeleteClickListener;

    public DonHangAdapter(Context context, List<DonHang> list, ItemDeleteClickListener itemDeleteClickListener) {
        this.context = context;
        this.list = list;
        this.itemDeleteClickListener = itemDeleteClickListener;
    }

    @NonNull
    @Override
    public DonHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_donhang, parent, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DonHangAdapter.MyViewHolder holder, int position) {
        DonHang donHang = list.get(position);
        if (donHang != null) {
            holder.id.setText("Id đơn hàng: " + donHang.getId());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            holder.tongtien.setText("Tổng tiền: " + decimalFormat.format(Double.parseDouble(donHang.getTongtien())) + "đ");
            holder.sdt.setText("Số điện thoại: " + donHang.getSodienthoai());
            holder.diachi.setText("Địa chỉ: " + donHang.getDiachi());
            int trang_thai = donHang.getTrangthai();
            switch (trang_thai) {
                case 0: holder.trangthai.setText("Đơn hàng đang được xử lý");
                    break;
                case 1: holder.trangthai.setText("Đơn hàng đã được xử lý");
                    break;
                case 2: holder.trangthai.setText("Đơn hàng đã được giao cho đơn vị vận chuyển");
                    break;
                case 3: holder.trangthai.setText("Đơn hàng đang được giao");
                    break;
                case 4:  holder.trangthai.setText("Giao hàng thành công");
                    break;
                case 5:  holder.trangthai.setText("Đơn hàng đã hủy");
                    break;
            }
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemDeleteClickListener.onDeleteClick(donHang.getId(), holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.isEmpty() ? 0 : list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id, tongtien, sdt, diachi, trangthai;
        ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id_donhang);
            tongtien = itemView.findViewById(R.id.tongtienDonHang);
            sdt = itemView.findViewById(R.id.sdtKhachhang);
            diachi = itemView.findViewById(R.id.diachiDonHang);
            trangthai = itemView.findViewById(R.id.trangthaidon);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
