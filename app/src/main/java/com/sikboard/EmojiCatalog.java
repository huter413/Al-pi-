package com.sikboard;

import java.util.*;

public final class EmojiCatalog {
    private EmojiCatalog(){}
    public static List<String> standard(){return Arrays.asList("😀","😃","😄","😁","😆","😅","😂","🤣","😊","🙂","🙃","😉","😎","😍","🥳","🤩","🤔","😐","😴","😭","😡","🤯","💀","👻","🤖","💩","❤️","🔥","⭐","✨","🎉","👍","👎","👏","🙏");}
    public static List<String> customSlots(){List<String> x=new ArrayList<>(); for(int i=1;i<=77;i++)x.add("sikemoji:custom_"+i); return x;}
}
