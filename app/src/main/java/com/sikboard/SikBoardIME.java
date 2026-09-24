package com.sikboard;

import android.inputmethodservice.InputMethodService;
import android.graphics.Color;
import android.view.*;
import android.view.inputmethod.InputConnection;
import android.widget.*;

public class SikBoardIME extends InputMethodService {
    private LinearLayout root, grid;
    private boolean customTab=false;
    private final String[] standard={"😀","😃","😄","😁","😆","😅","😂","🤣","😊","🙂","😉","😎","😍","🥳","🤩","🤔","😭","😡","💀","👻","🤖","💩","❤️","🔥","⭐","✨","🎉","👍","👏","🙏"};
    @Override public View onCreateInputView(){
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(8,8,8,8);
        TextView title=new TextView(this); title.setText("SikBoard • SikEmoji Renderer"); title.setTextSize(18); title.setPadding(8,8,8,8); root.addView(title);
        LinearLayout tabs=new LinearLayout(this);
        Button normal=new Button(this); normal.setText("Emoji"); normal.setOnClickListener(v->{customTab=false; rebuild();});
        Button custom=new Button(this); custom.setText("SikEmoji +77"); custom.setOnClickListener(v->{customTab=true; rebuild();});
        Button studio=new Button(this); studio.setText("Yeni Emoji"); studio.setOnClickListener(v->openStudio());
        Button settings=new Button(this); settings.setText("⚙"); settings.setOnClickListener(v->startActivity(new android.content.Intent(this,SettingsActivity.class)));
        tabs.addView(normal,new LinearLayout.LayoutParams(0,60,1)); tabs.addView(custom,new LinearLayout.LayoutParams(0,60,1)); tabs.addView(studio,new LinearLayout.LayoutParams(0,60,1)); tabs.addView(settings,new LinearLayout.LayoutParams(0,60,1)); root.addView(tabs);
        grid=new LinearLayout(this); grid.setOrientation(LinearLayout.VERTICAL); root.addView(grid,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout actions=new LinearLayout(this);
        Button voice=new Button(this); voice.setText("Konuş"); voice.setOnClickListener(v->getCurrentInputConnection());
        Button send=new Button(this); send.setText("Gönder"); send.setOnClickListener(v->{InputConnection ic=getCurrentInputConnection(); if(ic!=null)ic.performEditorAction(android.view.inputmethod.EditorInfo.IME_ACTION_SEND);});
        actions.addView(voice,new LinearLayout.LayoutParams(0,60,1)); actions.addView(send,new LinearLayout.LayoutParams(0,60,1)); root.addView(actions);
        rebuild(); return root;
    }
    private void rebuild(){
        grid.removeAllViews(); LinearLayout row=null; int i=0;
        String[] items=customTab?makeCustom():standard;
        for(String e:items){ if(i%6==0){row=new LinearLayout(this); grid.addView(row); } Button b=new Button(this); b.setText(customTab?"S"+((i%77)+1):e); b.setTextSize(22); b.setOnClickListener(v->commitEmoji(((Button)v).getText().toString())); row.addView(b,new LinearLayout.LayoutParams(0,64,1)); i++; }
    }
    private String[] makeCustom(){String[] a=new String[77]; for(int i=0;i<77;i++)a[i]="sikemoji:custom_"+(i+1); return a;}
    private void commitEmoji(String e){InputConnection ic=getCurrentInputConnection(); if(ic==null)return; if(EmojiStudio.isCustomId(e)) ic.commitText(InlineEmojiToken.encode(e),1); else ic.commitText(e,1);}
    private void openStudio(){Toast.makeText(this,"Yeni Emoji Studio: resim/GIF/3D varlıkları SikEmoji ID ile kaydedilebilir.",Toast.LENGTH_LONG).show();}
}
