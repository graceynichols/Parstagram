package com.example.parstagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.parstagram.R;
import com.example.parstagram.activity.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseUser;


public class SettingsFragment extends Fragment {
    private static final String TAG = "SettingsFragment";
    private Button btnLogout;
    private Button btnSetPic;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnSetPic = view.findViewById(R.id.btnSetPic);
        // Create logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View.OnClickListener myOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Logout user
                        ParseUser.logOut();
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        if (currentUser != null) {
                            Log.i(TAG, "Error logging out");
                            Toast.makeText(getContext(), "Logout failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i(TAG, "Logout successful");
                            // Take user back to login screen
                            Toast.makeText(getContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                };
                // Make the snackbar
                Snackbar.make(view, "Are you sure you want to logout?", Snackbar.LENGTH_LONG)
                        .setAction("Yes", myOnClickListener)
                        .show(); // Don’t forget to show!*/

            }
        });
        // Change profile pic
        btnSetPic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Switch to the profile pic fragment
                Fragment fragment = new ProfilePicFragment();
                getFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        });
    }
}