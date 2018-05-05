package com.quangannguyen.smartdoor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

public class MHMainAdminActivity extends AppCompatActivity {

    private Toolbar mToolbarMHMainAdmin;
    private ViewPager mVPMainAdmin;
    private MainAdminPagerAdapter mMainAdminPagerAdapter;
    private TabLayout mTLMainAdmin;
    private TextView mTVHoTen;
    private FloatingActionButton mFAB_Add;

    private DatabaseReference mUserDB;
    private FirebaseUser mCurrentUser;
    private String mUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhmain_admin);
        addControls();
        addFirebase();
        addEvents();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_mhmainadmin,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.btn_MenuDangXuat:
                FirebaseAuth.getInstance().signOut();
                Intent intent_ChuyenMHDangNhap = new Intent(MHMainAdminActivity.this, MHDangNhapActivity.class);
                startActivity(intent_ChuyenMHDangNhap);
                finish();
                break;
            default:
                Toast.makeText(this, "Lựa chọn không hợp lệ!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addEvents() {

        mFAB_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_MHThemNha = new Intent(MHMainAdminActivity.this, ThemNhaActivity.class);
                startActivity(intent_MHThemNha);
            }
        });

    }

    private void addFirebase() {

        mUserDB = FirebaseDatabase.getInstance().getReference().child("Users");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserID = mCurrentUser.getUid();

        mUserDB.child(mUserID).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTVHoTen.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        
    }

    private void addControls() {

        mToolbarMHMainAdmin = findViewById(R.id.tb_MHMainAdmin);
        setSupportActionBar(mToolbarMHMainAdmin);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.main_custom_bar,null);
        actionBar.setCustomView(action_bar_view);


        mVPMainAdmin = findViewById(R.id.vp_MHMainAdmin);
        mMainAdminPagerAdapter = new MainAdminPagerAdapter(getSupportFragmentManager());
        mVPMainAdmin.setAdapter(mMainAdminPagerAdapter);

        mTLMainAdmin = findViewById(R.id.tl_MHMainAdmin);
        mTLMainAdmin.setupWithViewPager(mVPMainAdmin);


        mTVHoTen = findViewById(R.id.tv_custom_bar);

        mFAB_Add = findViewById(R.id.fab_add);

    }
}
