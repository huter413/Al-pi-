package com.sikboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import java.util.*;

public final class SikEmojiStore {
    private static final String PREF="sikemoji_store";
    private final SharedPreferences p;
    public SikEmojiStore(Context c){p=c.getSharedPreferences(PREF,Context.MODE_PRIVATE);}
    public void save(SikEmoji e){p.edit().putString(e.id,e.name+"|"+e.type.name()+"|"+(e.asset==null?"":e.asset.toString())).apply();}
    public List<String> ids(){
        ArrayList<String> out=new ArrayList<>();
        for(String k:p.getAll().keySet()) if(k.startsWith("sikemoji:")) out.add(k);
        Collections.sort(out);
        return out;
    }
    public Uri asset(String id){
        String s=p.getString(id,null);
        if(s==null)return null;
        String[] a=s.split("\\|",-1);
        return a.length>2 && !a[2].isEmpty()?Uri.parse(a[2]):null;
    }
}
