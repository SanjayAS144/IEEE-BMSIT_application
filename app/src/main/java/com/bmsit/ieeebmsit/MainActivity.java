package com.bmsit.ieeebmsit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbarMain;
    private TabLayout tabLayoutMain;
    private ViewPager viewPagerMain;

    private MainFragment mainFragment;
    private WhyFragment whyFragment;
    private AboutFragment aboutFragment;

    FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth=FirebaseAuth.getInstance();
        toolbarMain = findViewById(R.id.toolbar_main);
        viewPagerMain = findViewById(R.id.view_pager_main);
        tabLayoutMain = findViewById(R.id.tab_layout_main);

        setSupportActionBar(toolbarMain);
        getSupportActionBar().setTitle("");

        mainFragment = new MainFragment();
        whyFragment = new WhyFragment();
        aboutFragment = new AboutFragment();

        tabLayoutMain.setupWithViewPager(viewPagerMain);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(mainFragment, "Events");
        viewPagerAdapter.addFragment(whyFragment, "Join Now");
        viewPagerAdapter.addFragment(aboutFragment, "About Us");
        viewPagerMain.setAdapter(viewPagerAdapter);

        tabLayoutMain.getTabAt(0).setIcon(R.drawable.ic_favorite_black_24dp);
        tabLayoutMain.getTabAt(1).setIcon(R.drawable.ic_explore_black_24dp);
        tabLayoutMain.getTabAt(2).setIcon(R.drawable.ic_info_black_24dp);


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser== null){

            sendtostart();

        }


    }
    private void sendtostart() {
        Intent intent=new Intent(MainActivity.this,StartActivity.class);
        startActivity(intent);
        finish();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.main_logout:
                FirebaseAuth.getInstance().signOut();
                sendtostart();
                return true;

            case R.id.account:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;


            default:
                return super.onOptionsItemSelected(item);


        }


    }
}
