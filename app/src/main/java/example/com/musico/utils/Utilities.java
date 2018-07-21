package example.com.musico.utils;

public class Utilities {

    public static String millisecondsToTimer (long milliSeconds) {
        String finalTimerString = "";
        String secondsString = "";

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

    public static int getProgressPercentage (long currentDuration, long totalDuration) {
        Double percentage;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        percentage = ((double)(currentSeconds) / totalSeconds) * 100;

        return percentage.intValue();
    }

    public static int progressToTimer (int progress, int totalDuration) {
        int currentDuration;
        totalDuration = totalDuration / 1000;
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        return currentDuration * 1000;
    }
}
