package com.ekeitho.resume.github;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.ekeitho.resume.R;
import java.util.List;

/**
 * An adapter that deals with information for my Github Repositories.
 */
public class GithubAdapter extends ArrayAdapter<Repo> {

    private List<Repo> itemList;
    private Context context;

    public GithubAdapter(List<Repo> itemList, Context ctx) {
        super(ctx, android.R.layout.simple_list_item_1, itemList);
        this.itemList = itemList;
        this.context = ctx;
    }

    public int getCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }

    public Repo getItem(int position) {
        if (itemList != null)
            return itemList.get(position);
        return null;
    }

    public long getItemId(int position) {
        if (itemList != null)
            return itemList.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }

        Repo c = itemList.get(position);
        TextView text = (TextView) v.findViewById(R.id.tvGitRepoName);
        text.setText(c.getRepoName());


        return v;

    }

    public List<Repo> getItemList() {
        return itemList;
    }

    public void setItemList(List<Repo> itemList) {
        this.itemList = itemList;
    }




}