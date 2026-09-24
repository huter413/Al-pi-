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
        LinearLayout l=new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); l.setPadding(24,24,24,24);
        TextView h=new TextView(this); h.setText("SikBoard Ayarları"); h.setTextSize(24); l.addView(h);
        CheckBox skin=new CheckBox(this); skin.setText("SikEmoji skin sistemi"); skin.setChecked(p.getBoolean("skin",true)); skin.setOnCheckedChangeListener((v,c)->p.edit().putBoolean("skin",c).apply()); l.addView(skin);
        CheckBox custom=new CheckBox(this); custom.setText("Özel emoji sekmesi"); custom.setChecked(p.getBoolean("custom",true)); custom.setOnCheckedChangeListener((v,c)->p.edit().putBoolean("custom",c).apply()); l.addView(custom);
        Button picker=new Button(this); picker.setText("Klavye seçimini aç"); picker.setOnClickListener(v->((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker()); l.addView(picker);
        setContentView(l);
    }
}
