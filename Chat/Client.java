import java.io.*;
import java.net.*;

class ClientServer implements Runnable{
	Socket s;

	public ClientServer(Socket s){
		this.s = s;
	}

	public void run(){
		try{
			PrintWriter toServer = new PrintWriter(s.getOutputStream(), true);
			BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));
			
			String message = "";
			while(true){
				message = fromKeyboard.readLine();
				toServer.println(message);
			}

		}catch(Exception e){System.out.println("Erro ClientServer");}
	}
}

class ServerClient implements Runnable{
	Socket s;

	public ServerClient(Socket s){
		this.s = s;
	}

	public void run(){
		try{
			BufferedReader fromServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			String message = "";
			while(true){
				message = fromServer.readLine();
				System.out.println(message);
			}

		}catch(Exception e){System.out.println("Erro ServerClient");}
	}
}

public class Client{

	public static void main(String[] args) throws Exception{
		Socket s = new Socket("127.0.0.1", 9999);
		
		new Thread(new ClientServer(s)).start();
		new Thread(new ServerClient(s)).start();
	}

}