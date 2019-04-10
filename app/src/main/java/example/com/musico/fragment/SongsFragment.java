package example.com.musico.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import example.com.musico.R;
import example.com.musico.utils.MusicData;
import example.com.musico.data.MusicItem;
import example.com.musico.adapter.MusicAdapter;

import static example.com.musico.activity.MainActivity.ARTIST;
import static example.com.musico.activity.MainActivity.ARTIST_NAME;

public class SongsFragment extends Fragment {

    private ArrayList<MusicItem> musicItems;
    private String artistName;

    public SongsFragment() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //get music items for a particular artist based on intent results
            musicItems = (ArrayList<MusicItem>) getArguments().getSerializable(ARTIST);
            artistName = getArguments().getString(ARTIST_NAME);
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
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        if (getContext() != null) {
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

            if (musicItems == null || musicItems.size() == 0) {
                //get all music items
                musicItems = MusicData.getMusicItemsList(getContext());
            }

            MusicAdapter musicAdapters = new MusicAdapter(getContext(), musicItems, getTag(), artistName);
            recyclerView.setAdapter(musicAdapters);
        }
    }
}
