package com.yamlin.search.image.simpletwitterclient.Views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yamlin.search.image.simpletwitterclient.EndlessScrollListener;
import com.yamlin.search.image.simpletwitterclient.MainActivity;
import com.yamlin.search.image.simpletwitterclient.R;
import com.yamlin.search.image.simpletwitterclient.TwitterClient;
import com.yamlin.search.image.simpletwitterclient.models.Tweet;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends Fragment {


    private ListView mTimelineList;
    private TweetsAdapter mAdapter;

    private SwipeRefreshLayout mRefrashLayout;

    private TimelineFragmentInteractionListener mListener;

    private JsonHttpResponseHandler mResultHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
            // Load json array into model classes
            Log.e("TimelineFragment", jsonArray.toString());
            List<Tweet> tweets = Tweet.fromJson(jsonArray);

            mAdapter.clear();
            mAdapter.addAll(tweets);
            mAdapter.notifyDataSetChanged();
            mRefrashLayout.setRefreshing(false);
            Tweet.saveTweets(tweets);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                              JSONObject jsonObject) {
            // load previous data
            List<Tweet> tweets = Tweet.getTweetsFromDb();
            mAdapter.clear();
            mAdapter.addAll(tweets);
            mAdapter.notifyDataSetChanged();
            mRefrashLayout.setRefreshing(false);
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment TimelineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        mRefrashLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        mRefrashLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListener.loadTimeLine(mResultHandler);
            }
        });

        // Configure the refreshing colors
        mRefrashLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        mTimelineList = (ListView) view.findViewById(R.id.timelineView);
        mTimelineList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mListener.loadTimeLine(mResultHandler);
            }
        });

        mAdapter = new TweetsAdapter((MainActivity) getActivity(), new ArrayList<Tweet>());
        mTimelineList.setAdapter(mAdapter);

        mListener.loadTimeLine(mResultHandler);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (TimelineFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface TimelineFragmentInteractionListener {

        public void loadTimeLine(JsonHttpResponseHandler handler);
    }



}
