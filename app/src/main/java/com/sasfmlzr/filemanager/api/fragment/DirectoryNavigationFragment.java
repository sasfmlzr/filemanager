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

import com.sasfmlzr.filemanager.R;
import com.sasfmlzr.filemanager.api.adapter.DirectoryNavigationAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DirectoryNavigationFragment extends Fragment {
    protected static final String STRING_CURRENT_PATH = "currentPath";

    private String currentPath;
    private View view;
    private OnFragmentInteractionListener listener;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


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
        view = inflater.inflate(R.layout.fragment_directory_navigation, container, false);
        recyclerView = view.findViewById(R.id.navigation_recycler_view);
        //layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        //layoutManager.sc
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        //TODO: ask FileSystems.getDefault().getSeparator()
        List<String> tempList = Arrays.asList(currentPath.split("/"));
        List<String> strings = new ArrayList<>(tempList.subList(1, tempList.size()));
        adapter = new DirectoryNavigationAdapter(strings);
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
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDirectoryNavigation();
    }
}
