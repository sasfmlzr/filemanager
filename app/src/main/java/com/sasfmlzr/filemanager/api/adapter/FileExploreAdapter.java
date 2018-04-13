package com.sasfmlzr.filemanager.api.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.model.FileModel;
import java.util.List;

/** File exlore to work properly listview */
@SuppressWarnings("unused")
public class FileExploreAdapter extends ArrayAdapter<FileModel> {
    private final LayoutInflater inflater;
    private final Context context;
    private final Resources resources;
    private int res;
    private List<FileModel> fileModels;

    public FileExploreAdapter(Context context, int resource, List<FileModel> fileModels) {
        super(context,resource,fileModels);
        res = resource;
        this.context = context;
        resources = context.getResources();
        this.fileModels = fileModels;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.current_item_file, parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        FileModel fileModel= fileModels.get(position);
        viewHolder.dateView.setText(fileModel.getDateFile());
        viewHolder.bottomView.setText(fileModel.getPathFile());
        viewHolder.nameView.setText(fileModel.getNameFile());
        viewHolder.imageView.setImageResource(fileModel.getImageIconFile());
        return convertView;
    }

    private String getPath(int position) {
        return fileModels.get(position).getPathFile();
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
