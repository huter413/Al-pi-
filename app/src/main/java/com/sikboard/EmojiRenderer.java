package com.sikboard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class EmojiRenderer {
    private final Map<String, Uri> assets = new HashMap<>();
    public void register(String id, Uri uri){ if(id!=null && uri!=null) assets.put(id, uri); }
    public boolean has(String id){ return assets.containsKey(id); }
    public void render(Context context, String id, ImageView target){
        Uri uri=assets.get(id);
        if(uri==null){ target.setImageDrawable(null); return; }
        try (InputStream in=context.getContentResolver().openInputStream(uri)) {
            target.setImageDrawable(in==null?null:Drawable.createFromStream(in,id));
        } catch(Exception e){ target.setImageDrawable(null); }
    }
}
