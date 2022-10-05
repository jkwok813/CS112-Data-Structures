package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		
		TrieNode root = new TrieNode(null,null,null);
		TrieNode childOne = new TrieNode(null,null,null);
		TrieNode tem = new TrieNode(null,null,null);
		childOne.substr = new Indexes(0, (short)0, (short)(allWords[0].length()-1));
		root.firstChild = childOne;
		String var; 
		TrieNode ptrNode = root.firstChild;
		if(allWords.length == 0) {
			return null;
		}	
		for(int i = 1; i < allWords.length; i++) {
			ptrNode = root.firstChild; //Pointer 
			String word = allWords[i];  //word = String at i 
				while(true) { //inf loop until break statement
					var = allWords[ptrNode.substr.wordIndex].substring(ptrNode.substr.startIndex,ptrNode.substr.endIndex+1);
					int length = word.length(); //length = the whole string length
					int var2L = build(allWords, var, ptrNode, word);
					String var2 = poo(allWords, var, ptrNode, word);
					Indexes ind = new Indexes(ptrNode.substr.wordIndex, (short)(ptrNode.substr.startIndex + var2L), (short)ptrNode.substr.endIndex);
					if(!var2.isEmpty()) { 
					tem = new TrieNode(ind, null, null); //tem is equal to new TrieNode, 
					}
					if(!var2.isEmpty()) {
						if (var2.equals(var)){
						ptrNode = ptrNode.firstChild;
					}	
						if(!var2.equals(var)) {
							if (ptrNode.firstChild == null) {
							ptrNode.firstChild = tem;
							ptrNode.substr.endIndex = (short)(ptrNode.substr.startIndex + var2L-1);
							ptrNode = ptrNode.firstChild;
						} 
						if(ptrNode.firstChild != null) {
							tem.firstChild = ptrNode.firstChild;
							ptrNode.firstChild = tem;
							ptrNode.substr.endIndex = (short)(ptrNode.substr.startIndex + var2L-1);
							ptrNode = ptrNode.firstChild;
						}//end of !=null 
		 			}//end of !var2.equals(var)
				}//end of isempty 	
					else{
					if (ptrNode.sibling == null) {
						ptrNode.sibling = new TrieNode(new Indexes(i, ptrNode.substr.startIndex, (short)(length-1)), null,null);
						break;
					} 
					else {
						ptrNode = ptrNode.sibling;
					}
				} 
			} //end of while loop		
		} //end of for loop
		return root;
	}
	private static int build(String[] allWords, String var, TrieNode ptrNode, String word) {
		var = allWords[ptrNode.substr.wordIndex].substring(ptrNode.substr.startIndex,ptrNode.substr.endIndex+1);
		String var2 = "";
		for (int count = ptrNode.substr.startIndex; count <= ptrNode.substr.endIndex; count++) {//either runs until count <= word length or there's a difference in letters.
			if (word.charAt(count) != var.charAt(count - ptrNode.substr.startIndex)) {
			break;
		}
			var2 = var.substring(0, count - ptrNode.substr.startIndex + 1); //Rewriting var2 until there is a difference
		} //end of for loop
		int var2L = var2.length(); //length of var2
		return var2L;
	}
	private static String poo(String[] allWords, String var, TrieNode ptrNode, String word) {
		var = allWords[ptrNode.substr.wordIndex].substring(ptrNode.substr.startIndex,ptrNode.substr.endIndex+1);
		String var2 = "";
		for (int count = ptrNode.substr.startIndex; count <= ptrNode.substr.endIndex; count++) {//either runs until count <= word length or there's a difference in letters.
			if (word.charAt(count) != var.charAt(count - ptrNode.substr.startIndex)) {
			break;
		}
			var2 = var.substring(0, count - ptrNode.substr.startIndex + 1); //Rewriting var2 until there is a difference
		} //end of for loop
		return var2;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		ArrayList<TrieNode> end = new ArrayList<TrieNode>();
		if (root == null) {
			return null;
		}
		TrieNode ptrNode = root.firstChild;
		
		while(ptrNode != null) {
			String word;
			if(ptrNode.firstChild == null) { 
				word = allWords[ptrNode.substr.wordIndex];
			} else {
				word = allWords[ptrNode.substr.wordIndex].substring(0, ptrNode.substr.endIndex + 1);
			}
			if(prefix.contains(word)) {
				if(word.equals(allWords[ptrNode.substr.wordIndex])){
					end.add(ptrNode);
					ptrNode = ptrNode.sibling;
				}else {
					end.addAll(completionList(ptrNode,allWords,prefix));
					ptrNode = ptrNode.sibling;
				}
			}
			else if(word.contains(prefix)) {
				if(word.equals(allWords[ptrNode.substr.wordIndex])){
					end.add(ptrNode);
					ptrNode = ptrNode.sibling;
				}else {
					end.addAll(completionList(ptrNode,allWords,prefix));
					ptrNode = ptrNode.sibling;
				}
			}
			else {
				ptrNode = ptrNode.sibling;
			}
		}//end of while loop
		if(!end.isEmpty()) {
		return end;
		}
		else return null;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
