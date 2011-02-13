package com.appamatto.dhammapada;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;

public class ReadingAdapter extends CursorTreeAdapter {
	private SQLiteDatabase db;

	public ReadingAdapter(Context context, SQLiteDatabase db) {
		super(db.rawQuery("SELECT _id, title FROM chapters", null), context);
		this.db = db;
	}

	@Override
	protected View newGroupView(Context context, Cursor cursor,
			boolean isExpanded, ViewGroup parent) {
		return new ChapterHeading(context);
	}

	@Override
	protected void bindGroupView(View view, Context context, Cursor cursor,
			boolean isExpanded) {
		ChapterHeading heading = (ChapterHeading) view;
		heading.setChapter(new Chapter(cursor));
	}

	@Override
	protected Cursor getChildrenCursor(Cursor cursor) {
		Cursor c = db
				.rawQuery(
						"SELECT _id, chapter_id, first, last, text, bookmarked FROM verses WHERE chapter_id = "
								+ new Chapter(cursor).id, null);
		return c;
	}

	@Override
	protected View newChildView(Context context, Cursor cursor,
			boolean isLastChild, ViewGroup parent) {
		return new FullVerseView(context);
	}

	@Override
	public void bindChildView(View view, Context context, Cursor cursor,
			boolean isLastChild) {
		VerseView verseView = (VerseView) view;
		verseView.setVerse(new Verse(cursor));
	}
}
