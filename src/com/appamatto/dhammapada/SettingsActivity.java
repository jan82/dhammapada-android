/*
 * 2011 April 9
 *
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class SettingsActivity extends DhammapadaActivity {
    private SQLiteDatabase db;
    private Spinner styles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setTitle("Dhammapada: Settings");
        styles = (Spinner) findViewById(R.id.styles);
        db = new DBHelper(this).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, name FROM styles", null);
        startManagingCursor(cursor);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_spinner_item,
                cursor,
                new String[] { "name" },
                new int[] { android.R.id.text1 });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        styles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences prefs = getSharedPreferences("Style", MODE_PRIVATE);
                prefs.edit().putLong("id", id).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        styles.setAdapter(adapter);
    }

    @Override
    protected int[] getDisabledMenuItems() {
        return new int[] { R.id.settings };
    }

    public void editClicked(View v) {
        Intent intent = new Intent(this, StyleEditor.class);
        intent.putExtra("id", styles.getSelectedItemId());
        startActivity(intent);
    }

    public void aboutClicked(View v) {
        /*
        Intent intent = new Intent(this, AboutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIIVITY_NO_HISTORY);
        startActivity(intent);
        */
    }

    public void feedbackClicked(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL,
                new String[] { "dhammapada@appamatto.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Dhammapada Android feedback");
        startActivity(intent);
    }

    public void legalClicked(View v) {
        Intent intent = new Intent(this, LegalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        db = null;
    }
}
