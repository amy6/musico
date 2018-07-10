package example.com.musico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

import example.com.musico.data.MusicData;
import example.com.musico.data.MusicItem;
import example.com.musico.utils.MusicAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView = findViewById(R.id.gridView);
        ArrayList<MusicItem> musicItems = MusicData.getMusicItemsList(this);

        MusicAdapter musicAdapter = new MusicAdapter(this, musicItems);
        gridView.setAdapter(musicAdapter);
    }
}
