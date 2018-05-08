package com.sasfmlzr.filemanager.api.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.adapter.DirectoryNavigationAdapter;
import com.sasfmlzr.filemanager.api.adapter.FileExploreAdapter;
import com.sasfmlzr.filemanager.api.file.FileOperation;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static com.sasfmlzr.filemanager.api.file.FileOperation.getParentsFile;

public class FileViewFragment extends Fragment {
    protected static final String BUNDLE_ARGS_CURRENT_PATH = "currentPath";
    private static final int PERMISSION_CODE_READ_EXTERNAL_STORAGE = 0;

    private RecyclerView fileListView;
    private File currentFile;
    private View view;
    private OnDirectorySelectedListener listener;
    private FileExploreAdapter.PathItemClickListener pathListener = (file) -> {
        if (file.exists()) {
            if (file.isDirectory()) {
                listener.onDirectorySelected(file);
            } else if (file.isFile()) {
                FileOperation.openFile(view.getContext(), file);
            }
        }
    };

    public interface OnDirectorySelectedListener {
        void onDirectorySelected(File currentFile);
    }

    public static FileViewFragment newInstance(final File file) {
        Bundle args = new Bundle();
        FileViewFragment fragment = new FileViewFragment();
        args.putString(BUNDLE_ARGS_CURRENT_PATH, file.getAbsolutePath());
        fragment.setArguments(args);
        return fragment;
    }

    public void requestReadPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity().getLayoutInflater()
                .getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE_READ_EXTERNAL_STORAGE);
        }
    }

    private void setAdapter(File path, FileExploreAdapter.PathItemClickListener listener) {
        List<File> fileList = FileOperation.loadPath(path, view.getContext());

        RecyclerView.Adapter fileExploreAdapter = new FileExploreAdapter(fileList, listener);
        fileListView.setAdapter(fileExploreAdapter);
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
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_file_view, container, false);

        fileListView = view.findViewById(R.id.fileList);
        RecyclerView.LayoutManager layoutManagerPathView = new LinearLayoutManager(view.getContext());
        fileListView.setLayoutManager(layoutManagerPathView);
        requestReadPermissions();
        setAdapter(currentFile, pathListener);

        RecyclerView recyclerView = view.findViewById(R.id.navigation_recycler_view);
        RecyclerView.LayoutManager layoutManagerRecView = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManagerRecView);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration
                (recyclerView.getContext(), LinearLayoutManager.HORIZONTAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        DirectoryNavigationAdapter.NavigationItemClickListener navigationListener = (file) ->
                listener.onDirectorySelected(file);
        List<File> files = getParentsFile(currentFile);
        RecyclerView.Adapter adapter = new DirectoryNavigationAdapter(files, navigationListener);
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setAdapter(currentFile, pathListener);
            } else {
                Toast.makeText(view.getContext(), this.getString(R.string.allow_permission), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnDirectorySelectedListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " " +
                    R.string.exception_OnFragmentInteractionListener);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
