package com.ekeitho.resume.github;


import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ekeitho.resume.R;
import com.ekeitho.resume.Resume;
import com.ekeitho.resume.github.database.GithubRepoSource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Keithmaynn on 12/13/14.
 */
public class GithubFragment extends Fragment {

    private GithubAdapter mGithubAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private GithubRepoSource repoSource;

    private ArrayList<Repo> list = new ArrayList<Repo>();
    private Activity mainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // these tells the main activity that this is the most recent fragment
        ((Resume) activity).onSectionAttached(2);
        mainActivity = activity;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }

    /**
     * This method should only set up and create instances of our views.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.github_fragment_repo, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.gitSwipeRefreshLayout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.github_repos);
        // start the sql up
        repoSource = new GithubRepoSource(this.getActivity());
        repoSource.open();

        return rootView;
    }

    /**
     * Supporting method after onCreateView, which set's up the meat of our logic.
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (repoSource.isDatabaseEmpty()) {
            // rather than grab from network - use a handy sql
            // for updates to my repo - there is the refresh scroll :)
            list = repoSource.getAllRepos();
        }
        mGithubAdapter = new GithubAdapter(list);


        // set the views adapter to our created one
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mainActivity);

        // set recycler views manager and the adapter
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mGithubAdapter);


        // the fancy refresh down swipe :)
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4A148C"));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncListViewLoader().execute("https://api.github.com/users/ekeitho/repos?sort=pushed");

            }
        });

        // this should only launch on first use off the app -- or deletion of cache
        if(!repoSource.isDatabaseEmpty()) {
            new AsyncListViewLoader().execute("https://api.github.com/users/ekeitho/repos?sort=pushed");
        }

    }


    public class AsyncListViewLoader extends AsyncTask<String, Void, ArrayList<Repo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Repo> doInBackground(String... params) {
            ArrayList<Repo> result = new ArrayList<Repo>();
            StringBuilder builder = new StringBuilder();


            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(params[0]);


            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } else {
                    //Log.e(ParseJSON.class.toString(), "Failed to download file");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONArray arr = new JSONArray(builder.toString());

                for (int i = 0; i < arr.length(); i++) {
                    result.add(convertRepo(arr.getJSONObject(i)));
                }

            } catch (JSONException e) {
                System.err.print(e);
            }

            return result;

        }

        @Override
        protected void onPostExecute(final ArrayList<Repo> result) {
            super.onPostExecute(result);
            // notify the adapter on change
            list = result;
            repoSource.updateAnyChanges(result);
            mGithubAdapter.setItemList(result);
            mGithubAdapter.SetOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {


                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();

                    GithubCardDetails details = new GithubCardDetails();
                    Bundle bundle = new Bundle();
                    bundle.putString("repo_detail_name", result.get(position).getRepoName());
                    details.setArguments(bundle);

                    transaction.addToBackStack(null);
                    transaction.replace(R.id.container, details, "transition_detailed").commit();


                }
            });
            mGithubAdapter.notifyDataSetChanged();
            // set the refresh off
            mSwipeRefreshLayout.setRefreshing(false);


        }


        // self made
        private Repo convertRepo(JSONObject obj) throws JSONException {
            String name = obj.getString("name");
            long id = obj.getLong("id");
            return new Repo(id, name);
        }
    }
}