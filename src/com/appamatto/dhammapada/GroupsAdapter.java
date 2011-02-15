/*
 * 2011 February 14
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.widget.ListAdapter;

public interface GroupsAdapter extends ListAdapter {
	int getGroupSize(int position);
}
