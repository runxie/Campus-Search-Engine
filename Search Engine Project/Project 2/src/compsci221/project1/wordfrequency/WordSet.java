package compsci221.project1.wordfrequency;

import java.util.HashMap;
import java.util.HashSet;

public class WordSet {

	// get a intersection of two files
	public HashSet<String> intersectionWords(String file1, String file2) {
		HashSet<String> intersections = new HashSet<String>();
		WordFrequency wf1 = new WordFrequency();
		WordFrequency wf2 = new WordFrequency();
		HashMap<String, Integer> file1Words = wf1.computerWordFrequencies(wf1.tokenize(file1));
		HashMap<String, Integer> file2Words = wf2.computerWordFrequencies(wf2.tokenize(file2));
			
		for(String key: file1Words.keySet()) {
			if(file2Words.containsKey(key)) intersections.add(key);
		}
			
		return intersections;
	}
		
	// get a reduce of two files
	public HashMap<String, Integer> reduceWords(String file1, String file2) {
		WordFrequency wf1 = new WordFrequency();
		WordFrequency wf2 = new WordFrequency();
		HashMap<String, Integer> file1Words = wf1.computerWordFrequencies(wf1.tokenize(file1));
		HashMap<String, Integer> file2Words = wf2.computerWordFrequencies(wf2.tokenize(file2));
		
		for(String word : file1Words.keySet()) {
			if(file2Words.containsKey(word)) {
				int freq = file2Words.get(word);
				file2Words.replace(word, freq + file1Words.get(word));
			} else {
				file2Words.put(word, file1Words.get(word));
			}
		}
			
		return file2Words;
	}	
}
