/*
 * 2011 September 2
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.app.Activity;
import android.content.Intent;
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
import android.content.SharedPreferences;

public class FavoritesActivity extends DhammapadaActivity {
    private SQLiteDatabase db;
    private ListView verses;
    private Bundle savedInstanceState;
    Cursor versesCursor;
    Cursor chaptersCursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.main);
        setTitle("Dhammapada: Favorites");

        db = new DBHelper(this).getWritableDatabase();
        SharedPreferences stylepref = getSharedPreferences("Style", MODE_PRIVATE);
        long id = stylepref.getLong("id", 1);
        
        Style currentStyle = Style.get(db,id);

        versesCursor = db
            .rawQuery(
                    "SELECT * FROM verses, bookmarks " + 
                    "WHERE verses.first <= bookmarks.verse AND verses.last >= bookmarks.verse " +
                    "AND bookmarked = 1 " +
                    "GROUP BY verses._id ORDER BY chapter_id",
                    null);
        chaptersCursor = db
            .rawQuery(
                    "SELECT chapters._id as _id, chapters.title as title, count(*) as members " +
                    "FROM verses, chapters, bookmarks " +
                    "WHERE verses.first <= bookmarks.verse AND verses.last >= bookmarks.verse " +
                    "AND verses.chapter_id = chapters._id AND bookmarked = 1 " +
                    "GROUP BY verses.chapter_id ORDER BY chapters._id",
                    null);
        verses = (ListView) findViewById(R.id.verses_list);

        GroupsAdapter groups = new ChaptersAdapter(this, chaptersCursor, currentStyle);
        final VersesAdapter members = new VersesAdapter(this, versesCursor,
                false, currentStyle);
        HeadingAdapter headingAdapter = new HeadingAdapter(groups, members);
        verses.setAdapter(headingAdapter);
        verses.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
                HeadingAdapter adapter = (HeadingAdapter) verses.getAdapter();
                if (!adapter.isGroup(position)) {
                    Verse verse = new Verse((Cursor) adapter.getItem(position));
                    Intent intent = new Intent(FavoritesActivity.this,
                        ReadingActivity.class);
                    intent.putExtra("verse_id", verse.id);
                    FavoritesActivity.this.startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        versesCursor.requery();
        chaptersCursor.requery();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
        db = null;
    }

    @Override
    protected int[] getDisabledMenuItems() {
        return new int[] { R.id.favorites };
    }

    @Override
    protected void onResume() {
        this.onCreate(savedInstanceState);
    }
}
