package project;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.trace.TraceEntry;


public class NNeighbor {
	
	public static double calcEuclidDist(List<List<Double>> values) {	
		double square = 0;
		
		int i = 1;
		for(List<Double> value : values) {
			square += Math.pow(value.get(0) - value.get(1), 2);
			System.out.println(i + ": " + value.get(0) + ", " + value.get(1));
			i++;
		}
		System.out.println("calc: " + square + ", " + Math.sqrt(square));
		return Math.sqrt(square);
	}
	
	public static ArrayList<GeoPosition[]> getNearestNeighbor(List<TraceEntry> onlineTrace, List<TraceEntry> offlineTrace, int bestAmount, int num) {
		
		double[] signalStrengthsOn = new double[num];
		ArrayList<List<Double>> signalStrengths = new ArrayList<List<Double>>();
		List<MACAddress> macs;
		ArrayList<Entry> shortestDist = new ArrayList<Entry>();
		GeoPosition[] trueEstimatedPositions;
		ArrayList<GeoPosition[]> results = new ArrayList<GeoPosition[]>();
		
		System.out.println(onlineTrace.size() + ", " + offlineTrace.size());
		for(TraceEntry onEntry : onlineTrace) {
			if(onEntry.getSignalStrengthSamples().getSortedAccessPoints().size() >= num) {
				macs = onEntry.getSignalStrengthSamples().getSortedAccessPoints().subList(0, num);
				for(int i = 0; i < num; i++) {
					signalStrengthsOn[i] = onEntry.getSignalStrengthSamples().getAverageSignalStrength(macs.get(i));
				}
				
				boolean containsAllKeys = true;
				for(TraceEntry offEntry : offlineTrace) {
					/* Why is there only a method for a Set not a List? */
					containsAllKeys = true;
					for(MACAddress mac : macs) {
						if(!offEntry.getSignalStrengthSamples().containsKey(mac))
							containsAllKeys = false;
					}
					if(containsAllKeys) {
						if(offEntry.getSignalStrengthSamples().getSortedAccessPoints().size() >= num) {
							List<Double> values;
							signalStrengths = new ArrayList<List<Double>>();
							for(int i = 0; i < num; i++) {
								values = new ArrayList<Double>();
								values.add(signalStrengthsOn[i]);
								values.add(offEntry.getSignalStrengthSamples().getAverageSignalStrength(macs.get(i)));
								signalStrengths.add(values);
							}
							double euclidDistance = calcEuclidDist(signalStrengths);
//							System.out.println(euclidDistance);
							
							shortestDist.add(new Entry(offEntry, euclidDistance));
							if(euclidDistance == 1.0) {
								System.out.println("STOP!!!");
								break;
							}
							if(shortestDist.size() > num) {
								System.out.println("Dist: " + shortestDist.get(0).getEuclidDistance());
								if(shortestDist.get(0).getEuclidDistance() == 1.0 || shortestDist.get(0).getEuclidDistance() == 0.0)
									break;
							}
							DistanceComparator comp = new DistanceComparator();
							Collections.sort(shortestDist, comp);
//							System.out.println("Stop sorting");
							
						}
					}
				}
				
				GeoPosition truePos = onEntry.getGeoPosition();
				
				int estimatedX = 0;
				int estimatedY = 0;
				int estimatedZ = 0;
				for(int i = 0; i < bestAmount; i++) {
					estimatedX += shortestDist.get(i).getEntry().getGeoPosition().getX();
					estimatedY += shortestDist.get(i).getEntry().getGeoPosition().getY();
					estimatedZ += shortestDist.get(i).getEntry().getGeoPosition().getZ();
				}
				estimatedX = estimatedX/bestAmount;
				estimatedY = estimatedY/bestAmount;
				estimatedZ = estimatedZ/bestAmount;
				
				trueEstimatedPositions = new GeoPosition[] {truePos, new GeoPosition(estimatedX, estimatedY, estimatedZ)};
				System.out.println(trueEstimatedPositions[0] + ", " + trueEstimatedPositions[1]);
				results.add(trueEstimatedPositions);
			}
		}
		return results;
	}
}

class DistanceComparator implements Comparator<Entry> {

	public int compare(Entry entry1, Entry entry2) {
		if(entry1.getEuclidDistance() > entry2.getEuclidDistance())
			return 1;
		if(entry1.getEuclidDistance() < entry2.getEuclidDistance())
			return -1;
		return 0;
	}
}

class Entry {
	
	private TraceEntry entry;
	private double euclidDistance;
	
	public Entry(TraceEntry entry, double euclidDistance) {
		this.entry = entry;
		this.euclidDistance = euclidDistance;
		System.out.println("Entry: " + euclidDistance);
	}
	
	public double getEuclidDistance() {
		return euclidDistance;
	}
	
	public TraceEntry getEntry() {
		return entry;
	}
}
