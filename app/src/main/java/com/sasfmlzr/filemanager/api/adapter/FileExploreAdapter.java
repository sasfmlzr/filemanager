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

    public interface PathItemClickListener {
        void pathClicked(File file);
    }

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
        File fileModel = fileModels.get(position).getFile();
        Long fileSize = fileModels.get(position).getSizeDirectory();
        String sizeFile = FileUtils.formatCalculatedSize(fileSize);
        if (fileSize != 0) {
            holder.sizeItemView.setText(sizeFile);
        }

        if (!cacheSizeDirectory.isEmpty()) {
            String kek = fileModel.getAbsolutePath();
            if (cacheSizeDirectory.containsKey(kek)) {
                Long lol = cacheSizeDirectory.get(kek);
                sizeFile = FileUtils.formatCalculatedSize(lol);
                holder.sizeItemView.setText(sizeFile);
            } else {
                replaceTextViewSize(fileModel, holder);
            }
        } else {
            replaceTextViewSize(fileModel, holder);
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

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
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

    private void replaceTextViewSize(File fileModel, ViewHolder holder) {
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
