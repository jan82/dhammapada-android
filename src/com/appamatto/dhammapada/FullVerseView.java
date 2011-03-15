/*
 * 2011 February 14
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class FullVerseView extends FrameLayout implements VerseView {
    private TextView verseRange;
    private TextView verseText;
    private ImageView verseRibbon;

    public FullVerseView(Context context) {
        super(context);
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

    public void setVerse(Verse verse) {
        if (verse.range.first == verse.range.last) {
            verseRange.setText("" + verse.range.first);
        } else {
            verseRange.setText("" + verse.range.first + "-" + verse.range.last);
        }
        verseText.setText(verse.text);
        setBookmarked(verse.bookmarked);
    }
}
