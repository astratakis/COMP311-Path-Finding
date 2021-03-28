package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import model.ProblemInstance;

/**
 * 
 * @author astratakis
 * @author dpetrou
 * 
 * It is used to test the implementation of the problem.
 */
public class Console {

	/**
	 * The name of the file that will be used if the user runs the program with no arguments.
	 */
	final static String INPUT_FILE = "sampleGraph3.txt";
	
	public static void main(String[] args) throws FileNotFoundException {
		
		Scanner scanner = null;
		
		try {
			scanner = new Scanner(new File("input" + File.separator + args[0]));
		}
		catch (IndexOutOfBoundsException e) {
			scanner = new Scanner(new File("src" + File.separator + "input" + File.separator + INPUT_FILE));
		}
		
		ProblemInstance pi = new ProblemInstance(scanner);
		scanner.close();
		pi.simulate();
		
		DataExtractor data = new DataExtractor(pi);
		data.exportFullData("output.txt");
		data.exportExel("exel.txt");
	}
}
