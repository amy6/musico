package example.com.musico.activity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
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
import example.com.musico.R;
import example.com.musico.utils.MusicData;
import example.com.musico.data.MusicItem;

import static example.com.musico.activity.MainActivity.ARTIST_NAME;
import static example.com.musico.activity.MainActivity.ITEM_POSITION;
import static example.com.musico.utils.Utilities.getProgressPercentage;
import static example.com.musico.utils.Utilities.millisecondsToTimer;
import static example.com.musico.utils.Utilities.progressToTimer;

public class SongDetailsActivity extends AppCompatActivity {

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

                int progress = getProgressPercentage(currentDuration, totalDuration);
                seekBar.setProgress(progress);

                totalTimeTextView.setText(String.valueOf(millisecondsToTimer(totalDuration)));
                currentTimeTextView.setText(String.valueOf(millisecondsToTimer(currentDuration)));

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

        Intent intent = getIntent();
        currentSongIndex = intent.getIntExtra(ITEM_POSITION, 0);
        String artistName = intent.getStringExtra(ARTIST_NAME);
        if (artistName != null && artistName.length() > 0) {
            musicItems = MusicData.getSongByArtist(this, artistName);
        } else {
            musicItems = MusicData.getMusicItemsList(this);
        }
        musicItem = musicItems.get(currentSongIndex);
        currentTimeTextView.setText(R.string.initial_time);
        title.setText(musicItem.getSongName());
        subTitle.setText(musicItem.getArtistName());
        albumImg.setImageResource(musicItem.getImageId());
        Uri mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + musicItem.getSongId());
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(this, mediaPath);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        duration = millisecondsToTimer(Integer.parseInt(duration));
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
                int currentPosition = progressToTimer(seekBar.getProgress(), totalDuration);

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
                    updateVectorTint(repeat, android.R.color.white);
                } else {
                    isRepeat = true;
                    isShuffle = false;
                    updateVectorTint(repeat, R.color.red);
                    updateVectorTint(shuffle, android.R.color.white);
                }
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShuffle) {
                    isShuffle = false;
                    updateVectorTint(shuffle, android.R.color.white);
                } else {
                    isShuffle = true;
                    isRepeat = false;
                    updateVectorTint(shuffle, R.color.red);
                    updateVectorTint(repeat, android.R.color.white);
                }
            }
        });

    }

    private void updateVectorTint(ImageButton button, int color) {
        DrawableCompat.setTint(button.getDrawable(), ContextCompat.getColor(this, color));
    }

    private void playNext() {
        if (currentSongIndex + 1 < musicItems.size()) {
            currentSongIndex++;
        } else {
            currentSongIndex = 0;
        }
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

        updateSeekBar();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                if (isRepeat) {
                    playSong(position);
                } else {
                    if (isShuffle) {
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
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            play.setImageResource(R.drawable.ic_play_arrow);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(this, musicItem.getSongId());
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
