package com.boruili.path_nav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void trainPath(View view) {
//        Intent intent = new Intent(this, TrainActivity.class);
        Intent intent = new Intent(this, ListTrainActivity.class);
        startActivity(intent);
    }

    public void navPath(View view) {
                Intent intent = new Intent(this, ListNavActivity.class);

        startActivity(intent);

    }
}
