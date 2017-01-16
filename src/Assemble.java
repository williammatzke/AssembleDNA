//import a few packages
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeSet;
import java.io.File;
import java.io.FileNotFoundException;

public class Assemble {

	static long startTime;
	
	public static void main(String[] args) {

		LinkedList<String> SHORT_READS = getReadsFromFile();

		int k = findFirstK(SHORT_READS);

		boolean solved = false;

		String solution = "";
		TreeSet<String> kmers = new TreeSet<String>();

		while (!solved) {
			kmers = makeKmer(k, SHORT_READS);
			
			solution = attemptAssembly(k, kmers);
			
			if (kmers.isEmpty())
				solved = true;
			else
				k--;
		}

		// the following block of code prints out the solution and provides code
		// to compare program's solution and a string
		System.out.println(solution);
		String givenSequence = "GCTAATACGGAGGCTAGCGCCTTTGTGCATGCAGGGGGCCCCAGAAGTGATGATCTCTGGTAGCTAGATCCAATCCAACCTGCTTCGTATTGCTAGCTCTGATTTGTAACACACGGTCGTGTCGATGTTTTTAGAAGCGTATCGCTCTAGGACCTAATCAGGTACCCCATCCACGGGCCGGTCCATAACCCGGCTTGGCTGCAAACCCGTTCACCTAAGGCCTGTAACTTTGAATTATAGTTAAAGTTACACCTTGGCCATCTAGGGGTCTATTGATATTGAGATAGACAAGGCAACCTAGGTATTCGTTGTACCCTTAGACTGCTCACGCGCGGGGCAAACGTAGTCACCCCGATTGCCGATGTTGAGCACCGAGCTCTCTAAGCGTGCCCAGATGACGCATGCGCGTAGTTTCAGGCCCGCCGGGAGTACTCCCAACTGTACTAGGCGATTTTGAAATAAGACAAATCAATAGCTGAAACACGTGAATCGACGTGGGTCGCTTCGTTACCCGCAGAAATATTGAATCCTCATCAGTCGTGACATGCATGTGCGACTTTGGGTCCCTTTTTGACTTAAGTATTAAACATATCCCGTCATTGAATCAACGGTTGTTTCACACCATCCTGCATTTATGAACTGTGTCATAGCATAAGCGACAGCAACGTTGACATCTTCGGATTCCTAGGTCGGCCGACCTGCCGGATATGACATGAGGACCACAGATGTGTTATGTTTCCAGTCGCTCCCTTCTGCTACGAAGAGACGCGCGCCTTCGCTTAGCAAGTCCCCGTAATTCCACTCTACCGACTATCTCCCCGAGCACAACCTGTTGGGTACCCAGGACCCGCAGATCGCCTGGCAACGTACGGGCCGCATTAGCATTGTTTTCCACTGTCCGCCCCGGTACCCAATCTTTCTATGATTCAAAGGATCAGAAAGACTATAAGAGTTCGAGTTGCTGGATTCTTTCTGCAAGTTGTTTTGAAAGTGTACGAAATCATAGGTAGACTTTGTCCGAATGCAATCGACTTTAGCGATACGTGAGCTCGAGTTCCCGTAACTGCGCGGACTCCTGGTTATAGTGCGTGATCACACGCGACTGAACATATCTGCATCACTCGAAAGTCCCCGACACGAGCGGCATTGGGAACCTGCTGGGGCTAAACGCAATGCTTTCAAGCGCCATCCTCCGCACCCGTTAACGCAGAGTACACATCGGTAGGACTTTCATGCGGGACGAAACAACAAGTGGCACACATTCGGGAGAGACTCGGCGTACTGAAACCTCATCTCCAACAGGACCGTTAATATCATGCTCACATGTCGTATATACGGGGTATGTCTATAGTACATCACGGTGCCCGTAATTCCGTACGGGCTCGAATAGATTGCCCCTTTGCCCTTACTGAACGGTAACTGACAGGCGTCCGTGAAACACTTTTGCAATGCCACGTAGAAGTGGCTAGATCGAAAAGCGTTAAGCTGTGGCATTATGTTATGACCCGGGTGTGGATTTTGACCTGATCCACCATGCCGACAGTCAGCCCAGGGCGCTTCCAGTGGAACAAAGAGGCGTGCTGTCCCGCCGAGCATCAGGCGCTTCCAGCCGTAGCGAACTCGACCGATTCAGACGAGTACCCATCGATGTTTGATGATCTGGTTTTCCTATCATAATCGTGCTGGACGGCCTTAACAATACTACGACGGATCGTTAGCGAGCTAGACCGCTACTCGTGTCGTCAAGCGGAAATTATGGTCCGAACACTCGATGCGAGAGGGAGAAAATGGGCATCGTCCTCCGGTAAGCCGAGGCCCAAGCTGAGCGGCTCAGCGCGGGCTGTTCTGAGGATCCAACGTCGCCCCATCTTTCTTAAAATGTAACTAGGTTGATTGTGTAAAGGTCCTCTCTAATATGTTCACTATGCCGAGCTATCAGGGTGAAACGTCCACGTTCCCTGCTGTGCTTTCCTGGGGACCACGGATCCAACGGGAAACCT";
		if (solution.equals(givenSequence))
			System.out.println("matches givenSequence");
		else
			System.out.println("givenSequence String does not match answer arrived at through algorithm");
		
		System.out.println(System.currentTimeMillis()-startTime);

	}

	public static LinkedList<String> getReadsFromFile() {

		LinkedList<String> listOfReads = new LinkedList<String>();

		// Scanner object to get user input
		Scanner scnr = new Scanner(System.in);

		// following try/catch block asks for user input, gets file, skips first
		// line, reads rest into listOfReads
		try {
			System.out.println("Note: first line in file is skipped.");
			System.out
					.println ("Enter name of file in same directory as java file: ");
			System.out.println("e.g. reads.txt");
			System.out.println();
			String fileName = scnr.nextLine();
			
			startTime = System.currentTimeMillis();

			File file = new File(fileName);
			Scanner fileReader = new Scanner(file);
			fileReader.nextLine();
			while (fileReader.hasNext())
				listOfReads.add(fileReader.nextLine());
			fileReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find File");
			scnr.close();
			System.exit(0);
		}
		scnr.close();
		return listOfReads;
	}

	// method called once at start to find the largest k that would allow kmers
	// of the same size
	public static int findFirstK(LinkedList<String> reads) {
		// sets k the the length of the first read in the given ArrayList
		int k = reads.get(0).length();
		// k gets compared against all Strings in given ArrayList
		for (int i = 0; i < reads.size(); i++) {
			if (k > reads.get(i).length()) {
				// if k is larger than a String in ArrayList, k gets set to the
				// length of that smaller String
				k = reads.get(i).length();
			}
		}
		return k;
	}

	// method called every time new, smaller kmers need to be made
	public static TreeSet<String> makeKmer(int k, LinkedList<String> readsList) {
		// initialize ArrayList that needs to be returned
		TreeSet<String> newList = new TreeSet<String>();
		// iterates through length of main method's "SHORT+READS"
		for (int i = 0; i < readsList.size(); i++) {
			// get String of a short read
			String read = readsList.get(i);
			// nested for loop to get all the kmers out of short read
			for (int j = 0; j + k <= read.length(); j++) {
				String toAdd = read.substring(j, j + k);
				// kmer is added to returned ArrayList iff it is unique
				if (!newList.contains(toAdd))
					newList.add(toAdd);
			}
		}
		return newList;
	}

	// method called each time new, smaller kmers are made. This contains the
	// core logic for finding a path through kmers
	public static String attemptAssembly(int k, TreeSet<String> kmers) {
		// attempt is the value that gets returned. It gets its start from the
		// (arbitrarily chosen) first entry in the kmers ArrayList
		
		
		String attempt = kmers.first();
		kmers.remove(attempt);
		
		// the string has been added to the attempt, so it is removed from kmers
		
		//kmers.remove(0);

		// boolean value that starts out as true but is changed to false as soon
		// as the loop is entered. This control is based on the idea that (since
		// the loop looks to add to both the front and back of the path within
		// the same loop) if a match isn't found the first time, the attempt is
		// guaranteed to fail on continued attempts. So the loop is told to end
		// every time UNLESS a match is found each time
		boolean solving = true;

		while (solving) {
			solving = false;

			String toRemove = null;
			

			for (String toAdd : kmers)
			{
				String suffixBack = toAdd.substring(0, k-1);
				String prefixBack = attempt.substring(attempt.length()-k+1);
				if (suffixBack.equals(prefixBack))
				{
					attempt += toAdd.substring(k-1, k);
					solving = true;
					toRemove = toAdd;
				}
				
				String prefixFront = toAdd.substring(1, k);
				String suffixFront = attempt.substring(0,k-1);
				if (prefixFront.equals(suffixFront))
				{
					attempt = toAdd.charAt(0) + attempt;
					solving = true;
					toRemove = toAdd;
				}
			}

			if (solving)
			{
				kmers.remove(toRemove);
			}		
			
			
			/**
			
			// loop that iterates through the size of kmers
			for (int j = 0; j < kmers.size(); j++) {
				// the following code inside the if statement looks to add
				// to the right (or "back") side of the attempt String.
				// Need to be in an if statement to prevent index from reaching
				// out of bounds.
				if (j < kmers.size()) {
					// creates suffix to be compared (excludes rightmost
					// character)
					String suffixBack = kmers.get(j).substring(0, k - 1);
					// creates prefix to be compared (excludes leftmost
					// character)
					String prefixBack = attempt.substring(attempt.length() - k
							+ 1);
					// if the two match, the rightmost character from the String
					// that was used to make suffixBack is concatenated to
					// attempt, the String is removed from kmers, and "solving"
					// is switched back to true to continue the loop
					if (prefixBack.equals(suffixBack)) {
						attempt += kmers.get(j).substring(k - 1, k);
						kmers.remove(j);
						solving = true;
					}
				}

				// The following code inside the if statement looks to add the
				// the left (of "front") side of the attempt String.
				// Needs to be in an if statement to prevent index from reaching
				// out of bounds
				if (j < kmers.size()) {
					// creates prefix to be compared (excludes leftmost
					// character)
					String prefixFront = kmers.get(j).substring(1, k);
					// created suffix to be compared (excludes rightmost
					// character)
					String suffixFront = attempt.substring(0, k - 1);
					// if the two match, the leftmost character from the String
					// used to make prefixFront is added to the attempt String.
					// That String is removed from kmers, and "solving" is
					// switched to true to allow the loop to continue
					if (prefixFront.equals(suffixFront)) {
						String swap = attempt;
						attempt = kmers.get(j);
						attempt += swap.substring(k - 1);
						kmers.remove(j);
						solving = true;
					}
				}
			}**/
		}
		return attempt;
	}
}