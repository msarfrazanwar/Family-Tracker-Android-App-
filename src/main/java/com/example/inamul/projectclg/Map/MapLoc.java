package com.example.inamul.projectclg.Map;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.example.inamul.projectclg.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MapLoc extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleMap gmap;
    Button go,live;
    FirebaseAuth mAuth;
    String uid;
    String intentUID;
    String lan;
    String lat;
    static Double latitude,longitude;
    DatabaseReference loc;
    GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_loc);
        mAuth =FirebaseAuth.getInstance();
        uid =mAuth.getCurrentUser().getUid();
        intentUID = getIntent().getStringExtra("UID");
      // Toast.makeText(getApplicationContext(),intentUID, Toast.LENGTH_LONG).show();
        loc = FirebaseDatabase.getInstance().getReference().child("Users").child(intentUID).child("Location");
        loc.child("Lang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    lan = dataSnapshot.getValue().toString();
                longitude = Double.parseDouble(lan);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        loc.child("Lat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lat = dataSnapshot.getValue().toString();
                 Toast.makeText(getApplicationContext(),lat, Toast.LENGTH_LONG).show();
                latitude = Double.parseDouble(lat);
                goToLocationZoom(latitude,longitude,11);
                LatLng x = new LatLng(latitude,longitude);
                MarkerOptions marker = new MarkerOptions().position(x).title("Last Located");
                 gmap.addMarker(marker);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        initMap();
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);
    }

    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        gmap.moveCamera(update);
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        gmap.moveCamera(update);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();



        //MarkerOptions marker = new MarkerOptions().position(latLng).title("Last Located");
        // gmap.addMarker(marker);


    }

    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, (LocationListener) this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //MarkerOptions marker = new MarkerOptions().position(latLng).title("Last Located");
       // gmap.addMarker(marker);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Location");

        Map newPost = new HashMap();
        newPost.put("Lat",location.getLatitude());
        newPost.put("Lang",location.getLongitude());
        mRef.setValue(newPost);
        // Toast.makeText(getApplicationContext(), new String(String.valueOf(location.getLongitude())), Toast.LENGTH_LONG).show();


        //move map camera
        //gmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //gmap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }
}
