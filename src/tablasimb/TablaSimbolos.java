package tablasimb;

import java.util.ArrayList;
import java.util.Hashtable;

import tipos.TTipo;


public class TablaSimbolos {
	Pila<Hashtable<String, Simbolo>> PTabla;
	
	public int Mnivelact;
	
	public TablaSimbolos(){
		PTabla = new Pila<Hashtable<String, Simbolo>>();
	}
	
	public void crear_nivel_lexico(){
		Mnivelact++;
		PTabla.apilar(new Hashtable<String, Simbolo>());
	}
	
	public void eliminar_nivel_lexico(){
		Mnivelact--;
		PTabla.desapilar();
	}
	
	public void guardar_constante_en_tabla(String lexema, TTipo te, int valor){
		Hashtable<String, Simbolo> taux = PTabla.tope();
		taux.put(lexema, new Constante(lexema, te, valor));
	}
	
	public void guardar_variable_en_tabla(String lexema, TTipo te, int nivelL, int desp){
		Hashtable<String, Simbolo> taux = PTabla.tope();
		taux.put(lexema, new Variable(lexema, te, nivelL, desp));
	}
	
	public void guardar_tipo_en_tabla(String lexema, TTipo te){
		Hashtable<String, Simbolo> taux = PTabla.tope();
		taux.put(lexema, new Tipo(lexema, te));
	}
	
	public void guardar_programa_en_tabla(String lexema){
		Hashtable<String, Simbolo> taux = PTabla.tope();
		taux.put(lexema, new Programa(lexema));
	}
	
	public void guardar_procedimiento_en_tabla(String lexema, ArrayList<Parametro> list, int nivelL, String etiqueta){
		Hashtable<String, Simbolo> taux = PTabla.tope();
		
		
		int cantpf = list.size();
		TTipo[] tipopf = new TTipo[cantpf];
		boolean[] pasajepf = new boolean[cantpf];
		int tapf = 0;
		
		int i = 0;
		for (Parametro par:list){
			tipopf[i] = par.tipo;
			pasajepf[i] = par.esPorValor;
			tapf += par.tipo.tammemoria;
		}
		
		taux.put(lexema, new Procedimiento(lexema, cantpf, tipopf, pasajepf, tapf, nivelL, etiqueta));
	}
	
	public void guardar_funcion_en_tabla(String lexema, ArrayList<Parametro> list, TTipo tr, int nivelL, int desp, String etiqueta){
		Hashtable<String, Simbolo> taux = PTabla.tope();
		
		
		int cantpf = list.size();
		TTipo[] tipopf = new TTipo[cantpf];
		boolean[] pasajepf = new boolean[cantpf];
		int tapf = 0;
		
		int i = 0;
		for (Parametro par:list){
			tipopf[i] = par.tipo;
			pasajepf[i] = par.esPorValor;
			tapf += par.tipo.tammemoria;
		}
		
		taux.put(lexema, new Funcion(lexema, cantpf, tipopf, pasajepf, tapf, nivelL, etiqueta, tr, desp));
	}

	public boolean existe_en_tabla(String lexema, int[] tipos, boolean entope){
		if (entope){
			Hashtable<String, Simbolo> taux = PTabla.tope();
			
			if (taux.containsKey(lexema)){
				Simbolo simb = taux.get(lexema);
				
				for (int t:tipos){
					if (simb.tipo_de_simbolo == t){
						return true;
					}
				}
				return false;
			}else{
				return false;
			}
		}else{
			PTabla.reset();
			while (PTabla.hasNext()){
				Hashtable<String, Simbolo> taux = PTabla.next();
				
				if (taux.containsKey(lexema)){
					Simbolo simb = taux.get(lexema);
					
					for (int t:tipos){
						if (simb.tipo_de_simbolo == t){
							return true;
						}
					}
				}
			}
			return false;
		}
	}
	
	public Simbolo obtener_de_tabla(String lexema, int[] tipos){
		PTabla.reset();
		while (PTabla.hasNext()){
			Hashtable<String, Simbolo> taux = PTabla.next();
			
			if (taux.containsKey(lexema)){
				Simbolo simb = taux.get(lexema);
				
				for (int t:tipos){
					if (simb.tipo_de_simbolo == t){
						return simb;
					}
				}
			}
		}
		return null;
	}
}
