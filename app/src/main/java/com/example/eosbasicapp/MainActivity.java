package com.example.eosbasicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageButton addContact;
    private ImageButton contact;
    private TextView phoneNum;
    private TextView[] dials = new TextView[10];
    private TextView star;
    private TextView sharp;
    private ImageButton message;
    private ImageButton call;
    private ImageButton backspace;

    private TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        setUpUi();

        if(phoneNum.getText().length()==0){
            message.setVisibility(View.GONE);
            backspace.setVisibility(View.GONE);
        }
    }

    private void checkPermissions(){
        int resultCall = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int resultSms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if(resultCall == PackageManager.PERMISSION_DENIED||resultSms == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, 1104);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1104){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "권한 허용됨 ", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this,"권한 허용이 필요합니다. 설정에서 허용을 해주세요.",Toast.LENGTH_SHORT).show();
                Log.d("PermissionDenied", "권한이 거부되어 앱을 종료합니다.");
                finish();
            }
        }
    }

    private void setUpUi() {

        name = findViewById(R.id.main_tv_name);

        addContact = findViewById(R.id.main_ibtn_add);
        contact = findViewById(R.id.main_ibtn_contact);
        phoneNum = findViewById(R.id.main_tv_phone);

        for (int i = 0; i < dials.length; i++) {
            dials[i] = findViewById(getResourceID("main_tv_" + i, "id", this));
        }
        star = findViewById(R.id.main_tv_star);
        sharp = findViewById(R.id.main_tv_sharp);
        message = findViewById(R.id.main_ibtn_message);
        call = findViewById(R.id.main_ibtn_call);
        backspace = findViewById(R.id.main_ibtn_backspace);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(MainActivity.this, AddEditActivity.class);
                addIntent.putExtra("phone_num",phoneNum.getText().toString());
                addIntent.putExtra("add_edit","add");
                startActivity(addIntent);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Intent contactIntent = new Intent(MainActivity.this, ContactActivity.class);
                                           startActivity(contactIntent);
                                       }
                                   });

        setOnClickDial(star, "*");
        setOnClickDial(sharp, "#");

        for (int i = 0; i < 10; i++) {
            setOnClickDial(dials[i], String.valueOf(i));
        }

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent messageIntent = new Intent(MainActivity.this, MessageActivity.class);
                messageIntent.putExtra("phone_num",phoneNum.getText().toString());
                startActivity(messageIntent);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum.getText()));
                startActivity(callIntent);
            }
        });

        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneNum.getText().length() > 0) {
                    if (phoneNum.getText().length() == 5 || phoneNum.getText().length() == 10) {
                        phoneNum.setText(changeToDial(phoneNum.getText().subSequence(0, phoneNum.getText().length() - 1).toString()));
                    }
                    phoneNum.setText(changeToDial(phoneNum.getText().subSequence(0, phoneNum.getText().length() - 1).toString()));
                    if(phoneNum.getText().length()==0){
                        message.setVisibility(View.GONE);
                        backspace.setVisibility(View.GONE);
                    }
                }
                findPhone();
            }
        });

        backspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                phoneNum.setText("");

                message.setVisibility(View.GONE);
                backspace.setVisibility(View.GONE);

                findPhone();
                return true;
            }
        });
    }

    private void setOnClickDial(View view, final String input) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNum.setText(changeToDial(phoneNum.getText() + input));

                message.setVisibility(View.VISIBLE);
                backspace.setVisibility(View.VISIBLE);

                findPhone();
            }
        });
    }

    private void findPhone() {
        String find = phoneNum.getText().toString().replaceAll("-", "");
        StringBuilder addName = new StringBuilder("");
        int num = 0;
        for (int i = 0; i < DummyDate.contacts.size(); i++) {
            if (DummyDate.contacts.get(i).getPhone().replaceAll("-", "").contains(find)) {
                name.setText(DummyDate.contacts.get(i).getName());
                addName.append(DummyDate.contacts.get(i).getName());
                addName.append(" ");
                num++;
            }
        }
        if(num == 0){
            name.setText("");
        } else if(num > 1){
            name.setText(addName);
        }
        num = 0;
    }

    private int getResourceID(final String resName, final String resType, final Context ctx) {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType, ctx.getApplicationInfo().packageName);
        if (ResourceID == 0) {
            throw new IllegalArgumentException("No resource string found with name " + resName);
        } else {
            return ResourceID;
        }
    }

    private String changeToDial(String phoneNum) {

        // 전화번호 기준 01037440834
        //4글자 이상일때 3번째 숫자 다음에 010-
        //8글자 이상일때 3번째 다음과 7번째 다음에 010-3744-0
        //12글자 이상일때 하이픈 전부 제거
        //특수문자있으면 하이픈 전부 제거
        if (phoneNum.length() >= 4) {
            if(phoneNum.indexOf("-")==3){
            }
            else{
                phoneNum = phoneNum.substring(0, 3) + "-" + phoneNum.substring(3, phoneNum.length());
            }
        }
        if(phoneNum.length()==12){
            phoneNum=phoneNum.replaceAll("-","");
            phoneNum = phoneNum.substring(0, 3) + "-" + phoneNum.substring(3, phoneNum.length());
            phoneNum = phoneNum.substring(0, 7) + "-" + phoneNum.substring(7, phoneNum.length());
        }
        if (phoneNum.length() >=9&&phoneNum.length()!=12){
            if(phoneNum.indexOf("-",8)==8){
            }
            else{
                phoneNum=phoneNum.replaceAll("-","");
                phoneNum = phoneNum.substring(0, 3) + "-" + phoneNum.substring(3, phoneNum.length());
                phoneNum = phoneNum.substring(0,8) + "-" + phoneNum.substring(8, phoneNum.length());
            }
        }
        if(phoneNum.length()>=14){
            phoneNum=phoneNum.replaceAll("-","");
        }
        if(phoneNum.contains("*")||phoneNum.contains("#")){
            phoneNum=phoneNum.replaceAll("-","");
        }
        return phoneNum;
    } 
    }