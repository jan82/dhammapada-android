/*
 * 2011 September 2
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhp;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class VersesAdapter extends CursorAdapter implements MembersAdapter {
    private boolean fullView;
    private Style currentStyle;

    public VersesAdapter(Context context, Cursor cursor, boolean fullView, Style currentStyle) {
        super(context, cursor);
        this.fullView = fullView;
        this.currentStyle = currentStyle;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        if (fullView) {
            return new FullVerseView(context);
        }
        return new BriefVerseView(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        VerseView verseView = (VerseView) view;

        verseView.setVerse(new Verse(cursor), currentStyle);
    }

    @Override
    public int getPosition(int groupPosition, int memberPosition,
            int unresolvedPosition) {
        return unresolvedPosition - groupPosition - 1;
    }
}
