package binauld.pierre.musictag.io;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import binauld.pierre.musictag.activities.MainActivity;
import binauld.pierre.musictag.adapter.FolderItem;
import binauld.pierre.musictag.adapter.LibraryItem;
import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.adapter.NodeItem;
import binauld.pierre.musictag.factory.LibraryItemFactory;

/**
 * Load all the audio files and directories from a folder to an adapter.
 * This task is executed asynchronously when execute is called.
 */
public class LibraryItemLoader extends AsyncTask<FolderItem, LibraryItem, Integer> {

    //TODO: Parameterize this for different screen size
    private static int UPDATE_STEP = 10;

    private final NodeItem node;
    private final LibraryItemLoaderManager manager;
    private LibraryItemAdapter adapter;
    private FileFilter filter;
    private LibraryItemFactory factory;

    public LibraryItemLoader(LibraryItemAdapter adapter, LibraryItemFactory libraryItemFactory, FileFilter filter, LibraryItemLoaderManager manager) {
        this.adapter = adapter;
        this.node = adapter.getCurrentNode();
        this.filter = filter;
        this.factory = libraryItemFactory;
        this.manager = manager;
    }

    @Override
    protected Integer doInBackground(FolderItem... values) {
        int count = 0;

        for (FolderItem folderItem : values) {
            folderItem.setIsLoaded(true);
            File folder = folderItem.getFile();
            File[] files = folder.listFiles(filter);
            if (null == files) {
                Log.w(this.getClass().toString(), "'" + folder.getAbsolutePath() + "' does not contains readable file.");
            } else {
                int j = 0;
                List<LibraryItem> items = new ArrayList<LibraryItem>();
                for (int i = 0; i < files.length; i++) {

                    try {
                        LibraryItem item = factory.build(files[i], folderItem);

                        items.add(item);
                        j = ++j % UPDATE_STEP;
                        if (j == 0 || i == files.length - 1) {
                            publishProgress(items.toArray(new LibraryItem[items.size()]));
                            count += items.size();
                            items = new ArrayList<LibraryItem>();
                        }
                    } catch (IOException e) {
                        Log.w(this.getClass().toString(), e.getMessage());
                    }
                }
            }
        }

        return count;
    }

    @Override
    protected void onProgressUpdate(LibraryItem... items) {
//        node.add(items);
        //TODO: When architecture will stabilize, comparator will may be used here.
        items[0].getParent().add(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Integer count) {
        super.onPostExecute(count);
        manager.remove(this);
        Log.i(this.getClass().toString(), count + " item(s) loaded from " + node.getPrimaryInformation());
    }

}
