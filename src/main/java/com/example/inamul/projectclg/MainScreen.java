package com.example.inamul.projectclg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inamul.projectclg.Map.MapLoc;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {

    FirebaseAuth mAuth;
    private ListView listview;
    DatabaseReference dref;
    public static String group="null"  ;
    String child;
    static String intentdata;
    ArrayList<String> users=new ArrayList<>();User user = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        listview = (ListView)findViewById(R.id.listview);
        mAuth = FirebaseAuth.getInstance();
        String uid =mAuth.getCurrentUser().getUid();




        //main refrence
        final DatabaseReference grpinfo = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Group");
        //listner not working properly
        grpinfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                group = dataSnapshot.getValue().toString();
                Toast.makeText(getApplicationContext(),group,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // reference for now
        final DatabaseReference userInfo = FirebaseDatabase.getInstance().getReference().child("Groups").child(group);


        // array adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,users);

        listview.setAdapter(adapter);

        //listner to populate list view
        userInfo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot ds :dataSnapshot.getChildren())
                { users.add(ds.getValue(String.class));

                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               //Toast.makeText(getApplicationContext(),((TextView)view).getText(),Toast.LENGTH_LONG).show();
                final Intent intent = new Intent(MainScreen.this,MapLoc.class);
                Query query =userInfo.orderByChild("Name").equalTo((String) ((TextView) view).getText());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // dataSnapshot is the "issue" node with all children with id 0
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {

                                  intentdata = issue.getKey().toString();
                               // Toast.makeText(getApplicationContext(),intentdata, Toast.LENGTH_LONG).show();
                                intent.putExtra("UID",intentdata);
                                startActivity(intent);

                            }   }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });



    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }




}
