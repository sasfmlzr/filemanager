package com.sasfmlzr.filemanager.api.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.adapter.FileExploreAdapter;
import com.sasfmlzr.filemanager.api.file.FileOperation;
import java.io.File;
import java.util.List;

public class FragmentFileView extends Fragment implements AdapterView.OnItemClickListener{
    protected static final String STRING_CURRENT_PATH = "currentPath";
    protected static final String DEFAULT_PATH = Environment
            .getExternalStorageDirectory()
            .getAbsolutePath();
    private static final String BUNDLE_CONTENT = "bundle_content";
    protected static final int READ_EXTERNAL_STORAGE = 0;

    private ListView fileListView;
    private String currentPath;
    private  View view;

    private static String content;

    public static FragmentFileView newInstance(final String content) {
        Bundle args = new Bundle();
        FragmentFileView fragment = new FragmentFileView();
        args.putString(STRING_CURRENT_PATH, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        if (getArguments() != null && getArguments().containsKey(BUNDLE_CONTENT)) {
            content = getArguments().getString(BUNDLE_CONTENT);
        } else {
            //throw new IllegalArgumentException("Must be created through newInstance(...)");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_file_view, container, false);
        fileListView = view.findViewById(R.id.fileList);
    /*    Intent intent = inflater
        if (intent.hasExtra(STRING_CURRENT_PATH)) {
            setAdapter(intent.getStringExtra(STRING_CURRENT_PATH));
        } else {
            setAdapter(DEFAULT_PATH);
        }*/
        setAdapter(DEFAULT_PATH);
        requestPermissions();
        fileListView.setOnItemClickListener(this);
        return view;
    }

    protected void requestPermissions() {
        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(view.getContext(), R.string.permission_is_not_granted,
                    Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        if (requestCode==READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setAdapter(DEFAULT_PATH);
            } else {
                Toast.makeText(view.getContext(), this.getString(R.string.allow_permission),Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void setAdapter(String path){
        List<File> fileList = FileOperation.loadPath(path, view.getContext());
        FileExploreAdapter fileExploreAdapter = new FileExploreAdapter(view.getContext(),
                R.layout.current_item_file, fileList);
        fileListView.setAdapter(fileExploreAdapter);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        File fileModels = (File) parent.getItemAtPosition(position);
        currentPath = fileModels.getAbsolutePath();
        File file = new File(currentPath);
        if (file.exists()) {
            if (file.isDirectory()) {
                //start(this);
            } else if (file.isFile()) {
                FileOperation.openFile(view.getContext(), file);
            }
        }
    }



}
