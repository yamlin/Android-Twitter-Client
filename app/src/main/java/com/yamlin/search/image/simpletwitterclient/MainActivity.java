package com.yamlin.search.image.simpletwitterclient;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.astuetz.PagerSlidingTabStrip;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yamlin.search.image.simpletwitterclient.Views.MentionFragment;
import com.yamlin.search.image.simpletwitterclient.Views.PostFragment;
import com.yamlin.search.image.simpletwitterclient.Views.ProfileFragment;
import com.yamlin.search.image.simpletwitterclient.Views.TimelineFragment;

import org.apache.http.Header;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity implements
        TimelineFragment.TimelineFragmentInteractionListener,
        MentionFragment.MentionFragmentInteractionListener,
        PostFragment.PostFragmentInteractionListener,
        ProfileFragment.ProfileFragmentInteractionListener {

    public static final String TAG = "MainActivity";

    private TwitterClient mClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mClient = (TwitterClient)
                TwitterClient.getInstance(TwitterClient.class, this);;
        if (mClient.isAuthenticated()) {
            onLoginSuccess();
        } else {
            mClient.connect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_main_post:
                PostFragment postFragment = PostFragment.newInstance();
                FragmentManager fm = getSupportFragmentManager();
                postFragment.show(fm, "post_frag");

                break;
            case R.id.action_main_menu:
                loadUserProfile(null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadTimeLine(JsonHttpResponseHandler handler) {
        mClient.getHomeTimeline(0, handler);
    }

    @Override
    public void postTweet(String text, JsonHttpResponseHandler handler) {
        mClient.postTweet(text, handler);
    }

    @Override
    public void loadMentions(JsonHttpResponseHandler handler) {
        mClient.getMention(0, handler);
    }

    public void loadUserProfile(String screenName) {

        // my own profile
        if (screenName == null) {
            mClient.getMyProfile(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      JSONObject obj) {
                    Log.d(TAG, "status: " + statusCode + " resp:" + obj.toString());
                    ProfileFragment profileFragment = ProfileFragment.newInstance(
                            obj.optString("name", ""), obj.optString("screen_name", ""),
                            obj.optString("profile_image_url", ""));

                    FragmentManager fm = getSupportFragmentManager();
                    profileFragment.show(fm, "profile_frag");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                      JSONObject jsonObject) {
                    Log.d(TAG, "status: " + statusCode + " resp:" + jsonObject);
                }
            });
        } else {
            mClient.getUserProfile(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      JSONObject obj) {
                    Log.d(TAG, "status: " + statusCode + " resp:" + obj.toString());
                    ProfileFragment profileFragment = ProfileFragment.newInstance(
                            obj.optString("name", ""), obj.optString("screen_name", ""),
                            obj.optString("profile_image_url", ""));

                    FragmentManager fm = getSupportFragmentManager();
                    profileFragment.show(fm, "profile_frag");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                      JSONObject jsonObject) {
                    Log.d(TAG, "status: " + statusCode + " resp:" + jsonObject);
                }
            });

        }
    }

   // @Override
    public void onLoginSuccess() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentManager fm = getSupportFragmentManager();

        viewPager.setAdapter(new SampleFragmentPagerAdapter(fm));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
    }

    //@Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }
}
