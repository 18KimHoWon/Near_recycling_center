package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private GoogleMap googleMap;
    TextView textView;
    Handler handler = new Handler();
    static RequestQueue requestQueue;
    String urlStr = "https://apis.data.go.kr/4180000/cctrashloc/getLocationList?serviceKey=LAu77k1VNRuinoOgapmWsw%2Bww%2BkSwgRR8Mdhqbdf%2FQFOYzbqw3CKcg6AxTeSkBjt6nVeGfPPxShR589%2FhU4itg%3D%3D&numOfRows=450&pageNo=1";

    double ourX = 0.0; // = 37.868914;
    double ourY = 0.0; // = 127.736132;
    double X, Y;
    public static final int REQUEST_CODE_MENU = 101;


    private void processResponse(String response) {
        Gson gson = new Gson();
        TrashListResult trashList = gson.fromJson(response, TrashListResult.class);
/*
        EditText editLat = findViewById(R.id.editTextLatitude);
        EditText editLng = findViewById(R.id.editTextLongitude);
        ourX = Double.parseDouble(editLat.getText().toString());
        ourY = Double.parseDouble(editLng.getText().toString());
*/
        int a = 0;
        double shortTemp = 1000000;
        int shortest = 0;
        for (int i = 0; i < trashList.items.size(); i++) {
            if (trashList.items.get(i).emd_nm.equals("효자동")) {
                double tempx = 1000 * (ourX - trashList.items.get(i).ps_lat);
                double tempy = 1000 * (ourY - trashList.items.get(i).ps_lng);

                double temp = tempx * tempx + tempy * tempy;
                if (i == 0)
                    shortTemp = temp;
                else {
                    if (shortTemp > temp) {
                        shortest = i;
                        a++;
                        shortTemp = temp;
                    }
                }

            }
        }
        X = trashList.items.get(shortest).ps_lat;
        Y = trashList.items.get(shortest).ps_lng;
        //println(Integer.toString(a));
        //println(Double.toString(trashList.items.get(shortest).ps_lat));
        //println(Double.toString(trashList.items.get(shortest).ps_lng));
        println("\n관리번호");
        println(trashList.items.get(shortest).mng_no + "\n");
        println("주소");
        println(trashList.items.get(shortest).prv + " " + trashList.items.get(shortest).cty + " "
                + trashList.items.get(shortest).addr + " " + trashList.items.get(shortest).plc_det + "\n");
        println("운영시간");
        println(trashList.items.get(shortest).trs_dt + "\n" + trashList.items.get(shortest).trs_tm_s + " ~ "
                + trashList.items.get(shortest).trs_tm_e + "\n");
        println("쓰레기 배출 규칙");
        println(trashList.items.get(shortest).trs_rul + "\n" + trashList.items.get(shortest).f_trs_rul + "\n"
                + trashList.items.get(shortest).re_trs_rul + "\n");
        println("관리부서");
        println(trashList.items.get(shortest).office + " " + trashList.items.get(shortest).office_num + "\n");

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textViewResult);
        EditText editLat = findViewById(R.id.editTextLatitude);
        EditText editLng = findViewById(R.id.editTextLongitude);

        /* 현재위치 불러오는 기능 미작동 (됐다가 안됐다가)
        Button LocationB = findViewById(R.id.addCurrentLocationButton);
        LocationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;

                }
                Location loc_Current = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


                double cur_lat = loc_Current.getLatitude(); //위도
                double cur_lon = loc_Current.getLongitude(); //경도


                editLat.setText(String.valueOf(cur_lat));
                editLng.setText(String.valueOf(cur_lon));
            }
        });
        */





        Button button = findViewById(R.id.calculateButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ourX = Double.parseDouble(editLat.getText().toString());
                //ourY = Double.parseDouble(editLng.getText().toString());
                if (TextUtils.isEmpty(editLat.getText().toString()) || TextUtils.isEmpty(editLng.getText().toString())) {
                    if (TextUtils.isEmpty(editLat.getText().toString())) {
                        println("위도값을 입력하세요");
                        Toast.makeText(getBaseContext(), "위도값이 비어있습니다.", Toast.LENGTH_SHORT).show();
                    }
                    if (TextUtils.isEmpty(editLng.getText().toString())) {
                        println("경도값을 입력하세요");
                        Toast.makeText(getBaseContext(), "경도값이 비어있습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    ourX = Double.parseDouble(editLat.getText().toString());
                    ourY = Double.parseDouble(editLng.getText().toString());
                    makeRequest();

                }


            }
        });
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        Button button2 = findViewById(R.id.nextActivityButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);


                intent.putExtra("ourX", X);
                intent.putExtra("ourY", Y);
                intent.putExtra("myX", ourX);
                intent.putExtra("myY", ourY);
                startActivity(intent);
                //startActivityForResult(intent, REQUEST_CODE_MENU);


            }
        });
    }

    private void request(String urlStr) {
        StringBuilder output = new StringBuilder();

        try {
            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(10000);

                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                int resCode = conn.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String line = null;
                while (true) {
                    line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    output.append(line + "\n");
                }
                br.close();
                conn.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        println("응답 : " + output.toString());
    }


    private void println(final String str) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.append(str + '\n');
            }
        });
    }




    private void makeRequest() {
        StringRequest request = new StringRequest(Request.Method.GET, urlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processResponse(response);
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                println("에러 : " +  error);
            }
        });
        request.setShouldCache(false);
        requestQueue.add(request);
        println("요청 보냄");
    }
}



