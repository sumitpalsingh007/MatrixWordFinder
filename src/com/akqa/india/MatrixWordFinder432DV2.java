package com.akqa.india;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MatrixWordFinder432DV2 {
	
	static char[][] inputs;
	static int ROW_COUNT;
	static int COLUMN_COUNT;
	static String[] words;
	static List<String> rowLines;
	static List<String> outputList = new ArrayList<String>();
	static List<String> columnsLines;
	static Set<String> inputWords = new HashSet<String>();
	static int count =0;
	static StringBuilder word=new StringBuilder();
	static StringBuilder order=new StringBuilder();
	static List<String> diagonalList = new ArrayList<String>();
	static StringBuilder sb = new StringBuilder();
	
	
    
	/**
	 * Main method to launch the program
	 * @param args
	 */
	public static void main(String[] args) {

		long startTime = System.nanoTime();
		String fileName = "input4a.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

			String[] indexes = br.readLine().split(",");
			ROW_COUNT = Integer.valueOf(indexes[0]);
			COLUMN_COUNT = Integer.valueOf(indexes[1]);
			inputs = new char[ROW_COUNT][COLUMN_COUNT];
			String[] inputLines = br.readLine().split(",");
			words = br.readLine().split(",");
			for (int i = 0; i < words.length; i++) {
				inputWords.add(words[i]);
			}
			// System.out.println("nputWords.size()"+inputWords.size());
			br.close();
			Long readingTime = System.nanoTime();
			words = null;
			rowLines = new ArrayList<String>();
			columnsLines = new ArrayList<String>();
			for (int i = 0; i < ROW_COUNT; i++) {
				String s = "";
				for (int j = 0; j < COLUMN_COUNT; j++) {
					s = s + inputLines[i * COLUMN_COUNT + j].charAt(0);
					inputs[i][j] = inputLines[i * COLUMN_COUNT + j].charAt(0);

				}
				rowLines.add(s);
			}
			for (int i = 0; i < COLUMN_COUNT; i++) {
				String s = "";
				for (int j = 0; j < ROW_COUNT; j++) {
					s = s + inputLines[j * COLUMN_COUNT + i].charAt(0);
				}
				columnsLines.add(s);
			}

			inputLines = null;
			long matrixPopultionTime = System.nanoTime();

			iterateVertically();
			long verticalTime = System.nanoTime();
			iterateHorizontally1();
			long horizontalTime = System.nanoTime();
			itrateDigonally();
			long diagonalTime = System.nanoTime();

			System.out.println("Words found " + count);
			long endTime = System.nanoTime();

			System.out.println("Reading time--->>>>" + (readingTime - startTime) / 1000000);
			System.out.println("Matix Population time--->>>>" + (matrixPopultionTime - readingTime) / 1000000);
			System.out.println("Vertical time--->>>>" + (verticalTime - matrixPopultionTime) / 1000000);
			System.out.println("Horizontal time--->>>>" + (horizontalTime - verticalTime) / 1000000);
			System.out.println("Diagonal time--->>>>" + (diagonalTime - horizontalTime) / 1000000);
			System.out.println("Execution time--->>>>" + (endTime - startTime) / 1000000);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * This method is used to iterate the matrix diagonally. It first iterates in North East Direction and then in Soth East
	 */
	private static void itrateDigonally() {
		StringBuilder temp = new StringBuilder();

		for (int i = 0; i < ROW_COUNT; i++) {

			for (int j = 0; j <= i; j++) {
				if (j < COLUMN_COUNT && (i - j) < ROW_COUNT) {
					if ("-".equals(inputs[i - j][j])) {
						temp.setLength(0);
						order.setLength(0);
						continue;
					}
					temp.append(inputs[i - j][j]);
				}
			}
			checkDiagonalNorthEast(temp.toString(), i, true);
			diagonalList.add(temp.toString());
			temp.setLength(0);
			order.setLength(0);
		}

		for (int i = 0; i < COLUMN_COUNT - 1; i++) {

			for (int j = 1; j <= ROW_COUNT - i; j++) {

				if ((i + j) < COLUMN_COUNT && (ROW_COUNT - j) < ROW_COUNT) {
					if ("-".equals(inputs[ROW_COUNT - j][j + i])) {
						temp.setLength(0);
						order.setLength(0);
						continue;
					}
					temp.append(inputs[ROW_COUNT - j][j + i]);
				}
			}
			checkDiagonalNorthEast(temp.toString(), diagonalList.size() + i - 1, false);
			diagonalList.add(temp.toString());
			temp.setLength(0);
			order.setLength(0);
		}
		diagonalList.clear();

		for (int i = ROW_COUNT - 1; i >= 1; i--) {
			for (int j = COLUMN_COUNT - 1; j >= i; j--) {
				if ((j - i) < ROW_COUNT && j < COLUMN_COUNT) {
					if ("-".equals(inputs[j - i][j])) {
						temp.setLength(0);
						order.setLength(0);
						continue;
					}
					temp.append(inputs[j - i][j]);
				}
			}
			checkDiagonalSouthWest(temp.toString(), i, true);
			diagonalList.add(temp.toString());
			temp.setLength(0);
			order.setLength(0);
		}

		for (int i = 0; i < ROW_COUNT; i++) {
			for (int j = ROW_COUNT - 1; j >= i; j--) {

				if ((j - i) < COLUMN_COUNT && j < ROW_COUNT) {
					if ("-".equals(inputs[j][j - i])) {
						temp.setLength(0);
						order.setLength(0);
						continue;
					}
					temp.append(inputs[j][j - i]);
				}
			}
			checkDiagonalSouthWest(temp.toString(), i - 1, false);
			diagonalList.add(temp.toString());
			temp.setLength(0);
			order.setLength(0);

		}
	}

	/**
	 * This method checks whether a particular diagonal(North-East only) contains a dictionary word or not.
	 * @param line
	 * @param i
	 * @param b
	 */
	private static void checkDiagonalNorthEast(String line, int i, boolean b) {
		Iterator itr = inputWords.iterator();
		while (itr.hasNext()) {
			String word = (String) itr.next();
			if (line.contains(word)) {
				itr.remove();
				int start = line.indexOf(word);
				int end = start + word.length();
				System.out.println(word + " " + getNorthEastOrder(i - end + 1, end - 1, word.length(), true));
				count++;
				continue;
			}
			String reverseWord = new StringBuffer(word).reverse().toString();
			if (line.contains(reverseWord)) {
				itr.remove();
				int start = line.indexOf(reverseWord);
				int end = start + word.length();
				System.out.println(word + " " + getNorthEastOrder(i - end + 1, end - 1, word.length(), false));
				// System.out.println(word+" "+getNorthEastOrder(i-end-1, end+1,
				// word.length(),false));
				count++;

			}
		}
	}
	
	
	/**
	 * This method checks whether a particular diagonal(South-West only) contains a dictionary word or not.
	 * @param line
	 * @param i
	 * @param b
	 */
	private static void checkDiagonalSouthWest(String line, int i, boolean b) {
		Iterator itr = inputWords.iterator();
		while (itr.hasNext()) {
			String word = (String) itr.next();
			if (line.contains(word)) {
				itr.remove();
				int start = line.indexOf(word);
				int end = start + word.length();
				if (i < end) {
					System.out.println(word + " "
							+ getSouthWestOrder(COLUMN_COUNT - end - i, ROW_COUNT - end, word.length(), true));
				} else {
					System.out.println(word + " "
							+ getSouthWestOrder(ROW_COUNT - end, COLUMN_COUNT - end - i - 1, word.length(), true));
				}
				// System.out.println(word+"
				// "+getSouthEastOrder(rows-i-word.length(), columns-i-1,
				// word.length(),true));
				count++;
				continue;
			}

			String reverseWord = new StringBuffer(word).reverse().toString();
			if (line.contains(reverseWord)) {
				itr.remove();
				int start = line.indexOf(reverseWord);
				int end = start + reverseWord.length();
				if (i > ROW_COUNT) {
					System.out.println(word + " "
							+ getSouthWestOrder(i - ROW_COUNT, end - COLUMN_COUNT, reverseWord.length(), false));
				} else {
					System.out.println(word + " " + getSouthWestOrder(COLUMN_COUNT - end, ROW_COUNT - i - end - 1,
							reverseWord.length(), false));
					// System.out.println(word+"
					// "+getSouthEastOrder(rows-i-end-1, columns-end,
					// reverseWord.length(),false));

				}
				count++;

			}
		}
	}
	
	/**
	 * This method gets the order for the word found in North-East diagonal
	 * @param i
	 * @param j
	 * @param length
	 * @param direction
	 * @return
	 */
	private static String getNorthEastOrder(int i, int j, int length, boolean direction) {
		sb.setLength(0);
		if (direction) {
			for (int k = length - 1; k >= 0; k--) {
				sb.append("[" + (i + k) + "][" + (j - k) + "] ");

			}
		} else {

			for (int k = 0; k < length; k++) {
				sb.append("[" + (i + k) + "][" + (j - k) + "] ");

			}
		}
		return sb.toString();
	}
	
	/**
	 * This method gets the order for the word found in South-West diagonal
	 * @param i
	 * @param j
	 * @param length
	 * @param b
	 * @return
	 */
	private static String getSouthWestOrder(int i, int j, int length, boolean b) {
		sb.setLength(0);
		if (b) {
			for (int k = length - 1; k >= 0; k--) {
				sb.append("[" + (i + k) + "][" + (j + k) + "] ");

			}
		} else {
			for (int k = 0; k < length; k++) {
				sb.append("[" + (i + k) + "][" + (j + k) + "] ");
			}

		}
		return sb.toString();
	}
	
	
	/**
	 * This method iterates the matrix vertically to look for a word
	 */
	private static void iterateVertically() {

		for (int i = 0; i < ROW_COUNT; i++) {
			Iterator itr = inputWords.iterator();
			String word2 = rowLines.get(i);
			while (itr.hasNext()) {
				String word = (String) itr.next();
				if (word2.contains(word)) {
					itr.remove();
					int start = word2.indexOf(word);
					int end = start + word.length();
					System.out.println(word + " " + getVerticalOrder(i, end - 1, word.length(), true));
					count++;
				} else if (word2.contains(new StringBuffer(word).reverse().toString())) {
					itr.remove();
					int start = word2.indexOf(new StringBuffer(word).reverse().toString());
					int end = start + word.length();
					System.out.println(word + " " + getVerticalOrder(i, end - 1, word.length(), false));
					count++;

				}
			}
		}
	}
	
	
	/**
	 * This method returns the order of a word found during vertical iteration
	 * @param k
	 * @param j
	 * @param length
	 * @param b
	 * @return
	 */
	private static StringBuilder getVerticalOrder(int k, int j, int length, boolean b) {
		sb.setLength(0);
		if (b) {
			for (int a = j - length + 1; a <= j; a++) {
				sb.append("[" + k + "][" + a + "] ");
			}
		} else {
			for (int a = j; a >= j - length + 1; a--) {
				sb.append("[" + k + "][" + a + "] ");
			}
		}

		return sb;
	}

	/**
	 * This method returns the order of a word found during horizontal iteration
	 * @param i
	 * @param j
	 * @param length
	 * @param b
	 * @return
	 */
	private static StringBuilder getHorizontalOrder(int i, int j, int length, boolean b) {
		sb.setLength(0);
		if (b) {
			for (int a = i - length + 1; a <= i; a++) {
				sb.append("[" + a + "][" + j + "] ");
			}
		} else {
			for (int a = i; a > i - length; a--) {
				sb.append("[" + a + "][" + j + "] ");
			}
		}
		return sb;
	}
	
	
/**
 * This method iterates the matrix horizontally to look for a word
 */
	private static void iterateHorizontally1() {

		for (int i = 0; i < COLUMN_COUNT; i++) {
			Iterator itr = inputWords.iterator();
			String word2 = columnsLines.get(i);
			while (itr.hasNext()) {
				String word = (String) itr.next();
				if (word2.contains(word)) {
					itr.remove();
					int start = word2.indexOf(word);
					int end = start + word.length();
					System.out.println(word + " " + getHorizontalOrder(end - 1, i, word.length(), true));
					count++;
				} else if (word2.contains(new StringBuffer(word).reverse().toString())) {
					itr.remove();
					int start = word2.indexOf(new StringBuffer(word).reverse().toString());
					int end = start + word.length();
					System.out.println(word + " " + getHorizontalOrder(end - 1, i, word.length(), false));
					count++;

				}
			}
		}
	}
	
	
}