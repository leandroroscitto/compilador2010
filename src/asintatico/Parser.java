package asintatico;

import java.io.IOException;

import alexico.Lexer;
import alexico.Token;
import excepciones.ExcepALexico;
import excepciones.ExcepASintatico;

public class Parser {

    private Lexer ALexico;
    private Token TActual;

    public Parser(String fileurl) throws ExcepALexico, IOException {
        ALexico = new Lexer(fileurl);
        TActual = ALexico.nextToken();
    }

    private void leerToken() throws ExcepALexico, IOException {
        TActual = ALexico.nextToken();
    }

    /*--------------------------------------------------------------*/
    // 				***********GRAMATICA***********
	/*--------------------------------------------------------------*/

    
    public boolean signo() throws ExcepALexico, IOException, ExcepASintatico {
        if ((TActual.tipo == Token.TOPERMAS)
                || (TActual.tipo == Token.TOPERMENOS)) {
            leerToken();
            return true;
        }

        throw new ExcepASintatico("Se esperaba un signo + o -.", TActual.nlinea,
                TActual);
    }

    /*--------------------------------------------------------------*/
	//			***********CONSTANTES***********
	/*--------------------------------------------------------------*/
    public boolean constante() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }

    public boolean definicion_de_constante() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }
  /*--------------------------------------------------------------*/
  //					***********TIPOS***********
  /*--------------------------------------------------------------*/
    public boolean tipo() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }

    public boolean definicion_de_tipo() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }
    
    public boolean tipo_simple() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;    
    }

    public boolean tipo_subrango() throws ExcepASintatico, ExcepALexico, IOException {
    	//TODO
    	return true;    
    }

    public boolean tipo_arreglo() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }
    
    /*--------------------------------------------------------------*/
    // 				***********VARIABLES***********
	/*--------------------------------------------------------------*/
    public boolean declaracion_de_variable() throws ExcepASintatico, ExcepALexico, IOException {
    	//TODO
    	return true;
    }

    public boolean siguiente_identificador() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }

    public boolean variable() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }

    public boolean variableP() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }
  
    public boolean variable_indexada() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }
    
    /*--------------------------------------------------------------*/
    // 				***********EXPRESIONES***********
	/*--------------------------------------------------------------*/    public boolean constante_sin_signo() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }
    
    public boolean factor() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }

    public boolean factorP() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }

    public boolean termino() throws IOException, ExcepALexico, ExcepASintatico {
    	//TODO
    	return true;
    }

    public boolean terminoP() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }

    public boolean expresion_simple() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }

    public boolean expresion_simpleP() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }
    
    public boolean expresion() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }

    public boolean expresionP() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    /*--------------------------------------------------------------*/
    // 					***********OPERADORES***********
	/*--------------------------------------------------------------*/  
    
    public boolean operador_de_multiplicacion() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }

    public boolean operador_de_suma() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    public boolean operador_de_relacion() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    /*--------------------------------------------------------------*/
    // 				***********DESIGNADOR DE FUNCION***********
	/*--------------------------------------------------------------*/   
    public boolean designador_de_funcion() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    public boolean designador_de_funcionP() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    public boolean siguiente_parametro_actual()throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true; 	
    }

    /*--------------------------------------------------------------*/
    // 				***********SENTENCIAS***********
	/*--------------------------------------------------------------*/

    public boolean sentencia() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }

    public boolean sentencia_simple() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    public boolean sentencia_se_asignacion() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }

    public boolean sentencia_se_asignacionP() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }

    public boolean sentencia_de_procedimiento() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    public boolean sentencia_de_procedimientoP() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    public boolean parametro_actual() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    public boolean sentencia_estructurada() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    public boolean sentencia_compuesta() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    public boolean sentencia_compuestaP() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
 
    public boolean sentencia_if() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }	
    
    public boolean sentencia_ifP() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }

    public boolean sentencia_simpleP() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;	
    }
 
    public boolean sentencia_while() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    /*--------------------------------------------------------------*/
    // 		***********DECLARACION DE PROCEDIMIENTOS***********
	/*--------------------------------------------------------------*/
   
    public boolean declaracion_de_procedimiento() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    public boolean bloque() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }

    public boolean encabezado_de_procedimiento()throws ExcepALexico, IOException, ExcepASintatico{
		//TODO
		return true;
    }
    
    public boolean encabezado_de_procedimientoP()throws ExcepALexico, IOException, ExcepASintatico{
		//TODO
		return true;
    }
    
    public boolean siguiente_seccion_de_parametros_formales()throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    } 
    
    public boolean seccion_de_parametros_formales()throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    } 
    
    public boolean grupo_de_parametros()throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    } 
    
    public boolean siguiente_grupo_de_parametros()throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    } 
    
    public boolean parte_de_definicion_de_constantes() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }

    public boolean sig_definicion_de_constante() throws ExcepASintatico, ExcepALexico, IOException {
    	//TODO
    	return true;
    }

    public boolean parte_de_definicion_de_tipos() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }

    public boolean siguiente_definicion_de_tipo() throws ExcepASintatico, ExcepALexico, IOException {
    	//TODO
    	return true;
    }

    public boolean parte_de_declaracion_de_variables() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }

    public boolean siguiente_declaracion_de_variable() throws ExcepASintatico, ExcepALexico, IOException {
    	//TODO
    	return true;
    }

    public boolean parte_de_declaracion_de_funciones_y_procedimientos()throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }

    public boolean siguiente_declaracion_de_procedimiento_o_funcion()throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
 
    public boolean declaracion_de_procedimiento_o_funcion()throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }

    public boolean parte_de_sentencias()throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }

    /*--------------------------------------------------------------*/
    // 		***********DECLARACION DE PROCEDIMIENTOS***********
	/*--------------------------------------------------------------*/

    public boolean declaracion_de_funcion()throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    public boolean encabezado_de_funcion()throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }   
    
    public boolean encabezado_de_funcionP()throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    /*--------------------------------------------------------------*/
    // 				***********PROGRAMAS***********
	/*--------------------------------------------------------------*/
    
    public boolean programa() throws ExcepALexico, IOException, ExcepASintatico {
    	//TODO
    	return true;
    }

    /*--------------------------------------------------------------*/
    // 				***********GRAMATICA***********
	/*--------------------------------------------------------------*/

    public boolean eof() throws ExcepASintatico {
        if (TActual.tipo == Token.TEOF) {
            return true;
        }

        System.out.println(TActual);
        throw new ExcepASintatico(
                "Se esperaba el final del archivo, pero se encontro algo mas...",
                TActual.nlinea, TActual);
    }
}
