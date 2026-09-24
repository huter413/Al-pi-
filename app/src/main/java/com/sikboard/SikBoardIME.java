package com.sikboard;

import android.inputmethodservice.InputMethodService;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.content.Intent;

public class SikBoardIME extends InputMethodService {
    private LinearLayout root, grid;
    private String mode = "ABC";
    private boolean shift = false;

    private final String[] standard = {
        "😀","😃","😄","😁","😆","😅","😂","🤣","😊","🙂","😉","😎",
        "😍","🥳","🤩","🤔","😭","😡","💀","👻","🤖","💩","❤️","🔥",
        "⭐","✨","🎉","👍","👏","🙏","😴","😇","🤗","😐","😮","😱"
    };

    private int dp(int v) {
        return (int)(v * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override public View onCreateInputView() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(6), dp(6), dp(6), dp(6));
        root.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("SikBoard");
        title.setTextSize(18);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(title, new LinearLayout.LayoutParams(-1, dp(42)));

        LinearLayout tabs = new LinearLayout(this);
        addTab(tabs, "ABC", "ABC");
        addTab(tabs, "123", "123");
        addTab(tabs, "😀", "EMOJI");
        addTab(tabs, "SikEmoji", "CUSTOM");
        addTab(tabs, "⚙", "SETTINGS");
        root.addView(tabs, new LinearLayout.LayoutParams(-1, dp(52)));

        grid = new LinearLayout(this);
        grid.setOrientation(LinearLayout.VERTICAL);
        ScrollView scroll = new ScrollView(this);
        scroll.addView(grid);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        LinearLayout actions = new LinearLayout(this);
        addAction(actions, "Konuş", v -> { });
        addAction(actions, "Gönder", v -> {
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) ic.performEditorAction(EditorInfo.IME_ACTION_SEND);
        });
        root.addView(actions, new LinearLayout.LayoutParams(-1, dp(52)));

        rebuild();
        return root;
    }

    private void addTab(LinearLayout row, String text, String target) {
        Button b = button(text, 15);
        b.setOnClickListener(v -> {
            if ("SETTINGS".equals(target)) {
                startActivity(new Intent(this, SettingsActivity.class));
            } else {
                mode = target;
                rebuild();
            }
        });
        row.addView(b, new LinearLayout.LayoutParams(0, -1, 1));
    }

    private void addAction(LinearLayout row, String text, View.OnClickListener listener) {
        Button b = button(text, 15);
        b.setOnClickListener(listener);
        row.addView(b, new LinearLayout.LayoutParams(0, -1, 1));
    }

    private Button button(String text, float size) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(size);
        b.setAllCaps(false);
        b.setMinHeight(dp(50));
        b.setMinimumHeight(dp(50));
        b.setPadding(dp(2), 0, dp(2), 0);
        return b;
    }

    private void rebuild() {
        grid.removeAllViews();
        if ("ABC".equals(mode)) buildLetters();
        else if ("123".equals(mode)) buildNumbers();
        else if ("EMOJI".equals(mode)) buildEmoji(standard);
        else if ("CUSTOM".equals(mode)) buildCustom();
    }

    private void buildLetters() {
        addKeyRow("QWERTYUIOP");
        addKeyRow("ASDFGHJKL");
        addKeyRow("ZXCVBNM");
        LinearLayout row = new LinearLayout(this);
        addKey(row, "⇧", 12, v -> shift = !shift);
        addKey(row, "⌫", 12, v -> deleteOne());
        addKey(row, "SPACE", 13, v -> commit(" "));
        addKey(row, "↵", 12, v -> commit("\n"));
        grid.addView(row, new LinearLayout.LayoutParams(-1, dp(58)));
    }

    private void addKeyRow(String letters) {
        LinearLayout row = new LinearLayout(this);
        for (int i = 0; i < letters.length(); i++) {
            String s = String.valueOf(letters.charAt(i));
            addKey(row, s, 19, v -> commit(shift ? s : s.toLowerCase()));
        }
        grid.addView(row, new LinearLayout.LayoutParams(-1, dp(58)));
    }

    private void buildNumbers() {
        addKeyRowRaw(new String[]{"1","2","3","4","5","6","7","8","9","0"});
        addKeyRowRaw(new String[]{"@","#","$","%","&","*","-","+","="});
        addKeyRowRaw(new String[]{"(",")","/","!","?","'",":",";","."});
        LinearLayout row = new LinearLayout(this);
        addKey(row, "ABC", 14, v -> { mode = "ABC"; rebuild(); });
        addKey(row, "⌫", 12, v -> deleteOne());
        addKey(row, "SPACE", 13, v -> commit(" "));
        addKey(row, "↵", 12, v -> commit("\n"));
        grid.addView(row, new LinearLayout.LayoutParams(-1, dp(58)));
    }

    private void addKeyRowRaw(String[] keys) {
        LinearLayout row = new LinearLayout(this);
        for (String s : keys) addKey(row, s, 17, v -> commit(s));
        grid.addView(row, new LinearLayout.LayoutParams(-1, dp(58)));
    }

    private void buildEmoji(String[] items) {
        addGrid(items, false);
    }

    private void buildCustom() {
        String[] items = new String[77];
        for (int i = 0; i < items.length; i++) items[i] = "sikemoji:custom_" + (i + 1);
        addGrid(items, true);
    }

    private void addGrid(String[] items, boolean custom) {
        LinearLayout row = null;
        for (int i = 0; i < items.length; i++) {
            if (i % 6 == 0) {
                row = new LinearLayout(this);
                grid.addView(row, new LinearLayout.LayoutParams(-1, dp(62)));
            }
            final String value = items[i];
            Button b = button(custom ? "S" + (i + 1) : value, custom ? 14 : 24);
            b.setOnClickListener(v -> commitEmoji(value));
            row.addView(b, new LinearLayout.LayoutParams(0, -1, 1));
        }
    }

    private void addKey(LinearLayout row, String text, float size, View.OnClickListener listener) {
        Button b = button(text, size);
        b.setOnClickListener(listener);
        row.addView(b, new LinearLayout.LayoutParams(0, -1, 1));
    }

    private void commit(String text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) ic.commitText(text, 1);
    }

    private void commitEmoji(String value) {
        if (EmojiStudio.isCustomId(value)) {
            commit(InlineEmojiToken.encode(value));
        } else {
            commit(value);
        }
    }

    private void deleteOne() {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) ic.deleteSurroundingText(1, 0);
    }
}
