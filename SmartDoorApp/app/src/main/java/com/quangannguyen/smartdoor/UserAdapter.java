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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    ArrayList<User> dataUser;
    Context context;

    public UserAdapter(ArrayList<User> dataUser, Context context) {
        this.dataUser = dataUser;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_thanhvien,parent,false);
        return new UserAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

         // or use DEFAULT

        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .round();

        ColorGenerator generator = ColorGenerator.MATERIAL;
        final User user = dataUser.get(position);
        final int randomColor = generator.getRandomColor();


        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(60)  // width in px
                .height(60) // height in px
                .endConfig()
                .buildRect(String.valueOf(user.getName().charAt(0)).toUpperCase(), randomColor);



        holder.mTVHoTen.setText(user.getName().trim());
        holder.mTVID.setText(user.getID().trim());
        holder.mImgAnhDaiDien.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return dataUser.size();
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
