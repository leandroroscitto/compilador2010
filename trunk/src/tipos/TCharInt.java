package tipos;

public class TCharInt extends TSimple {
	
	public TCharInt(String n, int c){
		super(n,c);
	}
	
	public TCharInt(){
		super("Char o Integer", TTipo.TPCHARINT);
	}
	
	public boolean comparar(TTipo t){
		return (t.clase == TPENTERO || t.clase == TPCARACTER);
	}
}
