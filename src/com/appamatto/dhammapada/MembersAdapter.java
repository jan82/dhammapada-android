/*
 * 2011 September 2
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.widget.ListAdapter;

public interface MembersAdapter extends ListAdapter {
    public int getPosition(int groupPosition, int memberPosition,
            int unresolvedPosition);
}
