package com.sasfmlzr.filemanager.api.adapter;

import android.content.ContentResolver;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.fragment.FileViewFragment;
import com.sasfmlzr.filemanager.api.other.FileUtils;

import java.io.File;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.sasfmlzr.filemanager.api.provider.CacheProviderOperation.addToContentProvider;

public class FileExploreAdapter extends RecyclerView.Adapter<FileExploreAdapter.ViewHolder> {
    private List<File> fileModels;
    private PathItemClickListener pathListener;
    private ContentResolver contentResolver;
    private HashMap<String, String> sizeDirectory;

    public interface PathItemClickListener {
        void pathClicked(File file);
    }

    public FileExploreAdapter(List<File> files,
                              PathItemClickListener listener,
                              HashMap<String, String> cacheSizeDirectory) {
        fileModels = files;
        pathListener = listener;
        sizeDirectory = cacheSizeDirectory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.current_item_file, parent, false);
        contentResolver = view.getContext().getContentResolver();
        return new ViewHolder(view, pathListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT, Locale.getDefault());
        File fileModel = fileModels.get(position);

        if (holder.sizeItemView.getText() == "") {
            holder.sizeItemView.setText("...");
            String sizeFile;
            if (!sizeDirectory.isEmpty()) {
                sizeFile = sizeDirectory.get(fileModel.getAbsolutePath());
                if (sizeFile != null) {
                    holder.sizeItemView.setText(sizeFile);
                } else {
                    replaceTextViewSize(fileModel, holder);
                }
            } else {
                replaceTextViewSize(fileModel, holder);
            }
        }

        holder.dateView.setText(df.format(fileModel.lastModified()));
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

    private void replaceTextViewSize(File fileModel, ViewHolder holder) {
        if (fileModel.isFile()) {
            holder.sizeItemView.setText(FileUtils.formatCalculatedSize(fileModel.length()));
        } else {
            FileViewFragment.OnCalculateSizeCompleted listener = string -> {
                holder.sizeItemView.setText(string);
                addToContentProvider(contentResolver, fileModel.getAbsolutePath(), string);
            };
            new FileViewFragment.AsyncRunnableCalculateSize(listener, contentResolver).execute(fileModel);
        }
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
