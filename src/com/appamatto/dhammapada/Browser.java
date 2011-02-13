package com.appamatto.dhammapada;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

public class Browser extends Activity {
	private SQLiteDatabase db;
	private VersesAdapter adapter;
	private ListView verses;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		db = new DBHelper(this).getReadableDatabase();
		adapter = new VersesAdapter(this, db);
		verses = (ListView) findViewById(R.id.verses_list);
		verses.setDividerHeight(5);
		verses.setAdapter(adapter);

		SharedPreferences prefs = getSharedPreferences("Browser", MODE_PRIVATE);
		int position = prefs.getInt("progress", 0);
		verses.setSelection(position);
	}

	@Override
	public void onDestroy() {
		int position = verses.getFirstVisiblePosition();
		SharedPreferences prefs = getSharedPreferences("Browser", MODE_PRIVATE);
		prefs.edit().putInt("progress", position).commit();
		super.onDestroy();
		adapter.close();
		adapter = null;
		db.close();
		db = null;
	}
}