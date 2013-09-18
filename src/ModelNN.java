public class ModelNN {

	public static void main(String[] args) {

		ModelkNN modelkNN = new ModelkNN(1,true,25,5);
		for(int n =0;n<100;n++) {
			
			modelkNN.generateTrace();
			modelkNN.model();
			System.out.println(n+1);
		}

	}
}
