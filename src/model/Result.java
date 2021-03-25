package model;

import java.util.LinkedList;

public class Result {
	
	
	private String algorithmName;
	private int visitedNodes;
	private long executionTime;
	private LinkedList<Road> roadPath;
	private double predictedCost;
	private double actualCost;
	private Day d;
	
	public Result(Day d) {
		this.d = d;		
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public int getVisitedNodes() {
		return visitedNodes;
	}

	public void setVisitedNodes(int visitedNodes) {
		this.visitedNodes = visitedNodes;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public LinkedList<Road> getRoadPath() {
		return roadPath;
	}

	public void setRoadPath(LinkedList<Road> roadPath) {
		this.roadPath = roadPath;
	}

	public double getPredictedCost() {
		return predictedCost;
	}

	public void setPredictedCost(double predictedCost) {
		this.predictedCost = predictedCost;
	}

	public double getActualCost() {
		return actualCost;
	}

	public void setActualCost(double actualCost) {
		this.actualCost = actualCost;
	}
	
	public void printResult() {
		System.out.println("<"+this.algorithmName+">");
		System.out.println("\tVisited Nodes Num: " + visitedNodes);
		System.out.println("\tExecution time: " + (this.executionTime < 1_000_000 ? this.executionTime + " ns" : (this.executionTime) / 1_000_000 + " ms"));
		
		StringBuilder buffer = new StringBuilder();
		
		for (int i=0; i<roadPath.size(); i++) {
			buffer.append(roadPath.get(i).getName() + " ( " + d.getPredictions().get(roadPath.get(i)) + " )");
			if (i < roadPath.size() - 1) {
				buffer.append(" -> ");
			}
		}
		
		System.out.println("\tPath: " + buffer.toString());
		System.out.printf("\tPredicted Cost: %.2f\n", this.predictedCost);
		System.out.printf("\tReal Cost: %.2f\n\n", this.actualCost);
	}

}
