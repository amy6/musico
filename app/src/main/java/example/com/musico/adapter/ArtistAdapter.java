package example.com.musico.adapter;

import android.content.Context;
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
import example.com.musico.fragment.ArtistsFragment.OnItemSelectedListener;
import example.com.musico.R;
import example.com.musico.data.MusicItem;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistItemHolder> {

    private Context context;
    private ArrayList<MusicItem> artistList;
    private OnItemSelectedListener listener;

    public ArtistAdapter(Context context, ArrayList<MusicItem> artistList, OnItemSelectedListener listener) {
        this.context = context;
        this.artistList = artistList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArtistItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArtistItemHolder(LayoutInflater.from(context).inflate(R.layout.layout_artist_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistItemHolder holder, int position) {
        final MusicItem artistItem = artistList.get(position);
        holder.songImageView.setImageResource(artistItem.getImageId());
        holder.subtitleTextView.setText(artistItem.getArtistName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onArtistSelected(artistItem.getArtistName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    static class ArtistItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.imageView)
        ImageView songImageView;
        @BindView(R.id.artistName)
        TextView subtitleTextView;

        ArtistItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
