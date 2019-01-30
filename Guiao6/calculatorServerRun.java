import java.io.*;
import java.net.*;

class serverRunnable implements Runnable{

	Socket cs;

	public serverRunnable(Socket clients){
		cs = clients;
	}


	public void run(){
		try{
			PrintWriter out = new PrintWriter(cs.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));

			int sum = 0;
			int count = 0;
			String current;
			while((current = in.readLine()) != null){
				System.out.println("Recebido: " + current);
				int val = Integer.parseInt(current);
				sum += val;
				count++;
				out.println("Total: " + sum);
			}

			out.println("Média: " + sum/count);
			out.close();
			in.close();

			cs.close();
		}catch(Exception e) {System.out.println("Erro");}	
	}
}

public class calculatorServerRun{

	public static void main(String args[]) throws IOException{
		ServerSocket ss = new ServerSocket(9999);
		while(true){
			Socket cs = ss.accept();
			new Thread (new serverRunnable(cs)).start();
		}
	}
}