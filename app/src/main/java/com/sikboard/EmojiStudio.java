package com.sikboard;

import android.graphics.Bitmap;
import java.util.UUID;

public final class EmojiStudio {
    public static final String PREFIX="sikemoji:";
    private EmojiStudio(){}
    public static String newId(){ return PREFIX+UUID.randomUUID(); }
    public static boolean isCustomId(String id){ return id!=null && id.startsWith(PREFIX); }
    public static Bitmap imageEmoji(Bitmap bitmap){ return bitmap; }
}
