package com.quangannguyen.smartdoor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MHDangKyActivity extends AppCompatActivity {
    private Toolbar mToolBarMHDangKy;
    private EditText mHoTen,mEmail,mMatKhau,mNhapLaiMatKhau, mMaSoAdmin;
    private Button mDangKy;
    private ProgressDialog mDangKyProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDB;
    private ArrayList<User> user_lists;

    private Boolean check_ID = false;
    private Random random_ID = new Random();
    private String current_user_ID = random_ID.nextInt(1000000) + "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhdang_ky);
        addControls();
        addFirebases();
        addEvents();
        

    }

    private void addFirebases() {
        mAuth = FirebaseAuth.getInstance();
        mUserDB = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    private void addEvents() {
        mDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hoTen = mHoTen.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String matKhau = mMatKhau.getText().toString().trim();
                String nhapLaiMatKhau = mNhapLaiMatKhau.getText().toString().trim();
                String maSoAdmin = mMaSoAdmin.getText().toString().trim();
                if(hoTen.equals("") || email.equals("") || matKhau.equals("") || nhapLaiMatKhau.equals("")){
                    Toast.makeText(MHDangKyActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                else{

                    if(matKhau.equals(nhapLaiMatKhau)){

                        mDangKyProgress.setTitle("Đăng ký tài khoản");
                        mDangKyProgress.setMessage("Chúng tôi đang đăng ký tài khoản của bạn. Vui lòng đợi trong giây lát!");
                        mDangKyProgress.show();
                        dangky_user(hoTen,email,matKhau);
                    }
                    else{
                        Toast.makeText(MHDangKyActivity.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void dangky_user(final String hoTen, String email, String matKhau) {
        mAuth.createUserWithEmailAndPassword(email, matKhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser current_User = mAuth.getCurrentUser();
                    final String userID = current_User.getUid();

                    String ID = checkID_User();

                    final HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", hoTen);
                    userMap.put("image", "default");
                    userMap.put("ID",ID);
                    userMap.put("MaSo","default");
                    userMap.put("hasHouse","false");
                    if(mMaSoAdmin.getText().toString().trim().equals("123456")){
                        userMap.put("admin","true");
                        mUserDB.child(userID).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    FirebaseDatabase.getInstance().getReference().child("Error_Scan").child(userID).child("num_error").setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                mDangKyProgress.dismiss();
                                                Intent intent_MHDangNhap = new Intent(MHDangKyActivity.this,MHDangNhapActivity.class);
                                                intent_MHDangNhap.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent_MHDangNhap);
                                                mAuth.signOut();
                                                finish();
                                            }
                                        }
                                    });

                                }else{
                                    mDangKyProgress.hide();
                                    Toast.makeText(MHDangKyActivity.this, "Đã xãy ra lỗi khi đăng ký", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        userMap.put("admin","false");
                        mUserDB.child(userID).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    FirebaseDatabase.getInstance().getReference().child("Error_Scan").child(userID).child("num_error").setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                mDangKyProgress.dismiss();
                                                Intent intent_MHDangNhap = new Intent(MHDangKyActivity.this,MHDangNhapActivity.class);
                                                intent_MHDangNhap.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent_MHDangNhap);
                                                mAuth.signOut();
                                                finish();
                                            }
                                        }
                                    });


                                }else{
                                    mDangKyProgress.hide();
                                    Toast.makeText(MHDangKyActivity.this, "Đã xãy ra lỗi khi đăng ký", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
                else {
                    mDangKyProgress.hide();
                    Toast.makeText(MHDangKyActivity.this, "Đã xãy ra lỗi khi đăng ký", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String checkID_User() {

        user_lists = new ArrayList<User>();

        mUserDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
                    user_lists.add(user);
                }

                while(true) {
                    for (User user_item : user_lists) {
                        if (user_item.getID().equals(current_user_ID)) {
                            current_user_ID = random_ID.nextInt(1000000) + "";
                            break;
                        }
                    }

                    break;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return current_user_ID;
    }

    private void addControls() {
        mToolBarMHDangKy = findViewById(R.id.tb_MHDangKy);
        setSupportActionBar(mToolBarMHDangKy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đăng ký");

        mHoTen = findViewById(R.id.edt_HoTen);
        mEmail = findViewById(R.id.edt_Email);
        mMatKhau = findViewById(R.id.edt_MatKhau);
        mNhapLaiMatKhau = findViewById(R.id.edt_NhapLaiMatKhau);
        mMaSoAdmin = findViewById(R.id.edt_MaSoAdmin);
        mDangKy = findViewById(R.id.btn_DangKy);

        mDangKyProgress = new ProgressDialog(this);

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
