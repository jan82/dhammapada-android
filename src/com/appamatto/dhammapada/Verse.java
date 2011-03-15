/*
 * 2011 February 14
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

public class Verse {
    public final Long id;
    public final long chapter;
    public final int chapterOffset;
    public final VerseRange range;
    public final String text;
    public final boolean bookmarked;

    public Verse(Long id, long chapter, int chapterOffset, VerseRange range,
            String text, boolean bookmarked) {
        this.id = id;
        this.chapter = chapter;
        this.chapterOffset = chapterOffset;
        this.range = range;
        this.text = text;
        this.bookmarked = bookmarked;
    }

    public Verse(long chapter, int chapterOffset, VerseRange range, String text) {
        this(null, chapter, chapterOffset, range, text, false);
    }

    public Verse(Cursor cursor) {
        ContentValues cvs = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, cvs);
        this.id = cvs.getAsLong("_id");
        this.chapter = cvs.getAsLong("chapter_id");
        this.chapterOffset = cvs.getAsInteger("chapter_offset");
        this.range = new VerseRange(cvs.getAsInteger("first"),
                cvs.getAsInteger("last"));
        this.text = cvs.getAsString("text");
        this.bookmarked = cvs.getAsInteger("bookmarked") == 1;
    }

    public long insert(SQLiteDatabase db) {
        ContentValues cvs = new ContentValues();
        cvs.put("chapter_id", chapter);
        cvs.put("chapter_offset", chapterOffset);
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
