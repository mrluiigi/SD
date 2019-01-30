import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

class BoundedBufferClass{
		
	private int[] buffer;
	private int curr;
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition cheio = lock.newCondition();
	private final Condition vazio = lock.newCondition();

	public BoundedBufferClass(int tamanho){
		this.buffer = new int[tamanho];
		this.curr = 0;
	}

	public void put(int v){
		lock.lock();
		try{
			while(curr >= this.buffer.length){
				try{
					this.cheio.await();
				}catch(InterruptedException e){System.out.println("Buffer cheio");}
			}
			this.buffer[curr] = v;
			System.out.println("Adicionado: " + v);
			this.curr++;
			this.vazio.signal();
		}finally{
			lock.unlock();
		}
	}

	public int get(){
		lock.lock();
		try{
			while(curr == 0){
				try{
					this.vazio.await();
				}catch(InterruptedException e){System.out.println("Buffer vazio");}
			}
			int res = this.buffer[0];
			System.out.println("Retirado: " + res);
			this.curr--;
			for(int i = 0; i < curr; i++){
				this.buffer[i] = this.buffer[i+1];
			}
			this.cheio.signal();
			return res;
		}finally{
			lock.unlock();
		}
	}
}

class Produtor implements Runnable{
	BoundedBufferClass b;

	public Produtor(BoundedBufferClass b){
		this.b = b;
	}

	public void run(){
		this.b.put(2);
	}
}

class Consumidor implements Runnable{
	BoundedBufferClass b;

	public Consumidor(BoundedBufferClass b){
		this.b = b;
	}

	public void run(){
		this.b.get();
	}
}


class BoundedBuffer{
	public static void main(String[] args){
		BoundedBufferClass buffer = new BoundedBufferClass(5);
		Produtor p = new Produtor(buffer);
		Consumidor c = new Consumidor(buffer);
		Thread[] t = new Thread[10];
		for(int i = 0; i < 6; i++){
			t[i] = new Thread(p);
		}
		for(int j = 6; j < 10; j++){
			t[j] = new Thread(c);
		}

		for(int i = 0; i < 10; i++){
			t[i].start();
		}
	}
}