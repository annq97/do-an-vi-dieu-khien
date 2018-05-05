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

public class MHQuenMatKhauActivity extends AppCompatActivity {
    private Toolbar mToolBarMHQuenMatKhau;
    private EditText mEDTEMail;
    private Button mBtnXacNhan;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhquen_mat_khau);
        addControls();
        addFirebase();
        addEvents();
    }

    private void addFirebase() {

        mAuth = FirebaseAuth.getInstance();

    }

    private void addEvents() {
        mBtnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEDTEMail.getText().toString().trim();
                if(!email.equals("")) {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MHQuenMatKhauActivity.this, "Reset thành công. Vui lòng check hộp thư email của bạn!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MHQuenMatKhauActivity.this, "Reset mật khẩu không thành công!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(MHQuenMatKhauActivity.this, "Vui lòng nhập vào địa chỉ email của bạn", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addControls() {
        mToolBarMHQuenMatKhau = findViewById(R.id.tb_MHQuenMatKhau);
        setSupportActionBar(mToolBarMHQuenMatKhau);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quên mật khẩu");

        mEDTEMail = findViewById(R.id.edt_Email);
        mBtnXacNhan = findViewById(R.id.btn_XacNhan);
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
