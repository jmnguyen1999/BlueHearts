package models;

import android.text.format.DateUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@ParseClassName("Token")
public class Token extends ParseObject {
    public static final String KEY_VALUE = "value";
    public static final String KEY_REPORT = "report";
    public static final String KEY_CREATEDAT = "createdAt";
    public static final String KEY_USERID = "userId";
    public Token(){}

    public Report getReportObj(){
        Report report = (Report) getParseObject(KEY_REPORT);
        try {
            report.fetchIfNeeded();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        return report;
    }

    public String getFormattedTimestamp(){
        return getRelativeTimeAgo(getCreatedAt().toString());
    }

    public int getValue(){
        return (int) getNumber(KEY_VALUE);
    }

    public void setUserId(String id){
        put(KEY_USERID, id);
    }
    public void setReport(Report report){
        put(KEY_REPORT, report);
    }
    public void setValue(int value){
        put(KEY_VALUE, value);
    }
    //Helper method to get timestamp:
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss z yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
