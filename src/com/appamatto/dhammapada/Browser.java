package com.appamatto.dhammapada;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class Browser extends Activity {
	private SQLiteDatabase db;
	private ExpandableListView verses;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		db = new DBHelper(this).getReadableDatabase();
		ExpandableListAdapter adapter = new ReadingAdapter(this, db);
		verses = (ExpandableListView) findViewById(R.id.verses_list);
		verses.setAdapter(adapter);

		SharedPreferences prefs = getSharedPreferences("Browser", MODE_PRIVATE);
		long packed = prefs.getLong("reading_position", 0);
		if (ExpandableListView.getPackedPositionType(packed) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
			verses.setSelectedGroup(ExpandableListView
					.getPackedPositionGroup(packed));
		} else {
			verses.setSelectedChild(
					ExpandableListView.getPackedPositionGroup(packed),
					ExpandableListView.getPackedPositionChild(packed), true);
		}
		verses.setGroupIndicator(null);
		verses.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View view,
					int groupPosition, int childPosition, long id) {
				Cursor cursor = (Cursor) parent.getExpandableListAdapter()
						.getChild(groupPosition, childPosition);
				Verse verse = new Verse(cursor);
				new Verse(verse.id, verse.chapter, verse.range, verse.text,
						!verse.bookmarked).insert(db);
				cursor.requery();
				return true;
			}
		});
	}

	@Override
	public void onDestroy() {
		int position = verses.getFirstVisiblePosition();
		SharedPreferences prefs = getSharedPreferences("Browser", MODE_PRIVATE);
		prefs.edit()
				.putLong("reading_position",
						verses.getExpandableListPosition(position)).commit();
		super.onDestroy();
		db.close();
		db = null;
	}
}