package com.yamlin.search.image.simpletwitterclient.Views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.yamlin.search.image.simpletwitterclient.models.Tweet;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MentionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MentionFragment extends Fragment {


    private MentionFragmentInteractionListener mListener;

    private ListView mListview;
    private SwipeRefreshLayout mRefrashLayout;
    private TweetsAdapter mAdapter;

    private final JsonHttpResponseHandler mResultHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
            // Load json array into model classes
            List<Tweet> tweets = Tweet.fromJson(jsonArray);
            Log.e("MentionFragment", jsonArray.toString());

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
     * @return A new instance of fragment MentionFragment.
     */
    public static MentionFragment newInstance() {
        MentionFragment fragment = new MentionFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public MentionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mention, container, false);
        mListview = (ListView) view.findViewById(R.id.fragment_mention_listview);
        mListview.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mListener.loadMentions(mResultHandler);
            }
        });

        mAdapter = new TweetsAdapter((MainActivity) getActivity(), new ArrayList<Tweet>());
        mListview.setAdapter(mAdapter);

        mRefrashLayout = (SwipeRefreshLayout)
                view.findViewById(R.id.fragment_mention_swipeContainer);

        mRefrashLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListener.loadMentions(mResultHandler);
            }
        });

        mListener.loadMentions(mResultHandler);
        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MentionFragmentInteractionListener) activity;
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
    public interface MentionFragmentInteractionListener {
        public void loadMentions(JsonHttpResponseHandler handler);
    }

}
