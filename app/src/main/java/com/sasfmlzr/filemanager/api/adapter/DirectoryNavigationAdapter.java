package com.sasfmlzr.filemanager.api.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sasfmlzr.filemanager.R;

import java.util.List;

//TODO: convert currentPath string to File
public class DirectoryNavigationAdapter extends RecyclerView.Adapter<DirectoryNavigationAdapter.ViewHolder> {
    private List<String> dataset;
    private static NavigationItemClickListener navListener;

    public interface NavigationItemClickListener {
        void navItemClicked(View view, int pos);
    }

    public DirectoryNavigationAdapter(List<String> directory,
                                      NavigationItemClickListener navigationListener) {
        navListener = navigationListener;
        dataset = directory;
    }

    @Override
    public DirectoryNavigationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.directory_navigation, parent, false);
        ViewHolder vh = new ViewHolder(view, navListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(dataset.get(position));
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
            navListener.navItemClicked(v, getAdapterPosition());
        }
    }
}
