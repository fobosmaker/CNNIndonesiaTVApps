package id.cnn.cnnindonesiatv.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeDifference {
    public String executeDateTimeDifference(String time){
        long diff;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        try {
            Date dateNow = Calendar.getInstance().getTime();
            String dateNowString = dateFormat.format(dateNow);
            Date now = dateFormat.parse(dateNowString);
            Date timeVideo = dateFormat.parse(time);
            diff = now.getTime() - timeVideo.getTime();
            long diffSeconds = diff / 1000;
            long diffMinutes = diff / (60 * 1000);
            long diffHours = diff / (60 * 60 * 1000);
            long diffDays = diff / (60 * 60 * 1000 * 24);
            long diffWeeks = diff / (60 * 60 * 1000 * 24 * 7);
            long diffMonths = (long) (diff / (60 * 60 * 1000 * 24 * 30.41666666));
            long diffYears = diff / ((long)60 * 60 * 1000 * 24 * 365);
            if (diffSeconds < 1)  return "now";
            else if (diffMinutes < 1) {
                if(diffSeconds > 1) return diffSeconds + " seconds ago";
                else return diffSeconds + " second ago";
            } else if (diffHours < 1) {
                if(diffMinutes > 1) return diffMinutes + " minutes ago";
                else return diffMinutes + " minute ago";
            } else if (diffDays < 1) {
                if(diffHours > 1) return diffHours + " hours ago";
                else return diffHours + " hour ago";
            } else if (diffWeeks < 1) {
                if(diffDays > 1) return diffDays + " days ago";
                else return diffDays + " day ago";
            } else if (diffMonths < 1) {
                if(diffWeeks > 1) return diffWeeks + " weeks ago";
                else return diffWeeks + " week ago";
            } else if (diffYears < 1) {
                if(diffMonths > 1) return diffMonths+" months ago";
                else return diffMonths+" month ago";
            } else {
                if(diffYears > 1) return diffYears+" years Ago";
                else return diffYears+" year Ago";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }
    }
}
