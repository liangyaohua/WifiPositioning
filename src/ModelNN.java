public class ModelNN {

	public static void main(String[] args) {

		ModelkNN modelkNN = new ModelkNN(1,true,25,5);
		for(int n=1;n<=100;n++) {
			
			modelkNN.generateTrace();
			modelkNN.model();
			System.out.println("Accuracy experiment #"+n+" done");
		}

	}
}
