package com.myapp.royalcounselling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.myapp.royalcounselling.ui.AboutRoyalActivity;
import com.myapp.royalcounselling.ui.ContactUsFragment;
import com.myapp.royalcounselling.ui.DisplayRegisteredSeminarFragment;
import com.myapp.royalcounselling.ui.DisplaySeminarFragment;
import com.myapp.royalcounselling.ui.PPTRequestFragment;
import com.myapp.royalcounselling.ui.PersonalCounsellingFragment;
import com.myapp.royalcounselling.ui.PersonalCounsellingRequestFragement;
import com.myapp.royalcounselling.ui.WhyCounsellingActivity;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    View header;
    TextView email;

    public NavigationDrawerActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.nav_app_bar_open_drawer_description,
                R.string.nav_app_bar_open_drawer_description);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        actionBarDrawerToggle.syncState();

        header = navigationView.getHeaderView(0);
        email = header.findViewById(R.id.nav_email);

        SharedPreferences sharedPreferences = getSharedPreferences("MYAPP", MODE_PRIVATE);
        String email1 = sharedPreferences.getString("KEY_EMAIL", "");
        Intent i = getIntent();
        String loadFragement;
        try {
            loadFragement = i.getExtras().getString("activityToDirect");
        } catch (Exception e) {
            loadFragement = "";
        }
        //Toast.makeText(this,"Loading Fra "+loadFragement,Toast.LENGTH_SHORT).show();
        navigationView.setNavigationItemSelectedListener(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        try {
            if (loadFragement.equals("PPTRequest")) {
                fragment = new PPTRequestFragment();
            } else if (loadFragement.equals("SeminarRegistered")) {
                fragment = new DisplayRegisteredSeminarFragment();
            } else if (loadFragement.equals("CounsellingRequested")) {
                fragment = new PersonalCounsellingRequestFragement();
            } else {
                //    Toast.makeText(this,"ELse",Toast.LENGTH_SHORT).show();
                fragment = new WhyCounsellingActivity();
            }
        } catch (Exception e) {
            //Toast.makeText(this,"Exception",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            fragment = new WhyCounsellingActivity();
        }

        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        Fragment fragment = null;
        if (id == R.id.nav_about_us) {
            fragment = new AboutRoyalActivity();

        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("MYAPP", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("KEY_NAME");
            editor.remove("KEY_EMAIL");
            editor.remove("KEY_PASSWORD");
            editor.remove("TOKEN");
            editor.remove("KEY_CREDITS");

            editor.apply();
            Intent i = new Intent(NavigationDrawerActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_why_counselling) {
            fragment = new WhyCounsellingActivity();
        } else if (id == R.id.nav_contact_us) {
            fragment = new ContactUsFragment();
        } else if (id == R.id.nav_view_seminar) {
            fragment = new DisplaySeminarFragment();
        } else if (id == R.id.nav_view_registered_seminar) {
            fragment = new DisplayRegisteredSeminarFragment();
        } else if (id == R.id.nav_view_personal_counselling) {
            fragment = new PersonalCounsellingFragment();
        } else if (id == R.id.nav_view_ppt_requests) {
            fragment = new PPTRequestFragment();
        } else if (id == R.id.nav_view_personal_counselling_request) {
            fragment = new PersonalCounsellingRequestFragement();
        }

        if (fragment != null) {


            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
}