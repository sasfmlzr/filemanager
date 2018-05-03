package com.sasfmlzr.filemanager.api.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.other.FileUtils;

import java.io.File;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

/**
 * File exlore to work properly listview
 */
public class FileExploreAdapter extends ArrayAdapter<File> {
    private final Context context;
    private List<File> fileModels;

    public FileExploreAdapter(Context context, int resource, List<File> fileModels) {
        super(context, resource, fileModels);
        this.context = context;
        this.fileModels = fileModels;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final ViewHolder viewHolder;
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT, Locale.getDefault());
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.current_item_file, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        File fileModel = fileModels.get(position);
        viewHolder.dateView.setText(df.format(fileModel.lastModified()));
        if (viewHolder.asyncTask != null) {
            viewHolder.asyncTask.cancel(false);
        }
        if (viewHolder.sizeItemView.getText() == "") {
            viewHolder.sizeItemView.setText("...");
        }
        viewHolder.asyncTask = new AsyncTask<File, Void, String>() {
            @Override
            protected String doInBackground(File... files) {
                String size;
                if (!files[0].canRead()) {
                    return null;
                }
                if (files[0].isFile()) {
                    size = FileUtils.formatCalculatedSize(files[0].length());
                } else {
                    size = FileUtils.formatCalculatedSize(FileUtils.getDirectorySize(files[0]));
                }
                return size;
            }

            @Override
            protected void onPostExecute(String s) {
                viewHolder.sizeItemView.setText(s);
                super.onPostExecute(s);
            }
        }.execute(fileModel);
        viewHolder.nameView.setText(fileModel.getName());
        if (fileModel.isFile()) {
            viewHolder.imageView.setImageResource(R.drawable.file);
        } else if (fileModel.isDirectory()) {
            viewHolder.imageView.setImageResource(R.drawable.path);
        }
        return convertView;
    }

    private class ViewHolder {
        final TextView nameView, sizeItemView, dateView;
        final ImageView imageView;
        AsyncTask asyncTask;

        ViewHolder(View view) {
            imageView = view.findViewById(R.id.icon_file);
            nameView = view.findViewById(R.id.file_name_view);
            sizeItemView = view.findViewById(R.id.size_item);
            dateView = view.findViewById(R.id.date_view);
        }
    }
}
