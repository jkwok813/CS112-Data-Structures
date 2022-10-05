package lse;

import java.io.*;
import java.util.*;



/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		HashMap<String, Occurrence> map = new HashMap<String, Occurrence>();
		Scanner scan = new Scanner(new File(docFile));
		while (scan.hasNext()) {
			String a = getKeyword(scan.next()); 
				if (a != null) {
					if(map.containsKey(a)) {
						Occurrence b = map.get(a);
						b.frequency++;
					}
					else {
						Occurrence b = new Occurrence(docFile, 1);
						map.put(a, b);
					}
				}
		}
		return map;
	}
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		for(String key : kws.keySet()) {
			ArrayList<Occurrence> list = new ArrayList<Occurrence>();
			if(keywordsIndex.containsKey(key)) {
				list = keywordsIndex.get(key);
			}
			list.add(kws.get(key));
			insertLastOccurrence(list);
			keywordsIndex.put(key, list);
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		if(word == null) {
			return null;
		}
		word = word.toLowerCase();
		int h = word.length()-1;
		int i = 0;
			while(i < h+1) {
				if((word.charAt(i) == '.')||(word.charAt(i) == ',')||(word.charAt(i) == '?')||(word.charAt(i) == ':')||(word.charAt(i) == ';')||(word.charAt(i) == '!')){
					System.out.println("punc detected");
					int test = word.length()-1;
					System.out.println(word.charAt(test));
					word = word.substring(0, i);
					i++;
					break;
				}
				else i++;
			}//end of while loop
			System.out.println(word);
			if(noiseWords.contains(word)) {
				return null;
			}
			for(int z = word.length()-1; z>=0; z--){
				if (!Character.isLetter(word.charAt(z))){
					return null;
				}
			}	
			return word;	
			
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		if (occs.isEmpty()){
			return null;
		}
		ArrayList<Integer> a = new ArrayList<Integer>();
		int low = 0;
		int high = occs.size()-2;
		Occurrence num = occs.get(occs.size() - 1);
		if(num.frequency > high) {
			return a;
		}
		if(num.frequency < low) {
			occs.add(low-1, num);
			occs.remove(occs.size()-1);
			return a;
		}
		else {
			int mid = 0;
			while(high >= low) { //binary search
			mid = (low + high) / 2;
			a.add(mid);
			if (occs.get(mid) == num) {
				break;
			}
			else if(num.frequency < occs.get(mid).frequency) {
				low = mid + 1;
			}
			else if (num.frequency > occs.get(mid).frequency) {
				high = mid - 1;
			}
		} //end of while loop
		occs.remove(occs.size()-1);
		occs.add(mid+1, num);
		} //end of else loop
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return a;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		System.out.println("HERE BEGINS TOP5");
		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Occurrence> list1 = new ArrayList<Occurrence>();
		ArrayList<Occurrence> list2 = new ArrayList<Occurrence>();
		ArrayList<Occurrence> comp = new ArrayList<Occurrence>();
		ArrayList<Occurrence> list3 = new ArrayList<Occurrence>();
		ArrayList<Occurrence> whee = new ArrayList<Occurrence>();
		if(keywordsIndex.containsKey(kw1)){
			list1 = keywordsIndex.get(kw1);
		}
		if(keywordsIndex.containsKey(kw1)){
			list2 = keywordsIndex.get(kw2);
		}
		list3.addAll(list1);
		list3.addAll(list2);
		if(list1.isEmpty() && list2.isEmpty()) {
			System.out.println("Not found");
			return null;
		}
		else if(list1.isEmpty() || list2.isEmpty()) {
			System.out.println("only in one list");
			int a = 0;
			while(a < 5) {
				result.add(list3.get(a).document);
			}
			return result;
		}
		////////////////////////////////// POINTBREAK
		else {
			int i = 0;
			int w = 0;
			while(!list3.isEmpty()) { //Looks for the top 11 largest frequencies, adds their occurrences into result.
				int h = 0;
				while(h < list3.size()){
					System.out.println("cry");
					int temp = 0;
					if(list3.get(h).frequency > temp) {
						temp = list3.get(h).frequency;
						w = h;
						h++;
					}
				}
				result.add(list3.get(w).document);
				list3.remove(w);
				i++;
			}//end of first while
				dupe(result);
				while(result.size() > 5) {
					result.remove(result.size()-1);
				}
				System.out.println("HERE IS THE RESULT ");
				return result;
			
		}//end of else 
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
	}

private static ArrayList<String> dupe(ArrayList<String> result) {
	for (int x = 0; x < result.size()-1; x++) //removes dupes
	{
		for (int y = x + 1; y < result.size(); y++)
		{
			if (result.get(x) == result.get(y)) {
			result.remove(y);
			}
		}	
	}
	return result;
}
}