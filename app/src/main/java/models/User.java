package models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("User")
public class User extends ParseUser {
    public static final String KEY_FNAME = "fName";
    public static final String KEY_LNAME = "lName";
    public static final String KEY_TOKENBALANCE = "tokenBalance";
    public static final String KEY_REPORTHISTORY = "reportHistory";
    ParseUser user;
    public User(ParseUser user){          //In order to convert a ParseUser into a LocalParseUser --> can use local getter methods!
        this.user = user;
    }

    public User(){}
    //Setters
    public void setFname(String fName){
        put(KEY_FNAME, fName);
    }
    public void setLname(String lName){
        put(KEY_LNAME, lName);
    }
    public void setTokenBalance(int newBalance){
        put(KEY_TOKENBALANCE, newBalance);
    }

    //Getters:
    public int getTokenBalance(){
        return (int) user.getNumber(KEY_TOKENBALANCE);
    }
    public List<Report> getReportHistory(){
        user.getList(KEY_REPORTHISTORY);
        return new ArrayList<>();
    }
}
