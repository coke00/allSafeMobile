package com.example.juancarlos.eppcontrol;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item){

               BottomNavFrag frag = new BottomNavFrag();
               Bundle bundle = new Bundle();


               switch(item.getItemId()){
                   case R.id.reporte:
                       bundle.putString("text","Fragment de reportes");
                       break;

                   case R.id.home:
                       bundle.putString("text","Epp Tracker APP");
                       break;

                   case R.id.entrega:
                       bundle.putString("text","Fragment de entregas");
                       break;
                   case R.id.firma:
                       Intent i = new Intent(MainActivity.this,FirmaActivity.class);
                       startActivity(i);
                       break;

               }
               frag.setArguments(bundle);
               FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
               transaction.replace(R.id.frame, frag);
               transaction.commit();
               return true;
           }
        });

        // default fragment
        BottomNavFrag initFrag = new BottomNavFrag();
        Bundle bundle = new Bundle();
        bundle.putString("text", "Epp Tracker APP");
        initFrag.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, initFrag);
        transaction.commit();


    }
}
