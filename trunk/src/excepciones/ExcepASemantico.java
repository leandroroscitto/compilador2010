package excepciones;


public class ExcepASemantico extends Exception {
    private static final long serialVersionUID = 1L;
    
    // Numero de linea del inicio de la excepcion
    public int NLinea;

    public ExcepASemantico(String msg, int nlin) {
        super("Linea " + nlin + ": " + msg);
        NLinea = nlin;
    }
}
