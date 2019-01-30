import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
	@Author mrLuiigi
*/

class RWLock{

	int val;
	int readers;
	int writers;
	final ReentrantLock lock;
	Condition reading;
	Condition writing;
	int nwriters;

	public RWLock(){
		this.val = 0;
		this.readers = 0;
		this.writers = 0;
		this.lock = new ReentrantLock();
		this.reading = lock.newCondition();
		this.writing = lock.newCondition();
		this.nwriters = 0;
	}

	public void readLock(){
		lock.lock();
		try{
			while(writers != 0 || nwriters > 0)
				this.reading.await();
			readers++;
		}
		catch(Exception e){}
		finally{
			lock.unlock();
		}
	}

	public void readUnlock(){
		lock.lock();
		try{
			readers--;
			if(readers == 0)
				this.writing.signal();
		}finally{
			lock.unlock();
		}
	}

	public void writeLock(){
		lock.lock();
		this.nwriters++;
		try{
			while(readers != 0 || writers != 0){
				this.writing.await();
			}
			this.writers++;
			this.nwriters--;
		}
		catch(Exception e){}
		finally{
			lock.unlock();
		}
	}

	public void writeUnlock(){
		lock.lock();
		try{
			writers--;
			if(this.nwriters > 0){
				this.writing.signalAll();
			}
			else{
				this.reading.signalAll();
			}
		}finally{
			lock.unlock();
		}
	}

	public int read(){
		readLock();
		int aux = val;
		readUnlock();
		return aux;
	}

	public void write(int v){
		writeLock();
		val = v;
		writeUnlock();
	}
}

class Reader implements Runnable{
	RWLock rw;
	int i;
	public Reader(RWLock r){
		rw = r;
	}

	public void run(){
		System.out.println(this.i + "  vai ler");
		int valor = rw.read();
		System.out.println(this.i + "  leu " + valor);
	}
}

class Writer implements Runnable{
	RWLock rw;
	int i;
	public Writer(RWLock r){
		rw = r;
	}

	public void run(){
		System.out.println(this.i + "  vai escrever");
		rw.write(45);
		System.out.println(this.i + "  escreveu 45");

	}
}


class RWLockEx{
	public static void main(String[] args){
		RWLock rw = new RWLock();
		//Reader r = new Reader(rw);
		//Writer w = new Writer(rw);
		Thread[] t = new Thread[10];
		for(int i = 0; i < 5; i++){
			t[i] = new Thread(new Reader(rw));
		}
		for(int i = 5; i < 10; i++){
			t[i] = new Thread(new Writer(rw));
		}
		for(int i = 0; i < 10; i++){
			t[i].start();
		}
	}
}