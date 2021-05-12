package velo.nlp.oracle.main;

import java.io.IOException;

import velo.nlp.oracle.data.DataModel;
import velo.nlp.oracle.data.OperationTypes;
import velo.nlp.oracle.models.Model;

public class Main {
	
	
	private final static String MODEL_VERSION =  "bety";
	private final static Integer DATA_INDEX = 1, INPUT_SIZE = 10;
	
	
	public static void main(String[] args) {	
		
		/* BASED ON MODE
		 * -e = evaluate [vec]
		 * -t = train [file]
		*/
		
		String mode = args[0];
		
		switch (mode) {
		case "-e":
			double[] vec = new double[INPUT_SIZE];
			Integer i = 0;		
			for(String a : args) {
				if(i >= DATA_INDEX) {
					vec[i] = Double.parseDouble(a);
					i+=1;	
				}
				
			}				
			System.out.println("[oracle]\twaking");
			Model model = new Model();		
			model.buildNewMode(INPUT_SIZE, 2, OperationTypes.values().length);		
			System.out.println("[oracle]\tthinking");
			model.pass(vec).getResultType().write();
			System.out.println("[oracle]\tdecided");
			break;

		case "-t":
			
			Model modelNew = new Model();		
			modelNew.buildNewMode(INPUT_SIZE, 2, OperationTypes.values().length);				
			try {
				modelNew.trainModel(new DataModel().trainingData(args[1]), new DataModel().trainingData(args[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			modelNew.snap();
			break;
		default:
			break;
		}

		
	}

}
