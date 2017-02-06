package com.example.patri.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{

    EditText username;
    EditText password;
    TextView changeSignUpMode;
    Boolean signUpModeActive;
    Button signUpButton;
    ImageView logo;
    RelativeLayout layout;

    @Override
    public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {

        if (KeyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){

            signUpOrLogIn(view);

        }
        return false;

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.changeTextView){

            if (signUpModeActive == true){

                signUpModeActive = false;
                changeSignUpMode.setText("Sign Up");
                signUpButton.setText("Log in");

            }else{

                signUpModeActive = true;
                changeSignUpMode.setText("Log In");
                signUpButton.setText("Sign Up");

            }
            //This else if makes the keyboard collapse from touching ID objects in else If
        }else if (view.getId() == R.id.logo || view.getId() == R.id.relativeLayout){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }

    }

    public void signUpOrLogIn(View view){

       if (signUpModeActive ==true) {

           ParseUser user = new ParseUser();
           user.setUsername(String.valueOf(username.getText()));
           user.setPassword(String.valueOf(password.getText()));

           user.signUpInBackground(new SignUpCallback() {
               @Override
               public void done(ParseException e) {
                   if (e == null) {

                       Log.i("AppInfo-", "SignUp Successful");
                       showUserList();

                   } else {

                       Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_LONG).show();

                   }
               }
           });

       }else {

           ParseUser.logInInBackground(String.valueOf(username.getText()), String.valueOf(password.getText()), new LogInCallback() {
               @Override
               public void done(ParseUser user, ParseException e) {
                   if (user != null){
                       Log.i("AppInfo-","LogIn Successful");
                       showUserList();
                   }else{
                       Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_LONG).show();
                   }
               }
           });

       }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        //Start of my code

        if (ParseUser.getCurrentUser() != null){
            showUserList();
        }

        signUpModeActive = true;

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        changeSignUpMode = (TextView)findViewById(R.id.changeTextView);
        signUpButton = (Button)findViewById(R.id.signUpButton);
        logo = (ImageView)findViewById(R.id.logo);
        layout = (RelativeLayout)findViewById(R.id.relativeLayout);

        changeSignUpMode.setOnClickListener(this);
        logo.setOnClickListener(this);
        layout.setOnClickListener(this);

        username.setOnKeyListener(this);
        password.setOnKeyListener(this);










    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showUserList(){
        Intent i = new Intent(getApplicationContext(), UserList.class);
        startActivity(i);
    }

}
