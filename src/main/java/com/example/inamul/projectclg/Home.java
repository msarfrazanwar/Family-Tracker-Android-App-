package com.example.inamul.projectclg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.inamul.projectclg.Grouping.Creategp;
import com.example.inamul.projectclg.Grouping.Joingp;
import com.example.inamul.projectclg.Registration.Login;
import com.google.firebase.auth.FirebaseAuth;


public class Home extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    Button create_gp,join_gp,btn2;

    private android.location.Location mLastLocation ;

    double latitude;
    double longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        create_gp = (Button) findViewById(R.id.create);
        join_gp = (Button) findViewById(R.id.join);

        create_gp.setOnClickListener(this);
        join_gp.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getCurrentUser().getUid();


        //extra btn
        btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(Home.this, MainScreen.class);
                startActivity(i);
            }
        });



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(1, 1, 2, "Log Out");
        menu.add(1,1,1,"Setting");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case 1:

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Home.this,Login.class);
                startActivity(intent);
                finish();
                break;
            case 2:
                break;


    }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.create: Intent i = new Intent(Home.this,Creategp.class);
                startActivity(i);
                finish(); break;

            case R.id.join : Intent j = new Intent(Home.this,Joingp.class);
                startActivity(j);
                finish();break;
        }

    }



    public void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


}

