package com.sasfmlzr.filemanager.api.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasfmlzr.filemanager.R;

import java.io.File;
import java.util.Objects;

public class EmptyPagerFragment extends Fragment implements FileViewFragment.OnDirectorySelectedListener {
    protected static final String BUNDLE_ARGS_CURRENT_PATH = "currentPath";
    private File currentFile;
    private boolean firstFragment = true;

    public static EmptyPagerFragment newInstance(final File file) {
        Bundle args = new Bundle();
        EmptyPagerFragment fragment = new EmptyPagerFragment();
        args.putString(BUNDLE_ARGS_CURRENT_PATH, file.getAbsolutePath());
        fragment.setArguments(args);
        return fragment;
    }

    public void replaceChildFragment(File currentFile) {
        this.currentFile = currentFile;
        Fragment childFragment = FileViewFragment.newInstance(currentFile);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment);
        if (!firstFragment) {
            transaction.addToBackStack(null);
        } else {
            firstFragment = false;
        }
        transaction.commit();
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        if (getArguments() != null) {
            currentFile = new File(Objects.requireNonNull
                    (getArguments().getString(BUNDLE_ARGS_CURRENT_PATH)));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_pager_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        replaceChildFragment(currentFile);
    }

    @Override
    public void onDirectorySelected(File currentFile) {
        replaceChildFragment(currentFile);
    }
}
