package com.example.parstagram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.parstagram.Post;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.R;
import com.example.parstagram.databinding.ActivityPostDetailsBinding;
import com.example.parstagram.fragments.PostsFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.io.File;

public class PostDetailsActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailsActivity";
    private static ActivityPostDetailsBinding binding;
    private Post post;
    private int index;
    private ParseUser user;
    //final FragmentManager fragmentManager = getSupportFragmentManager();

    // TODO figure this out
    @SuppressLint("WrongConstant")
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

        //Check if post is liked
        user = ParseUser.getCurrentUser();
        try {
            index = post.isLiked(user);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "Error checking if liked");
            index = -1;
        }
        Log.i(TAG, "Like index " + index);
        if (index == -1) {
            // Post is not liked
            binding.btnLike.setImageResource(R.drawable.ufi_heart);
        } else {
            // Filled in heart
            binding.btnLike.setImageResource(R.drawable.ufi_heart_active);
        }
        // Set post image and prof pic, if they exist
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(binding.imageView);
        }
        ParseFile profPic = post.getProfilePic();
        if (profPic != null) {
            Glide.with(this).load(profPic.getUrl()).circleCrop().into(binding.ivProfilePic);
        }
        Log.i(TAG, "USERS who liked " + post.getUsersWhoLiked().toString());

        // Clicking like button fills heart and adds to likes
        binding.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index == -1) {
                    // User has not liked the post
                    binding.btnLike.setImageResource(R.drawable.ufi_heart_active);
                    post.addUserToLikes(ParseUser.getCurrentUser());
                    post.addLike();

                } else {
                    // Unlike the post
                    // User has not liked the post
                    binding.btnLike.setImageResource(R.drawable.ufi_heart);
                    post.removeUserFromLikes(index);
                    post.subLike();
                }
                String likeCount = "" + post.getLikes();
                binding.tvLikes.setText(likeCount);
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while liking", e);
                            Toast.makeText(getApplicationContext(), "Error while liking", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Like saved!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //MainActivity.initializeBottomNavigationView(binding.bottomNavigation, getSupportFragmentManager());

    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "Onbackpressed");
        Intent i = new Intent();
        i.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
        setResult(RESULT_OK, i);
        finish();
    }
}