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
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.R;
import com.example.parstagram.databinding.ActivityPostDetailsBinding;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.io.File;

public class PostDetailsActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailsActivity";
    private static ActivityPostDetailsBinding binding;
    private Post post;
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

        post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        // Set all text information
        binding.tvCreatedAt.setText(PostsAdapter.getRelativeTimeAgo(post.getCreatedAt()));
        binding.tvDescription.setText(post.getDescription());
        binding.tvUsername.setText(post.getUser().getUsername());
        String likeCount = "" + post.getLikes();
        binding.tvLikes.setText(likeCount);

        // Set post image and prof pic, if they exist
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(binding.imageView);
        }
        ParseFile profPic = post.getProfilePic();
        if (profPic != null) {
            Glide.with(this).load(profPic.getUrl()).circleCrop().into(binding.ivProfilePic);
        }

        // Clicking like button fills heart and adds to likes
        binding.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fill in heart icon
                binding.btnLike.setImageResource(R.drawable.ufi_heart_active);
            }
        });
        //MainActivity.initializeBottomNavigationView(binding.bottomNavigation, getSupportFragmentManager());

    }
}