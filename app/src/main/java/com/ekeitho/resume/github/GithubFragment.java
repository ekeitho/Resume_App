package com.ekeitho.resume.github;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.ekeitho.resume.R;

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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keithmaynn on 12/13/14.
 */
public class GithubFragment extends FragmentActivity {

    private Button refreshButton;
    private ListView view;
    private GithubAdapter githubAdapter;
    private List<Repo> list = new ArrayList<Repo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.setContentView(R.layout.fragment_main);
        refreshButton = (Button) this.findViewById(R.id.refreshButton);
        view = (ListView) this.findViewById(R.id.github_repos);

        // first set the adapter as empty
        githubAdapter = new GithubAdapter(list, GithubFragment.this);
        // set the views adapter to our created one
        view.setAdapter(githubAdapter);


        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncListViewLoader().execute("https://api.github.com/users/ekeitho/repos?sort=pushed");
            }
        });

    }


    public class AsyncListViewLoader extends AsyncTask<String, Void, List<Repo>> {

        private final ProgressDialog dialog = new ProgressDialog(GithubFragment.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Downloading contacts...");
            dialog.show();
        }

        @Override
        protected List<Repo> doInBackground(String... params) {
            List<Repo> result = new ArrayList<Repo>();
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

                for(int i = 0; i < arr.length(); i++) {
                    result.add(convertContact(arr.getJSONObject(i)));
                }

            } catch (JSONException e) {
                System.err.print(e);
            }

            return result;

        }

        @Override
        protected void onPostExecute(List<Repo> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            githubAdapter.setItemList(result);
            githubAdapter.notifyDataSetChanged();
        }


        // self made

        private Repo convertContact(JSONObject obj) throws JSONException {
            String name = obj.getString("name");

            return new Repo(name);
        }


    }
}