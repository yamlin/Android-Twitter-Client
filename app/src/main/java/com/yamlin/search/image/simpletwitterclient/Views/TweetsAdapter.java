package com.yamlin.search.image.simpletwitterclient.Views;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.yamlin.search.image.simpletwitterclient.MainActivity;
import com.yamlin.search.image.simpletwitterclient.R;
import com.yamlin.search.image.simpletwitterclient.TwitterClient;
import com.yamlin.search.image.simpletwitterclient.models.Tweet;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by yamlin on 8/14/15.
 */
public class TweetsAdapter extends ArrayAdapter<Tweet> {

    private static class ViewHolder {
        TextView userName;
        TextView screenName;
        TextView timestamp;
        TextView text;
        ImageButton userImage;
    }


    private MainActivity mActivity;

    public TweetsAdapter(MainActivity activity, ArrayList<Tweet> tweets) {
        super(activity, 0, tweets);
        mActivity = activity;
    }


    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        final View contextView = convertView;

        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.tweet_item, parent, false);

            viewHolder.userName = (TextView) convertView.findViewById(R.id.tweet_item_user);
            viewHolder.screenName = (TextView)
                    convertView.findViewById(R.id.tweet_item_screen_user);
            viewHolder.text = (TextView) convertView.findViewById(R.id.tweet_item_text);
            viewHolder.timestamp = (TextView) convertView.findViewById(R.id.tweet_item_ts);
            viewHolder.userImage = (ImageButton) convertView.findViewById(R.id.tweet_item_user_image);
            convertView.setTag(viewHolder);
        } else {
           viewHolder = (ViewHolder) convertView.getTag();
        }

        final Tweet tweet = getItem(position);

        viewHolder.userName.setText(tweet.username);
        viewHolder.screenName.setText("@" + tweet.screenName);
        viewHolder.text.setText(tweet.body);
        viewHolder.timestamp.setText(getRelativeTimeAgo(tweet.timestamp));


        Picasso.with(convertView.getContext()).load(tweet.userImage)
                .fit().into(viewHolder.userImage);

        viewHolder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Tweet", tweet.screenName);
                mActivity.loadUserProfile(tweet.screenName);

//                mClient.getMyProfile(new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers,
//                                          JSONObject obj) {
//                        Log.d("Twitter", "status: " + statusCode + " resp:" + obj.toString());
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
//                                          JSONObject jsonObject) {
//                        Log.d("Twitter", "status: " + statusCode + " resp:" + jsonObject.toString());
//                    }
//                });
            }
        });
        return convertView;
    }

}
