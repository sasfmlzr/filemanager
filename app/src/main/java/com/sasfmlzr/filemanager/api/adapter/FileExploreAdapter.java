package com.sasfmlzr.filemanager.api.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.other.FileUtils;
import com.sasfmlzr.filemanager.api.other.data.DataCache;

import java.io.File;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FileExploreAdapter extends RecyclerView.Adapter<FileExploreAdapter.ViewHolder> {
    private List<File> fileModels;
    private PathItemClickListener pathListener;
    private View view;
    private HashMap<String, String> sizeDirectory;
    private static final String TAG = "FileExploreAdapter";

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
        this.view = view;
        this.sizeDirectory = selectAllToContentProvider();
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
            String sizeFile;
            if (!sizeDirectory.isEmpty()) {
                sizeFile = sizeDirectory.get(fileModel.getAbsolutePath());
                if (sizeFile != null) {
                    holder.sizeItemView.setText(sizeFile);
                }
            }
            if (fileModel.isFile()) {
                holder.sizeItemView.setText(FileUtils.formatCalculatedSize(fileModel.length()));
            } else {
                AsyncTask asyncTask = new AsyncRunnable() {
                    @Override
                    protected void onPostExecute(String s) {
                        holder.sizeItemView.setText(s);
                        addToContentProvider(fileModel.getAbsolutePath(), s);
                        super.onPostExecute(s);
                    }
                }.execute(fileModel);
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

    private static class AsyncRunnable extends AsyncTask<File, Void, String> {
        @Override
        protected String doInBackground(File... files) {
            String size;
            if (!files[0].canRead()) {
                return null;
            }
            size = FileUtils.formatCalculatedSize(FileUtils.getDirectorySize(files[0]));
            return size;
        }
    }

    public HashMap<String, String> selectAllToContentProvider() {
        HashMap<String, String> hashMap = new HashMap<>();
        String[] projection = {
                DataCache.Columns.PATH,
                DataCache.Columns.SIZE,
        };
        ContentResolver contentResolver = view.getContext().getContentResolver();
        Cursor cursor = contentResolver.query(DataCache.CONTENT_URI,
                projection,
                null,
                null,
                DataCache.Columns.PATH);
        if (cursor != null) {
            Log.d(TAG, "count: " + cursor.getCount());
            // перебор элементов
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i = i + 2) {
                    hashMap.put(cursor.getString(i), cursor.getString(i + 1));
                    Log.d(TAG, cursor.getColumnName(i) + " : " + cursor.getString(i));
                }
                Log.d(TAG, "=========================");
            }
            cursor.close();
        }
        return hashMap;
    }

    public void addToContentProvider(String path, String size) {
        ContentResolver contentResolver = view.getContext().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(DataCache.Columns.PATH, path);
        values.put(DataCache.Columns.SIZE, size);
        Uri uri = contentResolver.insert(DataCache.CONTENT_URI, values);
    }

    public void updateToContentProvider(String path, String size) {
        ContentResolver contentResolver = view.getContext().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(DataCache.Columns.PATH, path);
        values.put(DataCache.Columns.SIZE, size);
        String selection = DataCache.Columns.PATH + " = " + path;
        int count = contentResolver.update(DataCache.CONTENT_URI, values, selection, null);
    }

    public void deleteToContentProvider(String path) {
        ContentResolver contentResolver = view.getContext().getContentResolver();
        String selection = DataCache.Columns.PATH + " = ?";
        String[] args = {path};
        int count = contentResolver.delete(DataCache.CONTENT_URI, selection, args);
    }
}
