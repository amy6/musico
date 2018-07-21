package example.com.musico.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import example.com.musico.R;
import example.com.musico.data.MusicItem;
import example.com.musico.fragment.AlbumsFragment;
import example.com.musico.fragment.ArtistsFragment;
import example.com.musico.fragment.SongsFragment;
import example.com.musico.utils.MusicData;

public class MainActivity extends AppCompatActivity implements ArtistsFragment.OnItemSelectedListener {

    public static final String SONGS_TAG = "songs";
    public static final String ALBUMS_TAG = "albums";
    public static final String ARTISTS_TAG = "artists";
    public static final String ARTIST_NAME = "ARTIST_NAME";
    public static final String ARTIST = "ARTIST";
    public static final String ITEM_POSITION = "ITEM_POSITION";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private FragmentManager fragmentManager;
    private boolean doubleBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, new SongsFragment(), SONGS_TAG).commit();

        NavigationView navigationView = findViewById(R.id.nav_view);
        MenuItem item = navigationView.getMenu().getItem(0);
        item.setChecked(true);
        setTitle(item.getTitle());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                Class fragmentClass;
                switch (item.getItemId()) {
                    case R.id.albums:
                        fragmentClass = AlbumsFragment.class;
                        break;
                    case R.id.artists:
                        fragmentClass = ArtistsFragment.class;
                        break;
                    default:
                        fragmentClass = SongsFragment.class;
                }

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    String tag = SONGS_TAG;
                    switch (fragment.getClass().getSimpleName()) {
                        case "AlbumsFragment":
                            tag = ALBUMS_TAG;
                            break;
                        case "ArtistsFragment":
                            tag = ARTISTS_TAG;
                    }
                    fragmentManager.beginTransaction().replace(R.id.container, fragment, tag).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                item.setChecked(true);
                setTitle(item.getTitle());
                drawerLayout.closeDrawers();

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onArtistSelected(String artistName) {
        ArrayList<MusicItem> songsByArtist = MusicData.getSongByArtist(this, artistName);
        SongsFragment songsFragment = new SongsFragment();
        Bundle args = new Bundle();
        args.putString(ARTIST_NAME, artistName);
        args.putSerializable(ARTIST, songsByArtist);
        songsFragment.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.container, songsFragment, SONGS_TAG).addToBackStack(SONGS_TAG).commit();
        setTitle("Songs");
    }

    @Override
    public void onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed();
            return;
        }

        doubleBackPressed = true;
        Toast.makeText(this, R.string.exit_confirm, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackPressed = false;
            }
        }, 2000);

    }

}
