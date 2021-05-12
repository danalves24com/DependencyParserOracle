package velo.nlp.oracle.testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import velo.nlp.oracle.data.OperationTypes;
import velo.nlp.oracle.models.Model;

class ModelExperiment {

	@Test
	void test() {
		Model model = new Model();
		model.buildNewMode(5, 4, OperationTypes.values().length);
		model.pass(new double[] {1, 0, 0 ,0,0});
	}

}
