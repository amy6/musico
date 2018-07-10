package example.com.musico.data;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;

import java.util.ArrayList;

import example.com.musico.R;

import static example.com.musico.SongDetailsActivity.LOG_TAG;

public class MusicData {

    public static ArrayList<MusicItem> getMusicItemsList(Context context) {

        Resources resources = context.getResources();
        String[] songNameList = resources.getStringArray(R.array.songName);
        String[] album_artistList = resources.getStringArray(R.array.album_artistName);
        int[] songResourceList = {R.raw.cheap_thrills, R.raw.come_get_it, R.raw.despacito, R.raw.diamonds, R.raw.dream, R.raw.one_more_night, R.raw.see_you_again, R.raw.shape_of_you, R.raw.side_to_side, R.raw.this_is_what_you_came_for};
        TypedArray imageArray = resources.obtainTypedArray(R.array.imageID);
        int[] imageList = new int[imageArray.length()];
        for(int index = 0; index < imageList.length; index++) {
            imageList[index] = imageArray.getResourceId(index, 0);
        }
        imageArray.recycle();

        ArrayList<MusicItem> musicItems = new ArrayList<>();

        for(int i=0; i<songResourceList.length; i++) {
            Log.d(LOG_TAG, "Image ID : " + imageList[i]);
            MusicItem musicItem = new MusicItem(imageList[i], songNameList[i], album_artistList[i], songResourceList[i]);
            musicItems.add(musicItem);
        }

        return musicItems;
    }
}
