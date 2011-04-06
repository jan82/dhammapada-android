/*
 * 2011 April 5
 *
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;

public abstract class DhammapadaActivity extends Activity {
    protected int[] getDisabledMenuItems() {
        return new int[0];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        for (final int id : getDisabledMenuItems()) {
            menu.removeItem(id);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorites: {
                Intent intent = new Intent(this, FavoritesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            }
            case R.id.legal: {
                Intent intent = new Intent(this, LegalActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            }
            case R.id.feedback: {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[] { "dhammapada@appamatto.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Dhammapada Android feedback");
                startActivity(intent);
                break;
            }
            case R.id.settings: {
                Intent intent = new Intent(this, StyleEditor.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            }
            case R.id.reader: {
                Intent intent = new Intent(this, ReadingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
