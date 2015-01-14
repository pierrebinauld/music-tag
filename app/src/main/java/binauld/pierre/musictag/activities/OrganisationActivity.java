package binauld.pierre.musictag.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.melnykov.fab.FloatingActionButton;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import binauld.pierre.musictag.R;

public class OrganisationActivity extends Activity implements View.OnClickListener {
    private EditText placeholder;
    public static File root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation);
        placeholder = (EditText) findViewById(R.id.edit_text_organisation);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.organisation_valid);
        fab.setOnClickListener(this);

        Button btn_title = (Button) findViewById(R.id.btn_title);
        btn_title.setOnClickListener(this);
        Button btn_artist = (Button) findViewById(R.id.btn_artist);
        btn_artist.setOnClickListener(this);
        Button btn_album = (Button) findViewById(R.id.btn_album);
        btn_album.setOnClickListener(this);
        Button btn_year = (Button) findViewById(R.id.btn_year);
        btn_year.setOnClickListener(this);
        Button btn_disc = (Button) findViewById(R.id.btn_disc);
        btn_disc.setOnClickListener(this);
        Button btn_track = (Button) findViewById(R.id.btn_track);
        btn_track.setOnClickListener(this);
        Button btn_album_artist = (Button) findViewById(R.id.btn_album_artist);
        btn_album_artist.setOnClickListener(this);
        Button btn_composer = (Button) findViewById(R.id.btn_composer);
        btn_composer.setOnClickListener(this);
        Button btn_grouping = (Button) findViewById(R.id.btn_grouping);
        btn_grouping.setOnClickListener(this);
        Button btn_genre = (Button) findViewById(R.id.btn_genre);
        btn_genre.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_organisation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.organisation_valid:
                processOrganisation();
                break;
            case R.id.btn_title:
                addContentToPlaceHolder("{title}");
                break;
            case R.id.btn_artist:
                addContentToPlaceHolder("{artist}");
                break;
            case R.id.btn_album:
                addContentToPlaceHolder("{album}");
                break;
            case R.id.btn_year:
                addContentToPlaceHolder("{year}");
                break;
            case R.id.btn_disc:
                addContentToPlaceHolder("{disc}");
                break;
            case R.id.btn_track:
                addContentToPlaceHolder("{track}");
                break;
            case R.id.btn_album_artist:
                addContentToPlaceHolder("{album_artist}");
                break;
            case R.id.btn_composer:
                addContentToPlaceHolder("{composer}");
                break;
            case R.id.btn_grouping:
                addContentToPlaceHolder("{grouping}");
                break;
            case R.id.btn_genre:
                addContentToPlaceHolder("{genre}");
                break;
            default:
                break;
        }
    }

    private void addContentToPlaceHolder(String s) {
        int cursorPosition = placeholder.getSelectionStart();
        String initText = placeholder.getText().toString();
        String newText = initText.substring(0,cursorPosition);
        newText += s;
        newText += initText.substring(cursorPosition);
        placeholder.setText(newText);
        placeholder.setSelection(cursorPosition+s.length());
    }

    private void processOrganisation() {
        List<File> files = recursiveDirectoryContent(root);
        String placeholderContent = placeholder.getText().toString();
        //list all music files
        for(File f : files){
            AudioFile audio = null;
            try {
                audio = AudioFileIO.read(f);
            } catch (CannotReadException | InvalidAudioFrameException | ReadOnlyFileException | TagException | IOException e) {
                Log.e("error", e.getMessage());
            }
            String newPath = formatePath(placeholderContent, audio);

            Log.e("New Path : ", newPath);

        }
    }

    private String formatePath(String placeholder, AudioFile audio){
        String newPath = placeholder;
        Tag tags = audio.getTag();

        String title = "\\{title\\}";
        String artist = "\\{artist\\}";
        String album = "\\{album\\}";
        String year = "\\{year\\}";
        String disc = "\\{disc\\}";
        String track = "\\{track\\}";
        String album_artist = "\\{album_artist\\}";
        String composer = "\\{composer\\}";
        String grouping = "\\{grouping\\}";
        String genre = "\\{genre\\}";

        String newTitle = getTheTag(title, tags, FieldKey.TITLE);
        String newArtist = getTheTag(artist, tags, FieldKey.ARTIST);
        String newAlbum = getTheTag(album, tags, FieldKey.ALBUM);
        String newYear = getTheTag(year, tags, FieldKey.YEAR);
        String newDisc = getTheTag(disc, tags, FieldKey.DISC_NO);
        String newTrack = getTheTag(track, tags, FieldKey.TRACK);
        String newAlbumArtist = getTheTag(album_artist, tags, FieldKey.ALBUM_ARTIST);
        String newComposer = getTheTag(composer, tags, FieldKey.COMPOSER);
        String newGrouping = getTheTag(grouping, tags, FieldKey.GROUPING);
        String newGenre = getTheTag(genre, tags, FieldKey.GENRE);

        newPath = newPath.replaceAll(title, newTitle);
        newPath = newPath.replaceAll(artist, newArtist);
        newPath = newPath.replaceAll(album, newAlbum);
        newPath = newPath.replaceAll(year, newYear);
        newPath = newPath.replaceAll(disc, newDisc);
        newPath = newPath.replaceAll(track, newTrack);
        newPath = newPath.replaceAll(album_artist, newAlbumArtist);
        newPath = newPath.replaceAll(composer, newComposer);
        newPath = newPath.replaceAll(grouping, newGrouping);
        newPath = newPath.replaceAll(genre, newGenre);

        return newPath;
    }

    public String getTheTag(String component, Tag tags, FieldKey id){
        if(tags != null && tags.getFirst(id) != null && !tags.getFirst(id).equals("")) {
            return tags.getFirst(id);
        }else {
            return component;
        }
    }

    public List<File> recursiveDirectoryContent(File folder){
        File[] filesInFolder = folder.listFiles();
        List<File> files = new ArrayList<>();
        for(File f : filesInFolder){
            if (f.isDirectory()){
                List<File> filesInSubFolder = recursiveDirectoryContent(f);
                for(File file : filesInSubFolder) {
                    files.add(file);
                }
            }
            else{
                files.add(f);
            }
        }
        return files;
    }

    private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in;
        OutputStream out;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file
            out.flush();
            out.close();

            // delete the original file
            new File(inputPath + inputFile).delete();


        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }
}
