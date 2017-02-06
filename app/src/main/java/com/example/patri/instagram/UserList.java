package com.example.patri.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity {


    ArrayList<String> usernames;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Start Of My Code




        usernames = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,usernames);


        final ListView userList = (ListView)findViewById(R.id.userListView);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {

                Intent i = new Intent(getApplicationContext(),UserFeed.class);
                i.putExtra("username",usernames.get(postion));
                startActivity(i);

            }
        });

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.addDescendingOrder("username");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if (e == null){

                    if (objects.size() > 0){

                        for (ParseUser user : objects){
                            usernames.add(user.getUsername());
                        }

                        userList.setAdapter(arrayAdapter);
                    }
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {


            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 1);

            return true;
        }else if (id == R.id.logout){

            if(ParseUser.getCurrentUser() != null) {
                ParseUser.logOut();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){

            Uri selectedImage = data.getData();
            try {


                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);

                Log.i("AppInfo-", "Image recieved");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);

                byte[] byteArray = stream.toByteArray();

                ParseObject object =new ParseObject("Images");

                ParseFile file = new ParseFile("image.png",byteArray);

                object.put("username",ParseUser.getCurrentUser().getUsername());

                object.put("image",file);

                //public read access

                ParseACL parseACL = new ParseACL();
                parseACL.setPublicReadAccess(true);
                object.setACL(parseACL);

                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            Toast.makeText(getApplication().getBaseContext(),"Your image has been uploaded", Toast.LENGTH_LONG).show();
                        }else {
                            e.printStackTrace();
                            Toast.makeText(getApplication().getBaseContext(),"Upload failed - please try again", Toast.LENGTH_LONG).show();

                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplication().getBaseContext(),"Upload failed - please try again", Toast.LENGTH_LONG).show();

            }

        }
    }

}
