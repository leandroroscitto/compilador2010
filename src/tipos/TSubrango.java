package tipos;

public class TSubrango extends TSimple {
	public int base;
	public int tamano;
	
	public TSubrango(int b, int t){
		super("Subrango",TTipo.TPSUBRANGO);
		base = b;
		tamano = t;
	}
	
	public boolean comparar(TTipo tipo){
		if (tipo.clase == TPSUBRANGO){
			TSubrango ts = (TSubrango)tipo;
			return (ts.base == this.base) && (ts.tamano == this.tamano);
		}else{
			return false;
		}
	}
}
