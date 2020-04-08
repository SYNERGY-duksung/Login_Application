package com.example.login_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    public static String id;
    public static String password;
    public static String info;
    static int AutoLogin = 0;
    public static ProgressDialog mProgress;
    public static int LoginResult;
    public static Context mContext;
    static int Tab_Num = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        Button btn1 = findViewById(R.id.btn_login);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = ((EditText) findViewById(R.id.et_student_id)).getText().toString();
                password = ((EditText) findViewById(R.id.et_password)).getText().toString();

                if (id.length() < 6 || password.length() < 2) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("올바른 ID 또는 비밀번호를 입력해주세요.");
                    alert.show();
                } else {
                    loadingNowGrade();
                }
            }
        });
    }

    private <Type> void loadingNowGrade(){
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("로그인 중...");
        mProgress.setMessage("학교서버에서 데이터를 불러옵니다.");
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
        mProgress.show();
        mProgress.setContentView(R.layout.custom_progress);

        Thread thread = new Thread(){
            public void run(){
                Looper.prepare();
                LoginResult = 1;
                Parser parser = new Parser(id, password, mContext);
                parser.toParsing();
                Looper.loop();
            }
        };
        thread.start();
    }



}
