package com.example.patri.instagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class Start extends android.app.Application {

    @Override
    public void onCreate() {

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())

                .applicationId("myAppId")

                //.clientKey(null)

                .server("https://instagramluckysoftwarepatrick.herokuapp.com/parse/")

                .build()
        );


        //ParseUser.enableAutomaticUser();

        //ParseACL defaultACL = new ParseACL();

        // Optionally enable public read access.

        //defaultACL.setPublicReadAccess(true);

        //ParseACL.setDefaultACL(defaultACL, true);
        super.onCreate();
    }
}
