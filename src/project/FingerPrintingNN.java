package project;

public class FingerPrintingNN {

	public static void main(String[] args) {

		FingerPrintingkNN fingerPrintingkNN = new FingerPrintingkNN(1,true,25,5);
		System.out.println("Starting...");
		for(int n = 0; n < 100; n++) {
			
			fingerPrintingkNN.generateTrace();
			fingerPrintingkNN.fingerprint();
			System.out.println("Accuracy experiment #"+n+" done");
		}
		System.out.println("End");
	}
}
