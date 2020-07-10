package com.example.parstagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.parstagram.R;
import com.example.parstagram.fragments.ComposeFragment;
import com.example.parstagram.fragments.PostsFragment;
import com.example.parstagram.fragments.ProfileFragment;
import com.example.parstagram.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private static MenuItem lastPage;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Put instagram photo on the actionbar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        // Setup bottom nav bar
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        initializeBottomNavigationView(bottomNavigationView, fragmentManager);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    public static void initializeBottomNavigationView(final BottomNavigationView bottomNavigationView, final FragmentManager fManager) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                if (lastPage != null) {
                    // There was a previous page selected
                    unfill(lastPage);
                }
                lastPage = menuItem;
                switch (menuItem.getItemId()) {
                    // Unfill the last page's icon
                    // Go to the fragment they selected and fill in icon
                    case R.id.action_settings:
                        fragment = new SettingsFragment();
                        break;
                    case R.id.action_home:
                        menuItem.setIcon(R.drawable.instagram_home_filled_24);
                        fragment = new PostsFragment();
                        break;
                    case R.id.action_compose:
                        menuItem.setIcon(R.drawable.instagram_new_post_filled_24);
                        fragment = new ComposeFragment();
                        break;
                    case R.id.action_profile:
                        menuItem.setIcon(R.drawable.instagram_user_filled_24);
                        fragment = new ProfileFragment();
                        break;
                    default:
                        fragment = new PostsFragment();
                        break;
                }
                fManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
    }

    private static void unfill(@NonNull MenuItem menuItem) {
        switch (lastPage.getItemId()) {
            // Unfill the last page
            // Go to the fragment they selected and fill in icon
            case R.id.action_settings:
                break;
            case R.id.action_home:
                menuItem.setIcon(R.drawable.instagram_home_outline_24);
                break;
            case R.id.action_compose:
                menuItem.setIcon(R.drawable.instagram_new_post_outline_24);
                break;
            case R.id.action_profile:
                menuItem.setIcon(R.drawable.instagram_user_outline_24);
                break;
            default:
                break;
        }

    }
}