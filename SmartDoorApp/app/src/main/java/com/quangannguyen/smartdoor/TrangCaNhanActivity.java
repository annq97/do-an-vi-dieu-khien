package com.quangannguyen.smartdoor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrangCaNhanActivity extends AppCompatActivity {

    private Toolbar mToolbarMHTrangCaNhan;
    private TextView mTVHoTen;
    private TextView mTVID;

    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDB;
    private String mUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_ca_nhan);
        addControls();
        addFirebase();
        addEvents();
    }

    private void addFirebase() {

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserID = mCurrentUser.getUid();
        mUserDB = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserID);
        loadUser_info();

    }

    private void loadUser_info() {
        mUserDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTVHoTen.setText(dataSnapshot.getValue(User.class).getName().toString());
                mTVID.setText(dataSnapshot.getValue(User.class).getID().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addEvents() {
    }

    private void addControls() {
        mToolbarMHTrangCaNhan = findViewById(R.id.tb_MHTrangCaNhan);
        setSupportActionBar(mToolbarMHTrangCaNhan);
        getSupportActionBar().setTitle("Trang Cá Nhân");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTVHoTen = findViewById(R.id.tv_HoTen);
        mTVID = findViewById(R.id.tv_ID);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mhtrangcanhan,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.btn_MenuDoiMaBiMat:
                Intent intent_MHDoiMaSoBiMat = new Intent(TrangCaNhanActivity.this,DoiMaSoBiMatActivity.class);
                startActivity(intent_MHDoiMaSoBiMat);
                finish();
                break;
            case R.id.btn_MenuDoiMatKhau:
                Intent intent_MHDoiMatKhau = new Intent(TrangCaNhanActivity.this,DoiMatKhauActivity.class);
                startActivity(intent_MHDoiMatKhau);
                finish();
                break;
            default:
                Toast.makeText(this, "Lựa chọn không hợp lệ!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
