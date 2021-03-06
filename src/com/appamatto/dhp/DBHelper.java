/*
 * 2011 September 2
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 12;

    /*
     * DB HISTORY 10 - first release 11 - separated mutable content from
     * immutable content (bookmarks) 12 - added styles table
     */

    private Context context;

    public DBHelper(Context context) {
        super(context, Translation.get(context).id, null, DB_VERSION);
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
        db.execSQL("CREATE TABLE IF NOT EXISTS styles " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name, builtin, " +
                "chapters, ribbon, serif, text_size, text_color, bg_color, " +
                "marked_text_color, marked_bg_color)");
    }

    public void updateStyles(SQLiteDatabase db) {
        /* insert/update styles */
        Style style = Style.getBuiltIn(db, "medium");
        if (style == null) {
            Style.Builder builder = Style.Builder.empty(true);

            // sans

            builder.name = "medium";
            builder.chapters = 1;
            builder.ribbon = 1;
            builder.serif = 0;
            builder.textSize = 20;
            builder.text = Color.WHITE;
            builder.background = Color.BLACK;
            builder.markedText = Color.BLACK;
            builder.markedBackground = Color.WHITE;
            builder.build(db);

            // large

            builder.name = "large";
            builder.chapters = 1;
            builder.ribbon = 1;
            builder.serif = 0;
            builder.textSize = 25;
            builder.text = Color.WHITE;
            builder.background = Color.BLACK;
            builder.markedText = Color.BLACK;
            builder.markedBackground = Color.WHITE;
            builder.build(db);

            // small

            builder.name = "small";
            builder.chapters = 1;
            builder.ribbon = 1;
            builder.serif = 0;
            builder.textSize = 15;
            builder.text = Color.WHITE;
            builder.background = Color.BLACK;
            builder.markedText = Color.BLACK;
            builder.markedBackground = Color.WHITE;
            builder.build(db);

        }
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
            updateStyles(db);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    context.getAssets().open(Translation.get(context).id + ".txt")));
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
        if (oldVersion < 11) {
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
