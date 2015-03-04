package binauld.pierre.musictag.service;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.item.Item;
import binauld.pierre.musictag.task.ArtworkLoader;
import binauld.pierre.musictag.task.AsyncDrawable;
import binauld.pierre.musictag.task.DefaultArtworkLoader;

/**
 * Help to build artwork from audio file.
 */
public class ArtworkManager {

    private BitmapDecoder defaultArtworkDecoder;

    private LruCache<String, Bitmap> cache;

    public ArtworkManager(BitmapDecoder defaultArtworkDecoder) {
        this.defaultArtworkDecoder = defaultArtworkDecoder;
        // Use 1/8th of the available memory for this memory cache.
        this.cache = new LruCache<>((int) (Runtime.getRuntime().maxMemory() / 1024 / 8));
    }

    public void initDefaultArtwork(int artworkSize) {
        DefaultArtworkLoader loader = new DefaultArtworkLoader(this.cache, artworkSize);
        loader.execute(this.defaultArtworkDecoder);
    }

    /**
     * Set the Artwork associated to item to the imageView.
     * If the Artwork has not been yet loaded, then it is loaded a placeholder is put in the image view while loading.
     *
     * @param item      Current item.
     * @param imageView Associated image view.
     */
    public void setArtwork(Item item, ImageView imageView, int artworkSize) {

        final String key = item.getBitmapDecoder().getKey(artworkSize, artworkSize);

        final Bitmap bitmap = cache.get(key);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else if (cancelPotentialWork(item, imageView)) {
            Resources res = imageView.getResources();
            final ArtworkLoader task = new ArtworkLoader(imageView, cache, defaultArtworkDecoder, artworkSize);
            Bitmap placeholder = cache.get(defaultArtworkDecoder.getKey(artworkSize, artworkSize));
            final AsyncDrawable asyncDrawable = new AsyncDrawable(res, placeholder, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(item);
        }
    }

    /**
     * Cancel a potential AsyncTask from anAsyncDrawable of the ImageView if the AsyncTask is outdated.
     *
     * @param item      The item that the artwork must be load.
     * @param imageView The image view which has to display artwork thumbnail.
     * @return False if the same work is in progress.
     */
    private boolean cancelPotentialWork(Item item, ImageView imageView) {
        final ArtworkLoader artworkLoader = AsyncDrawable.retrieveBitmapLoader(imageView);

        if (artworkLoader != null) {
            final Item taskItem = artworkLoader.getWorkingItem();
            // If bitmapData is not yet set or it differs from the new data
            if (taskItem == null || taskItem != item) {
                // Cancel previous task
                artworkLoader.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }
}