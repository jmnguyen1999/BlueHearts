package models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("Report")
public class Report extends ParseObject {
    public static final String KEY_IMAGES = "images";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_THEMES = "themes";
    public static final String KEY_TITLE= "title";
    public static final String KEY_CREATEDAT = "createdAt";
    public static final String KEY_USERID = "userId";
    public static final String KEY_TOKENWORTH = "tokenWorth";

    public Report(){}
    //Setters: --> to initialize a local Report object + can use to save in Parse:
    public void setImages(List<ParseFile> images){
        put(KEY_IMAGES, images);
    }
    public void setTitle(String title){
        put(KEY_TITLE, title);
    }
    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }
    public void setThemes(List<String> themes){
        put(KEY_THEMES, themes);
    }
    public void setUserId(String userId){
        put(KEY_USERID, userId);
    }
    public void setTokenWorth(int tokenWorth){
        put(KEY_TOKENWORTH, tokenWorth);
    }
    //Getters --> to show Reports
    public List<ParseFile> getImages(){
        return getList(KEY_IMAGES);
    }
    public String getTitle(){
        return getString(KEY_TITLE);
    }
}
