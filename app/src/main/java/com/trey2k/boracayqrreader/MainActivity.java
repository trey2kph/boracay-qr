package com.trey2k.boracayqrreader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    public static TextView lblName, lblBday, lblBplace, lblAddress, lblGender;
    public static ImageView imgImage;
    private Button btnScan;
    Context context;
    String url1;
    String data1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgImage = (ImageView) findViewById(R.id.imgImage);
        lblName = (TextView) findViewById(R.id.lblName);
        lblBday = (TextView) findViewById(R.id.lblBday);
        lblBplace = (TextView) findViewById(R.id.lblBplace);
        lblAddress = (TextView) findViewById(R.id.lblAddress);
        lblGender = (TextView) findViewById(R.id.lblGender);

        btnScan = (Button)findViewById(R.id.btnScan);
        final Activity activity = this;

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(true);
                //integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNetwork();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        checkNetwork();

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String url1 = getString(R.string.app_link) + "/" + result.getContents().toString();
                fetchData process = new fetchData();
                process.execute(url1.toString());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileData = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnectedOrConnecting()) {
            lblName.setText("Scan QR Code to start");
            btnScan.setVisibility(View.VISIBLE);
        } else if (mobileData.isConnected()) {
            lblName.setText("Scan QR Code to start");
            btnScan.setVisibility(View.VISIBLE);
        } else {
            lblName.setText("PLEASE CONNECT TO INTERNET. Please restart the app once the network is restored");
            btnScan.setVisibility(View.GONE);
            lblBday.setVisibility(View.GONE);
            lblBplace.setVisibility(View.GONE);
            lblAddress.setVisibility(View.GONE);
            imgImage.setVisibility(View.GONE);
            lblGender.setVisibility(View.GONE);
        }
    }

}
