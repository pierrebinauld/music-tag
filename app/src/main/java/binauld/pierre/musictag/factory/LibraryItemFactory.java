package binauld.pierre.musictag.factory;


import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

import binauld.pierre.musictag.decoder.AudioFileBitmapDecoder;
import binauld.pierre.musictag.decoder.BitmapDecoder;
import binauld.pierre.musictag.item.AudioItem;
import binauld.pierre.musictag.item.FolderItem;
import binauld.pierre.musictag.item.LibraryItem;
import binauld.pierre.musictag.item.NodeItem;

/**
 * Build a library item from a source file.
 * Allows activity to use item as folder or audio file.
 */
public class LibraryItemFactory {

    private BitmapDecoder folderBitmapDecoder;
    private int thumbnailSize;

    public LibraryItemFactory(BitmapDecoder folderBitmapDecoder, int thumbnailSize) {
        this.folderBitmapDecoder = folderBitmapDecoder;
        this.thumbnailSize = thumbnailSize;
    }

    /**
     * Build a library item from a source file.
     * @param file The source file. It has to be either a folder or a supported audio file.
     * @param parent The parent of the item
     * @return A library item
     * @throws IOException Exception is thrown when a problem occurs with the reading, tags or the format.
     */
    public LibraryItem build(File file, NodeItem parent) throws IOException {
        if(file.isDirectory()) {
            FolderItem folder = new FolderItem(file, parent);
            folder.setDecoder(folderBitmapDecoder);
            return folder;
        } else {
            try {
                AudioFile audio = AudioFileIO.read(file);
                AudioItem audioItem = new AudioItem(audio);
                audioItem.setDecoder(new AudioFileBitmapDecoder(audioItem.getAudioFile(), thumbnailSize, thumbnailSize));
                audioItem.setParent(parent);
                return audioItem;
            } catch (CannotReadException e) {
                throw new IOException(e);
            } catch (TagException e) {
                throw new IOException(e);
            } catch (ReadOnlyFileException e) {
                throw new IOException(e);
            } catch (InvalidAudioFrameException e) {
                throw new IOException(e);
            }
        }
    }
}
