package com.sasfmlzr.filemanager.api.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
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
import com.sasfmlzr.filemanager.api.model.FileModel;
import com.sasfmlzr.filemanager.api.other.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.sasfmlzr.filemanager.api.file.FileOperation.getParentsFile;
import static com.sasfmlzr.filemanager.api.provider.CacheProviderOperation.selectAllToContentProvider;

public class FileViewFragment extends Fragment {
    private static final String BUNDLE_ARGS_CURRENT_PATH = "currentPath";

    private RecyclerView fileListView;
    private File currentFile;
    private View view;
    private OnDirectorySelectedListener listener;
    private AsyncTask runReadDatabase;


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
        setRetainInstance(true);
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

    @Override
    public void onStop() {
        runReadDatabase.cancel(true);
        FileExploreAdapter adapter = (FileExploreAdapter) fileListView.getAdapter();
        if (adapter.asyncRunnableCalculateSize != null) {
            if (adapter.asyncRunnableCalculateSize.getStatus()
                    .equals(AsyncTask.Status.RUNNING)) {
                adapter.asyncRunnableCalculateSize.cancel(true);
            }
        }
        super.onStop();
    }

    public interface OnDirectorySelectedListener {
        void onDirectorySelected(File currentFile);
    }

    private DirectoryNavigationAdapter.NavigationItemClickListener navigationListener = (file) ->
            listener.onDirectorySelected(file);

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

    private void setAdapter(File path, FileExploreAdapter.PathItemClickListener listener) {
        List<File> fileList = FileOperation.loadPath(path, view.getContext());
        List<FileModel> fileModels = new ArrayList<>();
        if (fileList != null) {
            for (File file : fileList) {
                fileModels.add(new FileModel(file));
            }
        }

        ReadDatabaseListener readDatabaseListener = cacheSizeDirectory -> {
            RecyclerView.Adapter fileExploreAdapter =
                    new FileExploreAdapter(fileModels, listener, cacheSizeDirectory);
            fileListView.setAdapter(fileExploreAdapter);
        };
        runReadDatabase = new RunReadDatabase(view.getContext().getContentResolver(), readDatabaseListener).execute();

        HashMap<String, String> cacheSizeDirectory = new HashMap<>();
        RecyclerView.Adapter fileExploreAdapter =
                new FileExploreAdapter(fileModels, listener, cacheSizeDirectory);
        fileListView.setAdapter(fileExploreAdapter);
    }

    private void loadListDirectory() {
        fileListView = view.findViewById(R.id.fileList);
        RecyclerView.LayoutManager layoutManagerPathView = new LinearLayoutManager(view.getContext());
        fileListView.setLayoutManager(layoutManagerPathView);
        fileListView.setNestedScrollingEnabled(false);
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

    public interface OnCalculateSizeCompleted {
        void onCalculateSize(String string);
    }

    public interface ReadDatabaseListener {
        void listenReadDatabase(HashMap<String, String> cacheSizeDirectory);
    }


    public class RunReadDatabase extends AsyncTask<Void, Void, HashMap> {
        ContentResolver contentResolver;
        ReadDatabaseListener listener;

        public RunReadDatabase(ContentResolver contentResolver, ReadDatabaseListener listener) {
            this.contentResolver = contentResolver;
            this.listener = listener;
        }

        @Override
        protected HashMap doInBackground(Void... voids) {
            HashMap<String, String> hashMap = selectAllToContentProvider(contentResolver);
            return hashMap;
        }

        @Override
        protected void onPostExecute(HashMap hashMap) {
            if (!hashMap.isEmpty()) {
                listener.listenReadDatabase(hashMap);
            }
            super.onPostExecute(hashMap);
        }
    }

    /*private Handler mUiHandler = new Handler();

    public class TestThread implements Runnable {
        ContentResolver contentResolver;
        ReadDatabaseListener listener;

        TestThread(ContentResolver contentResolver, ReadDatabaseListener listener) {
            this.contentResolver=contentResolver;
            this.listener=listener;
        }

        @Override
        public void run() {
            HashMap<String,String> hashMap = selectAllToContentProvider(contentResolver);
            mUiHandler.post(() -> {
                if (!hashMap.isEmpty()) {
                    listener.listenReadDatabase(hashMap);
                }
            });
        }
    }*/


    public static class AsyncRunnableCalculateSize extends AsyncTask<File, Void, String> {
        private FileViewFragment.OnCalculateSizeCompleted listener;
        private ContentResolver contentResolver;

        public AsyncRunnableCalculateSize(FileViewFragment.OnCalculateSizeCompleted listener,
                                          ContentResolver contentResolver) {
            this.listener = listener;
            this.contentResolver = contentResolver;
        }

        @Override
        protected String doInBackground(File... files) {
            String size;
            if (!files[0].canRead()) {
                return null;
            }
            size = FileUtils.formatCalculatedSize(FileUtils.getDirectorySize(files[0], contentResolver));
            return size;
        }

        @Override
        protected void onPostExecute(String s) {
            listener.onCalculateSize(s);
            super.onPostExecute(s);
        }
    }
}
