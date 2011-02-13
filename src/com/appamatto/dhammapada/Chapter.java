package com.appamatto.dhammapada;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Chapter {
	public final Long id;
	public final String title;

	public Chapter(String title) {
		this(null, title);
	}

	public Chapter(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	public Chapter(Cursor cursor) {
		this(cursor.getLong(0), cursor.getString(1));
	}

	public Chapter insert(SQLiteDatabase db) {
		ContentValues cvs = new ContentValues();
		cvs.put("title", title);
		if (id != null) {
			db.update("chapters", cvs, "_id = ?",
					new String[] { id.toString() });
			return this;
		} else {
			return new Chapter(db.insert("chapters", null, cvs), title);
		}
	}
}
