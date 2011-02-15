/*
 * 2011 February 14
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BriefVerseView extends LinearLayout implements VerseView {
	private TextView verseRange;
	private TextView verseText;

	public BriefVerseView(Context context) {
		super(context);
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

	public void setVerse(Verse verse) {
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
		verseText.setText(brief.toString());
		setBookmarked(verse.bookmarked);
	}
}
