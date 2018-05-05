package com.quangannguyen.smartdoor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThemNhaActivity extends AppCompatActivity {

    private Toolbar mToolbarThemNha;
    private EditText mIDChuNha, mMaSoBiMat, mMaQR, mMaSoAdmin;
    private Button mThemNha, mXoaNha;

    private DatabaseReference mUserDB;
    private DatabaseReference mHouseDB;

    private ArrayList<House> mHouseLists;
    private ArrayList<User> mUserLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nha);
        addControls();
        addFirebase();
        addEvents();
    }

    private void addEvents() {

        mThemNha.setOnClickListener(view -> {
            final String IDChuNha = mIDChuNha.getText().toString().trim();
            final String MaSoBiMat = mMaSoBiMat.getText().toString().trim();
            final String MaQR = mMaQR.getText().toString().trim();
            String MaSoAdmin = mMaSoAdmin.getText().toString().trim();

            if (IDChuNha.equals("") || MaSoBiMat.equals("") || MaQR.equals("") || MaSoAdmin.equals("")) {
                Toast.makeText(ThemNhaActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!MaSoAdmin.equals("123456")) {
                Toast.makeText(ThemNhaActivity.this, "Sai mã số Admin!", Toast.LENGTH_SHORT).show();
                mXoaNha.setEnabled(true);
                return;
            }

            mXoaNha.setEnabled(false);
            mHouseDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot house = find(dataSnapshot.getChildren(), d -> {
                        Object id = d.child("ID").getValue();
                        return id != null && id.toString().equals(IDChuNha);
                    });
                    if (house != null) {
                        Toast.makeText(ThemNhaActivity.this, "Nhà của ID này đã tồn tại!", Toast.LENGTH_SHORT).show();
                        mXoaNha.setEnabled(true);
                        return;
                    }

                    mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DataSnapshot userData = find(dataSnapshot.getChildren(), d -> {
                                Object id = d.child("ID").getValue();
                                return id != null && id.toString().equals(IDChuNha);
                            });
                            if (userData == null) {
                                Toast.makeText(ThemNhaActivity.this, "Không tìm thấy user có ID này!", Toast.LENGTH_SHORT).show();
                                mXoaNha.setEnabled(true);
                                return;
                            }

                            User user = userData.getValue(User.class);
                            if (user != null && user.getID().equals(IDChuNha)) {
                                String userKey = userData.getKey();
                                Map<String, Object> newHouse = new HashMap<>();
                                newHouse.put("qr_code", MaQR);
                                newHouse.put("current_check", "false");
                                newHouse.put("code_private", MaSoBiMat);
                                newHouse.put("ID", IDChuNha);

                                mHouseDB.child(userKey).setValue(newHouse).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        mUserDB.child(userKey).child("hasHouse").setValue("true").addOnCompleteListener(task1 -> {
                                            if(task1.isSuccessful()){
                                                Toast.makeText(ThemNhaActivity.this, "Thêm thành công nhà của ID này!", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(ThemNhaActivity.this, "Thêm không thành công!", Toast.LENGTH_SHORT).show();
                                            }
                                            mXoaNha.setEnabled(true);
                                        });
                                    } else {
                                        Toast.makeText(ThemNhaActivity.this, "Thêm không thành công!", Toast.LENGTH_SHORT).show();
                                        mXoaNha.setEnabled(true);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mXoaNha.setEnabled(true);
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mXoaNha.setEnabled(true);
                }
            });

        });

        mXoaNha.setOnClickListener(view -> {

            mThemNha.setEnabled(false);
            final String IDChuNha = mIDChuNha.getText().toString().trim();
            final String MaSoAdmin = mMaSoAdmin.getText().toString().trim();
            if (IDChuNha.equals("") || MaSoAdmin.equals("")) {
                Toast.makeText(ThemNhaActivity.this, "Vui lòng nhập vào ID của chủ nhà!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!MaSoAdmin.equals("123456")) {
                Toast.makeText(ThemNhaActivity.this, "Sai mã số Admin!", Toast.LENGTH_SHORT).show();
                mXoaNha.setEnabled(true);
                return;
            }

            mHouseDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final DataSnapshot house = find(dataSnapshot.getChildren(), d -> {
                        Object id = d.child("ID").getValue();
                        return id != null && id.equals(IDChuNha);
                    });


                    if (house != null) {
                        final Object id = house.child("ID").getValue();

                        if (id != null && id.toString().equals(IDChuNha)) {
                            mHouseDB.child(house.getKey()).removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ThemNhaActivity.this, "Xóa thành công nhà của ID này!", Toast.LENGTH_SHORT).show();
                                    mUserDB.child(house.getKey()).child("hasHouse").setValue("false").addOnCompleteListener(task1 -> {
                                        if(task1.isSuccessful()){
                                            Toast.makeText(ThemNhaActivity.this, "Thêm thành công nhà của ID này!", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(ThemNhaActivity.this, "Thêm không thành công!", Toast.LENGTH_SHORT).show();
                                        }
                                        mThemNha.setEnabled(true);
                                    });
                                } else {
                                    Toast.makeText(ThemNhaActivity.this, "Xóa không thành công!", Toast.LENGTH_SHORT).show();
                                    mThemNha.setEnabled(true);
                                }

                            });
                        }
                    } else {

                        Toast.makeText(ThemNhaActivity.this, "Không tìm thấy nhà của ID này!", Toast.LENGTH_SHORT).show();
                        mThemNha.setEnabled(true);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mThemNha.setEnabled(true);
                }
            });
        });

    }

    private void addFirebase() {

        mUserDB = FirebaseDatabase.getInstance().getReference().child("Users");
        mHouseDB = FirebaseDatabase.getInstance().getReference().child("Houses");

    }

    private void addControls() {

        mToolbarThemNha = findViewById(R.id.tb_MHThemNha);
        setSupportActionBar(mToolbarThemNha);
        getSupportActionBar().setTitle("Thay Đổi Nhà");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIDChuNha = findViewById(R.id.edt_IDChuNha);
        mMaSoBiMat = findViewById(R.id.edt_MaSoBiMat);
        mMaQR = findViewById(R.id.edt_MaQR);
        mMaSoAdmin = findViewById(R.id.edt_MaSoAdmin);

        mThemNha = findViewById(R.id.btn_ThemNha);
        mXoaNha = findViewById(R.id.btn_XoaNha);


        mHouseLists = new ArrayList<>();
        mUserLists = new ArrayList<>();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private <T> T find(Iterable<T> iterable, Predicate<T> predicate) {
        for (T t : iterable) {
            if (predicate.test(t)) return t;
        }
        return null;
    }

    @FunctionalInterface
    interface Predicate<T> {
        boolean test(T t);
    }
}
