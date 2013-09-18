package project;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Vector;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.io.TraceGenerator;
import org.pi4.locutil.trace.TraceEntry;

public class FingerPrintingkNN {

	public static void main(String[] args) {

		if(args.length > 0)
		{
			FingerPrintingkNN fingerPrintingkNN = new FingerPrintingkNN(Integer.parseInt(args[0]),true, 25, 5);
			System.out.println("Starting...");
			for(int n=1;n<=100;n++) {

				fingerPrintingkNN.generateTrace();
				fingerPrintingkNN.fingerprint();
				System.out.println("Accuracy experiment #"+n+" done");
			}
			System.out.println("End");
		}
		else
		{
			System.out.println("Usage : FingerPrintkNN k");
		}

	}

	public FingerPrintingkNN(int k, boolean output, int offlineSize, int onlineSize)
	{
		tg = TraceGeneratorFactory.createTraceGenerator(offlineSize, onlineSize);

		this.k = k;

		FileOutputStream f = null;

		try {
			if(output)
				f = new FileOutputStream("FingerPrinting" + k + "NN.txt", true);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		stdOut = System.out;

		this.output = output;
		if(output)
			System.setOut(fileOut = new PrintStream(f));

	}

	public Vector<Double> ErrorDistances = new Vector<Double>();

	public void generateTrace()
	{
		//Generate traces from parsed files
		tg.generate();
		//Iterate the trace generated from the offline file
		offlineTrace = tg.getOffline();	
		onlineTrace = tg.getOnline();	
	}

	private TraceGenerator tg;
	private int k;
	private PrintStream stdOut;
	private PrintStream fileOut;
	private List<TraceEntry> onlineTrace;
	private List<TraceEntry> offlineTrace;
	private boolean output;

	void fingerprint()
	{
		ErrorDistances.clear();

		if(output)
			System.setOut(fileOut);

		Vector<Pair<GeoPosition,GeoPosition>> positionPairs = NearestNeighbors.getEstimatedPositions(onlineTrace, offlineTrace, k);

		for(Pair<GeoPosition,GeoPosition> posPair : positionPairs)
		{
			if(output){
				//Print True position
				System.out.print(posPair.first.getX() + "," + posPair.first.getY() + "," + posPair.first.getZ() + "; ");
				//Print Estimated position
				System.out.println(posPair.second.getX() + "," + posPair.second.getY() + "," + posPair.second.getZ());
			}

			//Save Error Distances
			ErrorDistances.add(posPair.first.distance(posPair.second));
		}

		if(output)
			System.setOut(stdOut);
	}

}