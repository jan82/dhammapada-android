/*
 * 2011 September 2
 * 
 * The author disclaims copyright to this source code.
 */

package com.appamatto.dhammapada;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ChapterHeading extends FrameLayout {
    private TextView title;

    public ChapterHeading(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.chapter, this);
        title = (TextView) findViewById(R.id.chapter_title);
    }

    public void setChapter(Chapter chapter, Style currentStyle) {
        title.setText(chapter.title);
        title.setTextSize(currentStyle.textSize);
        title.setTypeface(currentStyle.font);
    }
}
