import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;
import java.util.Set;

// Using Diagrams
// needs much more heap space

public class MainI {

	public static int limit = 125538;
	public static int i = 124248;
	
	public static void main(String args[]){

//		IndexingWithWordPosition b= new IndexingWithWordPosition();
		IndexingWithDiagram b = new IndexingWithDiagram();
		
		Scanner s = new Scanner(System.in);
		while(true){
			System.out.println("Do you want to continue?(press n to break, others to continue)");
			if(s.next().toLowerCase().equals("n")){
				System.out.println("Exiting...");
				break;
			}

//			i= deSerialize_InitialFileNumber("F:\\Simplest Search Engine\\Dictionary_And_currently_IndexedFileNumber\\currentFile.obj");
//			limit = deSerialize_TotalFileNumber("F:\\Simplest Search Engine\\FileNumber.txt");
//			limit = i+10000;
			System.out.println("Indexed up to file:"+(i-1));
			System.out.println("Next indexing limit: "+(limit-1));
			if(i>=limit){
				System.out.println("fileNumber crossed limit! Exiting...");
				break;
			}

			//here comes the threading part:
			Object lock = new Object();
			int ThreadNumber = 20;
			Inverted_Index_Thread[] t = new Inverted_Index_Thread[ThreadNumber];
			for(int i=0; i<ThreadNumber; i++){
				t[i] = new Inverted_Index_Thread(b, lock);
			}
			for(int i=0; i<ThreadNumber; i++){
				try {
					t[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//threading part over.
			
//			System.out.println(b.dictionary);
			
			System.out.println("Do you want to serialize?(press n to break, others to continue)");
			if(s.next().toLowerCase().equals("n")){
				System.out.println("Serialization canceled. Exiting...");
				break;
			}
			System.out.println("Serializing last indexed file-number and Inverted Index.\nPlease wait...");
//			b.serializeObject(new Integer(i), "F:\\Simplest Search Engine\\Dictionary_And_currently_IndexedFileNumber\\currentFile.obj");
			b.serializeObject(b.dictionary, "F:\\Simplest Search Engine\\Dictionary_And_currently_IndexedFileNumber\\Dictionary.obj");
			System.out.println("Indexed up to file "+(i-1));
			
			System.out.println("Collecting garbage to get more free heap memory. Please wait...");
			System.gc(); //garbage collector
		}
	}
	
	public static int deSerialize_InitialFileNumber(String fileName){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		int i = 0;
		try{
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			i = (Integer)ois.readObject();
			
			System.out.println("Current file-number Read"); 
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
			System.out.println("Current file-number not found");
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
		return i;
	}
		
	public static int deSerialize_TotalFileNumber(String fileName){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		int fileNumber = 0;
		try{
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			fileNumber = (Integer)ois.readObject();
			System.out.println("Total file-number Read: "+fileNumber);
//			System.out.println(fileNumber); 
		} catch (FileNotFoundException e) {
//				e.printStackTrace();
			System.out.println("Total file-Number not found");
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
		return fileNumber;
	}

	
}
