package project;

import java.util.Comparator;

class DistanceComparator implements Comparator<EntryWrapper> {

	public int compare(EntryWrapper o1, EntryWrapper o2) {
		if ((o1 == null) || (o2 == null))
			throw new IllegalArgumentException("The arguments cannot be null.");

		return Double.compare(o2.Distance, o1.Distance);	
	}
}