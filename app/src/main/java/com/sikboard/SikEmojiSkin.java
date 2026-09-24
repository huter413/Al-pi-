package com.sikboard;

import android.content.Context;
import android.content.SharedPreferences;

public final class SikEmojiSkin {
    private final SharedPreferences p;
    public SikEmojiSkin(Context c){p=c.getSharedPreferences("sikboard_skin",0);}
    public String get(){return p.getString("skin","default");}
    public void set(String id){p.edit().putString("skin",id==null?"default":id).apply();}
}
