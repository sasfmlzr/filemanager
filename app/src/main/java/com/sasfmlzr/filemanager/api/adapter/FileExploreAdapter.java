package com.sasfmlzr.filemanager.api.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sasfmlzr.filemanager.R;

import java.util.List;

public class FileExploreAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final Resources mResources;

    public FileExploreAdapter(Context context, LayoutInflater inflater){
        mInflater=inflater;
        mContext = context;
        mResources = context.getResources();
    }

    private List<String> mData;


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // final ViewHolder viewHolder;
        if(convertView==null){
            convertView = mInflater.inflate(R.layout.activity_main, parent,false);
        } else{

        }

        return convertView;
    }

    private class ViewHolder {

        final TextView nameView, bottomView,dateView;
        ViewHolder(View view){
            nameView = (TextView) view.findViewById(R.id.file_name_view);
            bottomView = (TextView) view.findViewById(R.id.bottom_view);
            dateView = (TextView) view.findViewById(R.id.date_view);
         //   capitalView = (TextView) view.findViewById(R.id.capital);
        }
    }


}
