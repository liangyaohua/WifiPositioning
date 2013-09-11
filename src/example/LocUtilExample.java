package example;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.pi4.locutil.io.TraceGenerator;
import org.pi4.locutil.trace.Parser;
import org.pi4.locutil.trace.TraceEntry;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.trace.macfilter.MacFilterExplizit;

/**
 * Example of how to use LocUtil
 * @author mikkelbk
 */

public class LocUtilExample {

	/**
	 * Execute example
	 * @param args
	 */
	public static void main(String[] args) {
		
		String offlinePath = "data/MU.1.5meters.offline.trace", onlinePath = "data/MU.1.5meters.online.trace";
		
		//Construct parsers
		File offlineFile = new File(offlinePath);
		Parser offlineParser = new Parser(offlineFile);
		System.out.println("Offline File: " +  offlineFile.getAbsoluteFile());
		
		File onlineFile = new File(onlinePath);
		Parser onlineParser = new Parser(onlineFile);
		System.out.println("Online File: " + onlineFile.getAbsoluteFile());

		MacFilterExplizit certifiedAP = new MacFilterExplizit();

		// Keep only known access points (yellow dots on the map ; cf. file MU.AP.positions)
		certifiedAP.add(MACAddress.parse("00:14:BF:B1:7C:54"));
		certifiedAP.add(MACAddress.parse("00:16:B6:B7:5D:8F"));
		certifiedAP.add(MACAddress.parse("00:14:BF:B1:7C:57"));
		certifiedAP.add(MACAddress.parse("00:14:BF:B1:97:8D"));
		certifiedAP.add(MACAddress.parse("00:16:B6:B7:5D:9B"));
		certifiedAP.add(MACAddress.parse("00:14:6C:62:CA:A4"));
		certifiedAP.add(MACAddress.parse("00:14:BF:3B:C7:C6"));
		certifiedAP.add(MACAddress.parse("00:14:BF:B1:97:8A"));
		certifiedAP.add(MACAddress.parse("00:14:BF:B1:97:81"));
		certifiedAP.add(MACAddress.parse("00:16:B6:B7:5D:8C"));
		certifiedAP.add(MACAddress.parse("00:11:88:28:5E:E0"));

		offlineParser.setMacFilter(certifiedAP);
		onlineParser.setMacFilter(certifiedAP);
		
		//Construct trace generator
		TraceGenerator tg;
		try {
			int offlineSize = 25;
			int onlineSize = 5;
			tg = new TraceGenerator(offlineParser, onlineParser,offlineSize,onlineSize);
			
			//Generate traces from parsed files
			tg.generate();
			
			//Iterate the trace generated from the offline file
			List<TraceEntry> offlineTrace = tg.getOffline();			
			for(TraceEntry entry: offlineTrace) {
				//Print out coordinates for the collection point and the number of signal strength samples
				System.out.println(entry.getGeoPosition().toString() + " - " + entry.getSignalStrengthSamples().size());				
			}
			
			//Iterate the trace generated from the online file
			List<TraceEntry> onlineTrace = tg.getOnline();			
			for(TraceEntry entry: onlineTrace) {
				//Print out coordinates for the collection point and the number of signal strength samples
				System.out.println(entry.getGeoPosition().toString() + " - " + entry.getSignalStrengthSamples().size());
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}