package com.appamatto.dhammapada;

public class Chapter {
	public final VerseRange range;
	public final String title;

	public Chapter(VerseRange range, String title) {
		this.range = range;
		this.title = title;
	}

	public Chapter(int first, int last, String title) {
		this(new VerseRange(first, last), title);
	}
}
