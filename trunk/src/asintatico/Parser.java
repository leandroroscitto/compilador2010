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

    public boolean signo() throws ExcepALexico, IOException, ExcepASintatico {
        if ((TActual.tipo == Token.TOPERMAS)
                || (TActual.tipo == Token.TOPERMENOS)) {
            leerToken();
            return true;
        }

        throw new ExcepASintatico("Se esperaba un signo + o -.", TActual.nlinea,
                TActual);
    }

    public boolean constante() throws ExcepALexico, IOException, ExcepASintatico {
        if ((TActual.tipo == Token.TNUMERO)
                || (TActual.tipo == Token.TIDENTIFICADOR)
                || (TActual.tipo == Token.TCARACTER)) {
            leerToken();
            return true;
        }
        if ((TActual.tipo == Token.TOPERMAS)
                || (TActual.tipo == Token.TOPERMENOS)) {
            leerToken();
            constanteP();
            return true;
        }

        throw new ExcepASintatico("Se esperaba una constante.", TActual.nlinea,
                TActual);
    }

    public boolean constanteP() throws ExcepALexico, IOException,
            ExcepASintatico {
        if ((TActual.tipo == Token.TNUMERO)
                || (TActual.tipo == Token.TIDENTIFICADOR)) {
            leerToken();
            return true;
        } else {
            throw new ExcepASintatico(
                    "Se esperaba un numero o identificador para la constante.",
                    TActual.nlinea, TActual);
        }
    }

    public boolean definicion_de_constante() throws ExcepALexico, IOException,
            ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
                leerToken();
                constante();
                return true;
            } else {
                throw new ExcepASintatico(
                        "Error en la definicion de la constante, se esperaba un simbolo '='.",
                        TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico(
                    "Error en la definicion de la constante, se esperaba un identificador.",
                    TActual.nlinea, TActual);
        }
    }

    public boolean definicion_de_constanteP() throws ExcepALexico, IOException,
            ExcepASintatico {
        if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
            leerToken();
            constante();
            return true;
        } else {
            throw new ExcepASintatico(
                    "Error en la definicion de la constante, se esperaba un simbolo '='.",
                    TActual.nlinea, TActual);
        }
    }

    public boolean tipo_simple() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            tipo_simpleP();
            return true;
        }
        if ((TActual.tipo == Token.TNUMERO)
                || (TActual.tipo == Token.TCARACTER)) {
            leerToken();
            tipo_subrangoP();
            return true;
        }
        if ((TActual.tipo == Token.TOPERMAS)
                || (TActual.tipo == Token.TOPERMENOS)) {
            leerToken();
            constanteP();
            tipo_subrangoP();
            return true;
        }
        if (TActual.tipo == Token.TPALRES_ARRAY) {
            leerToken();
            tipo_arregloP();
            return true;
        }

        throw new ExcepASintatico("Se esperaba un tipo simple.", TActual.nlinea, TActual);
    }

    public boolean tipo_simpleP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TDOBLEPUNTO) {
            leerToken();
            constante();
            return true;
        } else {
            return true;
        }
    }

    public boolean tipo_subrangoP() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TDOBLEPUNTO) {
            leerToken();
            constante();
            return true;
        }

        throw new ExcepASintatico("Se esperaba un '..' para el subrango.", TActual.nlinea, TActual);
    }

    public boolean tipo_arregloP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TCORA) {
            leerToken();
            tipo_simple();
            if (TActual.tipo == Token.TCORC) {
                leerToken();
                if (TActual.tipo == Token.TPALRES_OF) {
                    leerToken();
                    tipo_simple();
                    return true;
                } else {
                    throw new ExcepASintatico("Se esperaba la palabra reservada 'of' para el arreglo.", TActual.nlinea, TActual);
                }
            } else {
                throw new ExcepASintatico("Se esperaba un ']' para el arreglo.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba un '[' para el arreglo.", TActual.nlinea, TActual);
        }
    }

    public boolean tipo() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            tipo_simpleP();
            return true;
        }
        if ((TActual.tipo == Token.TNUMERO)
                || (TActual.tipo == Token.TCARACTER)) {
            leerToken();
            tipo_subrangoP();
            return true;
        }
        if ((TActual.tipo == Token.TOPERMAS)
                || (TActual.tipo == Token.TOPERMENOS)) {
            leerToken();
            constanteP();
            tipo_subrangoP();
            return true;
        }
        if (TActual.tipo == Token.TPALRES_ARRAY) {
            leerToken();
            tipo_arregloP();
            return true;
        }

        throw new ExcepASintatico("Se espera un tipo.", TActual.nlinea, TActual);
    }

    public boolean definicion_de_tipo() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
                leerToken();
                tipo();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un simbolo igual en la definicion de tipo.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se espera un identificador en la definicion de tipo.", TActual.nlinea, TActual);
        }
    }

    public boolean definicion_de_tipoP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
            leerToken();
            tipo();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba un simbolo igual en la definicion de tipo.", TActual.nlinea, TActual);
        }
    }

    public boolean declaracion_de_variable() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            siguiente_identificador();
            if (TActual.tipo == Token.TDOSPUNTOS) {
                leerToken();
                tipo();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba ':' en la declaracion de variable.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba un identificador en la declaracion de variable.", TActual.nlinea, TActual);
        }
    }

    public boolean declaracion_de_variableP() throws ExcepASintatico, ExcepALexico, IOException {
        siguiente_identificador();
        if (TActual.tipo == Token.TDOSPUNTOS) {
            leerToken();
            tipo();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba ':' en la declaracion de variable.", TActual.nlinea, TActual);
        }
    }

    public boolean siguiente_identificador() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TCOMA) {
            leerToken();
            if (TActual.tipo == Token.TIDENTIFICADOR) {
                leerToken();
                siguiente_identificador();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    public boolean variable() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            variableP();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba un identificador para la variable.", TActual.nlinea, TActual);
        }
    }

    public boolean variableP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TCORA) {
            leerToken();
            expresion();
            if (TActual.tipo == Token.TCORC) {
                leerToken();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un ']' en la variable.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    public boolean variablePP() throws ExcepALexico, IOException, ExcepASintatico {
        expresion();
        if (TActual.tipo == Token.TCORC) {
            leerToken();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba un ']' en la variable.", TActual.nlinea, TActual);
        }
    }

    public boolean factor() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            factorP();
            return true;
        }
        if ((TActual.tipo == Token.TNUMERO)
                || (TActual.tipo == Token.TCARACTER)) {
            leerToken();
            return true;
        }
        if (TActual.tipo == Token.TPARENTA) {
            leerToken();
            expresion();
            if (TActual.tipo == Token.TPARENTC) {
                leerToken();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un ')' en el factor.", TActual.nlinea, TActual);
            }
        }
        if (TActual.tipo == Token.TOPER_NOT) {
            leerToken();
            factor();
            return true;
        }

        throw new ExcepASintatico("Factor mal formado.", TActual.nlinea, TActual);
    }

    public boolean factorP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TCORA) {
            leerToken();
            variablePP();
            return true;
        }
        if (TActual.tipo == Token.TPARENTA) {
            leerToken();
            designador_de_funcionPP();
            return true;
        }

        return true;
    }

    public boolean termino() throws IOException, ExcepALexico, ExcepASintatico {
        factor();
        terminoP();
        return true;
    }

    public boolean terminoP() throws ExcepALexico, IOException, ExcepASintatico {
        if ((TActual.tipo == Token.TOPERMULT)
                || (TActual.tipo == Token.TOPERDIV)
                || (TActual.tipo == Token.TOPER_AND)) {
            leerToken();
            factor();
            terminoP();
            return true;
        } else {
            return true;
        }
    }

    public boolean expresion() {
        // TODO: Terminar
        return true;
    }

    public boolean designador_de_funcionP() throws ExcepALexico, IOException, ExcepASintatico{
    	if(TActual.tipo == Token.TPARENTA){
    		leerToken();
    		parametro_actual();
    		siguiente_parametro();
    		if(TActual.tipo == Token.TPARENTC){
    			leerToken();
    			return true;
    		}else{
    			throw new ExcepASintatico("Se esperaba un \")\" al final de los parametros.", TActual.nlinea, TActual);
    		}
    	}else{
    		throw new ExcepASintatico("Se esperaba un \"(\" al comienzo de los parametros.", TActual.nlinea, TActual);
    	}
    }
    
    public boolean designador_de_funcionPP() throws ExcepALexico, IOException, ExcepASintatico{
        parametro_actual();
        siguiente_parametro();
    	if(TActual.tipo == Token.TPARENTC){
    		return true;
        }else{
        	throw new ExcepASintatico("Se esperaba un \")\" al final de los parametros.", TActual.nlinea, TActual);
        }
    }

    public boolean parametro_actual()throws ExcepALexico, IOException, ExcepASintatico{
    	expresion();
    	return true;
    }
    
    public boolean siguiente_parametro()throws ExcepALexico, IOException, ExcepASintatico{
    	if(TActual.tipo == Token.TCOMA){
    		leerToken();
    		parametro_actual();
    		siguiente_parametro();
    		return true;
    	}else{
    		return true;
    	}
    	
    }
    
    public boolean parte_de_definicion_de_constantes() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_CONST) {
            leerToken();
            definicion_de_constante();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                sig_definicion_de_constante();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un punto y coma al final de la definicion de constante.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    public boolean sig_definicion_de_constante() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            definicion_de_constanteP();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                sig_definicion_de_constante();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un punto y coma al final de la definicion de constante.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    public boolean parte_de_definicion_de_tipos() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_TYPE) {
            leerToken();
            definicion_de_tipo();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                sig_definicion_de_tipo();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un punto y coma al final de la definicion de tipo.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    public boolean sig_definicion_de_tipo() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            definicion_de_tipoP();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                sig_definicion_de_tipo();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un punto y coma al final de la definicion de tipo.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    public boolean parte_de_declaracion_de_variables() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_VAR) {
            leerToken();
            declaracion_de_variable();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                sig_declaracion_de_variable();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un punto y coma al final de la declaracion de variable.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    public boolean sig_declaracion_de_variable() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            declaracion_de_variableP();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                sig_declaracion_de_variable();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un punto y coma al final de la declaracion de variable.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    public boolean parte_de_declaracion_de_funciones_y_procedimientos()throws ExcepALexico, IOException, ExcepASintatico {
    	siguiente_declaracion_de_procedimiento_o_funcion();
        if (TActual.tipo == Token.TPUNTO_Y_COMA) {
        	leerToken();
        }else{
        	throw new ExcepASintatico("Se esperaba un punto y coma al final de la declaracion de de funciones y procedimientos.", TActual.nlinea, TActual);
        }
        return true;
    }

    public boolean siguiente_declaracion_de_procedimiento_o_funcion()throws ExcepALexico, IOException, ExcepASintatico{
    	if (TActual.tipo == Token.TPALRES_PROCEDURE){
    		leerToken();
    		if (TActual.tipo == Token.TIDENTIFICADOR){
    			leerToken();
    			encabezado_de_procedimientoP();
        		bloque();
        		siguiente_declaracion_de_procedimiento_o_funcion();
        		return true;
    		}else{
    			throw new ExcepASintatico("Se esperaba un identificador en la declaracion del procedimiento.", TActual.nlinea, TActual);
    		}
    	}else{
	   		if (TActual.tipo == Token.TPALRES_FUNCTION){
	   			leerToken();
	   			if(TActual.tipo == Token.TIDENTIFICADOR){
	   				leerToken();
	   				encabezado_de_funcionP();
	   				bloque();
	   				siguiente_declaracion_de_procedimiento_o_funcion();
	   				return true;
	       		}else{
	       			throw new ExcepASintatico("Se esperaba un identificador en la declaracion de la funcion.", TActual.nlinea, TActual);
	       		}
	   		}else{
	   			throw new ExcepASintatico("Se esperaba la palabra PROCEDURE o FUNCTION al comienzo de la declaracion.", TActual.nlinea, TActual);
	   		}
    	}	
    }
    
public boolean encabezado_de_procedimientoP()throws ExcepALexico, IOException, ExcepASintatico{
    	if (TActual.tipo == Token.TPUNTO_Y_COMA){
    		leerToken();
    		return true;
    	}else{
    		if(TActual.tipo == Token.TPARENTA){
    			leerToken();
    			seccion_de_parametros_formales();
    			siguiente_seccion_de_parametros_form();
    			if(TActual.tipo == Token.TPARENTC){
    				leerToken();
    				if(TActual.tipo == Token.TPUNTO_Y_COMA){
    					leerToken();
    					return true;
    				}else{
    					throw new ExcepASintatico("Se esperaba \";\" al final del encabezado de procedimiento .", TActual.nlinea, TActual);
    				}
    			}else{
    				throw new ExcepASintatico("Se esperaba \")\" al final de los parametros .", TActual.nlinea, TActual);
    			}
    		}else{
    			return true;
    		}
    	}
    }
    
    public boolean encabezado_de_funcionP()throws ExcepALexico, IOException, ExcepASintatico{
    	if(TActual.tipo == Token.TDOSPUNTOS){
    		leerToken();
    		if(TActual.tipo == Token.TIDENTIFICADOR){
    			leerToken();
        		if (TActual.tipo == Token.TPUNTO_Y_COMA){
            		leerToken();
            		return true;
        		}else{
        			throw new ExcepASintatico("Se esperaba un \";\" al final del encabezado de la funcion.", TActual.nlinea, TActual);
        		}
    		}else{
    			throw new ExcepASintatico("Se esperaba un IDENTIFICADOR al final del encabezado de la funcion.", TActual.nlinea, TActual);
    		}
    	}else{
    		if(TActual.tipo == Token.TPARENTA){
    			leerToken();
    			seccion_de_parametros_formales();
    			siguiente_seccion_de_parametros_form();
    			if(TActual.tipo == Token.TPARENTC){
    				leerToken();
    				if(TActual.tipo == Token.TDOSPUNTOS){
    					leerToken();
    					if(TActual.tipo == Token.TIDENTIFICADOR){
    						leerToken();
    						return true;
    					}else{
    						throw new ExcepASintatico("Se esperaba un IDENTIFICADOR al final del encabezado de la funcion.", TActual.nlinea, TActual);
    					}
    				}else{
    					throw new ExcepASintatico("Se esperaba \":\" al final de los parametros.", TActual.nlinea, TActual);
    				}
    			}else{
    				throw new ExcepASintatico("Se esperaba \")\" al final de los parametros .", TActual.nlinea, TActual);
    			}
    		}else{
    			return true;
    		}
    	}
    }
    
    public boolean siguiente_seccion_de_parametros_form()throws ExcepALexico, IOException, ExcepASintatico{
    	if(TActual.tipo == Token.TPUNTO_Y_COMA){
    		leerToken();
    		seccion_de_parametros_formales();
    		siguiente_seccion_de_parametros_form();
    		return true;
    	}else{
    		return true;
    	}
    }


    public boolean seccion_de_parametros_formales()throws ExcepALexico, IOException, ExcepASintatico{
    	if(TActual.tipo == Token.TIDENTIFICADOR){
    		leerToken();
    		grupo_de_parametrosP();
    		return true;
    	}else{
    		if(TActual.tipo == Token.TPALRES_VAR){
    			leerToken();
    			grupo_de_parametros();
    			return true;
    		}else{
    			throw new ExcepASintatico("Se esperaba la palabra reservada VAR o un IDENTIFICADOR", TActual.nlinea, TActual);    		
    		}
    	}
    }    
    
    public boolean grupo_de_parametrosP()throws ExcepALexico, IOException, ExcepASintatico{	
    	siguiente_identificador();
    	if (TActual.tipo == Token.TDOSPUNTOS){
    		leerToken();
    		if(TActual.tipo == Token.TIDENTIFICADOR){
    			leerToken();
    			return true;
    		}else{
    			throw new ExcepASintatico("Se esperaba la palabra un IDENTIFICADOR para el parametro", TActual.nlinea, TActual);
    		}
    	}else{
    		throw new ExcepASintatico("Se esperaba el simbolo \":\" luego del IDENTIFICADOR parametro", TActual.nlinea, TActual);
    	}
    }
    
    public boolean grupo_de_parametros()throws ExcepALexico, IOException, ExcepASintatico{
    	if(TActual.tipo == Token.TIDENTIFICADOR){
    		leerToken();
    		siguiente_identificador();
    		if (TActual.tipo == Token.TDOSPUNTOS){
    			leerToken();
    			if(TActual.tipo == Token.TIDENTIFICADOR){
    				leerToken();
    				return true;
    			}else{
    				throw new ExcepASintatico("Se esperaba la palabra un IDENTIFICADOR para el parametro", TActual.nlinea, TActual);
    			}
    		}else{
    			throw new ExcepASintatico("Se esperaba el simbolo \":\" luego del IDENTIFICADOR parametro", TActual.nlinea, TActual);
    		}
    	}else{
    		throw new ExcepASintatico("Se esperaba un IDENTIFICADOR para el parametro", TActual.nlinea, TActual);
    	}
    }
    
    
    public boolean sentencia_compuesta() throws ExcepALexico, IOException, ExcepASintatico{
        if(TActual.tipo == Token.TPALRES_BEGIN){
        	leerToken();
        	sentencia();
        	siguiente_sentencia();
        	if(TActual.tipo == Token.TPALRES_END){
        		return true;
        	}else{
        		throw new ExcepASintatico("Se esperaba la palabra reservada END al finalizar la sentencia", TActual.nlinea, TActual);
        	}
        }else{
        	throw new ExcepASintatico("Se esperaba la palabra reservada BEGIN al comienzo de la sentencia", TActual.nlinea, TActual);
        }
    }
    
  
    
    public boolean sentencia() throws ExcepALexico, IOException, ExcepASintatico{
    	if(TActual.tipo == Token.TIDENTIFICADOR){
    		leerToken();
    		sentencia_simple();
    		return true;
    	}else{
    		return sentencia_estructurada();
    	}
    }
 
    public boolean sentencia_simple() throws ExcepALexico, IOException, ExcepASintatico{
    	sentencia_simpleP();
    	return true;
    }

    public boolean sentencia_simpleP() throws ExcepALexico, IOException, ExcepASintatico{
    	if(TActual.tipo == Token.TPARENTA){
    		return sentencia_de_procedimientoP();
    	}else{
    		return sentencia_se_asignacionP();
    	}
    	
    }
 
    public boolean sentencia_se_asignacionP() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    public boolean sentencia_de_procedimientoP() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    public boolean sentencia_estructurada() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    
    public boolean siguiente_sentencia() throws ExcepALexico, IOException, ExcepASintatico{
    	if(TActual.tipo == Token.TPUNTO_Y_COMA){
    		leerToken();
    		sentencia();
    		siguiente_sentencia();
    		return true;
    	}else{
    		return true;
    	}
    }
    
    public boolean sentencia_if() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }	
    
    public boolean sentencia_ifP() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }	
    
    public boolean sentencia_while() throws ExcepALexico, IOException, ExcepASintatico{
    	//TODO
    	return true;
    }
    	
    public boolean bloque() throws ExcepALexico, IOException, ExcepASintatico {
        parte_de_definicion_de_constantes();
        parte_de_definicion_de_tipos();
        parte_de_declaracion_de_variables();
        parte_de_declaracion_de_funciones_y_procedimientos();
        sentencia_compuesta();
        return true;
    }

    public boolean programa() throws ExcepALexico, IOException, ExcepASintatico {
        encabezado_de_programa();
        bloque();
        if (TActual.tipo == Token.TPUNTO) {
            leerToken();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba el punto al final del programa.", TActual.nlinea, TActual);
        }
    }

    public boolean encabezado_de_programa() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TPALRES_PROGRAM) {
            leerToken();
            if (TActual.tipo == Token.TIDENTIFICADOR) {
                leerToken();
                if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                    leerToken();
                    return true;
                } else {
                    throw new ExcepASintatico("Se esperaba ';' al final del encabezado del programa.", TActual.nlinea, TActual);
                }
            } else {
                throw new ExcepASintatico("Se esperaba un identificador en el encabezado de programa.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("El codigo debe comenzar con 'program'.", TActual.nlinea, TActual);
        }
    }

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
