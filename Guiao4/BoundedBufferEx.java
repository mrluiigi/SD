class BoundedBuffer{
		
	private int[] buffer;
	private int curr;

	public BoundedBuffer(int tamanho){
		this.buffer = new int[tamanho];
		this.curr = 0;
	}

	public synchronized void put(int v){
		while(curr >= this.buffer.length){
			try{
				wait();
			}catch(InterruptedException e){System.out.println("Buffer cheio");}
		}
		this.buffer[curr] = v;
		System.out.println("Adicionado: " + v);
		this.curr++;
		notifyAll();
	}

	public synchronized int get(){
		while(curr == 0){
			try{
				wait();
			}catch(InterruptedException e){System.out.println("Buffer vazio");}
		}
		int res = this.buffer[0];
		System.out.println("Retirado: " + res);
		this.curr--;
		for(int i = 0; i < curr; i++){
			this.buffer[i] = this.buffer[i+1];
		}
		notifyAll();
		return res;
	}
}

class Produtor implements Runnable{
	BoundedBuffer b;

	public Produtor(BoundedBuffer b){
		this.b = b;
	}

	public void run(){
		this.b.put(2);
	}
}

class Consumidor implements Runnable{
	BoundedBuffer b;

	public Consumidor(BoundedBuffer b){
		this.b = b;
	}

	public void run(){
		this.b.get();
	}
}

class BoundedBufferEx{
	public static void main(String[] args){
		BoundedBuffer buffer = new BoundedBuffer(5);
		Produtor p = new Produtor(buffer);
		Consumidor c = new Consumidor(buffer);
		Thread[] t = new Thread[10];
		for(int i = 0; i < 7; i++){
			t[i] = new Thread(p);
		}
		for(int j = 7; j < 10; j++){
			t[j] = new Thread(c);
		}

		for(int i = 0; i < 10; i++){
			t[i].start();
		}
	}
}