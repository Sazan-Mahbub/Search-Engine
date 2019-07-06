import java.util.ArrayList;

public class ServerThread extends Thread{
	ClientData cdataControl;
	A a;
	
	public ServerThread(ClientData cdataControl, A a){
		this.cdataControl = cdataControl;
		this.a = a;
		this.start();
	}
	
	public void run(){
		try{
			while(true){
				String s = (String) cdataControl.read();
				System.out.println(this.getName()+" User's search-word: "+s);
				a.searchTheWord(s);
				cdataControl.write(a.finalResultList);
				System.out.println("Search Result Sent.");
				System.gc(); // garbage collector
			}
		}catch(Exception e){
//			e.printStackTrace();
			System.out.println("Client went offline.");
		}
	}
	
}
