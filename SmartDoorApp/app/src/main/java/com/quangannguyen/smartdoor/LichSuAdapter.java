package com.quangannguyen.smartdoor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by quangannguyen on 2/23/2018.
 */

public class LichSuAdapter extends RecyclerView.Adapter<LichSuAdapter.ViewHolder> {

    ArrayList<History> dataHistory;
    Context context;

    public LichSuAdapter(ArrayList<History> dataHistory, Context context) {
        this.dataHistory = dataHistory;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_lichsu,parent,false);
        return new LichSuAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTVHoTen.setText(dataHistory.get(position).getHoTen().toString().trim());
        holder.mTVDate.setText(dataHistory.get(position).getDate().toString().trim());


        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .round();

        ColorGenerator generator = ColorGenerator.MATERIAL;
        final History history = dataHistory.get(position);
        final int randomColor = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(60)  // width in px
                .height(60) // height in px
                .endConfig()
                .buildRect(String.valueOf(history.getHoTen().charAt(0)).toUpperCase(), randomColor);

        holder.mImgAnhDaiDien.setImageDrawable(drawable);

        if (history.getState().equals("true")) {
            holder.mImgState.setImageResource(R.drawable.ic_ok);
        } else {
            holder.mImgState.setImageResource(R.drawable.ic_fail);
        }

    }

    @Override
    public int getItemCount() {
        return dataHistory.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{

        TextView mTVHoTen,mTVDate;
        CircleImageView mImgAnhDaiDien;
        ImageView mImgState;

        public ViewHolder(View itemView) {
            super(itemView);
            mTVHoTen = itemView.findViewById(R.id.tv_HoTen);
            mTVDate = itemView.findViewById(R.id.tv_Date);
            mImgAnhDaiDien = itemView.findViewById(R.id.img_AnhDaiDien);
            mImgState = itemView.findViewById(R.id.img_State);
        }
    }
}
