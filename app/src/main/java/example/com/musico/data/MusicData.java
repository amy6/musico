package example.com.musico.data;

import java.util.ArrayList;

import example.com.musico.R;

public class MusicData {

    public static ArrayList<MusicItem> getMusicItemsList() {

        ArrayList<MusicItem> musicItems = new ArrayList<>();

        for(int i=0; i<10; i++) {
            MusicItem musicItem = new MusicItem(R.drawable.friends, "SongName", "ArtistName", R.raw.friends_theme_song);
            musicItems.add(musicItem);
        }

        return musicItems;
    }
}
