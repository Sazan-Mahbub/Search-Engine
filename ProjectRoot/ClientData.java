import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientData {
	Socket clientSocket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	public ClientData(Socket clientSocket){
		try{
			this.clientSocket = clientSocket;
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			ois = new ObjectInputStream(clientSocket.getInputStream());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Object read(){
		Object obj = null;
		try {
			obj=ois.readObject();
		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("Client not found.");
		}
		return obj;
	}
	
	public void write(Object obj){
		try {
			oos.writeObject(obj); 
			oos.flush();
		} catch (IOException e) {
//			e.printStackTrace();
			
		}
	}
	
	public void closeAllStreams(){
		try{
			oos.close();
			ois.close();
			clientSocket.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
