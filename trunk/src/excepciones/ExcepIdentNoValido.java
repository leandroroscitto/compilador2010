package excepciones;

public class ExcepIdentNoValido extends ExcepALexico {
	private static final long serialVersionUID = 1L;

	public ExcepIdentNoValido(int nlin) {
		super("Se encontro un identificador mal formado en la linea " + nlin + ".", nlin);
	}

}
