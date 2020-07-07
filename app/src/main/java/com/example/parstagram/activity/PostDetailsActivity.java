package com.example.parstagram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.parstagram.Post;
import com.example.parstagram.R;
import com.example.parstagram.databinding.ActivityPostDetailsBinding;

import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailsActivity";
    private static ActivityPostDetailsBinding binding;
    //final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Put instagram photo on the actionbar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        //this.getSupportActionBar().set
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        Intent intent = getIntent();
        Log.i(TAG,  "" + intent.getStringExtra("createdAt"));
        binding.tvCreatedAt.setText("" + intent.getStringExtra("createdAt"));
        binding.tvDescription.setText(intent.getStringExtra("description"));
        binding.tvUsername.setText(intent.getStringExtra("username"));
        Glide.with(this).load(intent.getStringExtra("image")).into(binding.imageView);
        //MainActivity.initializeBottomNavigationView(binding.bottomNavigation, fragmentManager);

    }
}