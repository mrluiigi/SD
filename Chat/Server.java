import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.ArrayList;


class Log{
	private final ArrayList<String> log;
	private final ReentrantLock l = new ReentrantLock();
	private final Condition aLer = l.newCondition();

	public Log(){
		this.log = new ArrayList<>();
	}

	public void add(String message){
		l.lock();
		try{
			this.log.add(message);
			this.aLer.signalAll();
		}finally{
			l.unlock();
		}
	}

	public String read(int linha){
		l.lock();
		try{
			while(this.log.size() < linha){
				try{
					aLer.await();
				}catch(Exception e){System.out.println("Erro read Log");}
			}
			String res = this.log.get(linha-1);
			return res;
		}finally{
			l.unlock();
		}
	}
}

class ClientLog implements Runnable{
	Socket s;
	Log l;

	public ClientLog(Socket s, Log l){
		this.s = s;
		this.l = l;
	}

	public void run(){
		try{
			BufferedReader fromClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String message = "";
			while(true){
				message = fromClient.readLine();
				l.add(message);
			}
		}catch(Exception e){System.out.println("Erro ClientLog");}
	}
}

class LogClient implements Runnable{
	Socket s;
	Log l;

	public LogClient(Socket s, Log l){
		this.s = s;
		this.l = l;
	}

	public void run(){
		try{
			PrintWriter toClient = new PrintWriter(s.getOutputStream(), true);
			int count = 1;
			String message = "";
			while(true){
				message = l.read(count);
				toClient.println(message);
				count++;
			}
		}catch(Exception e){System.out.println("Erro LogClient");}
	}
}



class Server{

	public static void main(String[] args) throws Exception{
		ServerSocket ss = new ServerSocket(9999);
		Log log = new Log();
		while(true){
			Socket cs = ss.accept();
			new Thread(new ClientLog(cs, log)).start();
			new Thread(new LogClient(cs, log)).start();
		}
	}
}