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

public class Joingp extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText jcode;
    Button join;
    String uname="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joingp);

        jcode = (EditText)findViewById(R.id.jcode);
        join = (Button)findViewById(R.id.join);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final String uid = firebaseUser.getUid();
        Log.d("blabla","Hry");

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



        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = jcode.getText().toString();


                DatabaseReference jRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(code).child(uid);
                Map newUser = new HashMap();
                newUser.put("Name",uname);

                jRef.setValue(newUser);

                DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                groupRef.child("Group").setValue(code);

                Intent intent = new Intent(Joingp.this, MainScreen.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(),"Group Joined",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
