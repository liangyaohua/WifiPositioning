import java.util.*;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.Statistics;
import org.pi4.locutil.trace.TraceEntry;


public class NearestNeighbors {

	//Returns pairs of true and estimated positions based on k nearest neighbors
	public static Vector<Pair<GeoPosition,GeoPosition>> getEstimatedPositions(List<TraceEntry> onlineTrace, List<TraceEntry> offlineTrace, int k)
	{
		DistanceComparator comparator = new DistanceComparator();
		Vector<Pair<GeoPosition,GeoPosition>> positionPairs = new Vector<Pair<GeoPosition,GeoPosition>>();
		
		for(TraceEntry entry: onlineTrace) {

			double ss1, ss1mark, ss2, ss2mark, ss3, ss3mark;
			
			LinkedList<EntryWrapper> shortestDistanceEntries = new LinkedList<EntryWrapper>();
			
			if(entry.getSignalStrengthSamples().getSortedAccessPoints().size() > 2)
			{
				List<MACAddress> arr = entry.getSignalStrengthSamples().getSortedAccessPoints().subList(0, 3);
				ss1 = entry.getSignalStrengthSamples().getAverageSignalStrength(arr.get(0));
				ss2 = entry.getSignalStrengthSamples().getAverageSignalStrength(arr.get(1));
				ss3 = entry.getSignalStrengthSamples().getAverageSignalStrength(arr.get(2));

				for(TraceEntry offEntry: offlineTrace) {

					if(offEntry.getSignalStrengthSamples().keySet().containsAll(arr))
					{
						ss1mark = offEntry.getSignalStrengthSamples().getAverageSignalStrength(arr.get(0));
						ss2mark = offEntry.getSignalStrengthSamples().getAverageSignalStrength(arr.get(1));
						ss3mark = offEntry.getSignalStrengthSamples().getAverageSignalStrength(arr.get(2));

						double distanceInSignalSpace = Misc.getEuclidianDistance(ss1, ss1mark, ss2, ss2mark, ss3, ss3mark);

						if(shortestDistanceEntries.size() < k)
							shortestDistanceEntries.add(new EntryWrapper(offEntry, distanceInSignalSpace));
						else if(shortestDistanceEntries.getFirst().Distance > distanceInSignalSpace)
							shortestDistanceEntries.set(0, new EntryWrapper(offEntry, distanceInSignalSpace));

						Collections.sort(shortestDistanceEntries,comparator);
					}
				}
				//True pos
				GeoPosition truePos = new GeoPosition(entry.getGeoPosition().getX() , entry.getGeoPosition().getY(), entry.getGeoPosition().getZ());

				double aproxX = 0, aproxY = 0, aproxZ = 0;

				for(int i = 0; i < k; i++)
				{
					aproxX += shortestDistanceEntries.get(i).Entry.getGeoPosition().getX();
					aproxY += shortestDistanceEntries.get(i).Entry.getGeoPosition().getY();
					aproxZ += shortestDistanceEntries.get(i).Entry.getGeoPosition().getZ();
				}
				
				aproxX /= k;
				aproxY /= k;
				aproxZ /= k;

				//Estimated Position
				GeoPosition estimatedPos = new GeoPosition(aproxX, aproxY, aproxZ);
				
				positionPairs.add(new Pair<GeoPosition,GeoPosition>(truePos,estimatedPos));
			}
		}
		
		return positionPairs;
	}
}
