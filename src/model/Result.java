package model;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author astratakis
 * @author dpetrou
 * 
 * It is used to store all the information of the algorithm used.
 * It creates a string that can be printed and show all the information saved.
 */
public final class Result {
	
	public Result(String name, int visitedNodes, long executionTime, LinkedList<Road> path, ArrayList<Double> weights, double predicted, double actual) {
		algorithmName = name;
		this.visitedNodes = visitedNodes;
		this.executionTime = executionTime;
		roadPath = path;
		predictedCost = predicted;
		actualCost = actual;
		this.weights = weights;
		
		// Create the output result...
		result = buildResult();
	}
	
	/**
	 * The name of the algorithm that was used to create this result.
	 */
	public final String algorithmName;
	
	/**
	 * The number of visited Nodes.
	 */
	public final int visitedNodes;
	
	/**
	 * The execution time of the algorithm in nanoseconds.
	 */
	public final long executionTime;
	
	/**
	 * The final road path created by the algorithm.
	 */
	final LinkedList<Road> roadPath;
	
	/**
	 * 
	 */
	final ArrayList<Double> weights;
	
	/**
	 * The predicted cost of the route. It is produced by the traffic predictions of the given day.
	 */
	public final double predictedCost;
	
	/**
	 * The actual cost of the route. It is produced by the actual traffic of the given day.
	 */
	public final double actualCost;
	
	/**
	 * The produced result.
	 */
	private final String result;

	/**
	 * It builds the output result.
	 * @return The final result.
	 */
	private String buildResult() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("<" + algorithmName + ">\n");
		buffer.append("\tVisited Nodes: " + visitedNodes + "\n");
		buffer.append("\tExecution Time: " + (executionTime < 1_000_000 ? executionTime + " ns" : executionTime / 1_000_000 + " ms") + "\n\t");
		
		for (int i=0; i<roadPath.size(); i++) {
			buffer.append(roadPath.get(i) + "( " + weights.get(i) + " )");
			if (i < roadPath.size() - 1) {
				buffer.append(" -> ");
			}
		}
		
		buffer.append("\n");
		
		buffer.append("\tPredicted cost: " + predictedCost + "\n");
		buffer.append("\tActual cost: " + actualCost + "\n");
		
		return buffer.toString();
	}
	
	@Override
	public String toString() {
		return result;
	}
}
