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
	}
	
	private List<Day> days;

	public void exportFullData(String filename) {
		
	}
	
	public void exportExel(String filename) throws FileNotFoundException {
		
		PrintWriter pr = null;
		
		try {
			pr = new PrintWriter(new File("src" + File.separator + "output" + File.separator + filename));
		}
		catch (Exception e) {
			pr = new PrintWriter(new File("output" + File.separator + filename));
		}
		
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
}
