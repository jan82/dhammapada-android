/*
 * 2011 September 2
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
        ContentValues cvs = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, cvs);
        this.id = cvs.getAsLong("_id");
        this.title = cvs.getAsString("title");
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
