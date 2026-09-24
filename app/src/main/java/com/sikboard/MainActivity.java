package com.sikboard;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.widget.*;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);

        TextView t = new TextView(this);
        t.setText("SikBoard\n\nKlavye değiştirme ve ekran klavyesi ayarlarını aç.");
        t.setTextSize(20);
        t.setPadding(32, 48, 32, 32);
        l.addView(t);

        Button e = new Button(this);
        e.setText("Ayarlar > Klavye ayarlarını aç");
        e.setOnClickListener(v -> openKeyboardSettings());
        l.addView(e);

        setContentView(l);
    }

    private void openKeyboardSettings() {
        try {
            startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS));
        } catch (Exception ignored) {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
        }
    }
}
