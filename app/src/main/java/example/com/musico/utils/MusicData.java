package example.com.musico.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;

import example.com.musico.R;
import example.com.musico.data.MusicItem;

public class MusicData {

    public static ArrayList<MusicItem> getMusicItemsList(Context context) {

        Resources resources = context.getResources();
        String[] songNameList = resources.getStringArray(R.array.songName);
        String[] album_artistList = resources.getStringArray(R.array.album_artistName);
        int[] songResourceList = {R.raw.cheap_thrills, R.raw.come_get_it, R.raw.despacito, R.raw.give_me_love, R.raw.diamonds, R.raw.dream, R.raw.one_more_night, R.raw.see_you_again, R.raw.shape_of_you, R.raw.side_to_side, R.raw.this_is_what_you_came_for, R.raw.all_of_the_stars, R.raw.the_heart_wants_what_it_wants, R.raw.perfect};

        TypedArray imageArray = resources.obtainTypedArray(R.array.album_imageID);
        int[] albumImageList = new int[imageArray.length()];

        for (int index = 0; index < albumImageList.length; index++) {
            albumImageList[index] = imageArray.getResourceId(index, 0);
        }

        imageArray.recycle();

        ArrayList<MusicItem> musicItems = new ArrayList<>();
        for (int i = 0; i < songResourceList.length; i++) {
            MusicItem musicItem = new MusicItem(albumImageList[i], songNameList[i], album_artistList[i], songResourceList[i]);
            musicItems.add(musicItem);
        }

        return musicItems;
    }

    public static ArrayList<MusicItem> getArtistImageList(Context context) {
        Resources resources = context.getResources();
        String[] artistList = resources.getStringArray(R.array.artistName);
        TypedArray imageArray = resources.obtainTypedArray(R.array.artist_imageID);
        int[] artistImageList = new int[imageArray.length()];

        for (int index = 0; index < artistImageList.length; index++) {
            artistImageList[index] = imageArray.getResourceId(index, 0);
        }

        imageArray.recycle();

        ArrayList<MusicItem> musicItems = new ArrayList<>();
        for (int i = 0; i < artistImageList.length; i++) {
            MusicItem musicItem = new MusicItem(artistImageList[i], artistList[i]);
            musicItems.add(musicItem);
        }

        return musicItems;
    }

    public static ArrayList<MusicItem> getSongByArtist(Context context, String artistName) {
        ArrayList<MusicItem> songsByArtist = new ArrayList<>();
        ArrayList<MusicItem> musicItems = getMusicItemsList(context);
        for (MusicItem musicItem : musicItems) {
            if (musicItem.getArtistName().equals(artistName)) {
                songsByArtist.add(musicItem);
            }
        }
        return songsByArtist;
    }
}
