package com.yamlin.search.image.simpletwitterclient;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.oauth.OAuthBaseClient;
import com.codepath.oauth.OAuthLoginActivity;


public class LoginActivity extends OAuthLoginActivity<TwitterClient> {

    public static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        startOAuth();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public void startOAuth() {
        TwitterClient client = (TwitterClient) TwitterClient.getInstance(TwitterClient.class, this);
        if (client.isAuthenticated()) {
            onLoginSuccess();
        } else {
            client.connect();
        }
    }

    @Override
    public void onLoginSuccess() {
        Log.e(TAG, "On log success");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }
}
