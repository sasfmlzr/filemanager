package com.sasfmlzr.filemanager.api.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.adapter.FileExploreAdapter;
import com.sasfmlzr.filemanager.api.file.FileOperation;
import com.sasfmlzr.filemanager.api.other.Permissions;
import java.io.File;
import java.util.List;

public class FragmentFileView extends Fragment implements AdapterView.OnItemClickListener{
    protected static final String STRING_CURRENT_PATH = "currentPath";
    protected static final String DEFAULT_PATH = Environment
            .getExternalStorageDirectory()
            .getAbsolutePath();

    private ListView fileListView;
    private String currentPath;
    private  View view;
    private OnArticleSelectedListener listener;

    public interface OnArticleSelectedListener {
        void onArticleSelected(String currentPath);
    }

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
        if (getArguments() != null && getArguments().containsKey(STRING_CURRENT_PATH)) {
            currentPath = getArguments().getString(STRING_CURRENT_PATH);
        } else {
            currentPath=DEFAULT_PATH;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_file_view, container, false);
        fileListView = view.findViewById(R.id.fileList);
        Permissions.requestReadPermissions(getActivity());
        setAdapter(currentPath);
        fileListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        if (requestCode==0) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setAdapter(currentPath);
            } else {
                Permissions.requestReadPermissions(getActivity());
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
                listener.onArticleSelected(currentPath);
                //start(this);
            } else if (file.isFile()) {
                FileOperation.openFile(view.getContext(), file);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnArticleSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }
}
