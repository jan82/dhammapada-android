/*
 * 2011 February 14
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "dhp";
    private static final int DB_VERSION = 11;
    private static final String DHP_FILE = "dhp.txt";

    /* DB HISTORY
     *
     * 10 - first release
     * 11 - separated mutable content from immutable content (bookmarks)
     */

    private Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public void createTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE chapters " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, title)");
        db.execSQL("CREATE TABLE verses " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "chapter_id, chapter_offset, first, last, text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS bookmarks " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, verse, bookmarked)");
    }

    public VerseRange parseRange(String line) {
        if (line == null || line.length() == 0 || line.charAt(0) != '#') {
            return null;
        }
        int start = 1, end;
        for (end = start; end < line.length()
                && Character.isDigit(line.charAt(end)); end++) {
                }
        String toParse = line.substring(start, end);
        int first, last;
        try {
            first = Integer.parseInt(toParse);
        } catch (NumberFormatException e) {
            return null;
        }
        if (end == line.length() || line.charAt(end) != '-') {
            return new VerseRange(first, first);
        }
        start = end + 1;
        for (end = start; end < line.length()
                && Character.isDigit(line.charAt(end)); end++) {
                }
        toParse = line.substring(start, end);
        try {
            last = Integer.parseInt(toParse);
        } catch (NumberFormatException e) {
            return null;
        }
        return new VerseRange(first, last);
    }

    public String parseChapter(String line) {
        if (line.length() < 1 || line.charAt(0) != '*') {
            return null;
        }
        return line.substring(1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            createTables(db);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                        context.getAssets().open(DHP_FILE)));
            String line;
            VerseRange range = null;
            Chapter chapter = null;
            int chapterOffset = 0;
            String text = null;
            while ((line = reader.readLine()) != null) {
                String title;
                if ((title = parseChapter(line)) != null) {
                    if (text != null) {
                        new Verse(chapter.id, chapterOffset++, range, text)
                            .insert(db);
                        text = null;
                    }
                    chapter = new Chapter(title).insert(db);
                    chapterOffset = 0;
                    continue;
                }
                VerseRange newRange;
                if ((newRange = parseRange(line)) != null) {
                    if (text != null) {
                        new Verse(chapter.id, chapterOffset++, range, text)
                            .insert(db);
                        text = null;
                    }
                    range = newRange;
                    continue;
                }
                if (text == null) {
                    text = line;
                } else {
                    text += '\n' + line;
                }
            }
            if (text != null) {
                new Verse(chapter.id, chapterOffset++, range, text).insert(db);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <= 10) {
            /* copy bookmark data into bookmarks table */
            db.execSQL("CREATE TABLE bookmarks (_id INTEGER PRIMARY KEY AUTOINCREMENT, verse, bookmarked)");
            Cursor bookmarked = db.rawQuery("SELECT * FROM verses WHERE bookmarked = 1", null);
            while (bookmarked.moveToNext()) {
                Verse verse = new Verse(bookmarked);
                verse.bookmark(db);
            }
            bookmarked.close();
        }
        db.execSQL("DROP TABLE IF EXISTS chapters");
        db.execSQL("DROP TABLE IF EXISTS verses");
        onCreate(db);
    }
}
