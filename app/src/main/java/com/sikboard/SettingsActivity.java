package com.sikboard;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

public class SettingsActivity extends Activity {
    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        SharedPreferences p=getSharedPreferences("sikboard",Context.MODE_PRIVATE);
        LinearLayout l=new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); l.setPadding(32,32,32,32);
        TextView h=new TextView(this); h.setText("SikBoard Ayarları"); h.setTextSize(26); l.addView(h);
        TextView info=new TextView(this); info.setText("Klavye boyutu: tuşları ve emoji görünümünü büyütmek/küçültmek için kaydırıcıyı kullan."); info.setTextSize(15); l.addView(info);
        SeekBar size=new SeekBar(this); size.setMax(100); size.setProgress(p.getInt("key_size",70)); l.addView(size);
        TextView value=new TextView(this); value.setText("Boyut: "+size.getProgress()+"%"); l.addView(value);
        size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            public void onProgressChanged(SeekBar s,int v,boolean from){if(v<40)v=40; p.edit().putInt("key_size",v).apply(); value.setText("Boyut: "+v+"%");}
            public void onStartTrackingTouch(SeekBar s){} public void onStopTrackingTouch(SeekBar s){}
        });
        CheckBox skin=new CheckBox(this); skin.setText("SikEmoji skin sistemi"); skin.setChecked(p.getBoolean("skin",true)); skin.setOnCheckedChangeListener((v,c)->p.edit().putBoolean("skin",c).apply()); l.addView(skin);
        CheckBox custom=new CheckBox(this); custom.setText("Özel emoji sekmesi"); custom.setChecked(p.getBoolean("custom",true)); custom.setOnCheckedChangeListener((v,c)->p.edit().putBoolean("custom",c).apply()); l.addView(custom);
        Button picker=new Button(this); picker.setText("Klavye seçimini aç"); picker.setOnClickListener(v->((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker()); l.addView(picker);
        setContentView(l);
    }
}
