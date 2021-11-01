package ra.enterwind.lapaskumala.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import ra.enterwind.lapaskumala.activities.auth.LoginActivity;

public class SessionManager {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private Context mContext;
    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LoginPref";
    private static final String IS_LOGIN = "IsLogeIn";

    public static final String KEY_ID = "id";
    public static final String KEY_AKSES = "akses_id";
    public static final String KEY_NAME = "nama";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_NIK = "nik";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FOTO = "foto";
    public static final String KEY_ONE = "one_id";
    public static final String KEY_PEER = "peer_id";


    public static final String DATASTREAMPREF = "enterwind.ra.aduan.session.DATASTREMPREFS";
    public static final String DATASTREAMID = "enterwind.ra.aduan.session.DATASTREAMID";



    public SessionManager(Context mContext){
        this.mContext = mContext;
        preferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void createLogin(String id, String akses_id, String name, String username, String nik, String password, String foto, String one_id, String peer_id){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_AKSES, akses_id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NIK, nik);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_FOTO, foto);
        editor.putString(KEY_ONE, one_id);
        editor.putString(KEY_PEER, peer_id);
        editor.commit();
    }

    public void chekLogin(){
        if (!this.isLoggedIn()){
            Intent aa = new Intent(mContext, LoginActivity.class);
            aa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            aa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(aa);
        }
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent aa = new Intent(mContext, LoginActivity.class);
        aa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        aa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(aa);
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ID, preferences.getString(KEY_ID, null));
        user.put(KEY_AKSES, preferences.getString(KEY_AKSES, null));
        user.put(KEY_NAME, preferences.getString(KEY_NAME, null));
        user.put(KEY_USERNAME, preferences.getString(KEY_USERNAME, null));
        user.put(KEY_NIK, preferences.getString(KEY_NIK, null));
        user.put(KEY_PASSWORD, preferences.getString(KEY_PASSWORD, null));
        user.put(KEY_FOTO, preferences.getString(KEY_FOTO, null));
        user.put(KEY_ONE, preferences.getString(KEY_ONE, null));
        user.put(KEY_PEER, preferences.getString(KEY_PEER, null));
        return user;
    }


    public void updateProfil(String foto){
        editor.putString(KEY_FOTO, foto);
        editor.commit();
    }


    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }
}
