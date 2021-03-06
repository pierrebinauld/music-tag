package binauld.pierre.musictag.adapter;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.item.Folder;
import binauld.pierre.musictag.item.Item;
import binauld.pierre.musictag.service.ArtworkService;
import binauld.pierre.musictag.visitor.ItemVisitor;

/**
 * Adapt a list of library item for a list view.
 */
public class LibraryComponentAdapter extends BaseAdapter {
    private Drawable background;

    static class ViewHolder {
        TextView firstLine;
        TextView secondLine;
        ImageView thumbnail;
    }

    private LibraryComposite currentComposite;
    private final ArtworkService artworkService;
    private int artworkSize;
    private ProgressBar progressBar;

    public LibraryComponentAdapter(ArtworkService artworkService, int artworkSize) {
        this.artworkService = artworkService;
        this.artworkSize = artworkSize;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        updateProgressBar();
    }

    @Override
    public int getCount() {
        return currentComposite.size();
    }

    @Override
    public Object getItem(int i) {
        return currentComposite.getChild(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ListView listView = (ListView) parent;

        if (convertView == null) {

            // inflate the layout
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.library_item_view, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.firstLine = (TextView) convertView.findViewById(R.id.first_line);
            viewHolder.secondLine = (TextView) convertView.findViewById(R.id.second_line);
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);

            // store the holder with the view.
            convertView.setTag(viewHolder);

            //TODO: Workaround
            if(null == background) {
                background = convertView.getBackground();
            }
        } else {
            // just use the viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // object item based on the position
        Item item = currentComposite.getChild(position).getItem();

        // assign values if the object is not null
        if (item != null) {
            viewHolder.firstLine.setText(item.getPrimaryInformation());
            viewHolder.secondLine.setText(item.getSecondaryInformation());
            artworkService.setArtwork(item, viewHolder.thumbnail, artworkSize);
            convertView.setTag(viewHolder);
            if(listView.isItemChecked(position)) {
                //TODO: Magic Color!
                convertView.setBackgroundColor(Color.parseColor("#ffe0b2"));
            } else {
                convertView.setBackground(background);
            }
        }

        return convertView;
    }

    /**
     * Set the progress bar.
     * @param progressBar The progress bar.
     */
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    /**
     * Initialize the progress bar (progress, max, visibility).
     */
    private void setUpProgressBar() {
        if (null != progressBar) {
            if (null == currentComposite) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                currentComposite.getItem().accept(new ItemVisitor() {
                    @Override
                    public void visit(AudioFile audioFile) {

                    }

                    @Override
                    public void visit(Folder folder) {
                        progressBar.setMax(folder.getMaxChildrenCount());
                    }
                });
                updateProgressBar();
            }
        }
    }

    /**
     * Update the progression of the progress bar.
     */
    private void updateProgressBar() {
        if (null != progressBar) {
            switch (currentComposite.getState()) {
                case LOADING:
                    progressBar.setProgress(currentComposite.size() + currentComposite.getInvalidComponentCount());
                    break;
                case LOADED:
                    progressBar.setVisibility(View.GONE);
                    break;
                case NOT_LOADED:
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Set the current node of the library tree list.
     *
     * @param currentComposite The current node to set.
     */
    public void setCurrentComposite(LibraryComposite currentComposite) {
        this.currentComposite = currentComposite;
        setUpProgressBar();
    }

    /**
     * Get the current node display by the list.
     *
     * @return The current node to get.
     */
    public LibraryComposite getCurrentComposite() {
        return currentComposite;
    }

}
