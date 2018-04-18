package com.sasfmlzr.filemanager.api.adapter;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sasfmlzr.filemanager.R;
import java.io.File;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

/** File exlore to work properly listview */
public class FileExploreAdapter extends ArrayAdapter<File> {
    private final Context context;
    private List<File> fileModels;

    public FileExploreAdapter(Context context, int resource, List<File> fileModels) {
        super(context,resource,fileModels);
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
            convertView = inflater.inflate(R.layout.current_item_file, parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        File fileModel = fileModels.get(position);
        if (position == 0 && fileModel.getAbsolutePath().equals(Environment
                .getExternalStorageDirectory()
                .getAbsolutePath())){
            viewHolder.dateView.setText(null);
            viewHolder.bottomView.setText(null);
            viewHolder.nameView.setText("...");
            return  convertView;
        }
        viewHolder.dateView.setText(df.format(fileModel.lastModified()));
        viewHolder.bottomView.setText(fileModel.getAbsolutePath());
        viewHolder.nameView.setText(fileModel.getName());
        if (fileModel.isFile()) {
            viewHolder.imageView.setImageResource(R.drawable.file);
        } else if (fileModel.isDirectory()) {
            viewHolder.imageView.setImageResource(R.drawable.path);
        }
        return convertView;
    }

    private class ViewHolder {
        final TextView nameView, bottomView,dateView;
        final ImageView imageView;
        ViewHolder(View view) {
            imageView = view.findViewById(R.id.icon_file);
            nameView = view.findViewById(R.id.file_name_view);
            bottomView = view.findViewById(R.id.bottom_view);
            dateView = view.findViewById(R.id.date_view);
        }
    }
}
