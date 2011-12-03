/*
 * 2011 September 2
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhp;

import android.widget.ListAdapter;

public interface GroupsAdapter extends ListAdapter {
    int getGroupSize(int position);
}
