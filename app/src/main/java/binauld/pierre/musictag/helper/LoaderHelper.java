package binauld.pierre.musictag.helper;

import java.io.FileFilter;
import java.util.Comparator;

import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.collection.LibraryItemComparator;
import binauld.pierre.musictag.factory.FileFilterFactory;
import binauld.pierre.musictag.factory.LibraryItemFactory;
import binauld.pierre.musictag.io.LibraryItemLoader;
import binauld.pierre.musictag.io.LibraryItemLoaderManager;
import binauld.pierre.musictag.item.LibraryItem;

/**
 * Help to build the AsyncTask loading library items list.
 */
public class LoaderHelper {

    /**
     * Help to build the AsyncTask loading library items list.
     *
     * @param adapter The adapter used to adapt library items for the list view.
     * @param manager The manager to the loader.
     * @return The loader built.
     */
    public static LibraryItemLoader buildLoader(LibraryItemAdapter adapter, LibraryItemFactory factory, LibraryItemLoaderManager manager, int updateStep) {
        FileFilterFactory filterFactory = new FileFilterFactory();

        FileFilter filter = filterFactory.build();

        Comparator<LibraryItem> comparator = new LibraryItemComparator();

        return new LibraryItemLoader(adapter, factory, filter, comparator, updateStep);
    }
}
