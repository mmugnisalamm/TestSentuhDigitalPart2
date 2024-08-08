package com.example.tessentuhdigitalpart2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tessentuhdigitalpart2.Models.ChuckModel;
import com.example.tessentuhdigitalpart2.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ChuckAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ChuckModel> mData;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public ChuckAdapter(Context mContext, ArrayList<ChuckModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chuck, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder){
            populateItemRows((MyViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    private void showLoadingView(LoadingViewHolder holder, int position) {
    }

    private void populateItemRows(MyViewHolder holder, int position) {
        try {
            holder.tvId.setText(String.valueOf(mData.get(position).getId()));
            try {
                holder.tvCategories.setText(String.valueOf(mData.get(position).getCategories().get(0)));
            } catch (JSONException e) {
                holder.tvCategories.setText("-");
            }
            holder.tvValue.setText(String.valueOf(mData.get(position).getValue()));
            Glide.with(mContext).load(String.valueOf(mData.get(position).getIcon_url())).into(holder.ivData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getItemViewType(int position) {
        return mData.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategories, tvId, tvValue;
        ImageView ivData;
        public MyViewHolder(View view) {
            super(view);
            tvId = view.findViewById(R.id.tv_id);
            tvCategories = view.findViewById(R.id.tv_categories);
            tvValue = view.findViewById(R.id.tv_value);
            ivData = view.findViewById(R.id.iv_data);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        public LoadingViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBarLoad);
        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
