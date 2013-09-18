package project;

import java.io.*;
import java.util.*;

import org.pi4.locutil.Statistics;

public class MedianAccuracyMk {
	
	public static void main(String[] args) {

		FileOutputStream f = null;
		try {
				f = new FileOutputStream("MedianAccuracyMk.txt", false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintStream stdOut = System.out; 
		PrintStream fileOut = new PrintStream(f);
		for(int k = 1; k <= 5; k++)
		{
			System.setOut(stdOut);
			System.out.println("k = " + k);
			ModelkNN modelkNN = new ModelkNN(k,false,25,5);
			Vector<Double> medians = new Vector<Double>();
			System.out.println("Starting...");
			for(int n =0;n<100;n++) {
				
				if(n % 5 == 0)
				{
					System.setOut(stdOut);
					System.out.println("Accuracy experiment #" + n);
				}
				
				modelkNN.generateTrace();
				modelkNN.model();
				medians.add(Statistics.median(modelkNN.ErrorDistances));
			}
			System.out.println("End");
			System.setOut(fileOut);
			System.out.println(k + " " + Statistics.avg(medians));
		}
	}

}
