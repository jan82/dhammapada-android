package com.appamatto.dhammapada;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class VersesAdapter extends BaseAdapter {
	private Cursor cursor;
	private Context context;
	private SQLiteDatabase db;

	public VersesAdapter(Context context, SQLiteDatabase db) {
		this.context = context;
		this.db = db;
		loadCursor();
	}

	private void loadCursor() {
		close();
		cursor = db.rawQuery(
				"SELECT _id, first, last, text, bookmarked FROM verses", null);
	}

	public void close() {
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
	}

	@Override
	public int getCount() {
		return cursor.getCount();
	}

	@Override
	public Object getItem(int position) {
		cursor.moveToPosition(position);
		return new Verse(cursor);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final VerseView view;
		final Verse verse = (Verse) getItem(position);
		if (convertView == null) {
			view = new VerseView(context);
		} else {
			view = (VerseView) convertView;
		}
		view.setVerse(verse);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Verse(verse.id, verse.range, verse.text, !verse.bookmarked)
						.insert(db);
				loadCursor();
				notifyDataSetChanged();
			}
		});
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}
