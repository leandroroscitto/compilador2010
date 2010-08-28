package prototipos;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Automata {

	public static final int EERROR = -1;
	public static final int EINICIAL = 0;
	public static final int EFVAR = 1;
	public static final int EV1 = 2;
	public static final int EA1 = 3;
	public static final int ER1 = 4;
	public static final int EIDNOF = 5;
	public static final int EFID = 6;
	public static final int EFPLUS = 7;
	public static final int EPLUSNOF = 8;

	public static Hashtable<Integer, Hashtable<String, Integer>> TablaTrans = new Hashtable<Integer, Hashtable<String, Integer>>();

	public static void setearReglas(int Estado, String[] Patrones, int[] Transiciones) {
		Hashtable<String, Integer> ListaP = new Hashtable<String, Integer>();

		int tam = Patrones.length;
		for (int i = 0; i < tam; i++) {
			ListaP.put(Patrones[i], Transiciones[i]);
		}
		TablaTrans.put(Estado, ListaP);
	}

	public static int proxEstado(int Eact, char Cact) {
		Hashtable<String, Integer> ListaP = TablaTrans.get(Eact);
		String pmachea = null;

		if (ListaP != null) {
			Set<String> Patrones = ListaP.keySet();

			if (Patrones != null) {
				for (String patron : Patrones) {
					Pattern p = Pattern.compile(patron);
					Matcher m = p.matcher(String.valueOf(Cact));
					if (m.matches()) {
						pmachea = patron;
						break;
					}
				}
			}
		}

		if (pmachea != null) {
			return ListaP.get(pmachea);
		} else {
			return EERROR;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		setearReglas(EINICIAL, new String[]{"[vV]", "[a-uw-zA-UW-Z0-9]","[+]", "[^a-zA-Z0-9+]"}, new int[]{EV1, EIDNOF, EPLUSNOF, EFID});
		setearReglas(EV1, new String[]{"[aA]", "[b-zB-Z0-9]", "[^a-zA-Z0-9]"}, new int[]{EA1, EIDNOF, EFID});
		setearReglas(EA1, new String[]{"[rR]", "[a-qA-Q0-9]", "[^a-zA-Z0-9]"}, new int[]{ER1, EIDNOF, EFID});
		setearReglas(ER1, new String[]{"[a-zA-z0-9]", "[^a-zA-Z0-9]"}, new int[]{EIDNOF, EFVAR});
		setearReglas(EIDNOF, new String[]{"[a-zA-Z0-9]", "[^a-zA-Z0-9]"}, new int[]{EIDNOF, EFID});
		setearReglas(EPLUSNOF, new String[]{"."}, new int[]{EFPLUS});
		setearReglas(EFPLUS, new String[]{}, new int[]{});
		setearReglas(EFID, new String[]{}, new int[]{});
		setearReglas(EFVAR, new String[]{}, new int[]{});

		ArrayList<Integer> EAcept = new ArrayList<Integer>();
		EAcept.add(EFID);
		EAcept.add(EFVAR);
		EAcept.add(EFPLUS);

		// == PRUEBAS ==
		String input = "h.ola var+2; dos:   var $";

		int estado = EINICIAL;
		int i = 0;
		char caracter = input.charAt(i);

		String token = "";
		while (caracter != '$') {
			estado = proxEstado(estado, caracter);

			if (EAcept.contains(estado)) {
				System.out.println("Estado: " + estado + ", token='" + token + "'.");
				estado = EINICIAL;
				token = "";
				
				// DESCARTO CARACTERES QUE NO ME SIRVAN (NO ESTAN EN NINGUN TOKEN)
				while ((Pattern.matches("[^a-zA-Z0-9+]", String.valueOf(caracter))) && (caracter != '$')) {
					i++;
					caracter = input.charAt(i);
				}
			} else {
				token += caracter;
				i++;
				caracter = input.charAt(i);
			}

		}
	}

}
