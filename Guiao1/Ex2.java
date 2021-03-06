class Counter
{
	private long i;

	public long get(){
		return i;
	}

	public void inc(){
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
		final long I = 100;
		for(int i = 0; i < I; i++){
			c.inc();
		}
	}

}

class Ex2 extends Thread
{

	public static void main(String args[]){
		try{
			final int N = 10;
			Counter c = new Counter();
			Incrementer worker = new Incrementer(c);
			Thread t[] = new Thread[N];

			for(int i = 0; i < N; i++){
				t[i] = new Thread(worker);
			}

			for(int i = 0; i < N; i++){
				t[i].start();
			}

			for(int i = 0; i < N; i++){
				t[i].join();
			}
			System.out.println(c.get());

		}catch(InterruptedException e) {}
	}
}