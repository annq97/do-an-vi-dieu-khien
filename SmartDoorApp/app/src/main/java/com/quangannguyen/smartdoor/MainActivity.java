package com.quangannguyen.smartdoor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolBarMHMain;
    private ViewPager mVPMHMain;
    private SectionPagerAdapter mSectionPagerAdapter;
    private TabLayout mTLMHMain;
    private FirebaseAuth mAuth;
    private Menu menu;
    private CircleImageView mImgAvatar;
    private DatabaseReference mUserDB;
    private DatabaseReference mHouseDB;
    private DatabaseReference mFamilyDB;
    private DatabaseReference mError_ScanDB;
    private FirebaseUser mCurrentUser;
    private String mUserID;
    private String mUserTopID;
    private TextView mTVHoTen;
    private FloatingActionButton mFAB_Camera;
    private Integer num_error = 0;

    private final int SMARTDOOR_CAMERA_PERMISSION_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addFirebase();
        addEvents();
    }

    private void addEvents() {
        mImgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_MHTrangCaNhan = new Intent(MainActivity.this,TrangCaNhanActivity.class);
                startActivity(intent_MHTrangCaNhan);
            }
        });
        
        mFAB_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, SMARTDOOR_CAMERA_PERMISSION_CODE);
                }else{
                    Intent intent_MHQRCode = new Intent(MainActivity.this, QuetMaQRActivity.class);
                    startActivity(intent_MHQRCode);
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case SMARTDOOR_CAMERA_PERMISSION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mError_ScanDB.child("num_error").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            num_error = Integer.valueOf(dataSnapshot.getValue().toString());
                            Toast.makeText(MainActivity.this, num_error + "", Toast.LENGTH_SHORT).show();
                            if(num_error < 3){
                                Intent intent_MHQRCode = new Intent(MainActivity.this, QuetMaQRActivity.class);
                                startActivity(intent_MHQRCode);
                            }else{
                                Toast.makeText(MainActivity.this, "'Bạn đã quét sai quá nhiều lần. Tính năng này bị khóa tạm thời!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else {
                    Toast.makeText(this, "Vui lòng cấp quyền cho camera để sử dụng tính năng này!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void addFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mUserID = mCurrentUser.getUid();
        mUserDB = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserID);
        mHouseDB = FirebaseDatabase.getInstance().getReference().child("Houses");
        mFamilyDB = FirebaseDatabase.getInstance().getReference().child("Families").child(mUserID);
        mError_ScanDB = FirebaseDatabase.getInstance().getReference().child("Error_Scan").child(mUserID);
        loadUser_info();
    }

    private void loadUser_info() {
        mUserDB.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTVHoTen.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUserDB.child("hasHouse").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String check_hasHouse = dataSnapshot.getValue().toString();
                if(check_hasHouse.equals("true")){

                    mHouseDB.child(mUserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            House currentHouse = dataSnapshot.getValue(House.class);

                            if(currentHouse.getCurrent_check().equals("true")){

                                menu.getItem(0).setIcon(R.drawable.ic_unlock);

                            }else{
                                menu.getItem(0).setIcon(R.drawable.ic_lock);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else{
                    mFamilyDB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue() != null){

                                for(DataSnapshot familyData : dataSnapshot.getChildren()){
                                    mUserTopID = familyData.getKey().toString();
                                    mHouseDB.child(mUserTopID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            House currentHouse = dataSnapshot.getValue(House.class);

                                            if(currentHouse.getCurrent_check().equals("true")){

                                                menu.getItem(0).setIcon(R.drawable.ic_unlock);

                                            }else{
                                                menu.getItem(0).setIcon(R.drawable.ic_lock);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                });
                                }

                            }else{
                                menu.getItem(0).setIcon(R.drawable.ic_lock);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void addControls() {

        mToolBarMHMain = findViewById(R.id.tb_MHMain);
        setSupportActionBar(mToolBarMHMain);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.main_custom_bar,null);
        actionBar.setCustomView(action_bar_view);

        mVPMHMain = findViewById(R.id.vp_MHMain);
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mVPMHMain.setAdapter(mSectionPagerAdapter);

        mTLMHMain = findViewById(R.id.tl_MHMain);
        mTLMHMain.setupWithViewPager(mVPMHMain);

        mImgAvatar = findViewById(R.id.img_custom_bar);

        mTVHoTen = findViewById(R.id.tv_custom_bar);
        
        mFAB_Camera = findViewById(R.id.fab_Camera);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mhmain,menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btn_MenuDangXuat:
                mAuth.signOut();
                Intent intent_ChuyenMHDangNhap = new Intent(MainActivity.this, MHDangNhapActivity.class);
                startActivity(intent_ChuyenMHDangNhap);
                finish();
                break;
            case R.id.btn_MenuThem:
                Intent intent_MHThemThanhVien = new Intent(this,ThemThanhVienActivity.class);
                startActivity(intent_MHThemThanhVien);
                break;
            case R.id.btn_MenuKhoa:
                Intent intent_MHMoKhoa = new Intent(this,MoKhoaActivity.class);
                startActivity(intent_MHMoKhoa);
                break;
            default:
                Toast.makeText(this, "Lựa chọn không hợp lệ!", Toast.LENGTH_SHORT).show();
             break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
