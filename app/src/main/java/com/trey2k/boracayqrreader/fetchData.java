package com.trey2k.boracayqrreader;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by User on 16/04/2018.
 */

public class fetchData extends AsyncTask<String, Void, Void> {
    String data1, parseName, parseBday, parseBplace, parseAddress, parseImage, parseGender, urlstr, sex, jo;
    @Override
    protected Void doInBackground(String... urls) {

        try {
            urlstr = urls[0];
            URL url2 = new URL(urls[0]);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            data1 = bufferedReader.readLine();

            JSONArray jsonArray = new JSONArray(data1);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);

            if (jsonObject.getString("gender") != "F") {
                sex = "MALE";
            } else {
                sex = "FEMALE";
            }

            parseName = jsonObject.get("fname").toString() + " " + jsonObject.get("mname").toString() + " " + jsonObject.get("lname").toString();
            parseBday = "Birthdate: " + jsonObject.get("bday").toString();
            parseBplace = "Birthplace: " + jsonObject.get("placeofbirth").toString();
            parseAddress = "Address: " + jsonObject.get("address").toString();
            parseGender = "Gender: " + sex;
            parseImage = "http://bayad.online/boracay/images/" + jsonObject.get("image_path").toString();
            jo = jsonArray.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (this.parseName != null) {

            MainActivity.lblBday.setVisibility(View.VISIBLE);
            MainActivity.lblBplace.setVisibility(View.VISIBLE);
            MainActivity.lblAddress.setVisibility(View.VISIBLE);
            MainActivity.imgImage.setVisibility(View.VISIBLE);
            MainActivity.lblGender.setVisibility(View.VISIBLE);
            MainActivity.lblName.setText(this.parseName);
            MainActivity.lblBday.setText(this.parseBday);
            MainActivity.lblBplace.setText(this.parseBplace);
            MainActivity.lblAddress.setText(this.parseAddress);
            MainActivity.lblGender.setText(this.parseGender);
            Picasso.get().load(this.parseImage)
                    .transform(new CircleTransform()).placeholder(R.mipmap.ic_launcher).into(MainActivity.imgImage);
        } else {
            MainActivity.lblName.setText("NOT FOUND");
            MainActivity.lblBday.setVisibility(View.GONE);
            MainActivity.lblBplace.setVisibility(View.GONE);
            MainActivity.lblAddress.setVisibility(View.GONE);
            MainActivity.imgImage.setVisibility(View.GONE);
            MainActivity.lblGender.setVisibility(View.GONE);
        }
    }
}
