package project;

public class ModelNN {

	public static void main(String[] args) {

		ModelkNN modelkNN = new ModelkNN(1,true,25,5);
		System.out.println("Starting...");
		for(int n=1;n<=100;n++) {
			
			modelkNN.generateTrace();
			modelkNN.model();
			System.out.println("Accuracy experiment #"+n+" done");
		}
		System.out.println("End");
	}
}
