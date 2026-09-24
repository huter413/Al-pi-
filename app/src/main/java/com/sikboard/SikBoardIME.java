package com.sikboard;

import android.app.*;
import android.content.*;
import android.content.ClipDescription;
import android.graphics.*;
import android.graphics.drawable.*;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.os.*;
import android.provider.Settings;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import java.util.*;

public class SikBoardIME extends InputMethodService {
    private LinearLayout root, grid;
    private String mode="ABC";
    private boolean shift=false;
    private float scale=1f;
    private final ArrayList<String> customIds=new ArrayList<>();
    private final String[] standard={"😀","😃","😄","😁","😆","😅","😂","🤣","😊","🙂","😉","😎","😍","🥳","🤩","🤔","😭","😡","💀","👻","🤖","💩","❤️","🔥","⭐","✨","🎉","👍","👏","🙏","😴","😇","🤗","😐","😮","😱"};

    private int dp(int v){return (int)(v*getResources().getDisplayMetrics().density+0.5f);}
    private int h(){return dp((int)(58*scale));}

    @Override public View onCreateInputView(){
        SharedPreferences p=getSharedPreferences("sikboard",MODE_PRIVATE);
        scale=Math.max(.7f,Math.min(1.5f,p.getInt("key_size",70)/70f));
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(dp(5),dp(5),dp(5),dp(5)); root.setBackgroundColor(Color.WHITE);
        TextView title=new TextView(this); title.setText("SikBoard"); title.setTextSize(18); title.setTextColor(Color.BLACK); title.setGravity(Gravity.CENTER_VERTICAL); root.addView(title,new LinearLayout.LayoutParams(-1,dp(40)));
        LinearLayout tabs=new LinearLayout(this);
        addTab(tabs,"ABC","ABC"); addTab(tabs,"123","123"); addTab(tabs,"😀","EMOJI"); addTab(tabs,"SikEmoji","CUSTOM"); addTab(tabs,"+ Emoji","ADD"); addTab(tabs,"⚙","SETTINGS");
        root.addView(tabs,new LinearLayout.LayoutParams(-1,dp(50)));
        grid=new LinearLayout(this); grid.setOrientation(LinearLayout.VERTICAL);
        ScrollView scroll=new ScrollView(this); scroll.addView(grid); root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        TextView desc=new TextView(this); desc.setText("ABC: yazı ve boşluk  •  123: sayılar/semboller  •  😀: normal emoji  •  SikEmoji: özel emoji  •  + Emoji: galeriden emoji ekle  •  ⚙: ayarlar ve boyut"); desc.setTextSize(12); desc.setTextColor(Color.DKGRAY); desc.setPadding(dp(4),dp(4),dp(4),dp(4)); root.addView(desc,new LinearLayout.LayoutParams(-1,dp(38)));
        LinearLayout actions=new LinearLayout(this); addAction(actions,"Konuş",v->{}); addAction(actions,"Gönder",v->{InputConnection ic=getCurrentInputConnection();if(ic!=null)ic.performEditorAction(EditorInfo.IME_ACTION_SEND);}); root.addView(actions,new LinearLayout.LayoutParams(-1,dp(50)));
        rebuild(); return root;
    }

    private void addTab(LinearLayout row,String text,String target){
        Button b=button(text,14); b.setOnClickListener(v->{if("ADD".equals(target)){pickEmojiImage();}else if("SETTINGS".equals(target)){startActivity(new Intent(this,SettingsActivity.class));}else{mode=target;rebuild();}}); row.addView(b,new LinearLayout.LayoutParams(0,-1,1));
    }
    private void addAction(LinearLayout row,String text,View.OnClickListener l){Button b=button(text,14);b.setOnClickListener(l);row.addView(b,new LinearLayout.LayoutParams(0,-1,1));}
    private Button button(String text,float size){Button b=new Button(this);b.setText(text);b.setTextSize(size);b.setAllCaps(false);b.setMinHeight(h());b.setMinimumHeight(h());b.setPadding(dp(1),0,dp(1),0);return b;}

    private void rebuild(){
        grid.removeAllViews();
        if("ABC".equals(mode))buildLetters(); else if("123".equals(mode))buildNumbers(); else if("EMOJI".equals(mode))addGrid(standard,false); else buildCustom();
    }
    private void buildLetters(){
        addKeyRow("QWERTYUIOP"); addKeyRow("ASDFGHJKL"); addKeyRow("ZXCVBNM");
        LinearLayout r=new LinearLayout(this); addKey(r,"⇧",12,v->{shift=!shift;rebuild();}); addKey(r,"⌫",12,v->deleteOne()); addKey(r,"SPACE",12,v->commit(" ")); addKey(r,"↵",12,v->commit("\n")); grid.addView(r,new LinearLayout.LayoutParams(-1,h()));
    }
    private void addKeyRow(String s){LinearLayout r=new LinearLayout(this);for(int i=0;i<s.length();i++){String k=""+s.charAt(i);addKey(r,k,18,v->commit(shift?k:k.toLowerCase()));}grid.addView(r,new LinearLayout.LayoutParams(-1,h()));}
    private void buildNumbers(){addRaw(new String[]{"1","2","3","4","5","6","7","8","9","0"});addRaw(new String[]{"@","#","$","%","&","*","-","+","="});addRaw(new String[]{"(",")","/","!","?","'",":",";","."});LinearLayout r=new LinearLayout(this);addKey(r,"ABC",13,v->{mode="ABC";rebuild();});addKey(r,"⌫",12,v->deleteOne());addKey(r,"SPACE",12,v->commit(" "));addKey(r,"↵",12,v->commit("\n"));grid.addView(r,new LinearLayout.LayoutParams(-1,h()));}
    private void addRaw(String[] a){LinearLayout r=new LinearLayout(this);for(String s:a)addKey(r,s,17,v->commit(s));grid.addView(r,new LinearLayout.LayoutParams(-1,h()));}
    private void addKey(LinearLayout r,String s,float z,View.OnClickListener l){Button b=button(s,z);b.setOnClickListener(l);r.addView(b,new LinearLayout.LayoutParams(0,-1,1));}

    private void buildCustom(){
        customIds.clear(); for(int i=1;i<=77;i++)customIds.add("sikemoji:custom_"+i);
        customIds.addAll(new SikEmojiStore(this).ids());
        LinkedHashSet<String> unique=new LinkedHashSet<>(customIds); customIds.clear(); customIds.addAll(unique);
        LinearLayout row=null;
        for(int i=0;i<customIds.size();i++){
            if(i%6==0){row=new LinearLayout(this);grid.addView(row,new LinearLayout.LayoutParams(-1,h()));}
            final String id=customIds.get(i); Uri uri=new SikEmojiStore(this).asset(id);
            if(uri!=null){
                ImageButton b=new ImageButton(this); b.setImageURI(uri); b.setContentDescription(id); b.setScaleType(ImageView.ScaleType.CENTER_INSIDE); b.setOnClickListener(v->sendCustomImage(id,uri));
                row.addView(b,new LinearLayout.LayoutParams(0,-1,1));
            }else{Button b=button("S"+(i+1),14);b.setOnClickListener(v->commitEmoji(id));row.addView(b,new LinearLayout.LayoutParams(0,-1,1));}
        }
    }

    private void addGrid(String[] a,boolean ignored){LinearLayout r=null;for(int i=0;i<a.length;i++){if(i%6==0){r=new LinearLayout(this);grid.addView(r,new LinearLayout.LayoutParams(-1,h()));}final String v=a[i];Button b=button(v,24);b.setOnClickListener(x->commit(v));r.addView(b,new LinearLayout.LayoutParams(0,-1,1));}}

    private void pickEmojiImage(){
        Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.setType("image/*");i.addCategory(Intent.CATEGORY_OPENABLE);i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        i.setClass(this, EmojiPickerActivity.class); i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(i);
    }
    private void sendCustomImage(String id,Uri uri){
        InputConnection ic=getCurrentInputConnection();if(ic==null)return;
        try{
            String mime=getContentResolver().getType(uri);if(mime==null)mime="image/png";
            ClipDescription d=new ClipDescription(id,new String[]{mime});
            InputContentInfo info=new InputContentInfo(uri,d,uri);
            boolean ok=ic.commitContent(info,InputConnection.INPUT_CONTENT_GRANT_READ_URI_PERMISSION,null);
            if(!ok)commit(InlineEmojiToken.encode(id));
        }catch(Exception e){commit(InlineEmojiToken.encode(id));}
    }
    private void commitEmoji(String id){Uri u=new SikEmojiStore(this).asset(id);if(u!=null)sendCustomImage(id,u);else commit(InlineEmojiToken.encode(id));}
    private void commit(String s){InputConnection ic=getCurrentInputConnection();if(ic!=null)ic.commitText(s,1);}
    private void deleteOne(){InputConnection ic=getCurrentInputConnection();if(ic!=null)ic.deleteSurroundingText(1,0);}
}
