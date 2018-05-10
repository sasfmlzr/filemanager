package com.sasfmlzr.filemanager.api.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.adapter.DirectoryNavigationAdapter;
import com.sasfmlzr.filemanager.api.adapter.FileExploreAdapter;
import com.sasfmlzr.filemanager.api.file.FileOperation;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static com.sasfmlzr.filemanager.api.file.FileOperation.getParentsFile;

public class FileViewFragment extends Fragment {
    private static final String BUNDLE_ARGS_CURRENT_PATH = "currentPath";

    private RecyclerView fileListView;
    private File currentFile;
    private View view;
    private OnDirectorySelectedListener listener;

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

        loadListDirectory();
        loadDirectoryNavigation();
        return view;
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

    public static FileViewFragment newInstance(final File file) {
        Bundle args = new Bundle();
        FileViewFragment fragment = new FileViewFragment();
        args.putString(BUNDLE_ARGS_CURRENT_PATH, file.getAbsolutePath());
        fragment.setArguments(args);
        return fragment;
    }

    private FileExploreAdapter.PathItemClickListener pathListener = (file) -> {
        if (file.exists()) {
            if (file.isDirectory()) {
                listener.onDirectorySelected(file);
            } else if (file.isFile()) {
                FileOperation.openFile(view.getContext(), file);
            }
        }
    };

    private DirectoryNavigationAdapter.NavigationItemClickListener navigationListener = (file) ->
            listener.onDirectorySelected(file);

    public interface OnDirectorySelectedListener {
        void onDirectorySelected(File currentFile);
    }

    private void setAdapter(File path, FileExploreAdapter.PathItemClickListener listener) {
        List<File> fileList = FileOperation.loadPath(path, view.getContext());

        RecyclerView.Adapter fileExploreAdapter = new FileExploreAdapter(fileList, listener);
        fileListView.setAdapter(fileExploreAdapter);
    }

    private void loadListDirectory() {
        fileListView = view.findViewById(R.id.fileList);
        RecyclerView.LayoutManager layoutManagerPathView = new LinearLayoutManager(view.getContext());
        fileListView.setLayoutManager(layoutManagerPathView);
        setAdapter(currentFile, pathListener);
    }

    private void loadDirectoryNavigation() {
        RecyclerView recyclerView = view.findViewById(R.id.navigation_recycler_view);
        RecyclerView.LayoutManager layoutManagerRecView = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManagerRecView);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration
                (recyclerView.getContext(), LinearLayoutManager.HORIZONTAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        List<File> files = getParentsFile(currentFile);
        RecyclerView.Adapter adapter = new DirectoryNavigationAdapter(files, navigationListener);
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        recyclerView.setAdapter(adapter);
    }
}
