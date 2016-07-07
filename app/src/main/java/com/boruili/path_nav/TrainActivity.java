package com.boruili.path_nav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TrainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
    }

    @Override
    public void onBackPressed() {

        // Otherwise defer to system default behavior.
        super.onBackPressed();
    }

    public void trainPath1(View view) {
        Intent intent = new Intent(this, TrainPathActivity.class);
        intent.putExtra("FILE_NAME", "train1");
        startActivity(intent);
    }

    public void trainPath2(View view) {
        Intent intent = new Intent(this, TrainPathActivity.class);
        intent.putExtra("FILE_NAME", "train2");
        startActivity(intent);
    }

    public void trainPath3(View view) {
        Intent intent = new Intent(this, TrainPathActivity.class);
        intent.putExtra("FILE_NAME", "train3");
        startActivity(intent);
    }
}
