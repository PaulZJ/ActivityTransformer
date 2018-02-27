package com.zj.transform;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MOVIE_LOCATION = "view_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_next_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rectView = findViewById(R.id.rect_view);
                int[] location = new int[2];
                rectView.getLocationOnScreen(location);
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra(EXTRA_MOVIE_LOCATION, location);
                startActivity(intent);
            }
        });

    }
}
