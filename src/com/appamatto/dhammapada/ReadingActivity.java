/*
 * 2011 February 14
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ReadingActivity extends DhammapadaActivity {
    private SQLiteDatabase db;
    private ListView verses;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("Dhammapada: Reading");

        db = new DBHelper(this).getWritableDatabase();
        Cursor versesCursor = db.rawQuery("SELECT * FROM verses LEFT OUTER JOIN bookmarks " +
                "ON bookmarks.verse >= verses.first AND bookmarks.verse <= verses.last " +
                "GROUP BY verses._id ORDER BY chapter_id",
                null);
        startManagingCursor(versesCursor);
        Cursor chaptersCursor = db
            .rawQuery(
                    "SELECT chapters._id as _id, chapters.title as title, count(*) as members "
                    + "FROM verses, chapters WHERE verses.chapter_id = chapters._id "
                    + "GROUP BY verses.chapter_id ORDER BY chapters._id",
                    null);
        startManagingCursor(chaptersCursor);
        verses = (ListView) findViewById(R.id.verses_list);
        GroupsAdapter groups = new ChaptersAdapter(this, chaptersCursor);
        final VersesAdapter members = new VersesAdapter(this, versesCursor,
                true);
        HeadingAdapter headingAdapter = new HeadingAdapter(groups, members);
        verses.setAdapter(headingAdapter);

        long verseId = getIntent().getLongExtra("verse_id", -1);
        long chId = getIntent().getLongExtra("chapter_id", -1);
        if (verseId == -1 && chId == -1) {
            SharedPreferences prefs = getSharedPreferences("Browser",
                    MODE_PRIVATE);
            int position = prefs.getInt("progress", 0);
            verses.setSelection(position);
        } else if (verseId != -1) {
            Cursor cursor = db.rawQuery("SELECT * FROM verses WHERE _id = "
                    + verseId, null);
            cursor.moveToFirst();
            Verse verse = new Verse(cursor);
            cursor.close();
            verses.setSelection(headingAdapter.getFlatPosition(
                        (int) (verse.chapter - 1), verse.chapterOffset));
        } else {
            Cursor cursor = db.rawQuery(
                "SELECT * FROM verses, chapters " +
                "WHERE chapter_id = " + chId +
                " ORDER BY _id LIMIT 1", 
                null
            );
            cursor.moveToFirst();
            Verse verse = new Verse(cursor);
            cursor.close();
            verses.setSelection(headingAdapter.getFlatPosition(
                        (int) (verse.chapter - 1), verse.chapterOffset));
        }
        verses.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
                HeadingAdapter adapter = (HeadingAdapter) verses.getAdapter();
                if (!adapter.isGroup(position)) {
                    Verse verse = new Verse((Cursor) adapter.getItem(position));
                    if (verse.bookmarked) {
                        verse.unbookmark(db);
                    } else {
                        verse.bookmark(db);
                    }
                    members.getCursor().requery();
                }
            }
        });
    }

    @Override
    protected int[] getDisabledMenuItems() {
        return new int[] { R.id.reader };
    }

    @Override
    public void onDestroy() {
        int position = verses.getFirstVisiblePosition();
        SharedPreferences prefs = getSharedPreferences("Browser", MODE_PRIVATE);
        prefs.edit().putInt("progress", position).commit();
        super.onDestroy();
        db.close();
        db = null;
    }
}
