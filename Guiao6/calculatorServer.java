import java.io.*;
import java.net.*;

public class calculatorServer{

	public static void main(String args[]) throws IOException, UnknownHostException{
		ServerSocket ss = new ServerSocket(9999);

		Socket cs = ss.accept();

		PrintWriter out = new PrintWriter(cs.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));

		int sum = 0;
		int count = 0;
		String current;
		while((current = in.readLine()) != null){
			int val = Integer.parseInt(current);
			sum += val;
			count++;
			System.out.println("Recebido: " + current);
			out.println("Total: " + sum);

		}


		out.println("Média: " + sum/count);
		in.close();
		
		out.close();
		cs.close();
		ss.close();

	}
}