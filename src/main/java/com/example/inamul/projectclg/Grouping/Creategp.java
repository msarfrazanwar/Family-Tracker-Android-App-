package com.example.inamul.projectclg.Grouping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inamul.projectclg.MainScreen;
import com.example.inamul.projectclg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.example.inamul.projectclg.R.id.code;

public class Creategp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private Button create;
    private EditText mcode;
    String uname="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategp);

        mAuth = FirebaseAuth.getInstance();
        create =(Button)findViewById(R.id.creategp);
        mcode = (EditText) findViewById(code);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final String uid = firebaseUser.getUid();

        final DatabaseReference userInfo = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Name");
        userInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uname = dataSnapshot.getValue().toString();
                Log.d("Updated","Hry");
                Toast.makeText(getApplicationContext(),uname,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mcode.getText().toString();

                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(code).child(uid);
                Map newPost = new HashMap();

                newPost.put("Name",uname);

                

                mRef.setValue(newPost);

                DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                groupRef.child("Group").setValue(code);

                Intent intent = new Intent(Creategp.this, MainScreen.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(),"Group Created",Toast.LENGTH_SHORT).show();

            }


        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

