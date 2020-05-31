package me.dylandavies.vstockapi.enums;

import java.util.Comparator;

public enum SortDirection {
	ASC {

		@Override
		public <T> int compare(T first, T second, Comparator<T> comparator) {
			return comparator.compare(first, second);
		}
	},
	DESC {
		@Override
		public <T> int compare(T first, T second, Comparator<T> comparator) {
			return comparator.compare(second, first);
		}
	};

	public <T extends Comparable<T>> int compare(T first, T second) {
		return compare(first, second, Comparable::compareTo);
	}

	public abstract <T> int compare(T first, T second, Comparator<T> comparator);
}
