/*
 * 2011 April 9
 *
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;

public class Style {
    public final long id;
    public final String name;
    public final boolean builtIn;
    public final boolean chapters;
    public final boolean ribbon;
    public final Typeface font;
    public final int textSize; /* in dp */
    public final int text;
    public final int background;
    public final int markedText;
    public final int markedBackground;

    public Style(Cursor cursor) {
        ContentValues cvs = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor, cvs);
        id = cvs.getAsLong("_id");
        name = cvs.getAsString("name");
        builtIn = cvs.getAsBoolean("builtin");
        chapters = cvs.getAsBoolean("chapters");
        ribbon = cvs.getAsBoolean("ribbon");
        font = cvs.getAsBoolean("font") ? Typeface.SERIF : Typeface.SANS_SERIF;
        textSize = cvs.getAsInteger("text_size");
        text = cvs.getAsInteger("text_color");
        background = cvs.getAsInteger("bg_color");
        markedText = cvs.getAsInteger("marked_text_color");
        markedBackground= cvs.getAsInteger("marked_bg_color");
    }

    public static Style get(SQLiteDatabase db, long id) {
        Cursor cursor = db.rawQuery("SELECT * FROM styles WHERE _id = " + id, null);
        try {
            if (cursor.moveToFirst()) {
                return new Style(cursor);
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    public static Style getBuiltIn(SQLiteDatabase db, String name) {
        Cursor cursor = db.rawQuery("SELECT * FROM styles WHERE name = ?",
                new String[] { name });
        try {
            if (cursor.moveToFirst()) {
                Style style = new Style(cursor);
                if (style.builtIn) {
                    return style;
                } else {
                    Builder builder = Builder.edit(style);
                    /* TODO: stringify */
                    builder.name = style.name + " (custom)";
                    builder.build(db);
                }
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    public static class Builder {
        public final Long id;
        public String name;
        public final boolean builtIn;
        public boolean chapters;
        public boolean ribbon;
        public Typeface font;
        public int textSize;
        public int text;
        public int background;
        public int markedText;
        public int markedBackground;

        private Builder(Long id, boolean builtIn) {
            this.id = id;
            this.builtIn = builtIn;
        }

        public static Builder empty(boolean builtIn) {
            return new Builder(null, builtIn);
        }

        public static Builder copy(Style style) {
            Builder builder = new Builder(null, false).init(style);
            builder.name = style.name + " copy";
            return builder;
        }

        public static Builder edit(Style style) {
            return new Builder(style.id, style.builtIn).init(style);
        }

        private Builder init(Style style) {
            name = style.name;
            chapters = style.chapters;
            ribbon = style.ribbon;
            font = style.font;
            textSize = style.textSize;
            text = style.text;
            background = style.background;
            markedText = style.markedText;
            markedBackground = style.markedBackground;
            return this;
        }

        public Style build(SQLiteDatabase db) {
            ContentValues cvs = new ContentValues();
            cvs.put("name", name);
            cvs.put("builtIn", builtIn);
            cvs.put("chapters", chapters);
            cvs.put("ribbon", ribbon);
            cvs.put("font", font == Typeface.SERIF);
            cvs.put("text_size", textSize);
            cvs.put("text_color", text);
            cvs.put("bg_color", background);
            cvs.put("marked_text_color", markedText);
            cvs.put("marked_bg_color", markedBackground);
            Long id = this.id;
            if (id != null) {
                db.update("styles", cvs, "_id = ?", new String[] { id.toString() });
            } else {
                id = db.insert("styles", null, cvs);
            }
            Cursor cursor = db.rawQuery("SELECT * FROM styles WHERE _id = ?",
                    new String[] { id.toString() });
            try {
                if (cursor.moveToFirst()) {
                    return new Style(cursor);
                } else {
                    return null;
                }
            } finally {
                cursor.close();
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
