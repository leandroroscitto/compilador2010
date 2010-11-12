package tablasimb;

import java.util.ArrayList;

public class Pila<E> extends ArrayList<E>{
	private static final long serialVersionUID = 1L;
	
	private int ultindice = 0;
	
	private int iteindice = 0;
	
	public Pila(){
		super();
	}
	
	public void apilar(E elemento){
		this.add(ultindice, elemento);
		ultindice++;
	}
	
	public E desapilar(){
		E elemento = this.get(ultindice);
		this.remove(ultindice);
		ultindice--;
		return elemento;
	}
	
	public E tope(){
		E elemento = this.get(ultindice);
		return elemento;
	}
	
	public int tamano(){
		return this.size();
	}
	
	public void reset(){
		iteindice = this.size();
	}

	public boolean hasNext() {
		return (iteindice>0);
	}

	public E next() {
		iteindice--;
		return this.get(iteindice);
	}
}
