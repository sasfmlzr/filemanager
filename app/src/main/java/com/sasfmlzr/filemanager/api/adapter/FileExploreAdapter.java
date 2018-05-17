package com.sasfmlzr.filemanager.api.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.model.FileModel;
import com.sasfmlzr.filemanager.api.other.FileUtils;

import java.io.File;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FileExploreAdapter extends RecyclerView.Adapter<FileExploreAdapter.ViewHolder> {

    private List<FileModel> fileModels;
    private PathItemClickListener pathListener;
    private HashMap<String, Long> cacheSizeDirectory;

    public FileExploreAdapter(List<FileModel> files,
                              PathItemClickListener listener,
                              HashMap<String, Long> cacheSizeDirectory) {
        fileModels = files;
        pathListener = listener;
        this.cacheSizeDirectory = cacheSizeDirectory;
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
        final File currentFile = fileModels.get(position).getFile();
        Long longSizeFile = fileModels.get(position).getSizeDirectory();
        String strSizeFile = FileUtils.formatCalculatedSize(longSizeFile);
        if (longSizeFile != 0) {
            holder.sizeItemView.setText(strSizeFile);
        }

        if (!cacheSizeDirectory.isEmpty()) {
            String path = currentFile.getAbsolutePath();
            if (cacheSizeDirectory.containsKey(path)) {
                longSizeFile = cacheSizeDirectory.get(path);
                strSizeFile = FileUtils.formatCalculatedSize(longSizeFile);
                holder.sizeItemView.setText(strSizeFile);
            } else {
                addFileSizeText(currentFile, holder);
            }
        } else {
            addFileSizeText(currentFile, holder);
        }

        holder.dateView.setText(df.format(currentFile.lastModified()));
        holder.nameView.setText(currentFile.getName());
        addImageView(currentFile, holder);
    }

    @Override
    public int getItemCount() {
        return fileModels.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public interface PathItemClickListener {
        void pathClicked(File file);
    }

    public void replaceSizeOnTextView(FileModel fileModel) {
        for (FileModel currentFileModel : fileModels) {
            if (currentFileModel.getFile() == fileModel.getFile()) {
                int pos = fileModels.indexOf(currentFileModel);
                fileModels.set(pos, fileModel);
                notifyItemChanged(pos);
                return;
            }
        }
    }

    private void addImageView(File file, ViewHolder holder) {
        if (file.isFile()) {
            holder.imageView.setImageResource(R.drawable.file);
        } else {
            holder.imageView.setImageResource(R.drawable.path);
        }
    }

    private void addFileSizeText(File fileModel, ViewHolder holder) {
        if (fileModel.isFile()) {
            holder.sizeItemView.setText(FileUtils.formatCalculatedSize(fileModel.length()));
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
            pathListener.pathClicked(fileModels.get(getAdapterPosition()).getFile());
        }
    }
}
