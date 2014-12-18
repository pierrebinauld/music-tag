package binauld.pierre.musictag.item;


import binauld.pierre.musictag.decoder.BitmapDecoder;

/**
 * Item from audio library (Song or folder).
 */
public interface LibraryItem {

    /**
     * Check if the item contain an audio file.
     * @return Return true if it is an audio file.
     */
    public boolean getAudio();

    /**
     * Get the primary information of the item (Like song title or folder name).
     * This information should be used for sorting and main display in the list.
     * @return A String of the primary information.
     */
    String getPrimaryInformation();

    /**
     * Get the primary information of the item (Like artist name)
     * This information should be displayed in addition to the main display.
     * @return A String of the secondary information.
     */
    String getSecondaryInformation();

    /**
     * Return the item's parent.
     * @return The parent.
     */
    NodeItem getParent();

    BitmapDecoder getDecoder();

    void switchDecoder(BitmapDecoder bitmapDecoder);
}