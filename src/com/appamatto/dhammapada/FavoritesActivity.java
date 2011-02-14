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

public class FavoritesActivity extends Activity {
	private SQLiteDatabase db;
	private ListView verses;
	Cursor versesCursor;
	Cursor chaptersCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		db = new DBHelper(this).getReadableDatabase();
		versesCursor = db
				.rawQuery(
						"SELECT * FROM verses WHERE bookmarked = 1 ORDER BY chapter_id",
						null);
		chaptersCursor = db
				.rawQuery(
						"SELECT chapters._id as _id, chapters.title as title, count(*) as members "
								+ "FROM verses, chapters "
								+ "WHERE verses.chapter_id = chapters._id AND verses.bookmarked = 1 "
								+ "GROUP BY verses.chapter_id ORDER BY chapters._id",
						null);
		verses = (ListView) findViewById(R.id.verses_list);
		GroupsAdapter groups = new ChaptersAdapter(this, chaptersCursor);
		final VersesAdapter members = new VersesAdapter(this, versesCursor,
				false);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.favorites, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.reader) {
			finish();
		}
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		db.close();
		db = null;
	}
}
