package com.asking.pad.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.UserEntity;
import com.google.gson.Gson;

/**
 * Created by jswang on 2017/4/12.
 */

public class BaseApplication extends MultiDexApplication {

    private SharedPreferences mPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mPreferences = getSharedPreferences(Constants.TAG, Context.MODE_PRIVATE);
    }

    public SharedPreferences getPreferences(){
        if(mPreferences == null){
            mPreferences = getSharedPreferences(Constants.TAG, Context.MODE_PRIVATE);
        }
        return mPreferences;
    }

    public String getPreferencesStr(String key){
        return getPreferences().getString(key,"");
    }

    public boolean getPreferencesBoolean(String key){
        return getPreferences().getBoolean(key,false);
    }

    public boolean setPreferencesValue(String key,Object value){
        SharedPreferences.Editor mEditor = getPreferences().edit();
        if(value instanceof String){
            mEditor.putString(key, (String)value);
        }else if(value instanceof Boolean){
            mEditor.putBoolean(key, (Boolean)value);
        }else if(value instanceof Integer){
            mEditor.putInt(key, (Integer)value);
        }
        return mEditor.commit();
    }

    private UserEntity userEntity;

    private String ticket = "";

    public boolean saveUserData(String str) {
        userEntity = new Gson().fromJson(str,UserEntity.class);
        return setPreferencesValue(Constants.USER_DATA, str);
    }

    public boolean saveUserData(UserEntity mUser) {
        userEntity = mUser;
        String str = new Gson().toJson(mUser);
        return  setPreferencesValue(Constants.USER_DATA, str);
    }

    public UserEntity getUserEntity() {
        try{
            if(userEntity == null){
                String  str = getPreferencesStr(Constants.USER_DATA);
                userEntity = new Gson().fromJson(str,UserEntity.class);
            }
        }catch (Exception e){}
        return userEntity;
    }

    public boolean isLogin() {
        if(getUserEntity() != null){
            return true;
        }
        return false;
    }

    public String getUserName() {
        String  userName = "";
        try{
            userEntity = getUserEntity();
            if(userEntity !=null){
                userName = userEntity.getUserName();
            }
        }catch (Exception e){}
        return userName;
    }

    public String getUserId() {
        String  userId = "";
        try{
            userEntity = getUserEntity();
            if(userEntity !=null){
                userId = userEntity.getId();
            }
        }catch (Exception e){}
        return userId;
    }

    public String getPassWord() {
        String  passWord = "";
        try{
            userEntity = getUserEntity();
            if(userEntity !=null){
                passWord = userEntity.getPassWord();
            }
        }catch (Exception e){}
        return passWord;
    }

    public boolean saveToken(String value) {
        ticket = value;
        return setPreferencesValue(Constants.TOKEN,value);
    }

    public String getToken() {
        try{
            if(TextUtils.isEmpty(ticket)){
                String  value = getPreferencesStr(Constants.TOKEN);
                ticket = value;
            }
        }catch (Exception e){}
        return ticket;
    }

    public boolean saveWYToken(String value) {
        return setPreferencesValue(Constants.WYBAIBAN_TOKEN,value);
    }

    public String getWYToken() {
        return getPreferencesStr(Constants.WYBAIBAN_TOKEN);
    }

    public boolean isUserDataPerfect() {
        return mPreferences.getBoolean(Constants.IS_USER_DATA_PERFECT, false);
    }

    public boolean setIsUserDataPerfect(boolean isPerfect) {
        return setPreferencesValue(Constants.IS_USER_DATA_PERFECT, isPerfect);
    }


    public boolean setLoginAccount(String value) {
        return setPreferencesValue(Constants.ASKPAD_ACCOUNT,value);
    }

    public String geLoginAccount() {
        return getPreferencesStr(Constants.ASKPAD_ACCOUNT);
    }


    public boolean setLoginPassword(String value) {
        return setPreferencesValue(Constants.ASKPAD_PASSWORD,value);
    }

    public String geLoginPassword() {
        return getPreferencesStr(Constants.ASKPAD_PASSWORD);
    }

    public void clearLoginInfo() {
        userEntity = null;
        ticket = "";
        setPreferencesValue(Constants.USER_DATA, "");
        setPreferencesValue(Constants.TOKEN, "");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
