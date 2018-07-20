package example.com.musico;

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

import example.com.musico.data.MusicData;
import example.com.musico.data.MusicItem;
import example.com.musico.utils.MusicAdapter;

public class AlbumsFragment extends Fragment {

    public AlbumsFragment() {
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
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ArrayList<MusicItem> musicItems = MusicData.getMusicItemsList(getContext());

        MusicAdapter musicAdapters = new MusicAdapter(getContext(), musicItems, getTag(), getTag());
        recyclerView.setAdapter(musicAdapters);
    }
}
