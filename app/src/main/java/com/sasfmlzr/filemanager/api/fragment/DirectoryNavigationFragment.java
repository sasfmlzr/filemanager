package com.sasfmlzr.filemanager.api.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasfmlzr.filemanager.R;

public class DirectoryNavigationFragment extends Fragment {
    protected static final String STRING_CURRENT_PATH = "currentPath";


    // TODO: Rename and change types of parameters
    private String currentPath;

    private OnFragmentInteractionListener mListener;

    public static DirectoryNavigationFragment newInstance(String currentPath) {
        DirectoryNavigationFragment fragment = new DirectoryNavigationFragment();
        Bundle args = new Bundle();
        args.putString(STRING_CURRENT_PATH, currentPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentPath = getArguments().getString(STRING_CURRENT_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_directory_navigation, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
