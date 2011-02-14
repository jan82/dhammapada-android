package com.appamatto.dhammapada;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Browser extends Activity {
	private SQLiteDatabase db;
	private ListView verses;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		db = new DBHelper(this).getReadableDatabase();
		Cursor versesCursor = db.rawQuery(
				"SELECT _id, chapter_id, first, last, text, bookmarked "
						+ "FROM verses ORDER BY chapter_id", null);
		Cursor chaptersCursor = db
				.rawQuery(
						"SELECT chapters._id as _id, chapters.title as title, count(*) as members "
								+ "FROM verses, chapters WHERE verses.chapter_id = chapters._id "
								+ "GROUP BY verses.chapter_id ORDER BY chapters._id",
						null);
		verses = (ListView) findViewById(R.id.verses_list);
		GroupsAdapter groups = new ChaptersAdapter(this, chaptersCursor);
		final VersesAdapter members = new VersesAdapter(this, versesCursor);
		HeadingAdapter headingAdapter = new HeadingAdapter(groups, members);
		verses.setAdapter(headingAdapter);

		SharedPreferences prefs = getSharedPreferences("Browser", MODE_PRIVATE);
		int position = prefs.getInt("progress", 0);
		verses.setSelection(position);
		verses.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HeadingAdapter adapter = (HeadingAdapter) verses.getAdapter();
				if (!adapter.isGroup(position)) {
					Verse verse = new Verse((Cursor) adapter.getItem(position));
					new Verse(verse.id, verse.chapter, verse.range, verse.text,
							!verse.bookmarked).insert(db);
					members.getCursor().requery();
				}
			}
		});
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