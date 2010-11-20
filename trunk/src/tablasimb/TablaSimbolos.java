package tablasimb;

import java.util.ArrayList;
import java.util.Hashtable;

import auxiliares.MEPa;
import auxiliares.ParametroForm;

import tipos.TBoolean;
import tipos.TChar;
import tipos.TCharIntSub;
import tipos.TEntero;
import tipos.TTipo;

public class TablaSimbolos {
	Pila<Hashtable<String, Simbolo>> PTabla;

	public int Mnivelact = -1;
	public final int MNivelPre = 0;

	public final int[] TodosSimb = { Simbolo.CONSTANTE, Simbolo.FUNCION, Simbolo.PROCEDIMIENTO, Simbolo.TIPO, Simbolo.VARIABLE };

	public TablaSimbolos() {
		PTabla = new Pila<Hashtable<String, Simbolo>>();
	}

	public void crear_nivel_lexico() {
		Mnivelact++;
		PTabla.apilar(new Hashtable<String, Simbolo>());
	}

	public void eliminar_nivel_lexico() {
		Mnivelact--;
		PTabla.desapilar();
	}

	public void cargarPredefinidas() {
		crear_nivel_lexico();

		// CONSTANTES
		guardar_constante_en_tabla("true", new TBoolean(), 1);
		guardar_constante_en_tabla("false", new TBoolean(), 0);

		guardar_constante_en_tabla("maxint", new TEntero(), MEPa.maxint);

		// TIPOS
		guardar_tipo_en_tabla("integer", new TEntero());
		guardar_tipo_en_tabla("char", new TChar());
		guardar_tipo_en_tabla("boolean", new TBoolean());

		// Funciones y procedimientos, crea un nuevo nivel lexico
		crear_nivel_lexico();
		
		// FUNCIONES
		ArrayList<ParametroForm> list = new ArrayList<ParametroForm>();
		list.add(new ParametroForm("x", new TCharIntSub(), true));

		guardar_funcion_en_tabla("succ", list, new TEntero(), MNivelPre, 0, "L0");
		guardar_funcion_en_tabla("pred", list, new TEntero(), MNivelPre, 1, "L1");
		guardar_funcion_en_tabla("ord", list, new TChar(), MNivelPre, 2, "L2");
		guardar_funcion_en_tabla("chr", list, new TEntero(), MNivelPre, 3, "L3");

		// PROCEDIMIENTOS
		ArrayList<ParametroForm> list2 = new ArrayList<ParametroForm>();
		list2.add(new ParametroForm("x", new TCharIntSub(), false));

		guardar_procedimiento_en_tabla("read", list2, MNivelPre, "LNADA");
		guardar_procedimiento_en_tabla("readln", list2, MNivelPre, "LNADA");

		guardar_procedimiento_en_tabla("write", list, MNivelPre, "LNADA");
		guardar_procedimiento_en_tabla("writeln", list, MNivelPre, "LNADA");
		
		// Termino de funciones y precedimientos elimina el nivel lexico
		eliminar_nivel_lexico();
	}

	public void guardar_constante_en_tabla(String lexema, TTipo te, int valor) {
		Hashtable<String, Simbolo> taux = PTabla.tope();
		taux.put(lexema, new Constante(lexema, te, valor));
	}

	public void guardar_variable_en_tabla(String lexema, TTipo te, int nivelL, int desp, boolean espv) {
		Hashtable<String, Simbolo> taux = PTabla.tope();
		taux.put(lexema, new Variable(lexema, te, nivelL-1, desp, espv));
	}

	public void guardar_tipo_en_tabla(String lexema, TTipo te) {
		Hashtable<String, Simbolo> taux = PTabla.tope();
		taux.put(lexema, new Tipo(lexema, te));
	}

	public void guardar_programa_en_tabla(String lexema) {
		Hashtable<String, Simbolo> taux = PTabla.tope();
		taux.put(lexema, new Programa(lexema));
	}

	public void guardar_procedimiento_en_tabla(String lexema, ArrayList<ParametroForm> list, int nivelL, String etiqueta) {
		// guarda el procedimiento en el nivel lexico anterior
		// TODO: Cambiado a nivel actual
		Hashtable<String, Simbolo> taux = PTabla.get(Mnivelact-1);

		int cantpf = list.size();
		TTipo[] tipopf = new TTipo[cantpf];
		boolean[] pasajepf = new boolean[cantpf];
		int tapf = 0;

		int i = 0;
		for (ParametroForm par : list) {
			tipopf[i] = par.tipo;
			pasajepf[i] = par.esPorValor;
			tapf += par.tipo.tammemoria;
			i++;
		}

		taux.put(lexema, new Procedimiento(lexema, cantpf, tipopf, pasajepf, tapf, nivelL, etiqueta));
	}

	public void guardar_funcion_en_tabla(String lexema, ArrayList<ParametroForm> list, TTipo tr, int nivelL, int desp, String etiqueta) {
		// guarda la funcion en el nivel lexixo anterior.
		// TODO: Cambiado a nivel actual
		Hashtable<String, Simbolo> taux = PTabla.get(Mnivelact-1);

		int cantpf = list.size();
		TTipo[] tipopf = new TTipo[cantpf];
		boolean[] pasajepf = new boolean[cantpf];
		int tapf = 0;

		int i = 0;
		for (ParametroForm par : list) {
			tipopf[i] = par.tipo;
			pasajepf[i] = par.esPorValor;
			tapf += par.tipo.tammemoria;
			i++;
		}

		taux.put(lexema, new Funcion(lexema, cantpf, tipopf, pasajepf, tapf, nivelL, etiqueta, tr, desp));
	}

	public boolean existe_en_tabla(String lexema, int[] tipos, boolean entope) {
		if (entope) {
			Hashtable<String, Simbolo> taux = PTabla.tope();

			if (taux.containsKey(lexema)) {
				Simbolo simb = taux.get(lexema);

				for (int t : tipos) {
					if (simb.tipo_de_simbolo == t) {
						return true;
					}
				}
				return false;
			} else {
				return false;
			}
		} else {
			PTabla.reset();
			while (PTabla.hasNext()) {
				Hashtable<String, Simbolo> taux = PTabla.next();

				if (taux.containsKey(lexema)) {
					Simbolo simb = taux.get(lexema);

					for (int t : tipos) {
						if (simb.tipo_de_simbolo == t) {
							return true;
						}
					}
				}
			}
			return false;
		}
	}

	public Simbolo obtener_de_tabla(String lexema, int[] tipos) {
		PTabla.reset();
		while (PTabla.hasNext()) {
			Hashtable<String, Simbolo> taux = PTabla.next();

			if (taux.containsKey(lexema)) {
				Simbolo simb = taux.get(lexema);

				for (int t : tipos) {
					if (simb.tipo_de_simbolo == t) {
						return simb;
					}
				}
			}
		}
		return null;
	}
}
