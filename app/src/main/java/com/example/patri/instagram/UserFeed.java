package com.example.patri.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class UserFeed extends AppCompatActivity {

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //start of my code

        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

        Intent i = getIntent();
        String activeUsername = i.getStringExtra("username");
        Log.i("AppInfo-", activeUsername);

        setTitle(activeUsername + "'s Feed");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("images");

        query.whereEqualTo("username",activeUsername);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){

                    if (objects.size() > 0){

                        for(ParseObject object: objects){

                            ParseFile file = (ParseFile) object.get("image");

                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if (e == null){

                                        Log.i("AppInfo","ImageDisplayed");

                                        Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);


                                        ImageView imageView = new ImageView(getApplicationContext());

                                        imageView.setImageBitmap(image);

                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(

                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));
                                        linearLayout.addView(imageView);

                                    }else{
                                        Log.i("Appinfo-", "ImageProblem");
                                        e.printStackTrace();
                                    }


                                }
                            });

                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_feed, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {

            ParseUser.logOut();


            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
