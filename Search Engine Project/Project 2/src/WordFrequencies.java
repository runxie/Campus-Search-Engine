import java.util.HashMap;
import java.util.List;

import compsci221.project1.wordfrequency.WordFrequency;
import compsci221.project1.wordfrequency.WordSet;



public class WordFrequencies {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WordFrequency wf = new WordFrequency();
		WordSet ws = new WordSet();
		String fileName = "/home/markpen/Desktop/CompSci 221/Project 1/Word Frequency/src/text.txt";
		String fileName2 = "/home/markpen/Desktop/CompSci 221/Project 1/Word Frequency/src/text2.txt";
		
		List<String> words = wf.tokenize(fileName);
		HashMap<String, Integer> frequencies = ws.reduceWords(fileName, fileName2);
		//HashMap<String, Integer> frequencies = wf.computerWordFrequencies(words);
		wf.print(frequencies);
		
	}

}
