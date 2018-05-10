package com.sasfmlzr.filemanager.api.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.other.FileUtils;

import java.io.File;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

public class FileExploreAdapter extends RecyclerView.Adapter<FileExploreAdapter.ViewHolder> {
    private List<File> fileModels;
    private PathItemClickListener pathListener;

    public interface PathItemClickListener {
        void pathClicked(File file);
    }

    public FileExploreAdapter(List<File> files, PathItemClickListener listener) {
        fileModels = files;
        pathListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.current_item_file, parent, false);
        return new ViewHolder(view, pathListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT, Locale.getDefault());
        File fileModel = fileModels.get(position);
        holder.dateView.setText(df.format(fileModel.lastModified()));

        if (holder.sizeItemView.getText() == "") {
            holder.sizeItemView.setText("...");
            if (fileModel.isFile()) {
                FileUtils.formatCalculatedSize(fileModel.length());
            } else {
               /* AsyncTask asyncTask = new AsyncTask<File, Void, String>() {
                    @Override
                    protected String doInBackground(File... files) {
                        String size;
                        if (!files[0].canRead()) {
                            return null;
                        }
                        holder.sizeItemView.setText(getStatus().toString());
                        size = FileUtils.formatCalculatedSize(FileUtils.getDirectorySize(files[0]));
                        return size;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        holder.sizeItemView.setText(s);
                        super.onPostExecute(s);
                    }
                }.execute(fileModel);*/
            }
        }
        holder.nameView.setText(fileModel.getName());
        if (fileModel.isFile()) {
            holder.imageView.setImageResource(R.drawable.file);
        } else if (fileModel.isDirectory()) {
            holder.imageView.setImageResource(R.drawable.path);
        }
    }

    @Override
    public int getItemCount() {
        return fileModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView nameView, sizeItemView, dateView;
        final ImageView imageView;

        ViewHolder(View view, PathItemClickListener listener) {
            super(view);
            pathListener = listener;
            imageView = itemView.findViewById(R.id.icon_file);
            nameView = itemView.findViewById(R.id.file_name_view);
            sizeItemView = itemView.findViewById(R.id.size_item);
            dateView = itemView.findViewById(R.id.date_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            pathListener.pathClicked(fileModels.get(getAdapterPosition()));
        }
    }
}
