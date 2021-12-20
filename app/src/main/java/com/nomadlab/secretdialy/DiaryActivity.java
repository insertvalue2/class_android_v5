package com.nomadlab.secretdialy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class DiaryActivity extends AppCompatActivity {

    EditText diaryEditText;
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        //
        SharedPreferences detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE);
        diaryEditText = findViewById(R.id.diaryEditText);
        diaryEditText.setText(detailPreferences.getString("detail", ""));

        // thread 기능 구현
        Runnable runnable = () -> {
            SharedPreferences preferences =  getSharedPreferences("diary", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("detail", diaryEditText.getText().toString());
            editor.apply();
        };

        diaryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 이전에 있는 녀셕이 있다면 지워주고 0.5초뒤에 동작 하게 한다.
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 500);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}