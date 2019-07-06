import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class A {
//	String fileName = "F:\\Simplest Search Engine\\Dictionary_And_currently_IndexedFileNumber\\Dictionary.obj";
	Hashtable<String, Hashtable<String, Integer>> dictionary = new Hashtable<String, Hashtable<String, Integer>>();
	Set<String> setOfKeyWords;
	HashSet<String> stopWords = new HashSet<String>();
	
	boolean triedToIntersect = false;
	ArrayList<String> singleWords = new ArrayList<String>();
	ArrayList<String> DoubleWords = new ArrayList<String>();
	
	ArrayList<String> finalResultList = new ArrayList<String>();
	
	//......
	public A(Hashtable<String, Hashtable<String, Integer>> dictionary, HashSet<String> stopWords){
		this.dictionary = dictionary;
		this.stopWords = stopWords;
		setOfKeyWords = dictionary.keySet();
		
	}
	
	//......
	public void searchTheWord(String searchWord){
		singleWords = new ArrayList<String>();
		DoubleWords = new ArrayList<String>();
		finalResultList = new ArrayList<String>();
		//......
//		searchWord = searchWord.replaceAll("[\\W]", " ").toLowerCase();
		searchWord = searchWord.replaceAll("\\p{Punct}|।", " ").toLowerCase(); // সব ভাষার জন্য
		StringTokenizer st = new StringTokenizer(searchWord);
		
		int tokenNumber = st.countTokens();
		System.out.println("Tokens: "+tokenNumber);
		String w1 = "";
		while(st.hasMoreTokens() && (w1 = st.nextToken())!=null && stopWords.contains(w1));
		singleWords.add(w1);
		while(st.hasMoreTokens()){
			String w2 = st.nextToken();
			if(stopWords.contains(w2)){
				continue;
			}
			singleWords.add(w2);
			String w = w1+" "+w2;
			DoubleWords.add(w);
//			System.out.println(w);
			w1 = w2;
		}
		
//		int deletion = 0; // number of words that should be cut off
		Set<String> DoubleWord_IntersectionSet = null;
		if(DoubleWords.size()>0){
			String[] arrDouble = new String[DoubleWords.size()];
			DoubleWords.toArray(arrDouble);
			
			DoubleWord_IntersectionSet = findIntersectionSet(arrDouble); // got the most relevant links
			for(int i=0; i<arrDouble.length-1; i++){ // addition, that -ve sign
				if(DoubleWord_IntersectionSet != null){
					finalResultList.addAll(DoubleWord_IntersectionSet);
				}else{
					System.out.println("2wrds: sorry no relevent results.");
				}
				DoubleWord_IntersectionSet = findIntersectionSet(arrDouble);
				if(DoubleWord_IntersectionSet!=null){
					DoubleWord_IntersectionSet.removeAll(finalResultList);
				}
			}
		}
		
		//>>>>>
//		if(true) return; //debug
		//>>>>>
		
		finalResultList.addAll(SortedWords(DoubleWords));
		
		//....
		String[] arrSingle = new String[singleWords.size()];
		singleWords.toArray(arrSingle);
		Set<String> SingleWord_IntersectionSet = findIntersectionSet(arrSingle); // got 2nd most relevant links
		for(int i=0; i<arrSingle.length-1; i++){
			if(SingleWord_IntersectionSet != null){
				if(finalResultList!=null){
					SingleWord_IntersectionSet.removeAll((finalResultList));
				}
				finalResultList.addAll(SingleWord_IntersectionSet);
			}else{
				System.out.println("1wrds: sorry no relevent results.");
			}
			SingleWord_IntersectionSet = findIntersectionSet(arrSingle);
		}

//		finalResultList.addAll(SortedWords(DoubleWords));
		
		finalResultList.addAll(SortedWords(singleWords));
	
	} // end of method searchTheWord(String searchWord)
	
	
	
	public ArrayList<String> SortedWords(ArrayList<String> Words){
		ArrayList<Map.Entry<String, Integer>> semiFinalList = new ArrayList<Map.Entry<String, Integer>>(); 
		ArrayList<String> sortedList = new ArrayList<String>();
		for(String str: Words){
			//debug portion:
			System.out.println(str+": ");
			semiFinalList = sortByValue(hashTable_For(str));
			if(semiFinalList==null){
				System.out.println("No direct result for: "+str);
				continue;
			}
			for(Map.Entry<String, Integer> x: semiFinalList){
//				System.out.println("link: "+x.getKey());
				sortedList.add(x.getKey());
//				System.out.println("frequency: "+x.getValue());
			}//end debug portion
		}
		sortedList.removeAll(finalResultList);
		return sortedList;
	}
	public ArrayList<Map.Entry<String, Integer>> sortByValue(Hashtable<String, Integer> t){
		if(t == null){
			System.out.println("Word not found");
			return null;
		}
		ArrayList<Map.Entry<String, Integer>> arrlist = new ArrayList<Map.Entry<String, Integer>>(t.entrySet());
        Collections.sort(arrlist, new Comparator<Map.Entry<String, Integer>>(){
		    public int compare(Map.Entry<String, Integer> x, Map.Entry<String, Integer> y) {
		           return -(x.getValue().compareTo(y.getValue()));
		    }
	    }
        );

//	        System.out.println(arrlist);
//	        for(Map.Entry<?, Integer> entry: arrlist){
//	     	   System.out.println(entry.getKey());
//	        }
        return arrlist;
	 }

	// >>> find a phrase
	public Set<String> findIntersectionSet(String[] searchWords){
		
		int leastWeight = 0;
		int leastWeightPos = 0;
		Set<String> intersectionSet = null;
		triedToIntersect = true;
		int i=0;
		for(;i<searchWords.length;i++){
			if(searchWords[i].equals("//>")){
				continue;
			}
			if(setOfKeyWords.contains(searchWords[i])){
				leastWeight = dictionary.get(searchWords[i]).size();
				leastWeightPos = i;
				intersectionSet = new HashSet<String>(hashTable_For(searchWords[i]).keySet()); //instantiates new set
				break;
			}
		}
		if(i==searchWords.length){
			return null;
		}
		for(;i<searchWords.length;i++){
			if(searchWords[i].equals("//>")){
				continue;
			}
			if(setOfKeyWords.contains(searchWords[i])){
				if(dictionary.get(searchWords[i]).size()<leastWeight){
					leastWeight = dictionary.get(searchWords[i]).size();
					leastWeightPos = i;
				}
				intersectionSet.retainAll(hashTable_For(searchWords[i]).keySet());
			}
		}
		searchWords[leastWeightPos] = "//>";
		//here the intersectionSet gets a high priority
		return intersectionSet;
		
	}
	
	public Hashtable<String, Integer> hashTable_For(String word){
		if(!setOfKeyWords.contains(word)) {
			return null;
		}
		Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
		Set<String> t = dictionary.get(word).keySet();
		for(String link: t){
			ht.put(link, dictionary.get(word).get(link));
		}
		return ht;
	
	}

	public void deSerializeHashtable(String fileName){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try{
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			dictionary = (Hashtable<String, Hashtable<String, Integer>>)ois.readObject();
			System.out.println("Index Access successful.");
		} catch (FileNotFoundException e) {
	//		e.printStackTrace();
			System.out.println("File not found for dictionary");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
			if(ois!=null){
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
	}

	
}
