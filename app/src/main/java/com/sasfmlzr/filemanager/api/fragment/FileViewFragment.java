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
import static com.sasfmlzr.filemanager.api.provider.CacheProviderOperation.addToContentProvider;
import static com.sasfmlzr.filemanager.api.provider.CacheProviderOperation.selectAllToContentProvider;

public class FileViewFragment extends Fragment {
    private static final String BUNDLE_ARGS_CURRENT_PATH = "currentPath";

    private RecyclerView fileListView;
    private File currentFile;
    private View view;
    private OnDirectorySelectedListener dirSelectedListener;
    private AsyncTask readDatabaseTask;
    private List<AsyncTask> calculateSize = new ArrayList<>();
    private DirectoryNavigationAdapter.NavigationItemClickListener navigationListener = (file) ->
            dirSelectedListener.onDirectorySelected(file);
    private FileExploreAdapter.PathItemClickListener pathListener = (file) -> {
        if (file.exists()) {
            if (file.isDirectory()) {
                dirSelectedListener.onDirectorySelected(file);
            } else if (file.isFile()) {
                FileOperation.openFile(view.getContext(), file);
            }
        }
    };

    private ContainerPagerFragment.OnVisible onVisibleListener;

    public static FileViewFragment newInstance(final File file) {
        Bundle args = new Bundle();
        FileViewFragment fragment = new FileViewFragment();
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
            dirSelectedListener = (OnDirectorySelectedListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " " +
                    R.string.exception_OnFragmentInteractionListener);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dirSelectedListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        interruptTasks();
        super.onStop();
    }

    private void interruptTasks() {
        if (readDatabaseTask != null) {
            readDatabaseTask.cancel(true);
        }
        if (!calculateSize.isEmpty()) {
            for (AsyncTask task : calculateSize) {
                task.cancel(true);
            }
        }
    }

    public interface OnCalculateSizeCompleted {
        void onCalculateSize(FileModel fileModel);
    }

    public interface ReadDatabaseListener {
        void listenReadDatabase(HashMap<String, Long> cacheSizeDirectory);
    }

    public interface OnDirectorySelectedListener {
        void onDirectorySelected(File currentFile);
    }

    public ContainerPagerFragment.OnVisible setVisibleListener() {
        if (onVisibleListener != null) {
            return onVisibleListener;
        }
        return null;
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

    private void setAdapter(File path, FileExploreAdapter.PathItemClickListener listener) {
        List<File> fileList = FileOperation.loadPath(path, view.getContext());
        List<FileModel> fileModels = new ArrayList<>();
        ContentResolver resolver = view.getContext().getContentResolver();

        if (fileList != null) {
            for (File file : fileList) {
                fileModels.add(new FileModel(file));
            }
        }

        ReadDatabaseListener readDatabaseListener = cacheSizeDirectory -> {
            FileExploreAdapter fileExploreAdapter =
                    new FileExploreAdapter(fileModels, listener, cacheSizeDirectory);
            fileListView.setAdapter(fileExploreAdapter);
            for (FileModel fileModel : fileModels) {
                if (!cacheSizeDirectory.containsKey(fileModel.getFile().getAbsolutePath()) &&
                        fileModel.getFile().isDirectory())
                    calculateSizeDirectory(fileModel.getFile(), fileExploreAdapter);
            }
        };
        readDatabaseTask = new ReadDatabaseTask(resolver, readDatabaseListener).execute();
        onVisibleListener = visible -> {
            if (!visible) {
                interruptTasks();
            }
        };
        HashMap<String, Long> cacheSizeDirectory = new HashMap<>();
        RecyclerView.Adapter fileExploreAdapter =
                new FileExploreAdapter(fileModels, listener, cacheSizeDirectory);
        fileListView.setAdapter(fileExploreAdapter);
    }

    private void calculateSizeDirectory(File file, FileExploreAdapter adapter) {
        ContentResolver resolver = Objects.requireNonNull(getContext()).getContentResolver();
        FileViewFragment.OnCalculateSizeCompleted listener = (FileModel fileModel) -> {
            adapter.replaceSizeOnTextView(fileModel);
        };
        CalculateSizeTask task = new CalculateSizeTask(file, resolver, listener);
        task.execute();
        calculateSize.add(task);
    }

    public static class ReadDatabaseTask extends AsyncTask<Void, Void, HashMap<String, Long>> {
        ContentResolver contentResolver;
        ReadDatabaseListener listener;

        ReadDatabaseTask(ContentResolver contentResolver, ReadDatabaseListener listener) {
            this.contentResolver = contentResolver;
            this.listener = listener;
        }

        @Override
        protected HashMap<String, Long> doInBackground(Void... voids) {
            return selectAllToContentProvider(contentResolver);
        }

        @Override
        protected void onPostExecute(HashMap<String, Long> hashMap) {
            listener.listenReadDatabase(hashMap);
            super.onPostExecute(hashMap);
        }
    }

    public static class CalculateSizeTask extends AsyncTask<Void, Void, FileModel> {
        private FileViewFragment.OnCalculateSizeCompleted listener;
        private ContentResolver resolver;
        private File file;

        CalculateSizeTask(File file,
                          ContentResolver contentResolver,
                          FileViewFragment.OnCalculateSizeCompleted listener) {
            this.listener = listener;
            this.file = file;
            resolver = contentResolver;
        }

        @Override
        protected FileModel doInBackground(Void... voids) {
            if (isCancelled()) {
                return null;
            }
            FileUtils.AddToDatabaseCallback callback = new FileUtils.AddToDatabaseCallback() {
                @Override
                public void addToDatabase(FileModel fileModel) {
                    addToContentProvider(resolver,
                            fileModel.getFile().getAbsolutePath(),
                            fileModel.getSizeDirectory());
                }

                @Override
                public boolean isTaskCancelled() {
                    return isCancelled();
                }
            };
            return FileUtils.getDirectorySize(file, callback);
        }

        @Override
        protected void onPostExecute(FileModel fileModel) {
            listener.onCalculateSize(fileModel);
            super.onPostExecute(fileModel);
        }
    }
}
