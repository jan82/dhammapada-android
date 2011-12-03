/*
 * 2011 September 2
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhp;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.lang.Math;
import android.graphics.Typeface;

public class ChapterHeading extends FrameLayout {
    private TextView title;
    private Context context;

    public ChapterHeading(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.chapter, this);
        title = (TextView) findViewById(R.id.chapter_title);
    }

    public void setChapter(Chapter chapter, Style currentStyle) {
        Typeface verajja = Typeface.createFromAsset(context.getAssets(), "fonts/verajjan.ttf");

        title.setText(chapter.title);
        title.setTextSize(Math.round(currentStyle.textSize*1.1));
        title.setTypeface(verajja);
    }
}
