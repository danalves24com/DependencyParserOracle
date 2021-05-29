package velo.nlp.oracle.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import velo.nlp.oracle.data.DataModel;
import velo.nlp.oracle.data.OperationTypes;
import velo.nlp.oracle.data.ResultVec;

public class Model implements Serializable {

	private int in, lSize, out;
	private Double thisAcc = 0.0;
	private MultiLayerNetwork model, bestModel;
	private DataSet data, testingData;

	public void buildNewMode(int inputs, int layerSize, int outputs) {
		this.in = inputs;
		this.lSize = layerSize;
		this.out = outputs;
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().iterations(1000).activation(Activation.RELU)
				.weightInit(WeightInit.XAVIER).learningRate(0.1).regularization(true).l2(0.0001).list()
				.layer(0, new DenseLayer.Builder().nIn(this.in).nOut(lSize).build())
				.layer(1, new DenseLayer.Builder().nIn(lSize).nOut(lSize).build())
				.layer(2,
						new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
								.activation(Activation.SOFTMAX).nIn(lSize).nOut(this.out).build())
				.backprop(true).pretrain(false).build();

		// compile the model
		model = new MultiLayerNetwork(conf);
		model.init();
	}

	public void test() throws FileNotFoundException {
		INDArray output = model.output(testingData.getFeatureMatrix());
		System.out.println(output);
		Evaluation eval = new Evaluation(3);
		eval.eval(testingData.getLabels(), output);
//		System.out.println(model.output(testing.getFeatureMatrix()));
		System.out.println(eval.stats());
		this.thisAcc = eval.accuracy();
//		System.out.println(eval.getCostArray());k
	}

	public void trainModel(DataSet data, DataSet testingData) {
		this.testingData = testingData;
		this.data = data;
		System.out.print("learning new data");
		Integer ep = 0;
		Double accuracy = 0.0, target = 0.4, best = 0.02;
		while (accuracy < 0.6 && ep < 70) {
			System.out.println("running Epoch: " + ep + " Current ACC: " + accuracy + " prop: " + accuracy / target);
			model.fit(data);
			try {
				this.test();
				accuracy = this.thisAcc;
				if (accuracy > best) {
					this.bestModel = this.model.clone();
				}
				ep += 1;
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public static Model load(String path) {
		if(new File(path).exists()) {
			try {
				FileInputStream file = new FileInputStream(path);
				ObjectInputStream in = new ObjectInputStream(file);
				Object r = in.readObject();	
				System.out.println(r.toString());
				in.close();
	            file.close();
	            return (Model)r;
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	public void snap() {
		try {
			FileOutputStream fileOut = new FileOutputStream("oracle"+((int)(Math.random()*99999))+".nnmodel");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
			System.out.println("Model saved");
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public ResultVec pass(double[] params) {
		INDArray output = this.model.output(new DataModel().VectorToDataSet(params).getFeatureMatrix());
		double[] distro = new double[output.length()];
		Integer i = 0;
		for (OperationTypes type : OperationTypes.values()) {
			distro[i] = output.getDouble(i);
			System.out.println(distro[i]);
			i += 1;
		}

		return new ResultVec(distro);
	}

	public void loadFromFile(String path) {

	}

}
