package tablasimb;

public class Simbolo {
	public static int PROGRAMA = 0;
	public static int CONSTANTE = 1;
	public static int TIPO = 2;
	public static int VARIABLE = 3;
	public static int FUNCION = 4;
	public static int PROCEDIMIENTO = 5;
	
	public String lexema;
	public int tipo_de_simbolo;
	
	public Simbolo(String l,int t){
		lexema = l;
		tipo_de_simbolo = t;
	}
}
