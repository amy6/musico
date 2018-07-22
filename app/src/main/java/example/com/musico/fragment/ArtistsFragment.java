package example.com.musico.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import example.com.musico.R;
import example.com.musico.utils.MusicData;
import example.com.musico.data.MusicItem;
import example.com.musico.utils.ArtistAdapter;

public class ArtistsFragment extends Fragment {

    private OnItemSelectedListener listener;
    private RecyclerView recyclerView;

    public ArtistsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnItemSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e.getMessage());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        updateGridColumns(getContext().getResources().getConfiguration());
        ArrayList<MusicItem> artistItems = MusicData.getArtistImageList(getContext());

        ArtistAdapter artistAdapter = new ArtistAdapter(getContext(), artistItems, listener);
        recyclerView.setAdapter(artistAdapter);
    }

    public interface OnItemSelectedListener {
        void onArtistSelected(String artistName);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateGridColumns(newConfig);
    }

    private void updateGridColumns(Configuration config) {
        int orientation = config.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        }
    }
}
