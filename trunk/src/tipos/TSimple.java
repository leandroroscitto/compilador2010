package tipos;

public class TSimple extends TTipo{
	
	public TSimple(String n, int c){
		super(n,c,1);
	}
	
	public boolean esTipoSimple(){
		return true;
	}
}
