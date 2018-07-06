package example.com.musico.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marcinmoskala.arcseekbar.ArcSeekBar;

import java.util.List;

import example.com.musico.R;
import example.com.musico.SongDetailsActivity;
import example.com.musico.data.MusicItem;

public class MusicAdapter extends ArrayAdapter<MusicItem> {


    public static final String MUSIC_OBJECT = "MUSIC_ITEM";
    public static final String ITEM_POSITION = "POSITION";

    public MusicAdapter(@NonNull Context context, @NonNull List<MusicItem> musicItems) {
        super(context, 0, musicItems);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_music_item, parent, false);
        }

        final MusicItem musicItem = getItem(position);
        ImageView songImageView = convertView.findViewById(R.id.imageView);
        TextView titleTextView = convertView.findViewById(R.id.songName);
        TextView subtitleTextView = convertView.findViewById(R.id.artistName);

        if (musicItem != null) {
            titleTextView.setText(musicItem.getSongName());
            subtitleTextView.setText(musicItem.getArtistName());
            songImageView.setImageResource(musicItem.getImageId());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SongDetailsActivity.class);
                intent.putExtra(ITEM_POSITION, position);
//                intent.putExtra(MUSIC_OBJECT, musicItem);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

}
