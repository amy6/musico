package example.com.musico.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

public class Utilities {

    /**
     * convert milliseconds to hh:mm string format
     * @param milliSeconds time to convert
     * @return milliseconds in hh:mm format
     */
    public static String millisecondsToTimer (long milliSeconds) {
        String finalTimerString = "";
        String secondsString;

        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) (milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000;

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString += minutes + ":" + secondsString;

        return finalTimerString;
    }

    /**
     * get progress percentage to update seekbar
     * @param currentDuration current duration of the song being played
     * @param totalDuration total duration of the song being played
     * @return progress precent
     */
    public static int getProgressPercentage (long currentDuration, long totalDuration) {
        Double percentage;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        percentage = ((double)(currentSeconds) / totalSeconds) * 100;

        return percentage.intValue();
    }

    /**
     * convert progress to milliseconds format
     * @param progress current progress
     * @param totalDuration total duration of the song being played
     * @return current progress in milliseconds
     */
    public static int progressToTimer (int progress, int totalDuration) {
        int currentDuration;
        totalDuration = totalDuration / 1000;
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        return currentDuration * 1000;
    }

    /**
     * get the total duration of the media player file
     * @param context context to access application resources
     * @param songId song resource id
     * @return duration of the song in timer format
     */
    public static String getDuration(Context context, int songId) {
        final Uri mediaPath = Uri.parse("android.resource://" + context.getPackageName() + "/" + songId);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context, mediaPath);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        duration = millisecondsToTimer(Integer.parseInt(duration));
        return duration;
    }
}
