package project;

import java.io.*;
import java.util.*;

import org.pi4.locutil.Statistics;

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
			System.out.println("Starting...");
			for(int n =0;n<100;n++) {
				
				if(n % 5 == 0)
				{
					System.setOut(stdOut);
					System.out.println("Accuracy experiment #" + n);
				}
				
				fingerPrintingkNN.generateTrace();
				fingerPrintingkNN.fingerprint();
				medians.add(Statistics.median(fingerPrintingkNN.ErrorDistances));
			}
			System.out.println("End");
			System.setOut(fileOut);
			System.out.println(k + " " + Statistics.avg(medians));
		}
	}

}
