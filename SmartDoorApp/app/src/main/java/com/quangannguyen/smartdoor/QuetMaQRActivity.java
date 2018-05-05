package com.quangannguyen.smartdoor;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import info.androidhive.barcode.BarcodeReader;


public class QuetMaQRActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {


    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDB;
    private DatabaseReference mHouseDB;
    private DatabaseReference mFamilyDB;
    private DatabaseReference mHistoryDB;
    private DatabaseReference mError_ScanDB;
    private String mUserID, mUserTopID;

    private BarcodeReader barcodeReader;
    private Toolbar mTbMHQuetMAQR;

    private Integer num_error;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        addFirebase();
        setContentView(R.layout.activity_quet_ma_qr);
        mTbMHQuetMAQR = findViewById(R.id.tb_MHQuetMaQR);
        setSupportActionBar(mTbMHQuetMAQR);
        getSupportActionBar().setTitle("Quét QRCode");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);

    }

    private void addFirebase() {

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserID = mCurrentUser.getUid();

        mUserDB = FirebaseDatabase.getInstance().getReference().child("Users");
        mHouseDB = FirebaseDatabase.getInstance().getReference().child("Houses");
        mHistoryDB = FirebaseDatabase.getInstance().getReference().child("Histories");
        mFamilyDB = FirebaseDatabase.getInstance().getReference().child("Families").child(mUserID);
        mError_ScanDB = FirebaseDatabase.getInstance().getReference().child("Error_Scan").child(mUserID);

    }



    private void check_qrcode(final String content) {

        if(content.equals("") != true){

            mUserDB.child(mUserID).child("hasHouse").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String check_hasHouse = dataSnapshot.getValue().toString();
                    if(check_hasHouse.equals("true")){

                        mHouseDB.child(mUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                House currentHouse = dataSnapshot.getValue(House.class);

                                if(currentHouse.getCurrent_check().equals("false")){

                                    if(currentHouse.getQr_code().equals(content) == true){

                                        mHouseDB.child(mUserID).child("current_check").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                add_history(mUserID, "", "true");
                                            }
                                        });

                                    }else{
                                        add_history(mUserID, "", "false");
                                        mError_ScanDB.child("num_error").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                num_error = Integer.valueOf(dataSnapshot.getValue().toString());

                                                num_error += 1;

                                                mError_ScanDB.child("num_error").setValue(num_error);

                                                if(num_error < 3){
                                                    Toast.makeText(QuetMaQRActivity.this, "Sai QRCode!", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(QuetMaQRActivity.this, "Sai QRCode!", Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(QuetMaQRActivity.this, "Bạn đã quét sai quá nhiều lần. Tính năng này bị khóa tạm thời!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                }else{
                                    Toast.makeText(QuetMaQRActivity.this, "Cửa nhà của bạn đã được mở ra rồi!", Toast.LENGTH_SHORT).show();
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
                                        mHouseDB.child(mUserTopID).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.getValue() != null){
                                                    House currentHouse = dataSnapshot.getValue(House.class);

                                                    if(currentHouse.getCurrent_check().equals("false")){

                                                        if(currentHouse.getQr_code().equals(content) == true){

                                                            mHouseDB.child(mUserTopID).child("current_check").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    add_history(mUserTopID, mUserID, "true");
                                                                }
                                                            });

                                                        }else{
                                                            add_history(mUserTopID, mUserID, "false");
                                                            mError_ScanDB.child("num_error").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    num_error = Integer.valueOf(dataSnapshot.getValue().toString());

                                                                    num_error += 1;

                                                                    mError_ScanDB.child("num_error").setValue(num_error);

                                                                    if(num_error < 3){
                                                                        Toast.makeText(QuetMaQRActivity.this, "Sai QRCode!", Toast.LENGTH_SHORT).show();
                                                                    }else{
                                                                        Toast.makeText(QuetMaQRActivity.this, "Sai QRCode!", Toast.LENGTH_SHORT).show();
                                                                        Toast.makeText(QuetMaQRActivity.this, "Bạn đã quét sai quá nhiều lần. Tính năng này bị khóa tạm thời!", Toast.LENGTH_SHORT).show();
                                                                        finish();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }

                                                    }else{
                                                        Toast.makeText(QuetMaQRActivity.this, "Cửa nhà của bạn đã được mở ra rồi!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                }else{
                                    Toast.makeText(QuetMaQRActivity.this, "Bạn không có nhà để mở khóa!", Toast.LENGTH_SHORT).show();
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


        }else{
            Toast.makeText(this, "Quét QRCode thất bại!", Toast.LENGTH_SHORT).show();
        }

        return;
    }


    private void add_history(final String mParentUserID, String mSubUserID, String state) {

        final String ts = DateFormat.getDateTimeInstance().format(new Date());

        if(mSubUserID.equals("")){

            mUserDB.child(mParentUserID).child("ID").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HistoryFB mCurrentHistoryFB = new HistoryFB(dataSnapshot.getValue().toString(), ts, state);
                    mHistoryDB.child(mParentUserID).push().setValue(mCurrentHistoryFB).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                if(state.equals("true")) {
                                    Toast.makeText(QuetMaQRActivity.this, "Mở khóa thành công!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(QuetMaQRActivity.this, "Mở khóa thất bại!", Toast.LENGTH_SHORT).show();
                                }
                                finish();
                            }else{
                                Toast.makeText(QuetMaQRActivity.this, "Mở khóa thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else{
            mUserDB.child(mSubUserID).child("ID").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HistoryFB mCurrentHistoryFB = new HistoryFB(dataSnapshot.getValue().toString(), ts, state);
                    mHistoryDB.child(mParentUserID).push().setValue(mCurrentHistoryFB).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(QuetMaQRActivity.this, "Mở khóa thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(QuetMaQRActivity.this, "Mở khóa thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    @Override
    public void onScanned(final Barcode barcode) {
        barcodeReader.playBeep();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                check_qrcode(barcode.displayValue);
            }
        });
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Toast.makeText(QuetMaQRActivity.this, "Quét Mã QR thất bại!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(QuetMaQRActivity.this, "Camera permission denied!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
