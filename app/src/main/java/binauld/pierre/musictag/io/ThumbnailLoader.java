package binauld.pierre.musictag.io;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.item.LibraryItem;

public class ThumbnailLoader extends AsyncTask<LibraryItem, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private final BitmapDecoder defaultThumbnailDecoder;
    private LibraryItem item;
    private Cache<Bitmap> cache;

    public ThumbnailLoader(ImageView imageView, Cache<Bitmap> cache, BitmapDecoder defaultThumbnailDecoder) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        this.imageViewReference = new WeakReference<>(imageView);
        this.cache = cache;
        this.defaultThumbnailDecoder = defaultThumbnailDecoder;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(LibraryItem... items) {
        item = items[0];
        BitmapDecoder decoder = item.getDecoder();
        String key = decoder.getId();
        Bitmap bitmap = decoder.decode();
        if (null != bitmap) {
            cache.put(key, bitmap);
        } else if (decoder != defaultThumbnailDecoder) {
            item.switchDecoder(defaultThumbnailDecoder);
            bitmap = this.doInBackground(item);
        }

        return bitmap;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            final ThumbnailLoader thumbnailLoader = AsyncDrawable.getBitmapLoader(imageView);
            if (this == thumbnailLoader && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public LibraryItem getWorkingItem() {
        return item;
    }
}
