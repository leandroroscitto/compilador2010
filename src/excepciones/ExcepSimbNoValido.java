package excepciones;

public class ExcepSimbNoValido extends ExcepALexico {
	private static final long serialVersionUID = 1L;

	// Simbolo no valido, no esta en el alfabeto de entrada
	public char simbolo;

	public ExcepSimbNoValido(char car, int nlin) {
		super("Símbolo no válido:'" + car + "' en la linea " + nlin + ".", nlin);
		simbolo = car;
	}
}
