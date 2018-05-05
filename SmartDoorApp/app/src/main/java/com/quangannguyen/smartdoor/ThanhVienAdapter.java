package com.quangannguyen.smartdoor;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by quangannguyen on 2/23/2018.
 */

public class ThanhVienAdapter extends  RecyclerView.Adapter<ThanhVienAdapter.ViewHolder>{

    ArrayList<User> dataThanhVien;
    Context context;

    public ThanhVienAdapter(ArrayList<User> dataThanhVien, Context context) {
        this.dataThanhVien = dataThanhVien;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_thanhvien,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTVHoTen.setText(dataThanhVien.get(position).getName().toString().toString().trim());
        holder.mTVID.setText(dataThanhVien.get(position).getID().toString().trim());

        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .round();

        ColorGenerator generator = ColorGenerator.MATERIAL;
        final User thanhvien = dataThanhVien.get(position);
        final int randomColor = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(60)  // width in px
                .height(60) // height in px
                .endConfig()
                .buildRect(String.valueOf(thanhvien.getName().charAt(0)).toUpperCase(), randomColor);

        holder.mImgAnhDaiDien.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return dataThanhVien.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTVID,mTVHoTen;
        CircleImageView mImgAnhDaiDien;
        public ViewHolder(View itemView) {
            super(itemView);
            mTVHoTen = itemView.findViewById(R.id.tv_HoTen);
            mTVID = itemView.findViewById(R.id.tv_ID);
            mImgAnhDaiDien = itemView.findViewById(R.id.img_AnhDaiDien);
        }
    }

}
