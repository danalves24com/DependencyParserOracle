package velo.nlp.oracle.testing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import velo.nlp.oracle.data.DataModel;

class DataTesting {

	@Test
	void test() {
		DataModel model = new DataModel();
		try {
			System.out.println(model.trainingData("train.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
