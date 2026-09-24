package com.sikboard;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.*;

public final class SikEmojiStore {
    private static final String PREF="sikemoji_store";
    private final SharedPreferences p;
    public SikEmojiStore(Context c){p=c.getSharedPreferences(PREF,Context.MODE_PRIVATE);}
    public void save(SikEmoji e){p.edit().putString(e.id,e.name+"|"+e.type.name()+"|"+(e.asset==null?"":e.asset.toString())).apply();}
    public List<String> ids(){return new ArrayList<>(p.getAll().keySet());}
}
