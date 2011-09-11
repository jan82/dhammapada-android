/*
 * 2011 September 2
 * 
 * The author disclaims copyright to this source code.
 */

package com.appamatto.dhammapada;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ChaptersActivity extends DhammapadaActivity {
    private SQLiteDatabase db;
    private ListView chapters;
    private Bundle savedInstanceState;
    Cursor chaptersCursor;
    Cursor versesCursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.main);
        setTitle("Dhammapada: Chapters");

        db = new DBHelper(this).getWritableDatabase();
        SharedPreferences stylepref = getSharedPreferences("Style", MODE_PRIVATE);
        long id = stylepref.getLong("id", 1);

        Style currentStyle = Style.get(db, id);

        chaptersCursor = db
                .rawQuery(
                        "SELECT chapters._id as _id, chapters.title as title, count(*) as members "
                                +
                                "FROM verses, chapters " +
                                "WHERE verses.chapter_id = chapters._id " +
                                "GROUP BY verses.chapter_id ORDER BY chapters._id",
                        null);
        chapters = (ListView) findViewById(R.id.verses_list);
        GroupsAdapter groups = new ChaptersAdapter(this, chaptersCursor, currentStyle);
        chapters.setAdapter(groups);

        chapters.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                GroupsAdapter adapter = (GroupsAdapter) chapters.getAdapter();
                Chapter chapter = new Chapter((Cursor) adapter.getItem(position));
                Intent intent = new Intent(ChaptersActivity.this,
                        ReadingActivity.class);
                intent.putExtra("chapter_id", chapter.id);
                ChaptersActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
        return new int[] {
            R.id.chapters
        };
    }

    @Override
    protected void onResume() {
        this.onCreate(savedInstanceState);
    }

}
