package com.avantrio.assessment.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.avantrio.assessment.R;
import com.avantrio.assessment.ui.home.Fragments.HomeFragment;
import com.avantrio.assessment.ui.home.Fragments.Setting.SettingFragment;
import com.avantrio.assessment.ui.home.Fragments.User.UserFragment;
import com.avantrio.assessment.ui.home.Fragments.UserLogs.UserLogsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottonnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setItemIconTintList(null);


        onNavigationItemSelected(bottomNavigationView.getMenu().findItem(R.id.page_user));
        bottomNavigationView.getMenu().findItem(R.id.page_user).setChecked(true);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.page_user:
                fragment = new UserFragment();
                break;
            case R.id.page_setting:
                fragment = new SettingFragment();
                break;
        }
        if (fragment != null) {
            loadFragment(fragment);
        }
        return true;
    }

    void loadFragment(Fragment fragment) {
        //to attach fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main_body, fragment).commit();
    }
}