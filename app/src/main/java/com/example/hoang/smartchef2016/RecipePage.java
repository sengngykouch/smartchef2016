package com.example.hoang.smartchef2016;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class RecipePage extends AppCompatActivity {
    private Button firstRec;
    private Button secondRecipe;
    private Button thirdRecipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);
        firstRec = (Button)findViewById(R.id.firstRecipe);
//        secondRecipe = (Button)findViewById(R.id.secondRecipe);
//        thirdRecipe = (Button)findViewById(R.id.thirdRecipe);

        firstRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecipePage.this, FirstRecipe.class);
                startActivity(i);
                //VisionServiceClient VisionServiceClient = new VisionServiceClient("85b66286720840e28dc1cc4f26b717fe");
            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
