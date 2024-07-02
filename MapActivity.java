package com.example.myapplication;

import static com.example.myapplication.MainActivity.REQUEST_CODE_MENU;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    Double a,b;
    Double mya, myb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        a = intent.getDoubleExtra("ourX", 0.0); // 기본값은 0.0
        b = intent.getDoubleExtra("ourY", 0.0); // 기본값은 0.0
        mya = intent.getDoubleExtra("myX", 0.0); // 기본값은 0.0
        myb = intent.getDoubleExtra("myY", 0.0); // 기본값은 0.0

        if(mya == 0.0 || myb == 0.0) {
            finish();
            Toast.makeText(getBaseContext(), "탐색을 해야합니다.", Toast.LENGTH_SHORT).show();
        }
        Button button = findViewById(R.id.backButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
/*
    Double a,b;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                a = data.getDoubleExtra("ourX", 0);
                b = data.getDoubleExtra("ourY", 0);
                Toast.makeText(this, "응답으로 전달된 name : " + a, Toast.LENGTH_LONG).show();

        }
    }
*/
    @Override
    public void onMapReady(final GoogleMap map) {

        LatLng garbageDump = new LatLng(a,b);
        LatLng myPlace = new LatLng(mya, myb);
        //garbage dump
        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(garbageDump);
        markerOptions1.title("쓰레기장");
        map.addMarker(markerOptions1);

        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(myPlace);
        markerOptions2.title("나의 위치");
        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        map.addMarker(markerOptions2);

        map.moveCamera(CameraUpdateFactory.newLatLng(garbageDump));
        map.animateCamera(CameraUpdateFactory.zoomTo(17));
    }
}