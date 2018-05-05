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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DoiMatKhauActivity extends AppCompatActivity {

    private Toolbar mToolbarMHDoiMatKhau;
    private EditText mEDTMatKhauCu, mEDTMatKhauMoi, mEDTXacNhanMatKhauMoi;
    private Button mBtnXacNhan;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);
        addControls();
        addFirebase();
        addEvents();
    }

    private void addFirebase() {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
    }

    private void addEvents() {

        mBtnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String matKhauCu = mEDTMatKhauCu.getText().toString().trim();
                final String matKhauMoi = mEDTMatKhauMoi.getText().toString().trim();
                final String xacNhanMatKhauMoi = mEDTXacNhanMatKhauMoi.getText().toString().trim();
                final String email = mCurrentUser.getEmail().toString();
                if(matKhauCu.equals("") != true && matKhauMoi.equals("") != true && xacNhanMatKhauMoi.equals("") != true ) {

                    if (matKhauMoi.equals(xacNhanMatKhauMoi)) {

                        mAuth.signInWithEmailAndPassword(email, matKhauCu).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mCurrentUser.updatePassword(matKhauMoi).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(DoiMatKhauActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(DoiMatKhauActivity.this, "Đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(DoiMatKhauActivity.this, "Đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(DoiMatKhauActivity.this, "Mật khẩu mới không trùng khớp!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(DoiMatKhauActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addControls() {
        mToolbarMHDoiMatKhau = findViewById(R.id.tb_MHDoiMatKhau);
        setSupportActionBar(mToolbarMHDoiMatKhau);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đổi Mật Khẩu");

        mEDTMatKhauCu = findViewById(R.id.edt_MatKhauCu);
        mEDTMatKhauMoi = findViewById(R.id.edt_MatKhauMoi);
        mEDTXacNhanMatKhauMoi = findViewById(R.id.edt_XacNhanMatKhauMoi);
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
