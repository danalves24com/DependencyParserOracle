package velo.nlp.oracle.data;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.writable.Writable;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;

public class DataModel {
	private String readLine(List<Writable> w) {
		List<String> word = new ArrayList<String>();
		for (Writable ww : w) {
			word.add((ww.toString()));
		}
		return String.join(", ", word);

	}
	
	public DataSet VectorToDataSet(double[] vec) {		
		return new DataSet(Nd4j.create(vec), Nd4j.create(new double[vec.length]));
	}
	

	public DataSet trainingData(String path) throws IOException, InterruptedException {
		double[][] inputs = null, outputs = null;
		/*
		 * SAMPLE DATA INPUT IN FILE -full vec-(0,0,1...1,0,0) ==> 0/1/2 \
		 * larc/rarc/shft
		 */
		OperationTypes[] optypesMap = OperationTypes.values();
		RecordReader recordReader = new CSVRecordReader(0, ',');
		recordReader.initialize(new FileSplit(new File(path)));
		List<double[]> inputsList = new ArrayList<double[]>();
		List<Integer> outputsList = new ArrayList<Integer>();
		while (recordReader.hasNext()) {
			String data = this.readLine(recordReader.next());
			String[] inputString = data.split("==>")[0].trim().split(", ");
			double[] input = new double[inputString.length];
			Integer it = 0;
			for (String inp : inputString) {
				input[it] = Double.parseDouble(inp);
				it += 1;
			}
			String outVal = data.split("==>")[1].trim();
			Integer indexOfOut = -1;
			Integer typeIt = 0;
			for (OperationTypes type : optypesMap) {
				if (type.toString().equals(outVal)) {
					indexOfOut = typeIt;
				}
				typeIt += 1;
			}
			outputsList.add(indexOfOut);
			inputsList.add(input);
			System.out.println();
		}
		inputs = new double[inputsList.size()][inputsList.get(0).length];
		outputs = new double[outputsList.size()][1];
		Integer i = 0;
		for(double[] inp : inputsList ) {
			inputs[i] = inp;
			i +=1;
		}
		i = 0;
		for(Integer o : outputsList) {
			double[] oo = new double[OperationTypes.values().length];
			for(double d : oo) {
				d = 0.0;
			}
			oo[o] = 1.0;
			outputs[i] = oo;
			i+=1;
		}
		return new DataSet(Nd4j.create(inputs), Nd4j.create(outputs));
	}

}
