package com.sasfmlzr.filemanager.api.fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasfmlzr.filemanager.R;

import java.io.File;
import java.util.Objects;

public class EmptyPagerFragment extends Fragment {
    private OnFragmentInteractionListener listener;
    protected static final String BUNDLE_ARGS_CURRENT_PATH = "currentPath";
    protected static final File DEFAULT_PATH = Environment
            .getExternalStorageDirectory();
    private File currentFile;

    public static EmptyPagerFragment newInstance(final File file) {
        Bundle args = new Bundle();
        EmptyPagerFragment fragment = new EmptyPagerFragment();
        args.putString(BUNDLE_ARGS_CURRENT_PATH, file.getAbsolutePath());
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pager_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        replaceChildFragment(currentFile);
    }

    public void replaceChildFragment(File currentFile) {
        Fragment childFragment = FileViewFragment.newInstance(currentFile);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + R.string.exception_OnFragmentInteractionListener);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
        void onEmptyFragmentSelected(File currentFile);
    }
}
