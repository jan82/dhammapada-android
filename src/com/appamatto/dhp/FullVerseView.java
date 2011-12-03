/*
 * 2011 September 2
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.lang.Math;
import android.graphics.Typeface;

public class FullVerseView extends FrameLayout implements VerseView {
    private TextView verseRange;
    private TextView verseText;
    private ImageView verseRibbon;
    private Context context;

    public FullVerseView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.verse, this);
        verseRange = (TextView) findViewById(R.id.verse_range);
        verseText = (TextView) findViewById(R.id.verse_text);
        verseRibbon = (ImageView) findViewById(R.id.verse_ribbon);
    }

    public void setBookmarked(boolean bookmarked) {
        if (bookmarked) {
            verseRange.setTextColor(0xff000000);
            verseText.setTextColor(0xff000000);
            setBackgroundDrawable(getContext().getResources().getDrawable(
                        R.drawable.bookmarked));
            verseRibbon.setVisibility(View.VISIBLE);
        } else {
            verseRange.setTextColor(0xff808080);
            verseText.setTextColor(0xffffffff);
            setBackgroundColor(0xff000000);
            verseRibbon.setVisibility(View.INVISIBLE);
        }
    }

    public void setVerse(Verse verse, Style currentStyle) {
       if (verse.range.first == verse.range.last) {
            verseRange.setText("" + verse.range.first);
        } else {
            verseRange.setText("" + verse.range.first + "-" + verse.range.last);
        }

        Typeface verajja = Typeface.createFromAsset(context.getAssets(), "fonts/verajjan.ttf");

        verseText.setText(verse.text);
        verseText.setTextSize(currentStyle.textSize);
        verseText.setTypeface(verajja);
        
        verseRange.setTextSize(Math.round(currentStyle.textSize*1.1));
        verseRange.setTypeface(verajja);

        setBookmarked(verse.bookmarked);
    }
}
