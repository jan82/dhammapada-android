package com.appamatto.dhammapada;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class VersesAdapter extends CursorAdapter implements MembersAdapter {
	public VersesAdapter(Context context, Cursor cursor) {
		super(context, cursor);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return new FullVerseView(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		VerseView verseView = (VerseView) view;
		verseView.setVerse(new Verse(cursor));
	}

	@Override
	public int getPosition(int groupPosition, int memberPosition,
			int unresolvedPosition) {
		return unresolvedPosition - groupPosition - 1;
	}
}
