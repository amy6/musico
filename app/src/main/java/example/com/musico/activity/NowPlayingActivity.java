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
import example.com.musico.data.MusicItem;
import example.com.musico.utils.MusicData;

import static example.com.musico.activity.MainActivity.ARTIST_NAME;
import static example.com.musico.activity.MainActivity.ITEM_POSITION;
import static example.com.musico.utils.Utilities.getDuration;
import static example.com.musico.utils.Utilities.getProgressPercentage;
import static example.com.musico.utils.Utilities.millisecondsToTimer;
import static example.com.musico.utils.Utilities.progressToTimer;

public class NowPlayingActivity extends AppCompatActivity implements View.OnClickListener {

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
    @BindView(R.id.playlist)
    ImageButton playlist;
    @BindView(R.id.favorite)
    ImageButton favorite;

    private ArrayList<MusicItem> musicItems;
    private MusicItem musicItem;
    private int currentSongIndex;
    private MediaPlayer mediaPlayer;
    private final Handler handler = new Handler();
    private Toast toast;
    private boolean isRepeat;
    private boolean isShuffle;
    private boolean isFavorite;


    //update seekbar progress every 100ms
    private final Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                int totalDuration = mediaPlayer.getDuration();
                int currentDuration = mediaPlayer.getCurrentPosition();

                //get progress in percentage
                int progress = getProgressPercentage(currentDuration, totalDuration);
                seekBar.setProgress(progress);

                //convert remaining duration to hh:mm format
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

        //display back button on action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        //get intent from MainActivity
        Intent intent = getIntent();
        //get the song to be played
        currentSongIndex = intent.getIntExtra(ITEM_POSITION, 0);
        //get the artist name whose songs are to be played
        String artistName = intent.getStringExtra(ARTIST_NAME);
        //fetch all songs or song by a particular artist based on intent results
        if (artistName != null && artistName.length() > 0) {
            musicItems = MusicData.getSongByArtist(this, artistName);
        } else {
            musicItems = MusicData.getMusicItemsList(this);
        }
        //set views for the Now Playing UI
        musicItem = musicItems.get(currentSongIndex);
        currentTimeTextView.setText(R.string.initial_time);
        title.setText(musicItem.getSongName());
        subTitle.setText(musicItem.getArtistName());
        albumImg.setImageResource(musicItem.getImageId());
        totalTimeTextView.setText(getDuration(this, musicItem.getSongId()));

        //set button click listeners for player controls
        play.setOnClickListener(this);
        skipNext.setOnClickListener(this);
        skipPrev.setOnClickListener(this);
        repeat.setOnClickListener(this);
        shuffle.setOnClickListener(this);
        favorite.setOnClickListener(this);
        playlist.setOnClickListener(this);

        //set seek touch event listeners
        seekBar.setOnStartTrackingTouch(new ProgressListener() {
            @Override
            public void invoke(int i) {
                removeCallbacks();
            }
        });

        seekBar.setOnStopTrackingTouch(new ProgressListener() {
            @Override
            public void invoke(int i) {
                removeCallbacks();
                //get progress on stop seek
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = progressToTimer(seekBar.getProgress(), totalDuration);

                //update mediaplayer progress accordingly
                mediaPlayer.seekTo(currentPosition);
                //update seekbar progress
                updateSeekBar();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.play_pause:
                //handle play/pause, update vector drawables
                if (mediaPlayer != null ) {
                    //if mediaplayer is already initialized, verify if the instance was playing or not and update the state accordingly
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        play.setImageResource(R.drawable.ic_play_arrow);
                    } else {
                        mediaPlayer.start();
                        play.setImageResource(R.drawable.ic_pause);
                    }

                } else {
                    //handle initializing the mediaplayer if there is no valid instance
                    playSong(currentSongIndex);
                }
                break;

            case R.id.skip_next:
                //handle playing next song
                playNext();
                break;

            case R.id.skip_previous:
                //handle playing previous song
                if (currentSongIndex - 1 >= 0) {
                    currentSongIndex--;
                } else {
                    //display toast message when there are no more previous songs available
                    cancelToast();
                    toast = Toast.makeText(NowPlayingActivity.this, R.string.first_song, Toast.LENGTH_SHORT);
                    toast.show();
                }
                //play previous song if available
                playSong(currentSongIndex);
                break;

            case R.id.repeat:
                //handle repeat mode
                if (isRepeat) {
                    isRepeat = false;
                    updateVectorTint(repeat, android.R.color.white);
                } else {
                    isRepeat = true;
                    isShuffle = false;
                    updateVectorTint(repeat, R.color.red);
                    updateVectorTint(shuffle, android.R.color.white);
                }
                break;

            case R.id.shuffle:
                //handle shuffle mode
                if (isShuffle) {
                    isShuffle = false;
                    updateVectorTint(shuffle, android.R.color.white);
                } else {
                    isShuffle = true;
                    isRepeat = false;
                    updateVectorTint(shuffle, R.color.red);
                    updateVectorTint(repeat, android.R.color.white);
                }
                break;

            case R.id.favorite:
                //add/remove song from favorites
                if (isFavorite) {
                    isFavorite = false;
                    favorite.setImageResource(R.drawable.ic_favorite_border);
                } else {
                    isFavorite = true;
                    favorite.setImageResource(R.drawable.ic_favorite);
                }
                break;

            case R.id.playlist:
                //intent to show all songs from NowPlaying screen
                Intent intent = new Intent(NowPlayingActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
                break;
        }
    }

    /**
     * plays the song at the specified position
     * @param position position of the song from the list
     */
    private void playSong(final int position) {

        //get the song object
        musicItem = musicItems.get(position);
        updateUI();
        //reset the player
        mediaPlayer.reset();

        //create the player instance
        mediaPlayer = MediaPlayer.create(this, musicItem.getSongId());

        //play the song
        mediaPlayer.start();
        play.setImageResource(R.drawable.ic_pause);

        //update the seekbar progress
        updateSeekBar();

        //handle onCompletion state
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //replay the song if repeat is enabled
                if (isRepeat) {
                    playSong(position);
                } else {
                    //play a song at a random index if shuffle is enabled
                    if (isShuffle) {
                        currentSongIndex = new Random().nextInt((musicItems.size() - 1) + 1);
                        playSong(currentSongIndex);
                    } else {
                        //if no shuffle is enabled, play the next song from the list
                        playNext();
                    }
                }
            }
        });
    }

    /**
     * method to update tint for a vector
     * @param button button that uses the vector drawable as a background
     * @param color tint to be used for the button's vector drawable
     */
    private void updateVectorTint(ImageButton button, int color) {
        DrawableCompat.setTint(button.getDrawable(), ContextCompat.getColor(this, color));
    }

    /**
     * plays the next song from the all songs list
     */
    private void playNext() {
        if (currentSongIndex + 1 < musicItems.size()) {
            currentSongIndex++;
        } else {
            //play from the first song if the list has reached to the end
            currentSongIndex = 0;
        }
        musicItem = musicItems.get(currentSongIndex);
        playSong(currentSongIndex);
    }

    /**
     * updates song details - the album image, song name, artist name when next/previous song is played
     */
    private void updateUI() {
        albumImg.setImageResource(musicItem.getImageId());
        title.setText(musicItem.getSongName());
        subTitle.setText(musicItem.getArtistName());
    }

    /**
     * cancel any outstanding toasts
     */
    private void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    /**
     * update seekbar via a handler
     */
    private void updateSeekBar() {
        handler.postDelayed(updateTimeTask, 100);
    }

    /**
     * unregister handler when not required
     */
    private void removeCallbacks() {
        handler.removeCallbacks(updateTimeTask);
    }

    /**
     * release media player resources when not required
     */
    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }


    /**
     * handle back navigation on action bar
     * @return boolean flag indication whether navigation was successfully handled
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * handles media player state with change in activity state
     */
    @Override
    protected void onPause() {
        super.onPause();
        removeCallbacks();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            play.setImageResource(R.drawable.ic_play_arrow);
        }
    }

    /**
     * handles media player state with change in activity state
     */
    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(this, musicItem.getSongId());
        seekBar.setMaxProgress(100);
        seekBar.setProgress(0);
        updateSeekBar();
    }

    /**
     * handles media player state with change in activity state
     */
    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

}
