package com.appamatto.dhammapada;

import android.widget.ListAdapter;

public interface MembersAdapter extends ListAdapter {
	public int getPosition(int groupPosition, int memberPosition,
			int unresolvedPosition);
}
