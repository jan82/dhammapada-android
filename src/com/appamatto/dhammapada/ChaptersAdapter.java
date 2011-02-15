/*
 * 2011 February 14
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class ChaptersAdapter extends CursorAdapter implements GroupsAdapter {
	public ChaptersAdapter(Context context, Cursor cursor) {
		super(context, cursor);
	}

	@Override
	public int getGroupSize(int position) {
		Cursor cursor = getCursor();
		cursor.moveToPosition(position);
		ContentValues cvs = new ContentValues();
		DatabaseUtils.cursorRowToContentValues(cursor, cvs);
		return cvs.getAsInteger("members");
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		((ChapterHeading) view).setChapter(new Chapter(cursor));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return new ChapterHeading(context);
	}
}
