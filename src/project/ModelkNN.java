package project;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;
import org.pi4.locutil.io.TraceGenerator;
import org.pi4.locutil.trace.TraceEntry;

public class ModelkNN {

	public static void main(String[] args) {

		if(args.length == 1)
		{
			ModelkNN modelkNN = new ModelkNN(Integer.parseInt(args[0]), true, 25, 5);
			System.out.println("Starting...");
			for(int n = 0; n < 100; n++)
			{
				modelkNN.generateTrace();
				modelkNN.model();
				System.out.println("Accuracy experiment #"+n+" done");
			}
			System.out.println("End");
		}
		else if(args.length == 4)
		{
			ModelkNN modelkNN = new ModelkNN(Integer.parseInt(args[0]), true, 25, 5,Double.parseDouble(args[1]),Double.parseDouble(args[2]),Double.parseDouble(args[3]));
			System.out.println("Starting...");
			for(int n = 1; n <= 100; n++)
			{
				modelkNN.generateTrace();
				modelkNN.model();
				System.out.println("Accuracy experiment #"+n+" done");
			}
			System.out.println("End");
		}
		else
		{
			System.out.println("Usage : ModelkNN k | (optional) d0 p(d0) n");
		}

	}

	public ModelkNN(int k, boolean output, int offlineSize, int onlineSize)
	{
		this(k,output,offlineSize,onlineSize, 1.0, -33.77, 3.415);
	}

	public ModelkNN(int k, boolean output, int offlineSize, int onlineSize, double d0, double p_d0, double n)
	{
		tg = TraceGeneratorFactory.createTraceGenerator(offlineSize, onlineSize);

		this.k = k;
		this.output = output;
		this.d0 = d0; // = 1
		this.p_d0 = p_d0; //-33.77;
		this.n = n; //3.415;

		FileOutputStream f = null;

		try {
			if(output)
				f = new FileOutputStream("Model"+k+"NN.txt", true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		stdOut = System.out;

		if(output)
			fileOut = new PrintStream(f);
	}
	FileOutputStream f;
	TraceGenerator tg;

	public void generateTrace()
	{
		//Generate traces from parsed files
		tg.generate();
		offlineTrace = tg.getOffline();	
		onlineTrace = tg.getOnline();	
	}

	public Vector<Double> ErrorDistances = new Vector<Double>();
	private boolean output;
	private int k;
	private PrintStream stdOut;
	private PrintStream fileOut;
	private List<TraceEntry> onlineTrace;
	private List<TraceEntry> offlineTrace;
	private double d0 = 1;
	private double p_d0 = -33.77;
	private double n = 3.415;

	void model()
	{
		ErrorDistances.clear();
		
		if(output)
			System.setOut(fileOut);

		//Replace signal strengths in the offline set with those from the model
		HashMap<MACAddress, Double> macToSignalStrengthMapping = new HashMap<MACAddress, Double>();
		for(TraceEntry offEntry : offlineTrace) 
		{
			for(MACAddress mac : offEntry.getSignalStrengthSamples().keySet())
			{
				double distanceToAP = offEntry.getGeoPosition().distance(Misc.getGeoPosOfAP(mac));

				double signalStrength =	getSignalStrengthFromDistance(distanceToAP);

				macToSignalStrengthMapping.put(mac, signalStrength);
			}

			for(MACAddress mac : macToSignalStrengthMapping.keySet())
			{
				offEntry.getSignalStrengthSamples().remove(mac);
				offEntry.getSignalStrengthSamples().put(mac,macToSignalStrengthMapping.get(mac));
			}

			macToSignalStrengthMapping.clear();
		}

		Vector<Pair<GeoPosition,GeoPosition>> positionPairs = NearestNeighbors.getEstimatedPositions(onlineTrace, offlineTrace, k);

		for(Pair<GeoPosition,GeoPosition> posPair : positionPairs)
		{
			if(output){
				//Print True position
				System.out.print(posPair.first.getX() + "," + posPair.first.getY() + "," + posPair.first.getZ() + ";");
				//Print Estimated position
				System.out.println(posPair.second.getX() + "," + posPair.second.getY() + "," + posPair.second.getZ());
			}

			//Save Error Distances
			ErrorDistances.add(posPair.first.distance(posPair.second));
		}

		if(output)
			System.setOut(stdOut);
	}

	double getSignalStrengthFromDistance(double distance)
	{
		
		double p_d = p_d0 - 10 * n * Math.log10(distance / d0);
		return p_d;
	}

}