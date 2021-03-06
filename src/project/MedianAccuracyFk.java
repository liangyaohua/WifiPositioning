package project;

import java.io.*;
import java.util.*;

import org.pi4.locutil.Statistics;

// Median accuracy for FingerPrintingkNN k = 1-5
public class MedianAccuracyFk {
	
	public static void main(String[] args) {

		FileOutputStream f = null;
		try {
				f = new FileOutputStream("MedianAccuracyFk.txt", false);
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
			FingerPrintingkNN fingerPrintingkNN = new FingerPrintingkNN(k,false,25,5);
			Vector<Double> medians = new Vector<Double>();
			
			for(int n = 0; n < 100; n++) {
				
				if(n % 10 == 0)
				{
					System.setOut(stdOut);
					System.out.println("Accuracy experiment #" + n);
				}
				
				fingerPrintingkNN.generateTrace();
				fingerPrintingkNN.fingerprint();
				medians.add(Statistics.median(fingerPrintingkNN.ErrorDistances));
			}
			
			System.setOut(fileOut);
			System.out.println(k + " " + Statistics.avg(medians));
		}
	}

}
