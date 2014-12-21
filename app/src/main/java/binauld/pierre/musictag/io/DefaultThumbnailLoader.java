package binauld.pierre.musictag.io;


import android.graphics.Bitmap;
import android.os.AsyncTask;

import binauld.pierre.musictag.decoder.BitmapDecoder;

public class DefaultThumbnailLoader extends AsyncTask<BitmapDecoder, Void, Integer> {

    private Cache<Bitmap> cache;


    public DefaultThumbnailLoader(Cache<Bitmap> cache) {
        this.cache = cache;
    }

    @Override
    protected Integer doInBackground(BitmapDecoder... decoders) {
        for(BitmapDecoder decoder : decoders) {
            cache.put(decoder.getId(), decoder.decode());
        }
        return decoders.length;
    }
}
