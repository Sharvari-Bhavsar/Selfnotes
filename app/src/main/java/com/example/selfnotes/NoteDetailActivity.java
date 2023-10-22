package com.example.selfnotes;

;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;


public class NoteDetailActivity extends AppCompatActivity {

    EditText Tile_text,content_text;
    ImageButton save_done ;
    String title,content,docId;
    boolean isEditMode = false;
    TextView delete_btn,editNoteTitle;
    ImageView delete_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        Tile_text = findViewById(R.id.Tile_text);
        content_text = findViewById(R.id.content_text);
        save_done = findViewById(R.id.save_done);
        editNoteTitle = findViewById(R.id.Title_add_note);
        delete_btn  = findViewById(R.id.delete_btn);
        delete_image  = findViewById(R.id.delete_image);



        //receive data
        title = getIntent().getStringExtra("title");
        content= getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        Tile_text.setText(title);
        content_text.setText(content);
        //if isEditMode is true  set title Edit note and set the delete button visible
        if(isEditMode){
            editNoteTitle.setText("Edit note");
            delete_image.setVisibility(View.VISIBLE);
            delete_btn.setVisibility(View.VISIBLE);
        }

        save_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNotes();
            }
        });
        delete_btn.setOnClickListener((v)-> deleteNoteFromFirebase() );
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
        note.setTimestamp(Timestamp.now());



        saveNoteToFirebase(note);

    }
    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if(isEditMode){
            //update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }else{
            //create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    if(isEditMode){
                        //update the note
                        Toast.makeText(NoteDetailActivity.this, "Note edited successfully!", Toast.LENGTH_SHORT).show();
                    }else{
                        //create new note
                        Toast.makeText(NoteDetailActivity.this, "Note added successfully!", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    if(isEditMode){
                        //update the note
                        Toast.makeText(NoteDetailActivity.this, "Note failed to edit!", Toast.LENGTH_SHORT).show();
                    }else{
                        //create new note
                        Toast.makeText(NoteDetailActivity.this, "Note failed to add!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
    void deleteNoteFromFirebase(){
         //only this change  documentReference.delete() from save note to firebase
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(NoteDetailActivity.this,"Note deleted successfully");
                    finish();
                }else{
                    Utility.showToast(NoteDetailActivity.this,"Failed while deleting note");
                }
            }
        });
    }

}