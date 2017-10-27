package com.jobin.test;

import android.content.Context;
import android.content.SharedPreferences;



public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE=0;
    private static final String pref_name =  "test-welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(pref_name,PRIVATE_MODE);
        editor = pref.edit();

    }
    public void setFirstTimeLaunch(boolean isFirstTime){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch(){
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH,true);
    }
}