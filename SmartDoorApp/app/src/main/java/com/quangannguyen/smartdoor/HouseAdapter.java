package com.quangannguyen.smartdoor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by quangannguyen on 2/23/2018.
 */

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.ViewHolder> {

    ArrayList<Item_House> dataHouse;
    Context context;

    public HouseAdapter(ArrayList<Item_House> dataHouse, Context context) {
        this.dataHouse = dataHouse;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_thanhvien,parent,false);
        return new HouseAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTVHoTen.setText(dataHouse.get(position).getName().toString().trim());
        holder.mTVID.setText(dataHouse.get(position).getID().toString().trim());
        holder.mImgAnhDaiDien.setImageResource(R.drawable.ic_house);
    }

    @Override
    public int getItemCount() {
        return dataHouse.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{

        TextView mTVHoTen, mTVID;
        CircleImageView mImgAnhDaiDien;

        public ViewHolder(View itemView) {
            super(itemView);
            mTVHoTen = itemView.findViewById(R.id.tv_HoTen);
            mTVID = itemView.findViewById(R.id.tv_ID);
            mImgAnhDaiDien = itemView.findViewById(R.id.img_AnhDaiDien);
        }
    }
}
