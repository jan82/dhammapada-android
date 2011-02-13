package com.appamatto.dhammapada;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Verse {
	public final Long id;
	public final long chapter;
	public final VerseRange range;
	public final String text;
	public final boolean bookmarked;

	public Verse(Long id, long chapter, VerseRange range, String text,
			boolean bookmarked) {
		this.id = id;
		this.chapter = chapter;
		this.range = range;
		this.text = text;
		this.bookmarked = bookmarked;
	}

	public Verse(long chapter, VerseRange range, String text) {
		this(null, chapter, range, text, false);
	}

	public Verse(Cursor cursor) {
		this(cursor.getLong(0), cursor.getLong(1), new VerseRange(
				cursor.getInt(2), cursor.getInt(3)), cursor.getString(4),
				cursor.getInt(5) == 1);
	}

	public long insert(SQLiteDatabase db) {
		ContentValues cvs = new ContentValues();
		cvs.put("chapter_id", chapter);
		cvs.put("first", range.first);
		cvs.put("last", range.last);
		cvs.put("text", text);
		cvs.put("bookmarked", bookmarked ? 1 : 0);
		if (id != null) {
			db.update("verses", cvs, "_id = ?", new String[] { id.toString() });
			return id;
		} else {
			return db.insert("verses", null, cvs);
		}
	}
}