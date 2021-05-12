package velo.nlp.oracle.data;


public class ResultVec {
	private double[] data;

	public ResultVec(double[] data) {
		super();
		this.data = data;
	}
	public WorkableDecision getResultType() {
		int indexTop = -1, index = 0;
		double largestVal = 0;
		for(double d : data) {
			if(d > largestVal) {
				indexTop = index;
			}
			index+=1;
		}
		return new WorkableDecision(OperationTypes.values()[indexTop]);
	}
}
