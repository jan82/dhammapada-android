/*
 * 2011 February 14
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import java.util.ArrayList;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class HeadingAdapter implements ListAdapter {
	private GroupsAdapter groups;
	private MembersAdapter members;

	private final DataSetObservable dataSetObservable = new DataSetObservable();
	private final InferiorDataSetObserver inferiorDataSetObserver = new InferiorDataSetObserver();

	private ArrayList<Integer> partitions;

	public HeadingAdapter(GroupsAdapter groups, MembersAdapter members) {
		this.groups = groups;
		this.members = members;
		groups.registerDataSetObserver(inferiorDataSetObserver);
		members.registerDataSetObserver(inferiorDataSetObserver);
		initPartitions();
	}

	private void initPartitions() {
		partitions = new ArrayList<Integer>();
		partitions.add(0);
	}

	private ResolvedPosition resolvePosition(int groupPosition, int position) {
		int offset = position - partitions.get(groupPosition);
		if (offset == 0) {
			return new ResolvedPosition(groupPosition, null);
		}
		return new ResolvedPosition(groupPosition, offset - 1);
	}

	public Integer getFlatPosition(int groupPosition, int memberPosition) {
		if (groupPosition >= groups.getCount()) {
			return null;
		}
		for (int i = partitions.size() - 1; i < groupPosition; i++) {
			int size = groups.getGroupSize(i) + partitions.get(i) + 1;
			partitions.add(size);
		}
		return partitions.get(groupPosition) + memberPosition + 1;
	}

	private int findPartition(int position) {
		int i = 0;
		int j = partitions.size();
		while (i != j) {
			int k = (i + j) / 2;
			int size = partitions.get(k);
			if (size == position) {
				return k;
			}
			if (size > position) {
				j = k;
			} else {
				i = k + 1;
			}
		}
		return i - 1;
	}

	private int addPartitions(int position) {
		for (int i = partitions.size() - 1; i < groups.getCount(); i++) {
			int size = groups.getGroupSize(i) + partitions.get(i) + 1;
			partitions.add(size);
			if (size > position) {
				return i;
			}
		}
		throw new RuntimeException("position " + position
				+ " was not located in a group.");
	}

	private ResolvedPosition resolvePosition(int position) {
		int groupPosition;
		if (partitions.get(partitions.size() - 1) > position) {
			/* We have this position in our partition table. Do a binary search. */
			groupPosition = findPartition(position);
		} else {
			/* We don't have this position. Linear search until we find it. */
			groupPosition = addPartitions(position);
		}
		return resolvePosition(groupPosition, position);
	}

	public boolean isGroup(int position) {
		ResolvedPosition resolved = resolvePosition(position);
		return resolved.isGroup();
	}

	@Override
	public int getCount() {
		return groups.getCount() + members.getCount();
	}

	@Override
	public Object getItem(int position) {
		ResolvedPosition resolved = resolvePosition(position);
		if (resolved.isGroup()) {
			return groups.getItem(resolved.groupPosition);
		}
		position = members.getPosition(resolved.groupPosition,
				resolved.memberPosition, position);
		return members.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		ResolvedPosition resolved = resolvePosition(position);
		if (resolved.isGroup()) {
			return groups.getItemViewType(resolved.groupPosition);
		}
		position = members.getPosition(resolved.groupPosition,
				resolved.memberPosition, position);
		return members.getItemViewType(position) + groups.getViewTypeCount();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ResolvedPosition resolved = resolvePosition(position);
		if (resolved.isGroup()) {
			return groups.getView(resolved.groupPosition, convertView, parent);
		}
		position = members.getPosition(resolved.groupPosition,
				resolved.memberPosition, position);
		return members.getView(position, convertView, parent);
	}

	@Override
	public int getViewTypeCount() {
		return groups.getViewTypeCount() + members.getViewTypeCount();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return groups.isEmpty() && members.isEmpty();
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		dataSetObservable.registerObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		dataSetObservable.unregisterObserver(observer);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return groups.areAllItemsEnabled() && members.areAllItemsEnabled();
	}

	@Override
	public boolean isEnabled(int position) {
		ResolvedPosition resolved = resolvePosition(position);
		if (resolved.isGroup()) {
			return groups.isEnabled(resolved.groupPosition);
		}
		position = members.getPosition(resolved.groupPosition,
				resolved.memberPosition, position);
		return members.isEnabled(position);
	}

	public void notifyDataSetChanged() {
		initPartitions();
		dataSetObservable.notifyChanged();
	}

	public void notifyDataSetInvalidated() {
		initPartitions();
		dataSetObservable.notifyInvalidated();
	}

	private class InferiorDataSetObserver extends DataSetObserver {
		public void onChanged() {
			notifyDataSetChanged();
		}

		public void onInvalidated() {
			notifyDataSetInvalidated();
		}
	}

	private static class ResolvedPosition {
		public final int groupPosition;
		public final Integer memberPosition;

		public ResolvedPosition(int groupPosition, Integer memberPosition) {
			this.groupPosition = groupPosition;
			this.memberPosition = memberPosition;
		}

		public boolean isGroup() {
			return memberPosition == null;
		}
	}
}
