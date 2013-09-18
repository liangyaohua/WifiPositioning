package project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.io.TraceGenerator;
import org.pi4.locutil.trace.Parser;
import org.pi4.locutil.trace.TraceEntry;
import org.pi4.locutil.trace.macfilter.MacFilterExplizit;

public class SignalStrength {

	public static void main(String[] args) {

		Vector<MACAddress> knownMacAddresses = new Vector<MACAddress>();
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
		
		for(MACAddress mac : knownMacAddresses)
		{
			SignalStrength signalStrength = new SignalStrength(mac, 25, 5);
			signalStrength.generateTrace();
			signalStrength.measureDistance();
		}

	}
	
	public SignalStrength(MACAddress mac, int offlineSize, int onlineSize)
	{
		tg = TraceGeneratorFactory.createTraceGenerator(offlineSize, onlineSize);
		
		FileOutputStream f = null;
		
		try {
			f = new FileOutputStream("SigalStrength" + mac.toString().replaceAll(":", ".") + ".txt", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.mac = mac;
		stdOut = System.out;
		System.setOut(fileOut = new PrintStream(f));
	}
	
	public void generateTrace()
	{
		//Generate traces from parsed files
		tg.generate();
		//Iterate the trace generated from the offline file
		offlineTrace = tg.getOffline();	
	}

	private TraceGenerator tg;
	private PrintStream stdOut;
	private PrintStream fileOut;
	private List<TraceEntry> offlineTrace;
	private MACAddress mac;

	void measureDistance()
	{
		System.setOut(fileOut);

		for(TraceEntry entry: offlineTrace)
		{
			if(entry.getSignalStrengthSamples().containsKey(mac))
			{
				double distance = Misc.getGeoPosOfAP(mac).distance(entry.getGeoPosition());
				
				double signalStrength = entry.getSignalStrengthSamples().getAverageSignalStrength(mac);
				
				System.out.println(Misc.roundTwoDecimals(distance) + " " + signalStrength);
			}
			
		}

		System.setOut(stdOut);

	}

	
}
