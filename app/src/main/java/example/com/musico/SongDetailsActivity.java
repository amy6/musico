package example.com.musico;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.musico.data.MusicData;
import example.com.musico.data.MusicItem;
import example.com.musico.utils.Utilities;

import static example.com.musico.MainActivity.ITEM_POSITION;

public class SongDetailsActivity extends AppCompatActivity {

    public static final String LOG_TAG = SongDetailsActivity.class.getSimpleName();
    @BindView(R.id.song_name)
    TextView title;
    @BindView(R.id.artist_name)
    TextView subTitle;
    @BindView(R.id.album_image)
    ImageView albumImg;
    @BindView(R.id.current_time)
    TextView currentTimeTextView;
    @BindView(R.id.total_time)
    TextView totalTimeTextView;
    @BindView(R.id.arcSeekBar)
    ArcSeekBar seekBar;
    @BindView(R.id.play_pause)
    ImageButton play;
    @BindView(R.id.skip_next)
    ImageButton skipNext;
    @BindView(R.id.skip_previous)
    ImageButton skipPrev;
    @BindView(R.id.shuffle)
    ImageButton shuffle;
    @BindView(R.id.repeat)
    ImageButton repeat;

    private MusicItem musicItem;
    private int currentSongIndex;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Utilities utils;
    private Toast toast;
    private boolean isRepeat;
    private boolean isShuffle;
    private ArrayList<MusicItem> musicItems;


    private Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                int totalDuration = mediaPlayer.getDuration();
                int currentDuration = mediaPlayer.getCurrentPosition();

                int progress = utils.getProgressPercentage(currentDuration, totalDuration);
                seekBar.setProgress(progress);

                totalTimeTextView.setText("" + utils.millisecondsToTimer(totalDuration));
                currentTimeTextView.setText("" + utils.millisecondsToTimer(currentDuration));

                handler.postDelayed(this, 100);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);
        utils = new Utilities();

        Intent intent = getIntent();
        currentSongIndex = intent.getIntExtra(ITEM_POSITION, 0);
        String artistName = intent.getStringExtra("ARTIST_NAME");
        if (artistName != null && artistName.length() > 0) {
            musicItems = MusicData.getSongByArtist(this, artistName);
        } else {
            musicItems = MusicData.getMusicItemsList(this);
        }
        musicItem = musicItems.get(currentSongIndex);
        title.setText(musicItem.getSongName());
        subTitle.setText(musicItem.getArtistName());
        albumImg.setImageResource(musicItem.getImageId());
        currentTimeTextView.setText("0:00");
        Uri mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + musicItem.getSongId());
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(this, mediaPath);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        duration = utils.millisecondsToTimer(Integer.parseInt(duration));
        totalTimeTextView.setText(duration);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.ic_play_arrow);
                } else {
                    playSong(currentSongIndex);
                }
            }
        });

        seekBar.setOnStartTrackingTouch(new ProgressListener() {
            @Override
            public void invoke(int i) {
                handler.removeCallbacks(updateTimeTask);
            }
        });

        seekBar.setOnStopTrackingTouch(new ProgressListener() {
            @Override
            public void invoke(int i) {
                handler.removeCallbacks(updateTimeTask);
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

                mediaPlayer.seekTo(currentPosition);

                updateSeekBar();
            }
        });

        skipNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNext();
            }
        });

        skipPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentSongIndex - 1 >= 0) {
                    currentSongIndex--;
                } else {
                    cancelToast();
                    toast = Toast.makeText(SongDetailsActivity.this, "This is the first song", Toast.LENGTH_SHORT);
                    toast.show();
                }
                playSong(currentSongIndex);
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRepeat) {
                    isRepeat = false;
                    repeat.setImageResource(R.drawable.ic_repeat);
                } else {
                    isRepeat = true;
                    isShuffle = false;
                    repeat.setImageResource(R.drawable.ic_repeat_black);
                    shuffle.setImageResource(R.drawable.ic_shuffle);
                }
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShuffle) {
                    isShuffle = false;
                    shuffle.setImageResource(R.drawable.ic_shuffle);
                } else {
                    isShuffle = true;
                    isRepeat = false;
                    shuffle.setImageResource(R.drawable.ic_shuffle_black);
                    repeat.setImageResource(R.drawable.ic_repeat);
                }
            }
        });

    }

    private void playNext() {
        if (currentSongIndex + 1 < musicItems.size()) {
            currentSongIndex++;
        } else {
            currentSongIndex = 0;
        }
        cancelToast();
        toast = Toast.makeText(SongDetailsActivity.this, "Playing song : " + currentSongIndex, Toast.LENGTH_SHORT);
        toast.show();
        musicItem = musicItems.get(currentSongIndex);
        playSong(currentSongIndex);
    }

    private void updateUI() {
        albumImg.setImageResource(musicItem.getImageId());
        title.setText(musicItem.getSongName());
        subTitle.setText(musicItem.getArtistName());
    }

    private void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    private void playSong(final int position) {

        musicItem = musicItems.get(position);
        updateUI();
        mediaPlayer.reset();

        mediaPlayer = MediaPlayer.create(this, musicItem.getSongId());

        mediaPlayer.start();
        play.setImageResource(R.drawable.ic_pause);

        Log.i(LOG_TAG, "Playing song at Position : " + position);

        updateSeekBar();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                if (isRepeat) {
                    Log.i(LOG_TAG, "Repeat is on!");
                    playSong(position);
                } else {
                    if (isShuffle) {
                        Log.i(LOG_TAG, "Shuffle is on!");
                        currentSongIndex = new Random().nextInt((musicItems.size() - 1) + 1);
                        playSong(currentSongIndex);
                    } else {
                        playNext();
                    }
                }
            }
        });
    }

    private void updateSeekBar() {
        handler.postDelayed(updateTimeTask, 100);
    }

    private void releaseMediaPlayer() {
        Log.i(LOG_TAG, "Releasing MediaPlayer");
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateTimeTask);
        Log.i(LOG_TAG, "Inside onPause");
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            play.setImageResource(R.drawable.ic_play_arrow);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(this, musicItem.getSongId());
        Log.i(LOG_TAG, "Inside onResume");
        seekBar.setMaxProgress(100);
        seekBar.setProgress(0);
        updateSeekBar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
