import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Assembler {

	public static ArrayList<String> shorts = new ArrayList<String>();
	public static int k = 10;
	
	public static void main(String[] args) {

		Scanner scnr = new Scanner(System.in);

		shorts = getReads(scnr);
		
		ArrayList<String> kmers = makeKmer(k, shorts);
		
		String solution = assembleToString(kmers);
		
		String cheatAnswer = "GCTAATACGGAGGCTAGCGCCTTTGTGCATGCAGGGGGCCCCAGAAGTGATGATCTCTGGTAGCTAGATCCAATCCAACCTGCTTCGTATTGCTAGCTCTGATTTGTAACACACGGTCGTGTCGATGTTTTTAGAAGCGTATCGCTCTAGGACCTAATCAGGTACCCCATCCACGGGCCGGTCCATAACCCGGCTTGGCTGCAAACCCGTTCACCTAAGGCCTGTAACTTTGAATTATAGTTAAAGTTACACCTTGGCCATCTAGGGGTCTATTGATATTGAGATAGACAAGGCAACCTAGGTATTCGTTGTACCCTTAGACTGCTCACGCGCGGGGCAAACGTAGTCACCCCGATTGCCGATGTTGAGCACCGAGCTCTCTAAGCGTGCCCAGATGACGCATGCGCGTAGTTTCAGGCCCGCCGGGAGTACTCCCAACTGTACTAGGCGATTTTGAAATAAGACAAATCAATAGCTGAAACACGTGAATCGACGTGGGTCGCTTCGTTACCCGCAGAAATATTGAATCCTCATCAGTCGTGACATGCATGTGCGACTTTGGGTCCCTTTTTGACTTAAGTATTAAACATATCCCGTCATTGAATCAACGGTTGTTTCACACCATCCTGCATTTATGAACTGTGTCATAGCATAAGCGACAGCAACGTTGACATCTTCGGATTCCTAGGTCGGCCGACCTGCCGGATATGACATGAGGACCACAGATGTGTTATGTTTCCAGTCGCTCCCTTCTGCTACGAAGAGACGCGCGCCTTCGCTTAGCAAGTCCCCGTAATTCCACTCTACCGACTATCTCCCCGAGCACAACCTGTTGGGTACCCAGGACCCGCAGATCGCCTGGCAACGTACGGGCCGCATTAGCATTGTTTTCCACTGTCCGCCCCGGTACCCAATCTTTCTATGATTCAAAGGATCAGAAAGACTATAAGAGTTCGAGTTGCTGGATTCTTTCTGCAAGTTGTTTTGAAAGTGTACGAAATCATAGGTAGACTTTGTCCGAATGCAATCGACTTTAGCGATACGTGAGCTCGAGTTCCCGTAACTGCGCGGACTCCTGGTTATAGTGCGTGATCACACGCGACTGAACATATCTGCATCACTCGAAAGTCCCCGACACGAGCGGCATTGGGAACCTGCTGGGGCTAAACGCAATGCTTTCAAGCGCCATCCTCCGCACCCGTTAACGCAGAGTACACATCGGTAGGACTTTCATGCGGGACGAAACAACAAGTGGCACACATTCGGGAGAGACTCGGCGTACTGAAACCTCATCTCCAACAGGACCGTTAATATCATGCTCACATGTCGTATATACGGGGTATGTCTATAGTACATCACGGTGCCCGTAATTCCGTACGGGCTCGAATAGATTGCCCCTTTGCCCTTACTGAACGGTAACTGACAGGCGTCCGTGAAACACTTTTGCAATGCCACGTAGAAGTGGCTAGATCGAAAAGCGTTAAGCTGTGGCATTATGTTATGACCCGGGTGTGGATTTTGACCTGATCCACCATGCCGACAGTCAGCCCAGGGCGCTTCCAGTGGAACAAAGAGGCGTGCTGTCCCGCCGAGCATCAGGCGCTTCCAGCCGTAGCGAACTCGACCGATTCAGACGAGTACCCATCGATGTTTGATGATCTGGTTTTCCTATCATAATCGTGCTGGACGGCCTTAACAATACTACGACGGATCGTTAGCGAGCTAGACCGCTACTCGTGTCGTCAAGCGGAAATTATGGTCCGAACACTCGATGCGAGAGGGAGAAAATGGGCATCGTCCTCCGGTAAGCCGAGGCCCAAGCTGAGCGGCTCAGCGCGGGCTGTTCTGAGGATCCAACGTCGCCCCATCTTTCTTAAAATGTAACTAGGTTGATTGTGTAAAGGTCCTCTCTAATATGTTCACTATGCCGAGCTATCAGGGTGAAACGTCCACGTTCCCTGCTGTGCTTTCCTGGGGACCACGGATCCAACGGGAAACCT";
		
		if (solution.equals(cheatAnswer))
		{
			System.out.println("they match");
		}
	}


	public static String assembleToString(ArrayList<String> kmers)
	{
		String attempt = kmers.get(0);
		kmers.remove(0);
		for (int i = 0; i<100; i++)//this needs to replaces with something that stops it when it's done
		{
			for (int j = 0; j<kmers.size(); j++)
			{
				String prefixBack = attempt.substring(attempt.length()-k+1);
				//System.out.println("prefix is "+prefixBack);
				
				String suffixBack = kmers.get(j).substring(0, k-1);
				//System.out.println("suffix is "+suffixBack);
				
				if (prefixBack.equals(suffixBack))
				{
					attempt += kmers.get(j).substring(k-1, k);
					kmers.remove(j);
				}
				//System.out.println("attempt is "+attempt);
				
				
				String prefixFront = kmers.get(j).substring(1, k);
				//System.out.println("drawing prefix from "+kmers.get(j));
				//System.out.println("prefix is "+prefixFront);
				String suffixFront = attempt.substring(0,k-1);
				//System.out.println("suffix is "+suffixFront);
				
				if (prefixFront.equals(suffixFront))
				{
					//System.out.println("before adding prefix, attempt is "+attempt);
					//System.out.println("prefix is "+prefixFront);
					String swap = attempt;//this may be problematic
					//System.out.println("swap is "+swap);
					attempt = kmers.get(j);
					//System.out.println("before adding swap back, attempt is "+attempt);
					attempt += swap.substring(k-1);
					//System.out.println("after adding prefix, attempt is "+attempt);
					//System.out.println("stopping");
					kmers.remove(j);
				}				
			}			
		}
		return attempt;
	}
	
	public static ArrayList<String> makeKmer(int k, ArrayList<String> readsList)
	{
		ArrayList<String> newList = new ArrayList<String>();
		
		for (int i = 0; i<readsList.size(); i++)
		{
			String read = readsList.get(i);
			for (int j = 0; j+k<=read.length(); j++)
			{
				String toAdd = read.substring(j, j+k);
				newList.add(toAdd);
			}
		}
		return newList;
	}
	
	public static ArrayList<String> getReads(Scanner scnr) {
		ArrayList<String> listOfReads = new ArrayList<String>();

		try {
			System.out.print("Enter name of input file: ");
			String fileName = scnr.nextLine();
			File file = new File(fileName);
			Scanner fileReader = new Scanner(file);
			fileReader.nextLine();
			while (fileReader.hasNext())
				listOfReads.add(fileReader.nextLine());
			fileReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find File");
			System.exit(0);
		}
		return listOfReads;
	}
	
}
