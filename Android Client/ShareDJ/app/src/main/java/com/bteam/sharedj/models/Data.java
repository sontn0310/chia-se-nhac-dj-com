package com.bteam.sharedj.models;

import org.json.JSONArray;
import org.json.JSONObject;

public class Data {
    public enum Typer {
        NODE, GETDATA,LOGIN,LOGINFB, SIGNUP, FEEDBACK,COMMENT,LIKE, DELETE
    }

    public JSONObject mJson;
    public JSONArray mJsonArray;

    public Typer mTyper;
}
