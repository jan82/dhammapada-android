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

	public void setChapter(Chapter chapter) {
		title.setText(chapter.title);
	}
}
