package compsci221.project1.wordfrequency;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class WordFrequency {
	
	private HashMap<String, Integer> frequencies;
	private Scanner file;
	List<String> tokenList;
	
	public WordFrequency() {
		frequencies = new HashMap<String, Integer>();
		tokenList = new ArrayList<String>();
	}
	
	// write texts in a file and return a list of the tokens
	public List<String> tokenize(String textFilePath) {
		openFile(textFilePath);
		readFile();
		
		closeFile();
		return tokenList;
	}
	
	// count the number off occurrences of each word in the token list	
	public HashMap<String, Integer> computerWordFrequencies(List<String> words) {
		//frequencies = new HashMap<String, Integer>();
		
		Iterator<String> itr = tokenList.iterator();
		while(itr.hasNext()) {
			String word = itr.next();
			if(frequencies.containsKey(word) == false) {
				frequencies.put(word, 1);
			} else {
				int frequency = frequencies.get(word);
				frequencies.replace(word, frequency + 1);
			}
		}
		
		return frequencies;
	}
	
	// print words and their frequencies
	public void print(Map<String, Integer> frequencies) {
		for(String word : frequencies.keySet()) {
			System.out.println(word + " : " + frequencies.get(word));
		}
	}
	
	
		
	// file operations
	private void openFile(String fileName) {
		try {
			file = new Scanner(new File(fileName));
		} catch(Exception e) {
			System.out.println("Could not find file");
		}
	}
	
	private void readFile() {
		while(file.hasNext()) {
			tokenList.add(file.next().toLowerCase());
		}
	}
	
	private void closeFile() {
		file.close();
	}
	
	
}
