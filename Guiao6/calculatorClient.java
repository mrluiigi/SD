import java.io.*;
import java.net.*;

public class calculatorClient{

	public static void main(String args[]) throws IOException, UnknownHostException{
		Socket cs = new Socket("127.0.0.1", 9999);

		PrintWriter out = new PrintWriter(cs.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));

		String current;
		BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Indique o valor para soma:");
		while((current = sin.readLine()) != null){
			out.println(current);
			System.out.println(in.readLine());
			System.out.println("Indique o valor para soma:");
		}
		System.out.println("\nShutdown Output");
		cs.shutdownOutput();
		System.out.println("Waiting for final result");
		System.out.println("The answer is: " + in.readLine());
		System.out.println("Done");

		in.close();
		out.close();
		sin.close();

	}
}