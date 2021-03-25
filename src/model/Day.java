package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Day {
	
	private final int ID;
	
	private Map<Road,Double> predictions;
	private Map<Road,Double> actual;
	private Vector<Result> results;
	
	
	public Day(int ID) {
		this.ID = ID;
		
		this.predictions = new HashMap<Road,Double>();
		this.actual = new HashMap<Road,Double>();
		this.results = new Vector<Result>();
		
	}


	public int getID() {
		return ID;
	}


	public Map<Road, Double> getPredictions() {
		return predictions;
	}


	public Map<Road, Double> getActual() {
		return actual;
	}


	public Vector<Result> getResults() {
		return results;
	}

}
