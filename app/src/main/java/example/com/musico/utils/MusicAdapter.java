package example.com.musico.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.musico.R;
import example.com.musico.SongDetailsActivity;
import example.com.musico.data.MusicItem;

import static example.com.musico.MainActivity.ALBUMS_TAG;
import static example.com.musico.MainActivity.ITEM_POSITION;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicItemHolder> {

    private Context context;
    private ArrayList<MusicItem> musicItems;
    private String fragmentTag;
    private String artistName;

    public MusicAdapter(Context context, ArrayList<MusicItem> musicItems, String tag, String artistName) {
        this.context = context;
        this.musicItems = musicItems;
        this.fragmentTag = tag;
        this.artistName = artistName;
    }

    @NonNull
    @Override
    public MusicItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (fragmentTag) {
            case ALBUMS_TAG:
                return new MusicItemHolder(LayoutInflater.from(context)
                        .inflate(R.layout.layout_album_item, parent, false));
                default:
                    return new MusicItemHolder(LayoutInflater.from(context)
                            .inflate(R.layout.layout_song_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MusicItemHolder holder, int position) {
        MusicItem musicItem = musicItems.get(position);
        holder.songImageView.setImageResource(musicItem.getImageId());

        holder.titleTextView.setSingleLine(true);
        holder.titleTextView.setText(musicItem.getSongName());

        holder.subtitleTextView.setSingleLine(true);
        holder.subtitleTextView.setText(musicItem.getArtistName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SongDetailsActivity.class);
                intent.putExtra(ITEM_POSITION, holder.getAdapterPosition());
                if (artistName != null && artistName.length() > 0) {
                    intent.putExtra("ARTIST_NAME", artistName);
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicItems.size();
    }

    static class MusicItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.imageView)
        ImageView songImageView;
        @BindView(R.id.songName)
        TextView titleTextView;
        @BindView(R.id.artistName)
        TextView subtitleTextView;

        MusicItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
