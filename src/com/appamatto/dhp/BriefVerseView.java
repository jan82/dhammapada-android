/*
 * 2011 September 2
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhp;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.Math;
import android.graphics.Typeface;

public class BriefVerseView extends LinearLayout implements VerseView {
    private TextView verseRange;
    private TextView verseText;
    private Context context;

    public BriefVerseView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.brief, this);
        setOrientation(LinearLayout.HORIZONTAL);
        verseRange = (TextView) findViewById(R.id.verse_range);
        verseText = (TextView) findViewById(R.id.verse_text);
    }

    public void setBookmarked(boolean bookmarked) {
        if (bookmarked) {
            verseRange.setTextColor(0xff000000);
            verseText.setTextColor(0xff000000);
            setBackgroundDrawable(getContext().getResources().getDrawable(
                        R.drawable.bookmarked));
        } else {
            verseRange.setTextColor(0xff808080);
            verseText.setTextColor(0xffffffff);
            setBackgroundColor(0xff000000);
        }
    }

    public void setVerse(Verse verse, Style currentStyle) {
        if (verse.range.first == verse.range.last) {
            verseRange.setText("" + verse.range.first);
        } else {
            verseRange.setText("" + verse.range.first + "-" + verse.range.last);
        }
        String[] split = verse.text.split("\n");
        StringBuilder brief = new StringBuilder();
        for (int i = 0; i < 2 && i < split.length; i++) {
            brief.append(split[i]);
            if (i != 1) {
                brief.append("\n");
            }
        }
        brief.append(" ...");

        Typeface verajja = Typeface.createFromAsset(context.getAssets(), "fonts/verajjan.ttf");

        verseText.setText(brief.toString());
        verseText.setTextSize(currentStyle.textSize);
        verseText.setTypeface(verajja);
        verseRange.setTextSize(Math.round(currentStyle.textSize*1.1));
        verseRange.setTypeface(verajja);
        setBookmarked(verse.bookmarked);
    }
}
