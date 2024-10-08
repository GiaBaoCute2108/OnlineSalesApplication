package com.example.appbanhangonline.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhangonline.model.SanPhamMoi;
import com.example.appbanhangonline.Interface.ItemClickListener;
import com.example.appbanhangonline.R;
import com.example.appbanhangonline.activity.ChiTietActivity;

import java.text.DecimalFormat;
import java.util.List;

public class LaptopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    List<SanPhamMoi> array;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public LaptopAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATA) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_laptop, parent, false);
            return new MyViewHolder(v);
        } else {
            View v= LayoutInflater.from(context).inflate(R.layout.item_loading, parent,false);
            return new LoadingViewHolder(v);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            SanPhamMoi sanPhamMoi = array.get(position);
            Glide.with(context).load(sanPhamMoi.getHinhanh()).into(myViewHolder.imghinhanh);
            myViewHolder.tensp.setText(sanPhamMoi.getTensp());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.giasp.setText("Giá" + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGia())) + "đ");
            myViewHolder.mota.setText("Mô tả: " + sanPhamMoi.getMota());
            myViewHolder.id.setText("Id: " + sanPhamMoi.getId());
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View v, int position) {
                        Intent intent = new Intent(context, ChiTietActivity.class);
                        intent.putExtra("sanpham", sanPhamMoi);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                }
            });
        } else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return array != null ? array.size() : 0;
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressbar);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imghinhanh;
        TextView tensp, giasp, mota, id;
        ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View v) {
            super(v);
            imghinhanh = v.findViewById(R.id.itemdt_image);
            tensp = v.findViewById(R.id.itemdt_ten);
            giasp = v.findViewById(R.id.itemdt_gia);
            mota = v.findViewById(R.id.itemdt_mota);
            id = v.findViewById(R.id.itemdt_id);
            v.setOnClickListener(this);
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
