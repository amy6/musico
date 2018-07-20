package example.com.musico;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.app.SearchManager;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import example.com.musico.data.MusicData;
import example.com.musico.data.MusicItem;
import example.com.musico.utils.MusicAdapter;

import static example.com.musico.utils.SuggestionsProvider.AUTHORITY;
import static example.com.musico.utils.SuggestionsProvider.MODE;

public class MainActivity extends AppCompatActivity implements ArtistsFragment.OnItemSelectedListener {

    public static final String ITEM_POSITION = "ITEM_POSITION";
    public static final String SONGS_TAG = "songs";
    public static final String ALBUMS_TAG = "albums";
    public static final String ARTISTS_TAG = "artists";

    private SearchView searchView;
    private ArrayList<MusicItem> musicItems;
    private SearchManager searchManager;
    private SearchRecentSuggestions recentSuggestions;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private FragmentManager fragmentManager;

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

//        handleIntent(getIntent());

//        recentSuggestions = new SearchRecentSuggestions(this, AUTHORITY, MODE);

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
                    case R.id.albums: fragmentClass = AlbumsFragment.class;
                        break;
                    case R.id.artists:fragmentClass = ArtistsFragment.class;
                        break;
                    default:fragmentClass = SongsFragment.class;
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

//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        musicItems = MusicData.getMusicItemsList(this);

//        MusicAdapter musicAdapters = new MusicAdapter(this, musicItems);
//        recyclerView.setAdapter(musicAdapters);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            /*case R.id.clear_search_history:
                recentSuggestions.clearHistory();
                break;*/
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
        Log.d("TAG : " , "Overridden interface method" + artistName);
        ArrayList<MusicItem> songsByArtist = MusicData.getSongByArtist(this, artistName);
        SongsFragment songsFragment = new SongsFragment();
        Bundle args = new Bundle();
        args.putString("ARTIST_NAME", artistName);
        args.putSerializable("ARTIST", songsByArtist);
        songsFragment.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.container, songsFragment, SONGS_TAG).commit();
        setTitle("Songs");
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconifiedByDefault(false);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFor(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                loadSuggestions(query);
                return true;
            }
        });

        return true;
    }

    private void loadSuggestions(String query) {
        List<String> songNames = new ArrayList<>();
        for (MusicItem musicItem : musicItems) {
            songNames.add(musicItem.getSongName());
        }

        String[] columns = new String[] { "_id", "text" };
        Object[] temp = new Object[] { 0, "default" };

        MatrixCursor cursor = new MatrixCursor(columns);

        for (int i = 0; i < songNames.size(); i++) {
//            if (songNames.get(i).contains(query)) {
                temp[0] = i;
                temp[1] = songNames.get(i);

                cursor.addRow(temp);
//            }
        }

        searchView.setSuggestionsAdapter(new SuggestionsAdapter(this, cursor, songNames));
    }

    private void handleIntent(Intent intent) {
        Log.d(LOG_TAG, "Intent from OnCreate");
        if ((Intent.ACTION_SEARCH).equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchFor(query);

            recentSuggestions.saveRecentQuery(query, null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(LOG_TAG, "Intent from onNewIntent");
        setIntent(intent);
        handleIntent(intent);
    }

    private void searchFor(String query) {
        Log.d(LOG_TAG, "Search method called for : " + query);
    }*/

}
