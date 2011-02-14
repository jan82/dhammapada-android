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

public class ReadingActivity extends Activity {
	private SQLiteDatabase db;
	private ListView verses;
	Cursor versesCursor;
	Cursor chaptersCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle("Dhammapada: Reading");

		db = new DBHelper(this).getReadableDatabase();
		versesCursor = db.rawQuery("SELECT * FROM verses ORDER BY chapter_id",
				null);
		chaptersCursor = db
				.rawQuery(
						"SELECT chapters._id as _id, chapters.title as title, count(*) as members "
								+ "FROM verses, chapters WHERE verses.chapter_id = chapters._id "
								+ "GROUP BY verses.chapter_id ORDER BY chapters._id",
						null);
		verses = (ListView) findViewById(R.id.verses_list);
		GroupsAdapter groups = new ChaptersAdapter(this, chaptersCursor);
		final VersesAdapter members = new VersesAdapter(this, versesCursor,
				true);
		HeadingAdapter headingAdapter = new HeadingAdapter(groups, members);
		verses.setAdapter(headingAdapter);

		long verseId = getIntent().getLongExtra("verse_id", -1);
		if (verseId == -1) {
			SharedPreferences prefs = getSharedPreferences("Browser",
					MODE_PRIVATE);
			int position = prefs.getInt("progress", 0);
			verses.setSelection(position);
		} else {
			Cursor cursor = db.rawQuery("SELECT * FROM verses WHERE _id = "
					+ verseId, null);
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
					new Verse(verse.id, verse.chapter, verse.chapterOffset,
							verse.range, verse.text, !verse.bookmarked)
							.insert(db);
					members.getCursor().requery();
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
		new MenuInflater(this).inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.favorites) {
			Intent intent = new Intent(this, FavoritesActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		if (item.getItemId() == R.id.legal) {
			Intent intent = new Intent(this, LegalActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		}
		if (item.getItemId() == R.id.feedback) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("plain/text");
			intent.putExtra(Intent.EXTRA_EMAIL,
					new String[] { "dhammapada@appamatto.com" });
			intent.putExtra(Intent.EXTRA_SUBJECT, "Dhammapada Android feedback");
			startActivity(intent);
		}
		return true;
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