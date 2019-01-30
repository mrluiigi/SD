import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

class Mover implements Runnable 
{
  Bank b;

  public Mover(Bank b) { 
    this.b = b; 
  }

  public void run() {
    Random rand = new Random();
    int slots=b.slots();
    int f;
    int t, tries;
    for(tries=0; tries<1000000; tries++){ 
      f=rand.nextInt(slots); // get one
      while((t=rand.nextInt(slots))==f); // get a distinct other. Carefull!
      b.transfer(f, t, 10);
    }
  }
}

class Adder implements Runnable 
{
  Bank b;

  public Adder(Bank b) { this.b = b; }

  public void run() 
  {
    int slots=b.slots();
    int sum, i, tries, lsum=0;
    for(tries=0; tries<1000000; tries++) 
    { 
      sum=0;
      synchronized (b){
	      for (i=0; i<slots; i++) 
	      {
	        // FAZER
          //sum = totalBalance(...);
          sum  = 0;
	      }
  	  }
      if (lsum!=sum) System.out.println("Total "+sum);
      lsum=sum;
    }
  }
}

class Conta{
  private float saldo;
  private final ReentrantLock lock = new ReentrantLock();


  public Conta(float valor){
    this.saldo = valor;
  }

  public synchronized void put(float v){
    this.saldo += v;
  }

  public synchronized void take(float v){
    this.saldo -= v;
  }

  public synchronized float getValor(){
    return this.saldo;
  }

  public void lock(){
    this.lock.lock();
  }

  public void unlock(){
    this.lock.unlock();
  }
}


class Bank
{
  private int num;
  private HashMap<Integer, Conta> contas;
  private final ReentrantLock lockBank = new ReentrantLock();

  public Bank(int num){
    this.num = 0;
    this.contas = new HashMap<>();
  }

  public int slots(){
    return this.num;
  }

  public int createAccount(float initialBalance){
    Conta c = new Conta(initialBalance);
    lockBank.lock();
    try{
      int id = this.num;
      this.num++;
      this.contas.put(id, c);
      return id;
    }finally{
      lockBank.unlock();
    }
  }

  public float closeAccount(int id){
    lockBank.lock();
    Conta c;
    float res = 0;
    try{
      c = this.contas.get(id);
      c.lock();
    }finally{
      lockBank.unlock();
    }
    try{
      res = c.getValor();
      this.contas.remove(id);
      return res;
    }finally{
      c.unlock();
    }
  }

  public void transfer(int from, int to, float amount){
    Conta cf, ct;
    lockBank.lock();
    try{
      cf = this.contas.get(from);
      ct = this.contas.get(to);
      if(from < to){
        cf.lock();
        ct.lock();
      }
      else{
        ct.lock();
        cf.lock();
      }
    }finally{
      lockBank.unlock();
    }
    try{
      cf.take(amount);
      ct.put(amount);
    }finally{
      cf.unlock();
      ct.unlock();
    }
  }

  public float totalBalance(int accounts[]){
    int size = accounts.length;
    float res = 0;
    for(int i = 0; i < size; i++){
      Conta c = this.contas.get(accounts[i]);
      c.lock();
      try{
        res += c.getValor();
      }finally{
        c.unlock();
      }
    }
    return res;
  }

}



class Ex
{
  public static void main(String[] args) 
  {
    int N = 10; // Number of accounts
    Bank b = new Bank(N);
    Mover m = new Mover(b);
    Adder c = new Adder(b);

    new Thread(m).start();
    new Thread(c).start();
  }
}

