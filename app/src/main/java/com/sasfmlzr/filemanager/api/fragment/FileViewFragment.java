package com.sasfmlzr.filemanager.api.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import java.util.Objects;

public class FileViewFragment extends Fragment implements AdapterView.OnItemClickListener {
    protected static final String BUNDLE_ARGS_CURRENT_PATH = "currentPath";
    private static final int PERMISSION_CODE_READ_EXTERNAL_STORAGE = 0;

    private ListView fileListView;
    private File currentFile;
    private View view;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onDirectorySelected(String currentPath);
    }

    public static FileViewFragment newInstance(final String content) {
        Bundle args = new Bundle();
        FileViewFragment fragment = new FileViewFragment();
        args.putString(BUNDLE_ARGS_CURRENT_PATH, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        if (getArguments() != null) {
            currentFile = new File(Objects.requireNonNull
                    (getArguments().getString(BUNDLE_ARGS_CURRENT_PATH)));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_file_view, container, false);
        fileListView = view.findViewById(R.id.fileList);
        requestReadPermissions();
        setAdapter(currentFile);
        fileListView.setOnItemClickListener(this);
        return view;
    }

    public void requestReadPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity().getLayoutInflater()
                .getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity().getLayoutInflater().getContext(), R.string.permission_is_not_granted,
                    Toast.LENGTH_SHORT).show();
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setAdapter(currentFile);
            } else {
                requestReadPermissions();
                Toast.makeText(view.getContext(), this.getString(R.string.allow_permission), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setAdapter(File path) {
        List<File> fileList = FileOperation.loadPath(path, view.getContext());
        FileExploreAdapter fileExploreAdapter = new FileExploreAdapter(view.getContext(),
                R.layout.current_item_file, fileList);
        fileListView.setAdapter(fileExploreAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        File file = (File) parent.getItemAtPosition(position);
        if (file.exists()) {
            if (file.isDirectory()) {
                listener.onDirectorySelected(file.getAbsolutePath());
            } else if (file.isFile()) {
                FileOperation.openFile(view.getContext(), file);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " " +
                    R.string.exception_OnArticleSelectedListener);
        }
    }
}
