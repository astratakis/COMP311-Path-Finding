package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import model.Day;
import model.ProblemInstance;
import model.Result;

public class DataExtractor {
	
	public DataExtractor(ProblemInstance pi) {
		this.days = pi.getDays();
		
		for (int i=0; i<3; i++) {
			calculateAverage(i);
		}
	}
	
	private List<Day> days;

	public void exportFullData(String filename) throws FileNotFoundException {
		PrintWriter pr = null;
		
		try {
			pr = new PrintWriter(new File("src" + File.separator + "output" + File.separator + filename));
		}
		catch (Exception e) {
			pr = new PrintWriter(new File("output" + File.separator + filename));
		}
		
		for (Day d : days) {
			pr.println("Day: " + d.ID);
			for (Result r : d.getResults()) {
				pr.println(r);
			}
		}
		
		pr.close();
	}
	
	public void exportExel(String filename) throws FileNotFoundException {
		
		PrintWriter pr = null;
		
		try {
			pr = new PrintWriter(new File("src" + File.separator + "output" + File.separator + filename));
		}
		catch (Exception e) {
			pr = new PrintWriter(new File("output" + File.separator + filename));
		}
		
		pr.println("Day,Algorithm name,Visited nodes,Execution time,Actual cost, ,"
				+ "Algorithm name,Visited nodes,Execution time,Actual cost, ,"
				+ "Algorithm name,Visited nodes,Execution time,Actual cost,");
		
		for (Day d : days) {
			pr.print(d.ID);
			for (Result r : d.getResults()) {
				pr.print("," + r.algorithmName + "," + r.visitedNodes +
						"," + r.executionTime + "," + r.actualCost + ", ");
			}
			pr.println();
		}
		
		pr.close();
	}
	
	private void calculateAverage(int index) {
		
		double totalCost = 0.0;
		double averageCost = 0.0;
		
		for (Day d : days) {
			totalCost += d.getResults().get(index).actualCost;
		}
		
		averageCost = totalCost / days.size();
		System.out.println(days.get(0).getResults().get(index).algorithmName + " - " + averageCost);
	}
}
