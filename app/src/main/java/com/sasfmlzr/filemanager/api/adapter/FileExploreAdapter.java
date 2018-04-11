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

@SuppressWarnings("unused")
public class FileExploreAdapter extends ArrayAdapter<FileModel> {
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final Resources mResources;
    private int res;
    private List<FileModel> mFileModels;

    public FileExploreAdapter(Context context, int resource, List<FileModel> fileModels){
        super(context,resource,fileModels);
        res=resource;
        mContext = context;
        mResources = context.getResources();
        mFileModels = fileModels;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder mViewHolder;
        if(convertView==null)
        {
            convertView = mInflater.inflate(R.layout.current_item_file, parent,false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else
        {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        FileModel fileModel= mFileModels.get(position);
        mViewHolder.dateView.setText(fileModel.getDateFile());
        mViewHolder.bottomView.setText(fileModel.getPathFile());
        mViewHolder.nameView.setText(fileModel.getNameFile());
        mViewHolder.imageView.setImageResource(fileModel.getImageIconFile());
        return convertView;
    }

    private  String getPath(int position){
        return mFileModels.get(position).getPathFile();
    }
    private class ViewHolder {
        final TextView nameView, bottomView,dateView;
        final ImageView imageView;
        ViewHolder(View view){
            imageView =     view.findViewById(R.id.icon_file);
            nameView =      view.findViewById(R.id.file_name_view);
            bottomView =    view.findViewById(R.id.bottom_view);
            dateView =      view.findViewById(R.id.date_view);
        }
    }


}
