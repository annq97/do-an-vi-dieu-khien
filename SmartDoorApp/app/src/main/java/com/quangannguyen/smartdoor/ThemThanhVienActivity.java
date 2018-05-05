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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ThemThanhVienActivity extends AppCompatActivity {

    private Toolbar mToolBarMHThemThanhVien;
    private EditText mEDTID,mEDTMaSo;
    private Button mBtnThem,mBtnXoa;
    private DatabaseReference mUserDB;
    private DatabaseReference mFamilyDB;
    private DatabaseReference mHouseDB;
    private FirebaseUser mCurrentUser;
    private String mUserID;
    private String mUserDownID;
    private Boolean boolCheckID = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_thanh_vien);
        addControls();
        addFirebase();
        addEvents();
    }

    private void addEvents() {
        mBtnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String code_pr = mEDTMaSo.getText().toString().trim();
                final String ID = mEDTID.getText().toString().trim();
                if(code_pr.equals("") != true && ID.equals("") != true){
                    mUserDB.child(mUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User currentUser = dataSnapshot.getValue(User.class);
                            if(currentUser.getHasHouse().equals("true")){
                                mHouseDB.child(mUserID).child("code_private").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getValue().equals(code_pr)){
                                            mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for(DataSnapshot userData : dataSnapshot.getChildren()){
                                                        if(userData.getValue(User.class).getID().equals(ID)){
                                                            mUserDB.child(mUserID).child("ID").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                        if(dataSnapshot.getValue().toString().equals(ID) != true) {

                                                                            mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    for (DataSnapshot userData : dataSnapshot.getChildren()) {
                                                                                        if (userData.getValue(User.class).getID().equals(ID)) {
                                                                                            mUserDownID = userData.getKey();
                                                                                            mFamilyDB.child(mUserID).child(mUserDownID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                    if(dataSnapshot.hasChild("check_family") != true){
                                                                                                        mFamilyDB.child(mUserID).child(mUserDownID).child("check_family").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                if (task.isSuccessful()) {
                                                                                                                    mFamilyDB.child(mUserDownID).child(mUserID).child("check_family").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                                            Toast.makeText(ThemThanhVienActivity.this, "Thêm thành viên mới thành công!", Toast.LENGTH_SHORT).show();
                                                                                                                        }
                                                                                                                    });
                                                                                                                } else {
                                                                                                                    Toast.makeText(ThemThanhVienActivity.this, "Thêm thành viên mới thất bại!", Toast.LENGTH_SHORT).show();
                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                                    }else{
                                                                                                        Toast.makeText(ThemThanhVienActivity.this, "ID này đã là thành viên!", Toast.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                                }
                                                                                            });
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                }
                                                                            });

                                                                        }else {
                                                                            Toast.makeText(ThemThanhVienActivity.this, "Bạn không thể tự thêm chính mình!", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });

                                                            boolCheckID = true;
                                                            break;
                                                        }
                                                    }
                                                    if (boolCheckID == false){
                                                        Toast.makeText(ThemThanhVienActivity.this, "ID không tồn tại!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }else{
                                            Toast.makeText(ThemThanhVienActivity.this, "Sai mã số bí mật. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }else{
                                Toast.makeText(ThemThanhVienActivity.this, "Bạn không có quyền thêm thành viên mới!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    Toast.makeText(ThemThanhVienActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String code_pr = mEDTMaSo.getText().toString().trim();
                final String ID = mEDTID.getText().toString().trim();
                if(code_pr.equals("") != true && ID.equals("") != true){
                    mUserDB.child(mUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User currentUser = dataSnapshot.getValue(User.class);
                            if(currentUser.getHasHouse().equals("true")){
                                mHouseDB.child(mUserID).child("code_private").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getValue().equals(code_pr)){
                                            mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for(DataSnapshot userData : dataSnapshot.getChildren()){
                                                        if(userData.getValue(User.class).getID().equals(ID)){
                                                            mUserDB.child(mUserID).child("ID").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                    if(dataSnapshot.getValue().toString().equals(ID) != true) {

                                                                        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                for (DataSnapshot userData : dataSnapshot.getChildren()) {
                                                                                    if (userData.getValue(User.class).getID().equals(ID)) {
                                                                                        mUserDownID = userData.getKey();
                                                                                        mFamilyDB.child(mUserID).child(mUserDownID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                if(dataSnapshot.hasChild("check_family") == true){
                                                                                                    mFamilyDB.child(mUserID).child(mUserDownID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                mFamilyDB.child(mUserDownID).child(mUserID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                                        Toast.makeText(ThemThanhVienActivity.this, "Xóa thành viên thành công!", Toast.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                });
                                                                                                            } else {
                                                                                                                Toast.makeText(ThemThanhVienActivity.this, "Xóa thành viên thất bại!", Toast.LENGTH_SHORT).show();
                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                                }else{
                                                                                                    Toast.makeText(ThemThanhVienActivity.this, "ID này chưa là thành viên!", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                                            }
                                                                                        });
                                                                                        break;
                                                                                    }
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                            }
                                                                        });

                                                                    }else {
                                                                        Toast.makeText(ThemThanhVienActivity.this, "Bạn không thể tự xóa chính mình!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });

                                                            boolCheckID = true;
                                                            break;
                                                        }
                                                    }
                                                    if (boolCheckID == false){
                                                        Toast.makeText(ThemThanhVienActivity.this, "ID không tồn tại!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }else{
                                            Toast.makeText(ThemThanhVienActivity.this, "Sai mã số bí mật. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }else{
                                Toast.makeText(ThemThanhVienActivity.this, "Bạn không có quyền xóa thành viên!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    Toast.makeText(ThemThanhVienActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void addFirebase() {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserID = mCurrentUser.getUid();
        mUserDB = FirebaseDatabase.getInstance().getReference().child("Users");
        mHouseDB = FirebaseDatabase.getInstance().getReference().child("Houses");
        mFamilyDB = FirebaseDatabase.getInstance().getReference().child("Families");
    }

    private void addControls() {
        mToolBarMHThemThanhVien = findViewById(R.id.tb_MHThemThanhVien);
        setSupportActionBar(mToolBarMHThemThanhVien);
        getSupportActionBar().setTitle("Thay Đổi Thành Viên");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEDTID = findViewById(R.id.edt_IDChuNha);
        mEDTMaSo = findViewById(R.id.edt_MaSoBiMat);
        mBtnThem = findViewById(R.id.btn_ThemNha);
        mBtnXoa = findViewById(R.id.btn_Xoa);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
