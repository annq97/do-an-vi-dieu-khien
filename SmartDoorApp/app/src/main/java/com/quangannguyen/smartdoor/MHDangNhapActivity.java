package com.quangannguyen.smartdoor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MHDangNhapActivity extends AppCompatActivity {
    private EditText mEmail,mMatKhau;
    private Button mDangNhap,mDangKy;
    private TextView mQuenMatKhau;

    private ProgressDialog mDangNhapProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDB;
    private String mUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhdang_nhap);
        addControls();
        addFirebases();
        addEvents();
    }

    private void addFirebases() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void addEvents() {

        mDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String matKhau = mMatKhau.getText().toString().trim();

                if(email.equals("") || matKhau.equals("")){
                    Toast.makeText(MHDangNhapActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                else{
                    mDangNhapProgress.setTitle("Đăng nhập tài khoản");
                    mDangNhapProgress.setMessage("Chúng tôi đang xác thực tài khoản của bạn. Vui lòng đợi trong giây lát!");
                    mDangNhapProgress.show();
                    dangnhap_user(email,matKhau);
                }
            }
        });

        mDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_ChuyenMHDangKy = new Intent(MHDangNhapActivity.this,MHDangKyActivity.class);
                startActivity(intent_ChuyenMHDangKy);
            }
        });

        mQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_ChuyenMHQuenMatKhau = new Intent(MHDangNhapActivity.this,MHQuenMatKhauActivity.class);
                startActivity(intent_ChuyenMHQuenMatKhau);
            }
        });

    }

    private void dangnhap_user(String email, String matKhau) {

        mAuth.signInWithEmailAndPassword(email,matKhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    mUserDB  = FirebaseDatabase.getInstance().getReference().child("Users");
                    mUserID  = mAuth.getCurrentUser().getUid();

                    mUserDB.child(mUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User currentUser = dataSnapshot.getValue(User.class);
                            if(currentUser.getAdmin().equals("true")){
                                mDangNhapProgress.dismiss();
                                Intent intent_ChuyenMHMainAdmin = new Intent(MHDangNhapActivity.this,MHMainAdminActivity.class);
                                intent_ChuyenMHMainAdmin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent_ChuyenMHMainAdmin);
                                finish();
                            }else{
                                mDangNhapProgress.dismiss();
                                Intent intent_ChuyenMHMain = new Intent(MHDangNhapActivity.this,MainActivity.class);
                                intent_ChuyenMHMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent_ChuyenMHMain);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else{
                    mDangNhapProgress.hide();
                    Toast.makeText(MHDangNhapActivity.this, "Đã xãy ra lỗi khi đăng nhập", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addControls() {
        mEmail = findViewById(R.id.edt_Email);
        mMatKhau = findViewById(R.id.edt_MatKhau);
        mDangNhap = findViewById(R.id.btn_DangNhap);
        mDangKy = findViewById(R.id.btn_DangKy);
        mQuenMatKhau = findViewById(R.id.tv_QuenMatKhau);

        mDangNhapProgress = new ProgressDialog(this);
    }
}
