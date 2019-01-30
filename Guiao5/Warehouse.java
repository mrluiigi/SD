import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.HashMap;

class Item{
	String nome;
	int quantidade;
	Condition emptyCondition;

	public Item(String nome, int q, Condition c){
		this.nome = nome;
		this.quantidade = q;
		this.emptyCondition = c;
	}
}

class Warehouse{

	HashMap<String, Item> armazem;
	final ReentrantLock lock = new ReentrantLock();

	public Warehouse(){
		this.armazem = new HashMap<>();
	}

	void supply(String item, int quantity){
		lock.lock();
		try{
			if(this.armazem.containsKey(item)){
				this.armazem.get(item).quantidade += quantity;
				this.armazem.get(item).emptyCondition.signalAll();
			}
			else{
				this.armazem.put(item, new Item(item, quantity, lock.newCondition()));
			}
		}finally{
			lock.unlock();
		}
	}

	void consume(String[] items){
		lock.lock();
		try{
			for(int i = 0; i < items.length; i++){
				if(this.armazem.containsKey(items[i])){
					while(this.armazem.get(items[i]).quantidade == 0){
						this.armazem.get(items[i]).emptyCondition.await();
						i = 0;
					}
				}
			}
			for(String s : items){
				if(this.armazem.containsKey(s)){
					this.armazem.get(s).quantidade--;
				}
			}
		}
		catch(Exception e){}
		finally{
			lock.unlock();
		}
	}
}