package com.sasfmlzr.filemanager.api.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sasfmlzr.filemanager.R;

import java.io.File;
import java.util.List;

public class DirectoryNavigationAdapter extends RecyclerView.Adapter<DirectoryNavigationAdapter.ViewHolder> {
    private List<File> dataset;
    private static NavigationItemClickListener navListener;

    public interface NavigationItemClickListener {
        void navItemClicked(File file);
    }

    public DirectoryNavigationAdapter(List<File> directory,
                                      NavigationItemClickListener navigationListener) {
        navListener = navigationListener;
        dataset = directory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.directory_navigation, parent, false);
        return new ViewHolder(view, navListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(dataset.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView textView;

        ViewHolder(final View view, NavigationItemClickListener listener) {
            super(view);
            navListener = listener;
            textView = view.findViewById(R.id.path_navigation);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            navListener.navItemClicked(dataset.get(getAdapterPosition()));
        }
    }
}
