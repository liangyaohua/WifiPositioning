package project;

import java.io.File;
import java.io.IOException;

import org.pi4.locutil.MACAddress;
import org.pi4.locutil.io.TraceGenerator;
import org.pi4.locutil.trace.Parser;
import org.pi4.locutil.trace.macfilter.MacFilterExplizit;

public class TraceGeneratorFactory {

	public static TraceGenerator createTraceGenerator(int offlineSize, int onlineSize)
	{
		TraceGenerator tg = null;
		String offlinePath = "src/data/MU.1.5meters.offline.trace", onlinePath = "src/data/MU.1.5meters.online.trace";

		//Construct parsers
		File offlineFile = new File(offlinePath);
		Parser offlineParser = new Parser(offlineFile);

		File onlineFile = new File(onlinePath);
		Parser onlineParser = new Parser(onlineFile);

		MacFilterExplizit knownMacAddresses = new MacFilterExplizit();

		knownMacAddresses.add(MACAddress.parse("00:14:BF:B1:7C:54"));
		knownMacAddresses.add(MACAddress.parse("00:16:B6:B7:5D:8F"));
		knownMacAddresses.add(MACAddress.parse("00:14:BF:B1:7C:57"));
		knownMacAddresses.add(MACAddress.parse("00:14:BF:B1:97:8D"));
		knownMacAddresses.add(MACAddress.parse("00:16:B6:B7:5D:9B"));
		knownMacAddresses.add(MACAddress.parse("00:14:6C:62:CA:A4"));
		knownMacAddresses.add(MACAddress.parse("00:14:BF:3B:C7:C6"));
		knownMacAddresses.add(MACAddress.parse("00:14:BF:B1:97:8A"));
		knownMacAddresses.add(MACAddress.parse("00:14:BF:B1:97:81"));
		knownMacAddresses.add(MACAddress.parse("00:16:B6:B7:5D:8C"));
		knownMacAddresses.add(MACAddress.parse("00:11:88:28:5E:E0"));

		//Ensure only known APs are used.
		offlineParser.setMacFilter(knownMacAddresses);
		onlineParser.setMacFilter(knownMacAddresses);

		//Construct trace generator
		try {
			tg = new TraceGenerator(offlineParser, onlineParser,offlineSize,onlineSize);


		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tg;
	}
}
