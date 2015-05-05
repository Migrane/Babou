package musicplayer.mmad.paul.com.babou;


public class Utilities {


    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     * */
    public String millisecondsToTimer(long milliSecs){
        String timeStr = "";
        String secsStr = "";

        // Convert total duration into time
        int h = (int)( milliSecs / (1000*60*60));
        int m = (int)(milliSecs % (1000*60*60)) / (1000*60);
        int s = (int) ((milliSecs % (1000*60*60)) % (1000*60) / 1000);
        // Add h if there
        if(h > 0){
            timeStr = h + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(s < 10){
            secsStr = "0" + s;
        }else{
            secsStr = "" + s;}

        timeStr = timeStr + m + ":" + secsStr;

        // return timer string
        return timeStr;
    }

    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }



}
