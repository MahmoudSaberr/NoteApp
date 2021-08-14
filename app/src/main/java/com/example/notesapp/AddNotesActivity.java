package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNotesActivity extends AppCompatActivity {
    EditText title,description;
    Button addNote;

    DatabaseClass db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        title = findViewById(R.id.add_notes_et_title);
        description = findViewById(R.id.add_notes_et_description);

        addNote = findViewById(R.id.add_notes_btn);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString())) //will be true only when both of the fields will be non-empty
                {
                    db = new DatabaseClass(AddNotesActivity.this);
                    db.addNotes(title.getText().toString(),description.getText().toString());

                    Intent intent = new Intent(AddNotesActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else // false
                {
                    Toast.makeText(AddNotesActivity.this,"Both Fields Required",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}