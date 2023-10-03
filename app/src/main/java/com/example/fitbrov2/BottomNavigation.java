package com.example.fitbrov2;

import static com.example.fitbrov2.R.color.teal_700;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.Stack;

public class BottomNavigation extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bottom_navigation);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(teal_700)));
        getSupportActionBar().setTitle("Home");

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentHome()).commit();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch(item.getItemId()){
                    case R.id.item1:
                        fragment = new FragmentHome();
                        getSupportActionBar().setTitle("Home");
                        break;
                    case R.id.item2:
                        fragment = new FragmentCalorieRecord1();
                        getSupportActionBar().setTitle("Calorie Record");
                        break;
                    case R.id.item3:
                        fragment = new FragmentHistory();
                        getSupportActionBar().setTitle("History");
                        break;
                    case R.id.item4:
                        fragment = new FragmentCalculate();
                        getSupportActionBar().setTitle("OCI Calculation");
                        break;
                }
                for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++){
                    getSupportFragmentManager().popBackStack();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.fragment_container))).commit();
            getSupportFragmentManager().popBackStack();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Are you sure you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                }
            });
            builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert=builder.create();
            alert.show();
        }
    }
}

