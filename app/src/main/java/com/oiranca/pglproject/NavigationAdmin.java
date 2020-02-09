package com.oiranca.pglproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;



import android.view.MenuItem;

import androidx.navigation.NavController;

import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;


import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class NavigationAdmin extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;
    private TextView textView;
    View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_navigation_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);


        headerView=navigationView.getHeaderView(0);
        textView = headerView.findViewById(R.id.textNav);


        Intent idUser = this.getIntent();
        Bundle user = idUser.getExtras();
        String emailUser;

        assert user != null;
        emailUser = user.getString("Admin");

        if (emailUser!=null){
            textView.setText(emailUser);
        }else {
            emailUser=user.getString("Family");
            textView.setText(emailUser);
        }



        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_activity, R.id.nav_newF,R.id.nav_newreports,R.id.nav_reports_month, R.id.nav_my_activity)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_admin, menu);


        return true;
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.action_back:



                Intent back = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(back);
                return true;



            case R.id.action_settings:

                Toast.makeText(getApplicationContext(), "Pendiente de configurar", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }




    }
}
