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
    //              ***********GRAMATICA***********
    /*--------------------------------------------------------------*/
    // <signo> : TOPERMAS | TOPERMENOS
    public boolean signo() throws ExcepALexico, IOException, ExcepASintatico {
        if ((TActual.tipo == Token.TOPERMAS)
                || (TActual.tipo == Token.TOPERMENOS)) {
            leerToken();
            return true;
        }

        throw new ExcepASintatico("Se esperaba un signo '+' o '-'.", TActual.nlinea, TActual);
    }

    /*--------------------------------------------------------------*/
    //			***********CONSTANTES***********
    /*--------------------------------------------------------------*/
    // <constante> :
    //      TNUMERO |
    //      TCARACTER |
    //      TIDENTIFICADOR |
    //      <signo> TNUMERO |
    //      <signo> TIDENTIFICADOR
    public boolean constante() throws ExcepALexico, IOException, ExcepASintatico {
        // Si comienza con un numero, caracter o identificador
        if ((TActual.tipo == Token.TNUMERO)
                || (TActual.tipo == Token.TCARACTER)
                || (TActual.tipo == Token.TIDENTIFICADOR)) {
            leerToken();
            return true;
        }

        // Si comienza con un signo
        signo();
        if ((TActual.tipo == Token.TNUMERO)
                || (TActual.tipo == Token.TIDENTIFICADOR)) {
            leerToken();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba un identificador o un numero para la constante.", TActual.nlinea, TActual);
        }
    }

    // <defincion de constante> :
    //      TIDENTIFICADOR TSIMBOLO_IGUAL <constante>
    public boolean definicion_de_constante() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
                leerToken();
                constante();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un simbolo '=' en la defincion de constante.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba un identificador en la defincion de constante.", TActual.nlinea, TActual);
        }
    }

    /*--------------------------------------------------------------*/
    //			***********TIPOS***********
    /*--------------------------------------------------------------*/
    // <tipo> : 
    //      <tipo simple> | <tipo arreglo>
    public boolean tipo() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR
                || TActual.tipo == Token.TNUMERO
                || TActual.tipo == Token.TCARACTER
                || TActual.tipo == Token.TOPERMAS
                || TActual.tipo == Token.TOPERMENOS) {
            tipo_simple();
            return true;
        }
        if (TActual.tipo == Token.TPALRES_ARRAY) {
            tipo_arreglo();
            return true;
        }
        throw new ExcepASintatico("Se esperaba un tipo valido.", TActual.tipo, TActual);
    }

    // <definicion de tipo> :
    //      TIDENTIFICADOR TSIMBOLO_IGUAL <tipo>
    public boolean definicion_de_tipo() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
                leerToken();
                tipo();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un simbolo '=' en la definicion de tipo.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba un identificador en la definicion de tipo.", TActual.nlinea, TActual);
        }
    }

    // <tipo simple> :
    //      TIDENTIFICADOR<tipo simple'> |
    //      TNUMERO TDOBLEPUNTO <constante> |
    //      TCARACTER TDOBLEPUNTO <constante> |
    //      TOPERMAS TNUMERO TDOBLEPUNTO <constante> |
    //      TOPERMENOS TIDENTIFICADOR TDOBLEPUNTO <constante>
    public boolean tipo_simple() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            tipo_simpleP();
            return true;
        }
        if (TActual.tipo == Token.TNUMERO
                || TActual.tipo == Token.TCARACTER) {
            leerToken();
            if (TActual.tipo == Token.TDOBLEPUNTO) {
                leerToken();
                constante();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un '..' en la declaracion del subrango.", TActual.nlinea, TActual);
            }
        }
        if (TActual.tipo == Token.TOPERMAS
                || TActual.tipo == Token.TOPERMENOS) {
            leerToken();
            if (TActual.tipo == Token.TNUMERO
                    || TActual.tipo == Token.TIDENTIFICADOR) {
                leerToken();
                if (TActual.tipo == Token.TDOBLEPUNTO) {
                    leerToken();
                    constante();
                    return true;
                } else {
                    throw new ExcepASintatico("Se esperaba un '..' en la declaracion del subrango.", TActual.nlinea, TActual);
                }
            } else {
                throw new ExcepASintatico("Se esperaba un identificador o un numero para la constante.", TActual.nlinea, TActual);
            }
        }

        throw new ExcepASintatico("Se esperaba un tipo simple valido.", TActual.nlinea, TActual);
    }

    // <tipo simple'> :
    //      TDOBLEPUNT <constante> |
    //      lambda
    public boolean tipo_simpleP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TDOBLEPUNTO) {
            leerToken();
            constante();
            return true;
        } else {
            return true;
        }
    }

    // <tipo subrango> :
    //      <constante> TDOBLEPUNTO <constante>
    public boolean tipo_subrango() throws ExcepASintatico, ExcepALexico, IOException {
        constante();
        if (TActual.tipo == Token.TDOBLEPUNTO) {
            leerToken();
            constante();
            return true;
        } else {
            throw new ExcepASintatico("Se esperaba '..' en la declaracion del subrango.", TActual.nlinea, TActual);
        }
    }

    // <tipo arreglo> :
    //      TPALRES_ARRAY TCORA <tipo simple> TCORC TPALRES_OF <tipo simple>
    public boolean tipo_arreglo() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_ARRAY) {
            leerToken();
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
                        throw new ExcepASintatico("Se esperaba la palabra reservada 'of' en la definicion del arreglo.", TActual.nlinea, TActual);
                    }
                } else {
                    throw new ExcepASintatico("Se esperaba un ']' en la definicion del arreglo.", TActual.nlinea, TActual);
                }
            } else {
                throw new ExcepASintatico("Se esperaba un '[' en la definicion del arreglo.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba la palabra reservada 'array' en la definicion del arreglo.", TActual.nlinea, TActual);
        }
    }

    /*--------------------------------------------------------------*/
    // 			***********VARIABLES***********
    /*--------------------------------------------------------------*/
    // <declaracion de variable> :
    //  TIDENTIFICADOR <siguiente identificador> TDOSPUNTOS <tipo>
    public boolean declaracion_de_variable() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            siguiente_identificador();
            if (TActual.tipo == Token.TDOSPUNTOS) {
                leerToken();
                tipo();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un ':' en la declaracion de variable.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba un identificador al inicion de la declaracion de variable.", TActual.nlinea, TActual);
        }
    }

    // <siguiente identificador> : 
    //      TCOMA TIDENTIFICADOR |
    //      lambda
    public boolean siguiente_identificador() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <variable> :
    //      TIDENTIFICADOR <variable'>
    public boolean variable() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <variable'> :
    //      TCORA <expresion> TCORC |
    //      lambda
    public boolean variableP() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // #<variable indexada> :
    //      TIDENTIFICADOR TCORA <expresion> TCORC
    public boolean variable_indexada() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    /*--------------------------------------------------------------*/
    //             ***********EXPRESIONES***********
    /*--------------------------------------------------------------*/
    // #<constante sin signo> : 
    //      TNUMERO | 
    //      TIDENTIFICADOR | 
    //      TCARACTER
    public boolean constante_sin_signo() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <factor> :
    //      TIDENTIFICADOR <factor'> |
    //      TNUMERO |
    //      TCARACTER |
    //      TPARENTA <expresion> TPARENTC |
    //      TOPER_NOT <factor>
    public boolean factor() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <factor'> :
    //      <variable'> |
    //      <designador de funcion'> |
    //      lambda
    public boolean factorP() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <termino> :
    //      <factor><termino'>
    public boolean termino() throws IOException, ExcepALexico, ExcepASintatico {
        //TODO
        return true;
    }

    // <termino'> :
    //      <operador de multiplicacion><factor><termino'> |
    //      lambda
    public boolean terminoP() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <expresion simple> :
    //      <termino><expresion simple'> |
    //      <signo><termino><expresion simple'>
    public boolean expresion_simple() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <expresion simple'> :
    //      <operador de suma><termino><expresion simple'> |
    //      lambda
    public boolean expresion_simpleP() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <expresion> :
    //      <expresion simple><expresion'>
    public boolean expresion() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <expresion'> :
    //      <operador de relecion><expresion simple> |
    //      lambda
    public boolean expresionP() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    /*--------------------------------------------------------------*/
    //              ***********OPERADORES***********
    /*--------------------------------------------------------------*/
    // <operador de multiplicacion> :
    //      TOPERMULT |
    //      TOPER_DIV |
    //      TOPER_AND
    public boolean operador_de_multiplicacion() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <operador de suma> :
    //      TOPERMAS |
    //      TOPERMENOS |
    //      TOPER_OR
    public boolean operador_de_suma() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <operador de relacion> :
    //      TSIMBOLO_IGUAL |
    //      TSIMBOLO_DISTINTO |
    //      TSIMBOLO_MENOR |
    //      TSIMBOLO_MENORIGUAL |
    //      TSIMBOLO_MAYOR |
    //      TSIMBOLO_MAYORIGUAL
    public boolean operador_de_relacion() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    /*--------------------------------------------------------------*/
    //      ***********DESIGNADOR DE FUNCION***********
    /*--------------------------------------------------------------*/
    // <designador de funcion> :
    //      TIDENTIFICADOR <designador de funcion'>
    public boolean designador_de_funcion() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <designador de funcion'> :
    //      TPARENTA <parametro actual> <siguiente parametro actual> TPARENTC |
    //      lambda
    public boolean designador_de_funcionP() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <siguiente parametro actual> :
    //      TCOMA <parametro actual> | 
    //      lambda
    public boolean siguiente_parametro_actual() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    /*--------------------------------------------------------------*/
    //              ***********SENTENCIAS***********
    /*--------------------------------------------------------------*/
    // <sentencia> :
    //      <sentencia simple> |
    //      <sentencia estructurada>
    public boolean sentencia() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <sentencia simple> :
    //      <sentencia de asignacion> |
    //      <sentencia de procedimiento> |
    //      lambda
    public boolean sentencia_simple() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <sentencia de asignacion> :
    //      TIDENTIFICADOR <sentencia de asignacion'>
    public boolean sentencia_se_asignacion() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <sentencia de asignacion'> :
    //      <variable'> TASIGN <expresion> |
    //      TASIGN <expresion>
    public boolean sentencia_se_asignacionP() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <sentencia de procedimiento> :
    //      TIDENTIFICADOR <sentencia de procedimiento'>
    public boolean sentencia_de_procedimiento() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <sentencia de procedimiento'> :
    //      TPARENTA <parametro actual><siguiente parametro actual> |
    //      lambda
    public boolean sentencia_de_procedimientoP() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <parametro actual> :
    //      <expresion>
    public boolean parametro_actual() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <sentencia estructurada> :
    //      <sentencia compuesta> |
    //      <sentencia if> |
    //      <sentencia while>
    public boolean sentencia_estructurada() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <sentencia compuesta> :
    //      TPALRES_BEGIN <sentencia><sentencia compuesta'> TPALRES_END
    public boolean sentencia_compuesta() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_BEGIN) {
            leerToken();
            sentencia();
            sentencia_compuestaP();
            if (TActual.tipo == Token.TPALRES_END) {
                leerToken();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba la palabra reservada 'end' al final de una sentencia compuesta.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba la palabra reservada 'begin' al inicio de una sentencia compuesta.", TActual.nlinea, TActual);
        }
    }

    // <sentencia compuesta'> :
    //      TPUNO_Y_COMA <sentencia> |
    //      lambda
    public boolean sentencia_compuestaP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPUNTO_Y_COMA) {
            leerToken();
            sentencia();
            return true;
        } else {
            return true;
        }
    }

    // <sentencia if> :
    //      TPALRES_IF <expresion> TPALRES_THEN <sentencia> <sentencia if'>
    public boolean sentencia_if() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <sentencia if'> :
    //      TPALRES_ELSE <sentencia> |
    //      lambda
    public boolean sentencia_ifP() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    // <sentencia while> :
    //      TPALRES_WHILE <expresion> TPALRES_DO <sentencia>
    public boolean sentencia_while() throws ExcepALexico, IOException, ExcepASintatico {
        //TODO
        return true;
    }

    /*--------------------------------------------------------------*/
    //      ***********DECLARACION DE PROCEDIMIENTOS***********
    /*--------------------------------------------------------------*/
    // <declaracion de procedimiento> :
    //      <encabezado de procedimiento><bloque>
    public boolean declaracion_de_procedimiento() throws ExcepALexico, IOException, ExcepASintatico {
    	if (TActual.tipo==Token.TPALRES_PROCEDURE){
            encabezado_de_procedimiento();
            bloque();
            return true;
        } else{
            throw new ExcepASintatico("Se esperaba la palabra reservada 'procedure' en la declaracion de procedimiento.", TActual.nlinea, TActual);
        }
    }

    // <bloque> :
    //      <parte de defincion de constantes><parte de defincion de tipos>
    //      <parte de declaracion de variables><parte de declaracion de funciones y procedimientos>
    //      <parte de sentencias>
    public boolean bloque() throws ExcepALexico, IOException, ExcepASintatico {
        parte_de_definicion_de_constantes();
        parte_de_definicion_de_tipos();
        parte_de_declaracion_de_variables();
        parte_de_declaracion_de_funciones_y_procedimientos();
        parte_de_sentencias();
        return true;
    }

    // <encabezado de procedimiento> :
    //      TPALRES_PROCEDURE TIDENTIFICADOR <encabezado de procedimiento'> TPUNTO_Y_COMA
    public boolean encabezado_de_procedimiento() throws ExcepALexico, IOException, ExcepASintatico {
    	if (TActual.tipo == Token.TPALRES_PROCEDURE) {
            leerToken();
            if (TActual.tipo == Token.TIDENTIFICADOR) {
                leerToken();
                encabezado_de_procedimientoP();
                if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                    leerToken();
                    return true;
                } else {
                    throw new ExcepASintatico("Se esperaba un ';' al final del encabezado de un procedimiento.", TActual.nlinea, TActual);
                }
            } else {
                throw new ExcepASintatico("Se esperaba un identificador en el nombre de un procedimiento en su encabezado.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba la palabra reservada 'procedure' al inicion de un encabezado de procedimiento.", TActual.nlinea, TActual);
        }
    }

    // <encabezado de procedimiento'> :
    //      TPARENTA <seccion de parametros formales><siguiente seccion de parametros formales> TPARENTC |
    //      lambda
    public boolean encabezado_de_procedimientoP() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPARENTA) {
            leerToken();
            seccion_de_parametros_formales();
            siguiente_seccion_de_parametros_formales();
            if (TActual.tipo == Token.TPARENTC) {
                leerToken();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un ')' al final de la seccion de parametros formales.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <siguiente seccion de parametros formales> :
    //      TPUNTO_Y_COMA <seccion de parametros formales><siguiente seccion de parametros formales> |
    //      lambda
    public boolean siguiente_seccion_de_parametros_formales() throws ExcepALexico, IOException, ExcepASintatico {
    	if (TActual.tipo == Token.TPUNTO_Y_COMA) {
           leerToken();
           seccion_de_parametros_formales();
           siguiente_seccion_de_parametros_formales();
             return true;
        } else {
           return true;
        }
    }

    // <seccion de parametros formales> :
    //      <grupo de parametros> |
    //      TPALRES_VAR <grupo de parametros>
    public boolean seccion_de_parametros_formales() throws ExcepALexico, IOException, ExcepASintatico {
    	if (TActual.tipo == Token.TPALRES_VAR) {
            leerToken();
            grupo_de_parametros();
            return true;
        }
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            grupo_de_parametros();
            return true;
        }

        throw new ExcepASintatico("Se esperaba una seccion de parametros formales.", TActual.nlinea, TActual);
    }

    // <grupo de parametros> :
    //      TIDENTIFICADOR <siguiente grupo de parametros> TDOSPUNTOS TIDENTIFICADOR
    public boolean grupo_de_parametros() throws ExcepALexico, IOException, ExcepASintatico {
    	 if (TActual.tipo == Token.TIDENTIFICADOR) {
             leerToken();
             siguiente_grupo_de_parametros();
             if (TActual.tipo == Token.TDOSPUNTOS) {
                 leerToken();
                 if (TActual.tipo == Token.TIDENTIFICADOR) {
                     leerToken();
                     return true;
                 } else {
                     throw new ExcepASintatico("Se esperaba un identificador de tipo en la seccion de parametros formales.", TActual.nlinea, TActual);
                 }
             } else {
                 throw new ExcepASintatico("Se esperaba un '=' en la seccion de parametros formales.", TActual.nlinea, TActual);
             }
         } else {
             throw new ExcepASintatico("Se esperaba un identificador de variable en la seccion de parametros formales.", TActual.nlinea, TActual);
         }
    }

    // <siguiente grupo de parametros> :
    //      TCOMA TIDENTIFICADOR <siguiente grupo de parametros> |
    //      lambda
    public boolean siguiente_grupo_de_parametros() throws ExcepALexico, IOException, ExcepASintatico {
    	if (TActual.tipo == Token.TCOMA) {
            leerToken();
            if (TActual.tipo == Token.TIDENTIFICADOR) {
                leerToken();
                siguiente_grupo_de_parametros();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un identificador de variable en la seccion de parametros formales.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <parte de definicion de constantes> :
    //      TPALRES_CONST <definicion de constante> TPUNTO_Y_COMA <siguiente definicion de constante> |
    //      lambda
    public boolean parte_de_definicion_de_constantes() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_CONST) {
            leerToken();
            definicion_de_constante();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                siguiente_definicion_de_constante();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un ';' al final de la defincion de constantes.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <siguiente definicion de constante> :
    //      <defincion de constante> TPUNTO_Y_COMA <siguiente definicion de constante> |
    //      lambda
    public boolean siguiente_definicion_de_constante() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            definicion_de_constante();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                siguiente_definicion_de_constante();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba ';' luego de una definicion de constante.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <parte de definicion de tipos> :
    //      TPALRES_TYPE <definicion de tipo> TPUNTO_Y_COMA <siguiente definicion de tipo> |
    //      lambda
    public boolean parte_de_definicion_de_tipos() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_TYPE) {
            leerToken();
            definicion_de_tipo();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                siguiente_definicion_de_tipo();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un ';' al final de la defincion de tipos.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <siguiente definicion de tipo> :
    //      <defincion de tipo> TPUNTO_Y_COMA <siguiente definicion de tipo> |
    //      lambda
    public boolean siguiente_definicion_de_tipo() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            definicion_de_tipo();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                siguiente_definicion_de_tipo();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un ';' en la definicion de tipos.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <parte de declaracion de variables> :
    //      TPALRES_VAR <declaracion de variable> TPUNTO_Y_COMA <siguiente declaracion de variable> |
    //      lambda
    public boolean parte_de_declaracion_de_variables() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_VAR) {
            leerToken();
            declaracion_de_variable();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                siguiente_declaracion_de_variable();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un ';' al final de la declaracion de variables.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <siguiente declaracion de variable> :
    //      <declaracion de variable> TPUNTO_Y_COMA <siguiente declaracion de variable> |
    //      lambda
    public boolean siguiente_declaracion_de_variable() throws ExcepASintatico, ExcepALexico, IOException {
        if (TActual.tipo == Token.TIDENTIFICADOR) {
            declaracion_de_variable();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                leerToken();
                siguiente_declaracion_de_variable();
                return true;
            } else {
                throw new ExcepASintatico("Se esperaba un ';' al final de la declaracion de variable.", TActual.nlinea, TActual);
            }
        } else {
            return true;
        }
    }

    // <parte de declaracion de funciones y procedimientos> :
    //      <siguiente declaracion de procedimiento o funcion>
    public boolean parte_de_declaracion_de_funciones_y_procedimientos() throws ExcepALexico, IOException, ExcepASintatico {
    	siguiente_declaracion_de_procedimiento_o_funcion();
        return true;
    }

    // <siguiente declaracion de procedimiento o funcion> :
    //      <declaracion de procedimiento o funcion> TPUNTO_Y_COMA <siguiente declaracion de procedimiento o funcion> |
    //      lambda
    public boolean siguiente_declaracion_de_procedimiento_o_funcion() throws ExcepALexico, IOException, ExcepASintatico {
    	
    	if((TActual.tipo == Token.TPALRES_FUNCTION)||(TActual.tipo == Token.TPALRES_PROCEDURE)){
    		declaracion_de_procedimiento_o_funcion();
    		if(TActual.tipo == Token.TPUNTO_Y_COMA){
    			leerToken();
    			siguiente_declaracion_de_procedimiento_o_funcion();
    			return true;
    		}else{
    			throw new ExcepASintatico("Se esperaba un ';' al finalizar la declaracion de un procedimiento o funcion ", TActual.nlinea, TActual);
    		}    		
    	}else{
    		return true;
    	}
    }

    // <declaracion de procedimiento o funcion> :
    //      <declaracion de procedimiento> |
    //      <declaracion de funcion>
    public boolean declaracion_de_procedimiento_o_funcion() throws ExcepALexico, IOException, ExcepASintatico {
    	   if (TActual.tipo == Token.TPALRES_PROCEDURE) {
               declaracion_de_procedimiento();
               return true;
           }
           if (TActual.tipo == Token.TPALRES_FUNCTION) {
               declaracion_de_funcion();
               return true;
           }
           throw new ExcepASintatico("Se esperaba una declaracion de procedimiento o funcion.", TActual.nlinea, TActual);
    }

    // <parte de sentencias> : <sentencia compuesta>
    public boolean parte_de_sentencias() throws ExcepALexico, IOException, ExcepASintatico {
        sentencia_compuesta();
        return true;
    }

    /*--------------------------------------------------------------*/
    // 		***********DECLARACION DE PROCEDIMIENTOS***********
    /*--------------------------------------------------------------*/
    // <declaracion de funcion> :
    //      <encabezado de funcion><bloque>
    public boolean declaracion_de_funcion() throws ExcepALexico, IOException, ExcepASintatico {
        encabezado_de_funcion();
        bloque();
        return true;
    }

    // <encabezado de funcion> :
    //      TPALES_FUNCTION TIDENTIFICADOR <encabezado de funcion'> TDOSPUNTOS TIDENTIFICADOR TPUNTO_Y_COMA
    public boolean encabezado_de_funcion() throws ExcepALexico, IOException, ExcepASintatico {
        if(TActual.tipo == Token.TPALRES_FUNCTION){
        	leerToken();
        	encabezado_de_funcionP();
        	if(TActual.tipo == Token.TDOSPUNTOS){
        		leerToken();
        		if(TActual.tipo == Token.TIDENTIFICADOR){
        			leerToken();
        			if(TActual.tipo == Token.TPUNTO_Y_COMA){
        				
        			}else{
        				throw new ExcepASintatico("Se esperaba el simbolo ';' al final del encabezado de la funcion.", TActual.nlinea, TActual);
        			}
        		}else{
        			throw new ExcepASintatico("Se esperaba un identificador de funcion en el encabezado de la funcion.", TActual.nlinea, TActual);
        		}
        	}else{
        		throw new ExcepASintatico("Se esperaba el simbolo ':' en el encabezado de la funcion.", TActual.nlinea, TActual);
        	}
        }else{
        	throw new ExcepASintatico("Se esperaba la palabra reservada 'funcion' al comienzo del encabezado de la funcion.", TActual.nlinea, TActual);
        }
        return true;
    }

    // <encabezado de funcion'> :
    //      TPARENTA <seccion de parametros formales><siguiente seccion de parametros formales> TPARENTC
    public boolean encabezado_de_funcionP() throws ExcepALexico, IOException, ExcepASintatico {
        if(TActual.tipo == Token.TPARENTA){
        	leerToken();
        	seccion_de_parametros_formales();
        	siguiente_seccion_de_parametros_formales();
        	if(TActual.tipo == Token.TPARENTC){
        		leerToken();
        		return true;
        	}else{
        		throw new ExcepASintatico("Se esperaba el simbolo ')' al final de los parametros formales.", TActual.nlinea, TActual);
        	}
        }else{
        	throw new ExcepASintatico("Se esperaba el simbolo '(' al comienzo de los parametros formales.", TActual.nlinea, TActual);
        }
    }

    /*--------------------------------------------------------------*/
    //              ***********PROGRAMAS***********
    /*--------------------------------------------------------------*/
    // <programa> : 
    //      TPALRES_PROGRAM TIDENTIFICADOR TPUNTO_Y_COMA <bloque> TPUNTO
    public boolean programa() throws ExcepALexico, IOException, ExcepASintatico {
        if (TActual.tipo == Token.TPALRES_PROGRAM) {
            leerToken();
            if (TActual.tipo == Token.TIDENTIFICADOR) {
                leerToken();
                if (TActual.tipo == Token.TPUNTO_Y_COMA) {
                    leerToken();
                    bloque();
                    if (TActual.tipo == Token.TPUNTO) {
                        return true;
                    } else {
                        throw new ExcepASintatico("Se esperaba el simbolo '.' al final del programa.", TActual.nlinea, TActual);
                    }
                } else {
                    throw new ExcepASintatico("Se esperaba el simbolo ';' al final del encabezado del programa.", TActual.nlinea, TActual);
                }
            } else {
                throw new ExcepASintatico("Se esperaba un identificador en el encabezado del programa.", TActual.nlinea, TActual);
            }
        } else {
            throw new ExcepASintatico("Se esperaba la palabra reservada 'PROGRAM' al inicio del codigo.", TActual.nlinea, TActual);
        }
    }

    /*--------------------------------------------------------------*/
    // 			***********GRAMATICA***********
    /*--------------------------------------------------------------*/
    // Si no lee el token que marca el final de archivo luego del ultimo
    // punto del codigo fuente, se levanta una excepcion.
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
