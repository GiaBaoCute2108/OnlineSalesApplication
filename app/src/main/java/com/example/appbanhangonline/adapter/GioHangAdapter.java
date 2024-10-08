package com.example.appbanhangonline.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhangonline.activity.XemDonActivity;
import com.example.appbanhangonline.model.EventBus.TinhTongEvent;
import com.example.appbanhangonline.model.GioHang;
import com.example.appbanhangonline.utils.Utils;
import com.example.appbanhangonline.Interface.ImageClickListener;
import com.example.appbanhangonline.R;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

import io.paperdb.Paper;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder>{
    Context context;
    List<GioHang> list;

    public GioHangAdapter(Context context, List<GioHang> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GioHang gioHang = list.get(position);
        if (gioHang != null) {
            Glide.with(context).load(gioHang.getHinhsp()).into(holder.image);
            holder.name.setText(gioHang.getTensp());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            holder.price.setText("Đơn giá: " + decimalFormat.format(Double.parseDouble(String.valueOf(gioHang.getGiasp()))) + "đ");
            holder.quantity.setText(gioHang.getSoluong()+"");
            holder.tongtien.setText("Thành tiền: " + decimalFormat.format(gioHang.getGiasp() * gioHang.getSoluong()) + "đ");
            holder.setImageClickListener(new ImageClickListener() {
                @Override
                public void onClick(View v, int position, int giatri) {
                    if (giatri == 1) {
                        if(list.get(position).getSoluong() > 1) {
                            int soluongmoi = list.get(position).getSoluong() - 1;
                            list.get(position).setSoluong(soluongmoi);
                            holder.quantity.setText(gioHang.getSoluong()+"");
                            holder.tongtien.setText("Thành tiền: " + decimalFormat.format(gioHang.getGiasp() * gioHang.getSoluong()) + "đ");
                            EventBus.getDefault().postSticky(new TinhTongEvent());
                        } else if (list.get(position).getSoluong() == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Thông báo!");
                            builder.setMessage("Bạn có muốn xóa sản phẩm này ra khỏi giỏ hàng");
                            builder.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Utils.manggiohang.remove(position);
                                    Utils.mangmuahang.remove(gioHang);
                                    Paper.book().write("giohang", Utils.manggiohang);
                                    notifyDataSetChanged();
                                    EventBus.getDefault().postSticky(new TinhTongEvent());
                                }
                            });
                            builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    } else if (giatri == 2) {
                        if(list.get(position).getSoluong() < 11) {
                            int soluongmoi = list.get(position).getSoluong() + 1;
                            if (soluongmoi <= list.get(position).getSoluongtonkho()) {
                                list.get(position).setSoluong(soluongmoi);
                                holder.quantity.setText(gioHang.getSoluong()+"");
                                holder.tongtien.setText("Thành tiền: " + decimalFormat.format(gioHang.getGiasp() * gioHang.getSoluong()) + "đ");
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            } else {
                                MotionToast.Companion.createToast((Activity) context,
                                        "Thông báo",
                                        "Quá s lượng tồn kho!",
                                        MotionToastStyle.WARNING,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(context,R.font.helvetica_regular));
                            }
                        }
                    }
                }
            });
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Utils.manggiohang.get(holder.getAdapterPosition()).setChecked(true);
                        if(!Utils.mangmuahang.contains(gioHang)){
                            Utils.mangmuahang.add(gioHang);
                        }
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    } else {
                        Utils.manggiohang.get(holder.getAdapterPosition()).setChecked(false);
                        for (int i = 0; i < Utils.mangmuahang.size(); i++) {
                            if (Utils.mangmuahang.get(i).getIdsp() == gioHang.getIdsp()) {
                                Utils.mangmuahang.remove(i);
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        }
                    }
                }
            });
            holder.checkBox.setChecked(gioHang.isChecked());
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image, btnSub, btnAdd;
        TextView name, price, quantity, tongtien;
        CheckBox checkBox;
        ImageClickListener imageClickListener;
        public MyViewHolder(@NonNull View v) {
            super(v);
            image = v.findViewById(R.id.itemhinhanh_GH);
            btnSub = v.findViewById(R.id.btnSub);
            btnAdd = v.findViewById(R.id.btnAdd);
            name = v.findViewById(R.id.itemten_GH);
            price  = v.findViewById(R.id.itemgia_GH);
            quantity = v.findViewById(R.id.itemQuantity);
            tongtien = v.findViewById(R.id.tongtien);
            checkBox = v.findViewById(R.id.checkbox_gh);
            btnSub.setOnClickListener(this);
            btnAdd.setOnClickListener(this);
        }

        public void setImageClickListener(ImageClickListener imageClickListener) {
            this.imageClickListener = imageClickListener;
        }

        @Override
        public void onClick(View v) {
            if(v == btnSub) {
                imageClickListener.onClick(v, getAdapterPosition(), 1);
            } else if (v == btnAdd) {
                imageClickListener.onClick(v, getAdapterPosition(), 2);
            }
            EventBus.getDefault().postSticky(new TinhTongEvent());
        }
    }
}
