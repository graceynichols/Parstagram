package com.example.parstagram.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parstagram.R;

public class ComposeCommentActivity extends AppCompatActivity {
    private EditText etComment;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_comment);

        etComment = findViewById(R.id.etComment);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Clicking submit posts your comment
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comm = etComment.getText().toString();
                if (comm.isEmpty()) {
                    Toast.makeText(ComposeCommentActivity.this, "Sorry, your comment cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("comment", comm);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });
    }
}