package example.com.musico.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.musico.R;
import example.com.musico.adapter.SampleFragmentPagerAdapter;
import example.com.musico.fragment.SampleFragment;

import static example.com.musico.activity.MainActivity.FRAGMENT_TAG;

public class FavoritesActivity extends AppCompatActivity implements SampleFragment.OnExploreListener{

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //display back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        //setup viewpager with adapter and tablayout
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * invoked when the back button is pressed on the action bar
     * @return boolean stating navigation is handled
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * invoked when Explore button is clicked
     * triggers an intent to display Songs/Albums/Artists
     * @param tag string containing fragment name to be displayed in MainActivity
     */
    @Override
    public void onExplore(String tag) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(FRAGMENT_TAG, tag);
        finish();
        startActivity(intent);
    }
}
