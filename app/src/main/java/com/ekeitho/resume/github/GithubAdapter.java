package com.ekeitho.resume.github;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekeitho.resume.R;

import java.util.List;

/**
 * Created by Keithmaynn on 12/14/14.
 */
public class GithubAdapter extends RecyclerView.Adapter<GithubAdapter.ViewHolder> {

    private List<Repo> itemList;
    OnItemClickListener mItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public View mView;
        public TextView mTextView;
        public CardView cardView;


        public ViewHolder(View v) {
            super(v);
            mView = v;

            cardView = (CardView) v.findViewById(R.id.git_card_view);
            cardView.setOnClickListener(this);
            mTextView = (TextView) v.findViewById(R.id.git_card_text_view);
        }

        @Override
        public void onClick(View v) {
            if( mItemClickListener!= null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public GithubAdapter(List<Repo> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.git_cards, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setPadding(10,10,10,10);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.mTextView.setText(itemList.get(i).getRepoName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItemList(List<Repo> itemList) {
        this.itemList = itemList;
    }


}

