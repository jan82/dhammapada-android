/*
 * 2011 April 9
 *
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends DhammapadaActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setTitle("Dhammapada: Settings");
        Spinner styles = (Spinner) findViewById(R.id.styles);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                new String[] { "Saffron", "Forrest" });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        styles.setAdapter(adapter);
    }

    @Override
    protected int[] getDisabledMenuItems() {
        return new int[] { R.id.settings };
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
}
