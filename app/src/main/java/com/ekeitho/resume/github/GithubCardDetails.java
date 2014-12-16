package com.ekeitho.resume.github;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekeitho.resume.R;

/**
 * Created by Keithmaynn on 12/15/14.
 */
public class GithubCardDetails extends Fragment {

    private TextView gitCardTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.github_fragment_detail, container, false);

        Log.v("Github", "Github card details fired");
        gitCardTextView = (TextView) rootView.findViewById(R.id.detail_text_view);
        gitCardTextView.setText(getArguments().getString("repo_detail_name"));


        return rootView;
    }

}
