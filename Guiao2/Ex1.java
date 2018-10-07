class Counter
{
	private long i;

	public synchronized long get(){
		return i;
	}

	public synchronized void inc(){
		i++;
	}
}

class Incrementer implements Runnable
{
	Counter c;

	public Incrementer(Counter cshared){
		c = cshared;
	}

	public void run(){
		final long I = 100000;
		for(int i = 0; i < I; i++) /*synchronized (c) */{
			c.inc();
		}
	}

}

class Ex1 extends Thread
{

	public static void main(String args[]){
		final int N = 10;
		Counter c;
		Incrementer worker;
		Thread t[] = new Thread[N];
		c = new Counter();
		worker = new Incrementer(c);
		
		try{
			for(int i = 0; i < N; i++){
				t[i] = new Thread(worker);
			}

			for(int i = 0; i < N; i++){
				t[i].start();
			}

			for(int i = 0; i < N; i++){
				t[i].join();
			}

		}catch(InterruptedException e) {}

		System.out.println(c.get());
	}
}