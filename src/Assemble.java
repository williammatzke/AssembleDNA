import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Assemble {

	private static long startTime;

	public static void main(String[] args) {

		LinkedList<String> SHORT_READS = getReadsFromFile();

		int k = findFirstK(SHORT_READS);

		boolean solved = false;

		String solution = "";
		ArrayList<String> kmers;

		while (!solved) {
			kmers = makeKmer(k, SHORT_READS);

			solution = attemptAssembly(k, kmers);

			if (kmers.isEmpty())
				solved = true;
			else
				k--;
		}

		// prints out the solution and compare program's solution and a string
		System.out.println(solution);
		String givenSequence = "GCTAATACGGAGGCTAGCGCCTTTGTGCATGCAGGGGGCCCCAGAAGTGATGATCTCTGGTAGCTAGATCCAATCCAACCTGCTTCGTATTGCTAGCTCTGATTTGTAACACACGGTCGTGTCGATGTTTTTAGAAGCGTATCGCTCTAGGACCTAATCAGGTACCCCATCCACGGGCCGGTCCATAACCCGGCTTGGCTGCAAACCCGTTCACCTAAGGCCTGTAACTTTGAATTATAGTTAAAGTTACACCTTGGCCATCTAGGGGTCTATTGATATTGAGATAGACAAGGCAACCTAGGTATTCGTTGTACCCTTAGACTGCTCACGCGCGGGGCAAACGTAGTCACCCCGATTGCCGATGTTGAGCACCGAGCTCTCTAAGCGTGCCCAGATGACGCATGCGCGTAGTTTCAGGCCCGCCGGGAGTACTCCCAACTGTACTAGGCGATTTTGAAATAAGACAAATCAATAGCTGAAACACGTGAATCGACGTGGGTCGCTTCGTTACCCGCAGAAATATTGAATCCTCATCAGTCGTGACATGCATGTGCGACTTTGGGTCCCTTTTTGACTTAAGTATTAAACATATCCCGTCATTGAATCAACGGTTGTTTCACACCATCCTGCATTTATGAACTGTGTCATAGCATAAGCGACAGCAACGTTGACATCTTCGGATTCCTAGGTCGGCCGACCTGCCGGATATGACATGAGGACCACAGATGTGTTATGTTTCCAGTCGCTCCCTTCTGCTACGAAGAGACGCGCGCCTTCGCTTAGCAAGTCCCCGTAATTCCACTCTACCGACTATCTCCCCGAGCACAACCTGTTGGGTACCCAGGACCCGCAGATCGCCTGGCAACGTACGGGCCGCATTAGCATTGTTTTCCACTGTCCGCCCCGGTACCCAATCTTTCTATGATTCAAAGGATCAGAAAGACTATAAGAGTTCGAGTTGCTGGATTCTTTCTGCAAGTTGTTTTGAAAGTGTACGAAATCATAGGTAGACTTTGTCCGAATGCAATCGACTTTAGCGATACGTGAGCTCGAGTTCCCGTAACTGCGCGGACTCCTGGTTATAGTGCGTGATCACACGCGACTGAACATATCTGCATCACTCGAAAGTCCCCGACACGAGCGGCATTGGGAACCTGCTGGGGCTAAACGCAATGCTTTCAAGCGCCATCCTCCGCACCCGTTAACGCAGAGTACACATCGGTAGGACTTTCATGCGGGACGAAACAACAAGTGGCACACATTCGGGAGAGACTCGGCGTACTGAAACCTCATCTCCAACAGGACCGTTAATATCATGCTCACATGTCGTATATACGGGGTATGTCTATAGTACATCACGGTGCCCGTAATTCCGTACGGGCTCGAATAGATTGCCCCTTTGCCCTTACTGAACGGTAACTGACAGGCGTCCGTGAAACACTTTTGCAATGCCACGTAGAAGTGGCTAGATCGAAAAGCGTTAAGCTGTGGCATTATGTTATGACCCGGGTGTGGATTTTGACCTGATCCACCATGCCGACAGTCAGCCCAGGGCGCTTCCAGTGGAACAAAGAGGCGTGCTGTCCCGCCGAGCATCAGGCGCTTCCAGCCGTAGCGAACTCGACCGATTCAGACGAGTACCCATCGATGTTTGATGATCTGGTTTTCCTATCATAATCGTGCTGGACGGCCTTAACAATACTACGACGGATCGTTAGCGAGCTAGACCGCTACTCGTGTCGTCAAGCGGAAATTATGGTCCGAACACTCGATGCGAGAGGGAGAAAATGGGCATCGTCCTCCGGTAAGCCGAGGCCCAAGCTGAGCGGCTCAGCGCGGGCTGTTCTGAGGATCCAACGTCGCCCCATCTTTCTTAAAATGTAACTAGGTTGATTGTGTAAAGGTCCTCTCTAATATGTTCACTATGCCGAGCTATCAGGGTGAAACGTCCACGTTCCCTGCTGTGCTTTCCTGGGGACCACGGATCCAACGGGAAACCT";
		if (solution.equals(givenSequence))
			System.out.println("matches givenSequence");
		else
			System.out.println("givenSequence String does not match answer arrived at through algorithm");

		System.out.println(System.currentTimeMillis() - startTime);

	}

	private static LinkedList<String> getReadsFromFile() {

		LinkedList<String> listOfReads = new LinkedList<>();

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
	// TODO: do I really need kmers of the same size? In practice you could get different read lengths(?)
	private static int findFirstK(LinkedList<String> reads) {
		// sets k the length of the first read in the given ArrayList
		int k = reads.get(0).length();
		// k gets compared against all Strings in given ArrayList
		for (String read : reads) {
			if (k > read.length()) {
				// if k is larger than a String in ArrayList, k gets set to the
				// length of that smaller String
				k = read.length();
			}
		}
		return k;
	}

	// method called every time new, smaller kmers need to be made
	private static ArrayList<String> makeKmer(int k, LinkedList<String> readsList) {
		// initialize ArrayList that needs to be returned
		ArrayList<String> newList = new ArrayList<>();
		// iterates through length of main method's "SHORT+READS"

		for (String read : readsList) {
			// nested for loop to get all the kmers out of short read
			for (int j = 0; j + k <= read.length(); j++) {
				String toAdd = read.substring(j, j + k);
				// kmer is added to returned ArrayList iff it is unique
				if (!newList.contains(toAdd))
					newList.add(toAdd);
			}
		}
		//System.out.println("my list of kmers is x long: " + newList.size());
		return newList;
	}


	// method called each time new, smaller kmers are made. This contains the
	// core logic for finding a path through kmers
	private static String attemptAssembly(int k, ArrayList<String> kmers) {

		// attempt is the value that gets returned. It gets its start from the
		// (arbitrarily chosen) first entry in the kmers ArrayList
		String attempt = kmers.get(0);
		kmers.remove(0);

		// sentinel value changed to false as soon as the loop is entered. This control is based on the idea that (since
		// the loop looks to add to both the front and back of the path within the same loop) if a match isn't found
		// the first time, the attempt is guaranteed to fail on continued attempts. So the loop is told to end
		// every time UNLESS a match is found each time
		boolean solving = true;

		while (solving) {
			solving = false;

			// loop that iterates through kmers
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
			}
		}
		return attempt;
	}
}