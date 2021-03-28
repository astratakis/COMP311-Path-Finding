package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author astratakis
 * @author dpetrou
 * 
 * It contains necessary data to run simulation of the traffic each data.
 * Is is also used to store the results of each algorithm in it.
 */
public class Day {

	/**
	 * Creates a new day with a distinct identity.
	 * @param ID The identity of the day.
	 */
	public Day(int ID) {
		this.ID = ID;
		
		predictions = new HashMap<Road, Double>();
		actual = new HashMap<Road, Double>();
		results = new ArrayList<Result>();
	}
	
	/**
	 * The identity of the day.
	 */
	public final int ID;
	
	/**
	 * The traffic predictions of this day.
	 */
	Map<Road, Double> predictions;
	
	/**
	 * The actual traffic of this day.
	 */
	Map<Road, Double> actual;
	
	/**
	 * The algorithm results of this day.
	 */
	List<Result> results;
	
	/**
	 * Returns the results of this day.
	 * @return The results of this day.
	 */
	public List<Result> getResults() {
		return results;
	}
	
	@Override
	public String toString() {
		return "Day: " + ID;
	}
}
