
public class Inverted_Index_Thread extends Thread{

	IndexingWithDiagram b = null;
	Object lock = null;	
	public Inverted_Index_Thread(IndexingWithDiagram b, Object lock){
		this.b = b;
		this.lock = lock;
		this.start();
	}
	
	public void run(){
		while(MainI.i<MainI.limit){
			String v = null;
			synchronized (lock) {
				v = "F:\\Simplest Search Engine\\CrawlerBot\\File_"+MainI.i+".txt";
				MainI.i++;
			}
			System.out.println(this.getName()+">\n"+v);
			try{
				b.checkForSingleWord(v);
				b.checkForDoubleWord(v);
			}catch(Exception e){
				e.printStackTrace();
			}
//			System.gc(); //gabage collector
		}
		
		System.out.println("Done Indexing in: "+ this.getName());
	}

}
