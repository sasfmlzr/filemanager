package com.sasfmlzr.filemanager.api.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.adapter.DirectoryNavigationAdapter;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static com.sasfmlzr.filemanager.api.file.FileOperation.getParentsFile;

public class DirectoryNavigationFragment extends Fragment {
    protected static final String BUNDLE_ARGS_CURRENT_PATH = "currentPath";

    private File currentFile;
    private View view;
    private OnFragmentInteractionListener listener;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public interface OnFragmentInteractionListener {
    }

    public static DirectoryNavigationFragment newInstance(File file) {
        DirectoryNavigationFragment fragment = new DirectoryNavigationFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_ARGS_CURRENT_PATH, file.getAbsolutePath());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentFile = new File(Objects.requireNonNull
                    (getArguments().getString(BUNDLE_ARGS_CURRENT_PATH)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_directory_navigation, container, false);
        recyclerView = view.findViewById(R.id.navigation_recycler_view);
        layoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration
                (recyclerView.getContext(), LinearLayoutManager.HORIZONTAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        DirectoryNavigationAdapter.NavigationItemClickListener listener = (view, position) -> {
            Toast.makeText(view.getContext(), " " + position, Toast.LENGTH_SHORT).show();
        };
        List<File> files = getParentsFile(currentFile);
        adapter = new DirectoryNavigationAdapter(files, listener);
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " " + R.string.exception_OnFragmentInteractionListener);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
