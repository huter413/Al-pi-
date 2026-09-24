package com.sikboard;

public final class InlineEmojiToken {
    private InlineEmojiToken(){}
    public static String encode(String id){return "["+id+"]";}
    public static String decode(String text){if(text!=null&&text.startsWith("[sikemoji:")&&text.endsWith("]"))return text.substring(1,text.length()-1);return null;}
}
