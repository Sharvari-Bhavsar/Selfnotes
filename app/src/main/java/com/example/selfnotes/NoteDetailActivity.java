package com.example.selfnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.security.Timestamp;


public class NoteDetailActivity extends AppCompatActivity {

    EditText Tile_text,content_text;
    ImageButton save_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        Tile_text = findViewById(R.id.Tile_text);
        content_text = findViewById(R.id.content_text);
        save_done = findViewById(R.id.save_done);


        save_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNotes();
            }
        });
    }

    private void saveNotes() {
        String noteTitle = Tile_text.getText().toString();
        String noteContent = content_text.getText().toString();


        if(noteTitle==null || noteTitle.isEmpty()){
            Tile_text.setError("Title is required");
            return;

        }

        //to save notes on firebase go to firebase console and then firebase database
        // in android studio tool->cloud firebase->add dependency
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);

        note.setTimestamp(FieldValue.serverTimestamp());

        saveNoteToFirebase(note);

    }
    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        documentReference =Utility.getCollectionReferenceForNotes().document();

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is added successfully
                    Toast.makeText(NoteDetailActivity.this, "Note added successful!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(NoteDetailActivity.this, "Note failed to add", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}