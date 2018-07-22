package example.com.musico.activity;

import android.content.Intent;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
    public static final String FRAGMENT_TAG = "tag";
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

        //set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set up navigation menu icon
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        //setup navigation icon animation
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        //reference to fragment manager to handle fragment transactions
        fragmentManager = getSupportFragmentManager();

        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        //handle intent fired from the FavoritesActivity
        if (getIntent() != null) {
            //set SongsFragment as the default fragment
            Fragment fragment = new SongsFragment();
            String tag = SONGS_TAG;
            menu.getItem(0).setChecked(true);
            setTitle("Songs");
            if (getIntent().getStringExtra(FRAGMENT_TAG) != null) {
                tag = getIntent().getStringExtra(FRAGMENT_TAG);
                switch (tag) {
                    case ALBUMS_TAG:
                        fragment = new AlbumsFragment();
                        tag = ALBUMS_TAG;
                        menu.getItem(1).setChecked(true);
                        setTitle("Albums");
                        break;
                    case ARTISTS_TAG:
                        fragment = new ArtistsFragment();
                        tag = ARTISTS_TAG;
                        menu.getItem(2).setChecked(true);
                        setTitle("Artists");
                        break;
                }
            }
            //replace fragment as per the intent data from FavoritesActivity
            fragmentManager.beginTransaction().replace(R.id.container, fragment, tag).commit();
        } else {
            //setting default fragment when the app is opened without explicit intent
            fragmentManager.beginTransaction().replace(R.id.container, new SongsFragment(), SONGS_TAG).commit();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //intent to FavoritesActivity
                if (item.getItemId() == R.id.favorite) {
                    Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                    startActivity(intent);
                    finish();
                }

                //handle navigation drawer menu items other then Favorites
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
                    //display appropriate fragment based on menu item selected
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

    /**
     * inflates search menu to the toolbar
     * @param menu menu reference to inflate the search option
     * @return boolean flag indicating whether menu inflation was handled successfully
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconifiedByDefault(false);
        return true;
    }

    /**
     * overridden to indicate to the system to handle both toolbar and navigation drawer menu
     * @param item reference to menu item
     * @return boolean flag indicating whether menu item selection was handled successfully
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    /**
     * required to display the navigation drawer menu icon
     * @param savedInstanceState bundle object containing the state of the activity
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    /**
     * required to handle navigation drawer menu icon animation on configuration changes
     * @param newConfig reference to changed configuration
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * overridden interface method defined by ArtistFragment to display songs for a particular artist
     * @param artistName name of the artist selected by the user
     */
    @Override
    public void onArtistSelected(String artistName) {
        ArrayList<MusicItem> songsByArtist = MusicData.getSongByArtist(this, artistName);
        SongsFragment songsFragment = new SongsFragment();
        Bundle args = new Bundle();
        args.putString(ARTIST_NAME, artistName);
        args.putSerializable(ARTIST, songsByArtist);
        songsFragment.setArguments(args);
        //add fragment to backstack so the user can again get back to choosing a different artist
        fragmentManager.beginTransaction().replace(R.id.container, songsFragment, SONGS_TAG).addToBackStack(SONGS_TAG).commit();
        setTitle("Songs");
    }


    /**
     * handle double press back to exit
     */
    @Override
    public void onBackPressed() {
        //do not show the toast message if there are fragments remaining in the back stack
        if (doubleBackPressed || fragmentManager.getBackStackEntryCount() > 0) {
            super.onBackPressed();
            setTitle("Artists");
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
