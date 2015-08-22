package com.yamlin.search.image.simpletwitterclient.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.DatabaseHelper;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.DatabaseHelper.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "Tweets")
public class Tweet extends Model {
    // Define database columns and associated fields
    @Column(name = "userId")
    public Long userId;
    @Column(name = "username")
    public String username;

    //@Column(name = "userImage")
    public String userImage;

    public String id;

    @Column(name = "screenName")
    public String screenName;
    @Column(name = "timestamp")
    public String timestamp;
    @Column(name = "body")
    public String body;

    // Make sure to always define this constructor with no arguments
    public Tweet() {
        super();
    }

    // Add a constructor that creates an object from the JSON response
    public Tweet(JSONObject object){
        super();

        try {
            this.id = object.getString("id_str");
            this.timestamp = object.getString("created_at");
            this.body = object.getString("text");

            JSONObject user = object.getJSONObject("user");
            this.userId = user.getLong("id");
            this.username = user.getString("name");
            this.screenName = user.getString("screen_name");
            this.userImage = user.getString("profile_image_url_https");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = new Tweet(tweetJson);
            tweets.add(tweet);
        }

        return tweets;
    }

    public static List<Tweet> getTweetsFromDb() {
        return new Select().from(Tweet.class).execute();
    }

    public static void saveTweets(List<Tweet> tweets) {


        // delete all previous data
        new Delete().from(Tweet.class).execute();


        for (Tweet t: tweets) {
            t.save();
        }
    }
}