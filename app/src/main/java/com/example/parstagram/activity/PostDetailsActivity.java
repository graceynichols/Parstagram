package com.example.parstagram.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.parstagram.Adapters.CommentsAdapter;
import com.example.parstagram.Post;
import com.example.parstagram.Adapters.PostsAdapter;
import com.example.parstagram.R;
import com.example.parstagram.databinding.ActivityPostDetailsBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailsActivity";
    private static ActivityPostDetailsBinding binding;
    private Post post;
    private int index;
    private ParseUser user;
    private CommentsAdapter adapter;
    private List<String> comments;
    private static int REQUEST_CODE = 20;

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

        if (post.getUsersWhoLiked() == null) {
            post.setUsersWhoLiked(new JSONArray());
        }
        //Check if post is liked
        user = ParseUser.getCurrentUser();
        try {
            index = post.isLiked(user);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "Error checking if liked");
            index = -1;
        }
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

        // Like or unlike post
        binding.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.pbLoading.setVisibility(ProgressBar.VISIBLE);
                if (index == -1) {
                    // User has not liked the post
                    binding.btnLike.setImageResource(R.drawable.ufi_heart_active);
                    index = post.addUserToLikes(ParseUser.getCurrentUser());
                    post.addLike();

                } else {
                    // User has liked the post -> unlike the post
                    binding.btnLike.setImageResource(R.drawable.ufi_heart);
                    post.removeUserFromLikes(index);
                    post.subLike();
                    index = -1;
                }
                String likeCount = "" + post.getLikes();
                binding.tvLikes.setText(likeCount);
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while liking", e);
                            Toast.makeText(getApplicationContext(), "Error while liking", Toast.LENGTH_SHORT).show();
                        }
                        binding.pbLoading.setVisibility(ProgressBar.INVISIBLE);
                    }
                });
            }
        });

        // Set up comment section
        comments = new ArrayList<>();
        adapter = new CommentsAdapter(this, comments);
        binding.rvComments.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvComments.setLayoutManager(layoutManager);
        try {
            getComments();
        } catch (JSONException e) {
            Log.i(TAG, "Error fetching comments");
            e.printStackTrace();
        }

        // Setup add a comment button
        binding.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start compose activity so user can write their comment
                Intent intent = new Intent(getApplicationContext(), ComposeCommentActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // Go to profile page action
        View.OnClickListener onClickL = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                i.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                startActivity(i);
            }
        };

        // User can tap username or profile to go to that profile page
        binding.tvUsername.setOnClickListener(onClickL);
        binding.ivProfilePic.setOnClickListener(onClickL);
    }

    @Override
    public void onBackPressed() {
        // Necessary for "saving" like information when people go back to posts screen
        Log.i(TAG, "Onbackpressed");
        Intent i = new Intent();
        i.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
        setResult(RESULT_OK, i);
        finish();
    }

    private void getComments() throws JSONException {
        // Get comments from parse and put in recyclerview
        JSONArray array = post.getComments();
        if (array == null) {
            array = new JSONArray();
            post.setComments(array);
        }
        Log.i(TAG, array.toString());
        for (int i = 0; i < array.length(); i++) {
            comments.add((String) array.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            binding.pbLoading.setVisibility(View.VISIBLE);
            // Get the comment from the compose activity
            final String comm = data.getStringExtra("comment");
            // Add this comment to the post in Parse
            final ParseUser user = ParseUser.getCurrentUser();
            final String userAndComm = user.getUsername() + ": " + comm;
            try {
                post.addComment(userAndComm);
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i(TAG, "Comment saved successfully");
                        // Modify data source of comments
                        comments.add(0, userAndComm);
                        adapter.notifyItemInserted(0);
                        binding.pbLoading.setVisibility(View.INVISIBLE);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "Error posting comment");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}