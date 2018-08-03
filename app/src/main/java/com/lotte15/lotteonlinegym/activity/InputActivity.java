package com.lotte15.lotteonlinegym.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lotte15.lotteonlinegym.R;

public class InputActivity extends AppCompatActivity {

    private EditText mEditUserName;
    private TextView mUserName;

    private EditText mEditUserGender;
    private TextView mUserGender;

    private EditText mEditUserAge;
    private TextView mUserAge;

    private EditText mEditUserWeight;
    private TextView mUserWeight;

    private EditText mEditUserHeight;
    private TextView mUserHeight;

    private Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        initUi();
    }

    private void initUi(){
        mUserName = (TextView) findViewById(R.id.textview_username);
        mEditUserName = (EditText) findViewById(R.id.edittext_username);

        mUserGender = (TextView)findViewById(R.id.textview_user_gender);
        mEditUserGender = (EditText) findViewById(R.id.edittext_user_gender);

        mUserAge = (TextView)findViewById(R.id.textview_user_age);
        mEditUserAge = (EditText) findViewById(R.id.edittext_user_age);

        mUserWeight = (TextView)findViewById(R.id.textview_user_weight);
        mEditUserWeight = (EditText) findViewById(R.id.edittext_user_weight);

        mUserHeight = (TextView)findViewById(R.id.textview_user_height);
        mEditUserHeight = (EditText) findViewById(R.id.edittext_user_height);

        mSubmit = (Button) findViewById(R.id.button_submit);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 사용자 정보 post
                Intent intent=new Intent(InputActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
