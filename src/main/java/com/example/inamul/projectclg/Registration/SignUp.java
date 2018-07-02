package com.example.inamul.projectclg.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inamul.projectclg.Home;
import com.example.inamul.projectclg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private EditText mEmail, mPass, mPhone , mName;
    private Button mRegister;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthStateListnener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmail = (EditText) findViewById(R.id.etemail);
        mPass = (EditText) findViewById(R.id.pass);
        mRegister = (Button) findViewById(R.id.reg);
        mName = (EditText) findViewById(R.id.etname);
        mPhone = (EditText) findViewById(R.id.etmob);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseAuthStateListnener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(SignUp.this, Home.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString();
                final String pass = mPass.getText().toString();


                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), " Error", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
                            String user_id = mAuth.getCurrentUser().getUid();

                            String name = mName.getText().toString();
                            String phone = mPhone.getText().toString();
                            String email = mAuth.getCurrentUser().getEmail();
                            String group = "null";


                            DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                            Map newPost = new HashMap();
                            newPost.put("Name",name);
                            newPost.put("Phone",phone);
                            newPost.put("Group",group);


                            current_user.setValue(newPost);

                        }
                    }
                });

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mFirebaseAuthStateListnener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.addAuthStateListener(mFirebaseAuthStateListnener);
    }
}