import java.io.*;
import java.util.*;

import org.pi4.locutil.Statistics;

public class MedianAccuracyMos {
	
	public static void main(String[] args) {

		FileOutputStream f = null;
		try {
				f = new FileOutputStream("MedianAccuracyMos.txt", false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintStream stdOut = System.out; 
		PrintStream fileOut = new PrintStream(f);
		for(int onlineSize = 1; onlineSize <= 10; onlineSize++)
		{
			System.setOut(stdOut);
			System.out.println("onlineSize = " + onlineSize);
			ModelkNN modelkNN = new ModelkNN(3,false,25,onlineSize);
			Vector<Double> medians = new Vector<Double>();
			for(int n =0;n<100;n++) {
				
				if(n % 5 == 0)
				{
					System.setOut(stdOut);
					System.out.println("run number " + n);
				}
				
				modelkNN.generateTrace();
				modelkNN.model();
				medians.add(Statistics.median(modelkNN.ErrorDistances));
			}
			
			System.setOut(fileOut);
			System.out.println(onlineSize + " " + Statistics.avg(medians));
		}
	}

}
