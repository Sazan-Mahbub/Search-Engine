import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class Server {
	String fileName = "F:\\Simplest Search Engine\\Dictionary_And_currently_IndexedFileNumber\\Dictionary.obj";
	Hashtable<String, Hashtable<String, Integer>> dictionary = new Hashtable<String, Hashtable<String, Integer>>();
//	Set<String> setOfKeyWords;
	HashSet<String> stopWords = new HashSet<String>();
	
	private ServerSocket serverSocket;
	public Integer clientNumber = 1;
	public Hashtable<ClientData, Integer> t = new Hashtable<ClientData, Integer>();
	
	//......
	public Server(){
//		System.out.println("Loading Index. Please wait...");
		deSerializeStopWords("F:\\Simplest Search Engine\\StopWords.obj");
		System.out.println("Loading Index. Please wait...");
		deSerializeHashtable(fileName);
		System.out.println("Calling garbage collector. Please wait...");
		System.gc(); // garbage collector, to retrieve some heap memory 
		System.out.println("Number of total words: "+dictionary.size());
		
		try {
			serverSocket = new ServerSocket(1234); //server port: 1234
			while(true){
				System.out.println("Waiting for Users...");
				Socket clientSocket = serverSocket.accept();
				System.out.println("User "+clientNumber+" connected.");
				ClientData cdataControl = new ClientData(clientSocket);
				t.put(cdataControl, clientNumber);
				clientNumber++;
				A a = new A(dictionary, stopWords);
				new ServerThread(cdataControl, a);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
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


}
