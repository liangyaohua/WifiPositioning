package project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.io.TraceGenerator;
import org.pi4.locutil.trace.Parser;
import org.pi4.locutil.trace.SignalStrengthSamples;
import org.pi4.locutil.trace.TraceEntry;

public class NearestNeighbor {

	private TraceGenerator tg;
	private List<TraceEntry> ont;
	private List<TraceEntry> offt;

	private double calcEuclidianDistance() {

		return 2.0;
	}

	private void generateTraceGenerator() {
		String offlinePath = "data/MU.1.5meters.offline.trace", onlinePath = "data/MU.1.5meters.online.trace";

		// Construct parsers
		File offlineFile = new File(offlinePath);
		Parser offlineParser = new Parser(offlineFile);
		System.out.println("Offline File: " + offlineFile.getAbsoluteFile());

		File onlineFile = new File(onlinePath);
		Parser onlineParser = new Parser(onlineFile);
		System.out.println("Online File: " + onlineFile.getAbsoluteFile());

		// Construct trace generator

		try {
			int offlineSize = 25;
			int onlineSize = 5;
			tg = new TraceGenerator(offlineParser, onlineParser, offlineSize,
					onlineSize);

			// Generate traces from parsed files
			tg.generate();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<TraceEntry> getOffline() {
		return tg.getOffline();
	}
	
	public List<TraceEntry> getOnline() {
		return tg.getOnline();
	}

//	private List<TraceEntry> getOffline() {
//		generateTraceGenerator();
//		List<TraceEntry> offlineTrace = tg.getOffline();
//		int k = 1;
//		
//		for(int i = 0; i < 10; i++) {
//			List<MACAddress> macs = offlineTrace.get(i).getSignalStrengthSamples().getSortedAccessPoints();
//		}
//		
////		for (TraceEntry entry : offlineTrace) {
////			SignalStrengthSamples sss = entry.getSignalStrengthSamples();
////			System.out.println("Entry " + k + ":");
////			List<MACAddress> macs = sss.getSortedAccessPoints();
////			System.out.println(macs.size());
////			System.out.println(entry.getSignalStrengthSamples().getAverageSignalStrength(macs.get(3)));
////			k++;
////		}
//	}

	static class Tester {
		public static void main(String[] args) {
			NearestNeighbor nn = new NearestNeighbor();
			nn.generateTraceGenerator();
			ArrayList<GeoPosition[]> result = NNeighbor.getNearestNeighbor(nn.getOnline(), nn.getOffline(), 3, 3);
			System.out.println("Start");
			System.out.println(result);
			System.out.println("End");
		}
	}
}
