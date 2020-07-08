package com.example.parstagram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.parstagram.EndlessRecyclerViewScrollListener;
import com.example.parstagram.Post;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.PostsAdapterGrid;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private static ParseUser user;
    private static final String TAG = "UserProfileActivity";
    private static final int POST_LIMIT = 20;
    private RecyclerView rvPosts;
    protected PostsAdapterGrid adapter;
    protected List<Post> allPosts;
    private ImageView profilePic;
    private TextView tvUsername;

    public UserProfileActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        rvPosts = findViewById(R.id.rvPosts);
        tvUsername = findViewById(R.id.tvUsername);
        profilePic = findViewById(R.id.profilePic);
        allPosts = new ArrayList<>();
        adapter = new PostsAdapterGrid(this, allPosts);

        rvPosts.setAdapter(adapter);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvPosts.setLayoutManager(layoutManager);
        Post post =  Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        user = post.getUser();

        tvUsername.setText(user.getUsername());
        ParseFile profPic = user.getParseFile("profilePic");
        if (profPic != null) {
            Glide.with(this).load(profPic.getUrl()).circleCrop().into(profilePic);
        }

        queryPosts();
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + " Username: " + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
                Log.i(TAG, "Query posts finished");
            }
        });
    }
}