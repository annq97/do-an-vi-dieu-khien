package com.quangannguyen.smartdoor;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoiMaSoBiMatActivity extends AppCompatActivity {

    private Toolbar mToolbarMHDoiMaSoBiMat;
    private EditText mEDTMaSoCu, mEDTMaSoMoi, mEDTXacNhanMaSoMoi;
    private Button mBtnXacNhan;

    private DatabaseReference mUserDB;
    private DatabaseReference mHouseDB;
    private FirebaseUser mCurrentUser;
    private String mUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_ma_so_bi_mat);
        addControls();
        addFirebase();
        addEvents();
    }

    private void addEvents() {

        mBtnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String MaSoCu = mEDTMaSoCu.getText().toString().trim();
                final String MaSoMoi = mEDTMaSoMoi.getText().toString().trim();
                String XacNhanMaSoMoi = mEDTXacNhanMaSoMoi.getText().toString().trim();
                
                if(MaSoCu.equals("") != true && MaSoMoi.equals("") != true && XacNhanMaSoMoi.equals("") != true){
                    if(MaSoMoi.equals(XacNhanMaSoMoi)){
                        mUserDB.child(mUserID).child("hasHouse").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue().toString().equals("true")){
                                    mHouseDB.child(mUserID).child("code_private").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.getValue().toString().equals(MaSoCu)){

                                                mHouseDB.child(mUserID).child("code_private").setValue(MaSoMoi).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(DoiMaSoBiMatActivity.this, "Đổi mã số bí mật thành công!", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            Toast.makeText(DoiMaSoBiMatActivity.this, "Đổi mã số bí mật thất bại!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            }else {
                                                Toast.makeText(DoiMaSoBiMatActivity.this, "Mã số bí mật không chính xác!", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }else{
                                    Toast.makeText(DoiMaSoBiMatActivity.this, "Bạn không có quyền đổi mã số bí mật!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(DoiMaSoBiMatActivity.this, "Mã số bí mật không trùng khớp!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(DoiMaSoBiMatActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                
            }
        });

    }

    private void addFirebase() {

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserID = mCurrentUser.getUid();
        mUserDB = FirebaseDatabase.getInstance().getReference().child("Users");
        mHouseDB = FirebaseDatabase.getInstance().getReference().child("Houses");

    }

    private void addControls() {
        mToolbarMHDoiMaSoBiMat = findViewById(R.id.tb_MHDoiMaSoBiMat);
        setSupportActionBar(mToolbarMHDoiMaSoBiMat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đổi Mã Số Bí Mật");

        mEDTMaSoCu = findViewById(R.id.edt_MaSoCu);
        mEDTMaSoMoi = findViewById(R.id.edt_MaSoMoi);
        mEDTXacNhanMaSoMoi = findViewById(R.id.edt_XacNhanMaSoMoi);
        mBtnXacNhan = findViewById(R.id.btn_XacNhan);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                Toast.makeText(this, "Lựa chọn không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
