public class Ex1 extends Thread{
	private int contador;

	public Ex1(int num){
		this.contador = num;
	}

	public void run(){
		System.out.println(contador);
	}

	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]);
		int I = Integer.parseInt(args[1]);
		Thread t[] = new Thread[N];

		for(int i = 0; i < N; i++){
			t[i] = new Thread(new Ex1(i));
		}

		for(int i = 0; i < I; i++){
			t[i].start();
		}
	}
}