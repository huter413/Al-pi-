package com.sikboard;

import android.net.Uri;

public final class SikEmoji {
    public enum Type { IMAGE, GIF, MODEL3D }
    public final String id, name; public final Type type; public final Uri asset;
    public SikEmoji(String id,String name,Type type,Uri asset){this.id=id;this.name=name;this.type=type;this.asset=asset;}
    public boolean isCustom(){return id!=null && id.startsWith("sikemoji:");}
}
