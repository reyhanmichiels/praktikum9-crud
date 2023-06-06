package com.example.praktikum9;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertNoteActivity extends Activity implements View.OnClickListener {
    private Button btn_submit;
    FirebaseAuth mAuth;

    private EditText et_title, et_description;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_note);

        mAuth = FirebaseAuth.getInstance();

        et_title = findViewById(R.id.insert_title);
        et_description = findViewById(R.id.insert_description);
        btn_submit = findViewById(R.id.insert_submit);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        note = new Note();

        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btn_submit.getId()){
            submitData();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null){
//            tv_email.setText(currentUser.getEmail());
//            tv_uid.setText(currentUser.getUid());
//        }
    }

//    public void logOut(){
//        mAuth.signOut();
//        Intent intent = new Intent(InsertNoteActivity.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
//        startActivity(intent);
//    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(et_title.getText().toString())) {
            et_title.setError("Required");
            result = false;
        } else {
            et_title.setError(null);
        }
        if (TextUtils.isEmpty(et_description.getText().toString())) {
            et_description.setError("Required");
            result = false;
        } else {
            et_description.setError(null);
        }
        return result;
    }

    public void submitData(){
        if (!validateForm()){
            return;
        }
        String title = et_title.getText().toString();
        String desc = et_description.getText().toString();
        Note baru = new Note(title, desc);
        String key = databaseReference.child("notes").push().getKey();
        baru.setId(key);
        databaseReference.child("notes").child(mAuth.getUid()).child(key).setValue(baru).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(InsertNoteActivity.this, "Add data", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(InsertNoteActivity.this, ReadNoteActivity.class);
                startActivity(it);
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InsertNoteActivity.this, "Failed to Add data", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(InsertNoteActivity.this, ReadNoteActivity.class);
                startActivity(it);
            }
        });

//        databaseReference.child("notes").child(mAuth.getUid()).setValue(baru);
//        finish();

//        Intent it = new Intent(InsertNoteActivity.this, ReadNoteActivity.class);
//        startActivity(it);
    }
}
