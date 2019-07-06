import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;

//....garbage collector, makes the application too slow

public class IndexingWithDiagram {

	Hashtable<String, Hashtable<String, Integer>> dictionary = new Hashtable<String, Hashtable<String, Integer>>();
//	Scanner s = null;
//	String docName = null;
//	String PageUrl = null;
	String fileName = "F:\\Simplest Search Engine\\Dictionary_And_currently_IndexedFileNumber\\Dictionary.obj";
	HashSet<String> stopWords = new HashSet<String>();
	
	public IndexingWithDiagram(){
		System.out.println("Loading previous index. Please wait...");
		deSerializeHashtable(fileName);
		deSerializeStopWords("F:\\Simplest Search Engine\\StopWords.obj");

	}
	
	public synchronized void checkForSingleWord(String doc1){
		Scanner s = null;
		String PageUrl = null;
		try {
			s = new Scanner(new File(doc1));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		if(s.hasNext()) {
			PageUrl = s.nextLine();
//			System.out.println(PageUrl);
		}
		while(s.hasNext()){
			String w = s.next().toLowerCase(); // single-word collection
			
//			if(w.length() == 1){ // not sure about it
//				continue;
//			}
			if(stopWords.contains(w)){
//				System.out.println(w+": avoiding stopwords");
				continue;
			}
//			System.out.println(w);
			if(dictionary.containsKey(w)){
				Hashtable<String, Integer> x = dictionary.get(w);
				if(x.containsKey(PageUrl)){
					x.put(PageUrl, x.get(PageUrl)+1);
				}
				else{
					x.put(PageUrl, 1);
				}
				dictionary.put(w, x);
//				System.out.println(w+" put");
			}
			else{
				Hashtable<String, Integer> x = new Hashtable<String, Integer>();
				x.put(PageUrl, 1);
				dictionary.put(w, x);	
//				System.out.println(w+" put");
			}
//			System.gc(); //grabage collector, makes the application too slow
		}
		s.close();
//		System.gc(); //grabage collector
	}
	
	public synchronized void checkForDoubleWord(String doc1){
		Scanner s = null;
		String PageUrl = null;
		try {
			s = new Scanner(new File(doc1));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		if(s.hasNext()) {
			PageUrl = s.nextLine();
//			System.out.println(PageUrl);
		}
//		String w1 = null;
//		if(s.hasNext()){
//			w1 = s.next().toLowerCase(); // double-word collection
//		}
		String w1 = "";
		while(s.hasNext() && (w1 = s.next())!=null && stopWords.contains(w1)){
//			System.out.println(w1+": avoiding stopwords");
		}; // double-word collection
		
		while(s.hasNext()){
			String w2 = s.next().toLowerCase(); // double-word collection
			if(stopWords.contains(w2)){
//				System.out.println(w2+": avoiding stopwords");
				continue;
			}
			String w = w1+" "+w2;
//			if(w.length() == 1){ // not sure about it
//				continue;
//			}
			
//			System.out.println(w);
			if(dictionary.containsKey(w)){
				Hashtable<String, Integer> x = dictionary.get(w);
				if(x.containsKey(PageUrl)){
					x.put(PageUrl, x.get(PageUrl)+1);
				}
				else{
					x.put(PageUrl, 1);
				}
				dictionary.put(w, x);
//				System.out.println(w+" put");
			}
			else{
				Hashtable<String, Integer> x = new Hashtable<String, Integer>();
				x.put(PageUrl, 1);
				dictionary.put(w, x);
//				System.out.println(w+" put");
			}
			w1 = w2;
//			System.gc(); //grabage collector
		}
		s.close();
//		System.gc(); //grabage collector
	}
	
	
	public void deSerializeHashtable(String fileName){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try{
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			dictionary = (Hashtable<String, Hashtable<String, Integer>>)ois.readObject(); 
			System.out.println("Previous Index Access successful."); 
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
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
	
	
	public void deSerializeStopWords(String fileName){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try{
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			stopWords = (HashSet<String>) ois.readObject();
			System.out.println("StopWords Access successful.");
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
	
	public void serializeObject(Object o, String fileName){
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try {
			fos = new FileOutputStream(fileName);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
//			System.out.println("Object Written");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
			if(oos!=null){
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
//	
//	public void main(String args[]){
//		ForASingleFile a = new ForASingleFile();
////		String v = ‪"‪C:\\Users\\USER\\Desktop\\Saifur Sir.txt";
//		String v= "D:\\9.57pm.txt";
//		a.checkInDoc("‪D:\\9.57pm.txt");
//	}
//	
}
