package velo.nlp.oracle.data;

import java.io.FileWriter;
import java.io.IOException;

public class WorkableDecision {

	private OperationTypes type;

	public WorkableDecision(OperationTypes type) {
		this.type = type;
	}

	public void write() {
		try {
			FileWriter myWriter = new FileWriter("oracle_decision.txt");
			myWriter.write(this.type.toString());
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
