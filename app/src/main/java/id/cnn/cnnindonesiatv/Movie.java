/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package id.cnn.cnnindonesiatv;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
public class Movie implements Serializable {
    //static final long serialVersionUID = 727566175075960653L;
    //private long id;
    private String id;
    private String title;
    private String description;
    private String bgImageUrl;
    private String cardImageUrl;
    private String videoUrl;
    private String studio;
    private static final String TAG = "Movie";

    public Movie(String id, String title, String description, String bgImageUrl, String cardImageUrl, String videoUrl, String studio) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.bgImageUrl = bgImageUrl;
        this.cardImageUrl = cardImageUrl;
        this.videoUrl = videoUrl;
        this.studio = executeDateTimeDifference(studio);
    }

    //public long getId() {return id;}

    //public void setId(long id) {this.id = id;}


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

//    public void setTitle(String title) {
//        this.title = title;
//    }

    public String getDescription() {
        return description;
    }

//    public void setDescription(String description) {
//        this.description = description;
//    }

    public String getStudio() {
        return studio;
    }

//    public void setStudio(String studio) {
//        this.studio = studio;
//    }

    public String getVideoUrl() {
        return videoUrl;
    }

//    public void setVideoUrl(String videoUrl) {
//        this.videoUrl = videoUrl;
//    }

    public String getBackgroundImageUrl() {
        return bgImageUrl;
    }

//    public void setBackgroundImageUrl(String bgImageUrl) {
//        this.bgImageUrl = bgImageUrl;
//    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

//    public void setCardImageUrl(String cardImageUrl) {
//        this.cardImageUrl = cardImageUrl;
//    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", backgroundImageUrl='" + bgImageUrl + '\'' +
                ", cardImageUrl='" + cardImageUrl + '\'' +
                '}';
    }

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
            if (diffSeconds < 1) {
                return "now";
            } else if (diffMinutes < 1) {
                if(diffSeconds > 1){
                    return diffSeconds + " seconds ago";
                } else {
                    return diffSeconds + " second ago";
                }
            } else if (diffHours < 1) {
                if(diffMinutes > 1){
                    return diffMinutes + " minutes ago";
                } else {
                    return diffMinutes + " minute ago";
                }
            } else if (diffDays < 1) {
                if(diffHours > 1){
                    return diffHours + " hours ago";
                } else {
                    return diffHours + " hour ago";
                }
            } else if (diffWeeks < 1) {
                if(diffDays > 1){
                    return diffDays + " days ago";
                } else {
                    return diffDays + " day ago";
                }
            } else if (diffMonths < 1) {
                if(diffWeeks > 1){
                    return diffWeeks + " weeks ago";
                } else {
                    return diffWeeks + " week ago";
                }
            } else if (diffYears < 1) {
                if(diffMonths > 1){
                    return diffMonths+" months ago";
                } else {
                    return diffMonths+" month ago";
                }

            } else {
                if(diffYears > 1){
                    return diffYears+" years Ago";
                } else {
                    return diffYears+" year Ago";
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "executeDateTimeDifference: "+e.getMessage());
            return time;
        }
    }
}
