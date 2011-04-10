/*
 * 2011 April 9
 *
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Typeface;

public class Style {
    public final Long id;
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
        this.id = cvs.getAsLong("_id");
        this.builtIn = cvs.getAsInteger("builtin") != null;
        this.chapters = cvs.getAsInteger("chapters") != null;
        this.ribbon = cvs.getAsInteger("ribbon") != null;
        this.font = "serif".equals(cvs.getAsString("font")) ?
            Typeface.SERIF : Typeface.SANS_SERIF;
        this.textSize = cvs.getAsInteger("text_size");
        this.text = cvs.getAsInteger("text_color");
        this.background = cvs.getAsInteger("bg_color");
        this.markedText = cvs.getAsInteger("marked_text_color");
        this.markedBackground= cvs.getAsInteger("marked_bg_color");
    }
}
