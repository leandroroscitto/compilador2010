package tipos;

public class TCharIntSub extends TSimple {
	
	public TCharIntSub(String n, int c){
		super(n,c);
	}
	
	public TCharIntSub(){
		super("Char o Integer", TTipo.TPCHARINT);
	}
	
	public boolean comparar(TTipo t){
		return (t.clase == TPENTERO || t.clase == TPCARACTER || t.clase == TPSUBRANGO);
	}
}
