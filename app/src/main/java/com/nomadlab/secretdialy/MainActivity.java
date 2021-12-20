package com.nomadlab.secretdialy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {

    private NumberPicker numberPicker1;
    private NumberPicker numberPicker2;
    private NumberPicker numberPicker3;
    private Button openButton;
    private Button changePasswordButton;
    private Boolean changePasswordMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        addEventListener();
    }

    private void initData() {
        numberPicker1 = findViewById(R.id.numberPicker1);
        numberPicker2 = findViewById(R.id.numberPicker2);
        numberPicker3 = findViewById(R.id.numberPicker3);
        numberPicker1.setMaxValue(9);
        numberPicker1.setMinValue(0);
        numberPicker2.setMaxValue(9);
        numberPicker2.setMinValue(0);
        numberPicker3.setMaxValue(9);
        numberPicker3.setMinValue(0);
        openButton = findViewById(R.id.openButton);
        changePasswordButton = findViewById(R.id.changePasswordButton);
    }


    private void addEventListener() {
        // 비밀 다이어리 오픈 버튼 눌렀을 경우 동작
        openButton.setOnClickListener(view -> {
            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경중 입니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            // 저장되어 있는 패스워드 값을 가져 와서 확인 해야 한다. (N.P)
            // 패스워드 저장하는 방식 - 로컬 DB, 파일에 저장하는 방식
            // name : 파일에 이름
            // mode : 파일을 다른 앱과 공유하게 만들 수 있다.
            SharedPreferences passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE);
            String passwordFormUser = "" + numberPicker1.getValue() +
                    numberPicker2.getValue() +
                    numberPicker3.getValue();

            // 값을 셋팅 하지 않았을 경우 초기값 셋팅
//            passwordPreferences.getString("password", "000");
            if (passwordPreferences.getString("password", "000").equals(passwordFormUser)) {
                // 패스워트 성공
                // TODO 다이어리 페이지 작성 후에 넘겨 주어야 함.
                Intent intent = new Intent(this, DiaryActivity.class);
                startActivity(intent);
            } else {
                // 실패
                showErrorAlertDialog();
            }
        });

        // 비밀번호 변경 버튼 눌렀을 경우 동작
        changePasswordButton.setOnClickListener(view -> {
            SharedPreferences passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE);
            String passwordFormUser = "" + numberPicker1.getValue() +
                    numberPicker2.getValue() +
                    numberPicker3.getValue();

            if (changePasswordMode) {
                // 번호를 저장하는 기능
                SharedPreferences.Editor editor = passwordPreferences.edit();
                editor.putString("password", passwordFormUser);
                // apply(비동기 방식), commit (UI 멈추고 기다리는 방식)
                editor.apply();
                changePasswordMode = false;
                changePasswordButton.setBackgroundColor(Color.BLACK);
            } else {
                // changePasswordMode 가 활성화 : 조건 -> 비밀번호가 맞는지 체크
                if (passwordPreferences.getString("password", "000").equals(passwordFormUser)) {
                    changePasswordMode = true;
                    Toast.makeText(this, getString(R.string.str_change_password), Toast.LENGTH_SHORT).show();
                    changePasswordButton.setBackgroundColor(Color.RED);

                } else {
                    showErrorAlertDialog();
                }
            }
        });
    }

    private void showErrorAlertDialog() {
        // 실패
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("실패").
                setMessage("비밀번호가 잘못되었습니다.")
                .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 동작 정의 하지 않음
                    }
                });
        builder.show();
    }

}