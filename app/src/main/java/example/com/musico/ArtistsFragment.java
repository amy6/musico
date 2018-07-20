package example.com.musico;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import example.com.musico.data.MusicData;
import example.com.musico.data.MusicItem;
import example.com.musico.utils.ArtistAdapter;
import example.com.musico.utils.MusicAdapter;

public class ArtistsFragment extends Fragment {

    private OnItemSelectedListener listener;

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
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ArrayList<MusicItem> artistItems = MusicData.getArtistImageList(getContext());

        ArtistAdapter artistAdapter = new ArtistAdapter(getContext(), artistItems, listener);
        recyclerView.setAdapter(artistAdapter);
    }

    public interface OnItemSelectedListener {
        public void onArtistSelected(String artistName);
    }
}
