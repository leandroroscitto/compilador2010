package tipos;

public class TArreglo extends TTipo {
	public int base;
	public int tamano;
	public TSimple tbase;
	
	public TArreglo(int b, int t, TSimple tb){
		super("Arreglo",TTipo.TPARREGLO,t);
		base = b;
		tamano = t;
		tbase = tb;
	}
	
	public TArreglo(TSubrango ti, TSimple tb){
		super("Arreglo",TTipo.TPARREGLO,ti.tamano);
		base = ti.base;
		tamano = ti.tamano;
		tbase = tb;
	}
	
	public boolean esTipoSimple(){
		return false;
	}
	
	public boolean comparar(TTipo tipo){
		if (tipo.clase == TPARREGLO){
			TArreglo ta = (TArreglo)tipo;
			return ((ta.tamano == this.tamano) && (ta.tbase.comparar(this.tbase)));
		}else{
			return false;
		}
	}
}
