package com.example.praktikum9;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateNoteActivity extends AppCompatActivity {
    Note note;
    EditText updateTitle, updateDescription;
    Button btnUpdate;
    FirebaseAuth mAuth;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        updateTitle = findViewById(R.id.update_title);
        updateDescription = findViewById(R.id.update_description);
        btnUpdate = findViewById(R.id.update_submit);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        note = getIntent().getParcelableExtra("note");

        if (note == null) {
            note = new Note();
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
            }
        });
    }

    public void updateNote() {
        if (!validateForm()){
            return;
        }
        String title = updateTitle.getText().toString().trim();
        String desc = updateDescription.getText().toString().trim();
        note.setTitle(title);
        note.setDescription(desc);
        databaseReference.child("notes").child(mAuth.getUid()).child(note.getId()).setValue(note);
        finish();

        displayToast("Update Success");

        Intent it = new Intent(UpdateNoteActivity.this, ReadNoteActivity.class);
        startActivity(it);
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(updateTitle.getText().toString())) {
            updateTitle.setError("Required");
            result = false;
        } else {
            updateTitle.setError(null);
        }
        if (TextUtils.isEmpty(updateDescription.getText().toString())) {
            updateDescription.setError("Required");
            result = false;
        } else {
            updateDescription.setError(null);
        }
        return result;
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}

