package com.appamatto.dhammapada;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Verse {
	public final Long id;
	public final VerseRange range;
	public final String text;
	public final boolean bookmarked;

	public Verse(Long id, VerseRange range, String text, boolean bookmarked) {
		this.id = id;
		this.range = range;
		this.text = text;
		this.bookmarked = bookmarked;
	}

	public Verse(VerseRange range, String text) {
		this(null, range, text, false);
	}

	public Verse(Cursor cursor) {
		this(cursor.getLong(0), new VerseRange(cursor.getInt(1),
				cursor.getInt(2)), cursor.getString(3), cursor.getInt(4) == 1);
	}

	public void insert(SQLiteDatabase db) {
		ContentValues cvs = new ContentValues();
		cvs.put("first", range.first);
		cvs.put("last", range.last);
		cvs.put("text", text);
		cvs.put("bookmarked", bookmarked ? 1 : 0);
		if (id != null) {
			db.update("verses", cvs, "_id = ?", new String[] { id.toString() });
		} else {
			db.insert("verses", null, cvs);
		}
	}
}