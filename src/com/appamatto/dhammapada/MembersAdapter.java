/*
 * 2011 February 14
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.widget.ListAdapter;

public interface MembersAdapter extends ListAdapter {
    public int getPosition(int groupPosition, int memberPosition,
            int unresolvedPosition);
}
