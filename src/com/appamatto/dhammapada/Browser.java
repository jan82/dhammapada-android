package com.appamatto.dhammapada;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

public class Browser extends Activity {
	private SQLiteDatabase db;
	private VersesAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		db = new DBHelper(this).getReadableDatabase();
		adapter = new VersesAdapter(this, db);
		ListView verses = (ListView) findViewById(R.id.verses_list);
		verses.setDividerHeight(5);
		verses.setAdapter(adapter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		adapter.close();
		adapter = null;
		db.close();
		db = null;
	}
}