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
  public void signo() throws ExcepALexico, IOException, ExcepASintatico {
    if ((TActual.tipo == Token.TOPERMAS)
            || (TActual.tipo == Token.TOPERMENOS)) {
      leerToken();
      return;
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
  //      <signo> <constante'>
  public void constante() throws ExcepALexico, IOException, ExcepASintatico {
    // Si comienza con un numero, caracter o identificador
    if ((TActual.tipo == Token.TNUMERO)
            || (TActual.tipo == Token.TCARACTER)
            || (TActual.tipo == Token.TIDENTIFICADOR)) {
      leerToken();
      return;
    }
    // Si comienza con un signo
    if (TActual.tipo == Token.TOPERMAS
            || TActual.tipo == Token.TOPERMENOS) {
      signo();
      constanteP();
      return;
    }
    // Si no es nada de lo de arriba
    throw new ExcepASintatico("Declaracion de constante invalida.", TActual.nlinea, TActual);
  }

  // <constante'>:
  //       TNUMERO |
  //       TIDENTIFICADOR
  public void constanteP() throws ExcepALexico, IOException, ExcepASintatico {
    if ((TActual.tipo == Token.TNUMERO)
            || (TActual.tipo == Token.TIDENTIFICADOR)) {
      leerToken();
      return;
    } else {
      throw new ExcepASintatico("Declaracion de constante invalida.", TActual.nlinea, TActual);
    }
  }

  //<constante de subrango> :
  //      TNUMERO |
  //      TIDENTIFICADOR |
  //      <signo> <constante'>
  public void constante_de_subrango() throws ExcepALexico, IOException, ExcepASintatico {
    // Si comienza con un numero, caracter o identificador
    if ((TActual.tipo == Token.TNUMERO)
            || (TActual.tipo == Token.TIDENTIFICADOR)) {
      leerToken();
      return;
    }
    // Si comienza con un signo
    if (TActual.tipo == Token.TOPERMAS
            || TActual.tipo == Token.TOPERMENOS) {
      signo();
      constanteP();
      return;
    }
    // Si no es nada de lo de arriba
    throw new ExcepASintatico("Declaracion de constante invalida.", TActual.nlinea, TActual);
  }

  // <defincion de constante> :
  //      TIDENTIFICADOR TSIMBOLO_IGUAL <constante>
  public void definicion_de_constante() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TIDENTIFICADOR) {
      leerToken();
      if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
        leerToken();
        constante();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo '='.", TActual.nlinea, TActual);
      }
    } else {
      throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
    }
  }

  /*--------------------------------------------------------------*/
  //			***********TIPOS***********
    /*--------------------------------------------------------------*/
  // <tipo> :
  //      <tipo simple> |
  //      <tipo arreglo>
  public void tipo() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TIDENTIFICADOR
            || TActual.tipo == Token.TNUMERO
            || TActual.tipo == Token.TCARACTER
            || TActual.tipo == Token.TOPERMAS
            || TActual.tipo == Token.TOPERMENOS) {
      tipo_simple();
      return;
    }
    if (TActual.tipo == Token.TPALRES_ARRAY) {
      tipo_arreglo();
      return;
    }
    throw new ExcepASintatico("Se esperaba un tipo.", TActual.nlinea, TActual);
  }

  // <definicion de tipo> :
  //      TIDENTIFICADOR TSIMBOLO_IGUAL <tipo>
  public void definicion_de_tipo() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TIDENTIFICADOR) {
      leerToken();
      if (TActual.tipo == Token.TSIMBOLO_IGUAL) {
        leerToken();
        tipo();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo '='.", TActual.nlinea, TActual);
      }
    } else {
      throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
    }
  }

  // <tipo simple> :
  //      TIDENTIFICADOR <tipo simple'> |
  //      TNUMERO TDOBLEPUNTO <constante de subrango> |
  //      TOPERMAS <tipo simple de signo>  
  //      TOPERMENOS <tipo simple de signo>
  public void tipo_simple() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TIDENTIFICADOR) {
      leerToken();
      tipo_simpleP();
      return;
    }
    if (TActual.tipo == Token.TNUMERO) {
      leerToken();
      if (TActual.tipo == Token.TDOBLEPUNTO) {
        leerToken();
        constante_de_subrango();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo '..'.", TActual.nlinea, TActual);
      }
    }
    if (TActual.tipo == Token.TOPERMAS
            || TActual.tipo == Token.TOPERMENOS) {
      leerToken();
      tipo_simple_de_signo();
      return;
    }
    throw new ExcepASintatico("Se esperaba un tipo.", TActual.nlinea, TActual);
  }

  //<tipo simple de signo>:
  //	TNUMERO TDOBLEPUNTO <constante de subrango> |
  //	TIDENTIFICADOR TDOBLEPUNTO <constante de subrango> |
  public void tipo_simple_de_signo() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TNUMERO
            || TActual.tipo == Token.TIDENTIFICADOR) {
      leerToken();
      if (TActual.tipo == Token.TDOBLEPUNTO) {
        leerToken();
        constante_de_subrango();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo '..'.", TActual.nlinea, TActual);
      }
    } else {
      throw new ExcepASintatico("Se esperaba un tipo.", TActual.nlinea, TActual);
    }
  }

  // <tipo simple'> :
  //      TDOBLEPUNT <constante de subrango> |
  //      lambda
  public void tipo_simpleP() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TDOBLEPUNTO) {
      leerToken();
      constante_de_subrango();
      return;
    } else {
      return;
    }
  }

  // <tipo arreglo> :
  //      TPALRES_ARRAY TCORA <tipo simple> TCORC TPALRES_OF <tipo simple>
  public void tipo_arreglo() throws ExcepALexico, IOException, ExcepASintatico {
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
            return;
          } else {
            throw new ExcepASintatico("Se esperaba la palabra reservada 'of'.", TActual.nlinea, TActual);
          }
        } else {
          throw new ExcepASintatico("Se esperaba el simbolo ']'.", TActual.nlinea, TActual);
        }
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo '['.", TActual.nlinea, TActual);
      }
    } else {
      throw new ExcepASintatico("Se esperaba la palabra reservada 'array'.", TActual.nlinea, TActual);
    }
  }

  /*--------------------------------------------------------------*/
  // 			***********VARIABLES***********
    /*--------------------------------------------------------------*/
  // <declaracion de variable> :
  //  TIDENTIFICADOR <siguiente identificador> TDOSPUNTOS <tipo>
  public void declaracion_de_variable() throws ExcepASintatico, ExcepALexico, IOException {
    if (TActual.tipo == Token.TIDENTIFICADOR) {
      leerToken();
      siguiente_identificador();
      if (TActual.tipo == Token.TDOSPUNTOS) {
        leerToken();
        tipo();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo ':'.", TActual.nlinea, TActual);
      }
    } else {
      throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
    }
  }

  // <siguiente identificador> :
  //      TCOMA TIDENTIFICADOR <siguiente identificador> |
  //      lambda
  public void siguiente_identificador() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TCOMA) {
      leerToken();
      if (TActual.tipo == Token.TIDENTIFICADOR) {
        leerToken();
        siguiente_identificador();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
      }
    } else {
      return;
    }
  }

  // <variable'> :
  //      TCORA <expresion> TCORC |
  //      lambda
  public void variableP() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TCORA) {
      leerToken();
      expresion();
      if (TActual.tipo == Token.TCORC) {
        leerToken();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo ']'.", TActual.nlinea, TActual);
      }
    } else {
      return;
    }
  }

  /*--------------------------------------------------------------*/
  //             ***********EXPRESIONES***********
    /*--------------------------------------------------------------*/
  // <factor> :
  //      TIDENTIFICADOR <factor'> |
  //      TNUMERO |
  //      TCARACTER |
  //      TPARENTA <expresion> TPARENTC |
  //      TOPER_NOT <factor>
  public void factor() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TIDENTIFICADOR) {
      leerToken();
      factorP();
      return;
    }
    if (TActual.tipo == Token.TNUMERO
            || TActual.tipo == Token.TCARACTER) {
      leerToken();
      return;
    }
    if (TActual.tipo == Token.TPARENTA) {
      leerToken();
      expresion();
      if (TActual.tipo == Token.TPARENTC) {
        leerToken();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo ')'.", TActual.nlinea, TActual);
      }
    }
    if (TActual.tipo == Token.TOPER_NOT) {
      leerToken();
      factor();
      return;
    }

    throw new ExcepASintatico("Factor invalido.", TActual.nlinea, TActual);
  }

  // <factor'> :
  //      <variable'> |
  //      <designador de funcion'> |
  //      lambda
  public void factorP() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TCORA) {
      variableP();
      return;
    }
    if (TActual.tipo == Token.TPARENTA) {
      designador_de_funcionP();
      return;
    }
    // En el caso de lambda
    return;
  }

  // <termino> :
  //      <factor><termino'>
  public void termino() throws IOException, ExcepALexico, ExcepASintatico {
    factor();
    terminoP();
    return;
  }

  // <termino'> :
  //      <operador de multiplicacion><factor><termino'> |
  //      lambda
  public void terminoP() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TOPERMULT
            || TActual.tipo == Token.TOPERDIV
            || TActual.tipo == Token.TOPER_AND) {
      operador_de_multiplicacion();
      factor();
      terminoP();
      return;
    } else {
      return;
    }
  }

  // <expresion simple> :
  //      <termino><expresion simple'> |
  //      <signo><termino><expresion simple'>
  public void expresion_simple() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TIDENTIFICADOR
            || TActual.tipo == Token.TNUMERO
            || TActual.tipo == Token.TCARACTER
            || TActual.tipo == Token.TPARENTA
            || TActual.tipo == Token.TOPER_NOT) {
      termino();
      expresion_simpleP();
      return;
    }
    if (TActual.tipo == Token.TOPERMAS
            || TActual.tipo == Token.TOPERMENOS) {
      signo();
      termino();
      expresion_simpleP();
      return;
    }

    throw new ExcepASintatico("Expresion invalida.", TActual.nlinea, TActual);
  }

  // <expresion simple'> :
  //      <operador de suma><termino><expresion simple'> |
  //      lambda
  public void expresion_simpleP() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TOPERMAS
            || TActual.tipo == Token.TOPERMENOS
            || TActual.tipo == Token.TOPER_OR) {
      operador_de_suma();
      termino();
      expresion_simpleP();
      return;
    } else {
      return;
    }
  }

  // <expresion> :
  //      <expresion simple><expresion'>
  public void expresion() throws ExcepALexico, IOException, ExcepASintatico {
    expresion_simple();
    expresionP();
    return;
  }

  // <expresion'> :
  //      <operador de relacion><expresion simple> |
  //      lambda
  public void expresionP() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TSIMBOLO_IGUAL
            || TActual.tipo == Token.TSIMBOLO_DISTINTO
            || TActual.tipo == Token.TSIMBOLO_MAYOR
            || TActual.tipo == Token.TSIMBOLO_MAYORIGUAL
            || TActual.tipo == Token.TSIMBOLO_MENOR
            || TActual.tipo == Token.TSIMBOLO_MENORIGUAL) {
      operador_de_relacion();
      expresion_simple();
      return;
    } else {
      return;
    }
  }

  /*--------------------------------------------------------------*/
  //              ***********OPERADORES***********
    /*--------------------------------------------------------------*/
  // <operador de multiplicacion> :
  //      TOPERMULT |
  //      TOPER_DIV |
  //      TOPER_AND
  public void operador_de_multiplicacion() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TOPERMULT
            || TActual.tipo == Token.TOPERDIV
            || TActual.tipo == Token.TOPER_AND) {
      leerToken();
      return;
    } else {
      throw new ExcepASintatico("Se esperaba una operador de multiplicacion.", TActual.nlinea, TActual);
    }
  }

  // <operador de suma> :
  //      TOPERMAS |
  //      TOPERMENOS |
  //      TOPER_OR
  public void operador_de_suma() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TOPERMAS
            || TActual.tipo == Token.TOPERMENOS
            || TActual.tipo == Token.TOPER_OR) {
      leerToken();
      return;
    } else {
      throw new ExcepASintatico("Se esperaba una operador de suma.", TActual.nlinea, TActual);
    }
  }

  // <operador de relacion> :
  //      TSIMBOLO_IGUAL |
  //      TSIMBOLO_DISTINTO |
  //      TSIMBOLO_MENOR |
  //      TSIMBOLO_MENORIGUAL |
  //      TSIMBOLO_MAYOR |
  //      TSIMBOLO_MAYORIGUAL
  public void operador_de_relacion() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TSIMBOLO_IGUAL
            || TActual.tipo == Token.TSIMBOLO_DISTINTO
            || TActual.tipo == Token.TSIMBOLO_MAYOR
            || TActual.tipo == Token.TSIMBOLO_MAYORIGUAL
            || TActual.tipo == Token.TSIMBOLO_MENOR
            || TActual.tipo == Token.TSIMBOLO_MENORIGUAL) {
      leerToken();
      return;
    } else {
      throw new ExcepASintatico("Se esperaba una operador de relacion.", TActual.nlinea, TActual);
    }
  }

  /*--------------------------------------------------------------*/
  //      ***********DESIGNADOR DE FUNCION***********
    /*--------------------------------------------------------------*/
  // <designador de funcion'> :
  //      TPARENTA <parametro actual> <siguiente parametro actual> TPARENTC |
  //      lambda
  public void designador_de_funcionP() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPARENTA) {
      leerToken();
      parametro_actual();
      siguiente_parametro_actual();
      if (TActual.tipo == Token.TPARENTC) {
        leerToken();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo ')'.", TActual.nlinea, TActual);
      }
    } else {
      return;
    }
  }

  // <siguiente parametro actual> :
  //      TCOMA <parametro actual><siguiente parametro actual> |
  //      lambda
  public void siguiente_parametro_actual() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TCOMA) {
      leerToken();
      parametro_actual();
      siguiente_parametro_actual();
      return;
    } else {
      return;
    }
  }

  /*--------------------------------------------------------------*/
  //              ***********SENTENCIAS***********
    /*--------------------------------------------------------------*/
  // <sentencia> :
  //      <sentencia simple> |
  //      <sentencia estructurada> |
  //      lambda
  public void sentencia() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TIDENTIFICADOR) {
      sentencia_simple();
      return;
    }
    if ((TActual.tipo == Token.TPALRES_BEGIN)
            || (TActual.tipo == Token.TPALRES_IF)
            || (TActual.tipo == Token.TPALRES_WHILE)) {
      sentencia_estructurada();
      return;
    }
    return;
  }

  // <sentencia simple> :
  //      TIDENTIFICADOR <sentencia simple'>
  public void sentencia_simple() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TIDENTIFICADOR) {
      leerToken();
      sentencia_simpleP();
      return;
    } else {
      throw new ExcepASintatico("Sentencia invalida.", TActual.nlinea, TActual);
    }
  }

  // <sentencia simple'> :
  //      <sentencia de asignacion'> |
  //      <sentencia de procedimiento'> |
  //      lambda
  public void sentencia_simpleP() throws ExcepALexico, IOException, ExcepASintatico {
    if ((TActual.tipo == Token.TCORA)
            || (TActual.tipo == Token.TASIGN)) {
      sentencia_de_asignacionP();
      return;
    }
    if (TActual.tipo == Token.TPARENTA) {
      sentencia_de_procedimientoP();
      return;
    }
    //throw new ExcepASintatico("Se esperaba una sentencia de asignacion o una sentencia de procedimiento validos.", TActual.nlinea, TActual);
    return;
  }

  // <sentencia de asignacion'> :
  //      <variable'> TASIGN <expresion> |
  //      TASIGN <expresion>
  public void sentencia_de_asignacionP() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TCORA) {
      variableP();
    }
    if (TActual.tipo == Token.TASIGN) {
      leerToken();
      expresion();
      return;
    } else {
      throw new ExcepASintatico("Se esperaba el simbolo ':='.", TActual.nlinea, TActual);
    }
  }

  // <sentencia de procedimiento'> :
  //      TPARENTA <parametro actual><siguiente parametro actual> TPARENTC
  public void sentencia_de_procedimientoP() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPARENTA) {
      leerToken();
      parametro_actual();
      siguiente_parametro_actual();
      if (TActual.tipo == Token.TPARENTC) {
        leerToken();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo ')'.", TActual.nlinea, TActual);
      }
    }
  }

  // <parametro actual> :
  //      <expresion>
  public void parametro_actual() throws ExcepALexico, IOException, ExcepASintatico {
    expresion();
    return;
  }

  // <sentencia estructurada> :
  //      <sentencia compuesta> |
  //      <sentencia if> |
  //      <sentencia while>
  public void sentencia_estructurada() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPALRES_BEGIN) {
      sentencia_compuesta();
      return;
    }
    if (TActual.tipo == Token.TPALRES_IF) {
      sentencia_if();
      return;
    }
    if (TActual.tipo == Token.TPALRES_WHILE) {
      sentencia_while();
      return;
    }
    throw new ExcepASintatico("Sentencia invalida.", TActual.nlinea, TActual);
  }

  // <sentencia compuesta> :
  //      TPALRES_BEGIN <sentencia><sentencia compuesta'> TPALRES_END
  public void sentencia_compuesta() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPALRES_BEGIN) {
      leerToken();
      sentencia();
      sentencia_compuestaP();
      if (TActual.tipo == Token.TPALRES_END) {
        leerToken();
        return;
      } else {
        if (TActual.tipo == Token.TIDENTIFICADOR || TActual.tipo == Token.TPALRES_BEGIN) {
          throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
        } else {
          throw new ExcepASintatico("Se esperaba un identificador o la palabra reservada 'begin'.", TActual.nlinea, TActual);
        }
      }
    } else {
      throw new ExcepASintatico("Se esperaba la palabra reservada 'begin'.", TActual.nlinea, TActual);
    }
  }

  // <sentencia compuesta'> :
  //      TPUNTO_Y_COMA<sentencia><sentencia compuesta'> |
  //      lambda
  public void sentencia_compuestaP() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPUNTO_Y_COMA) {
      leerToken();
      sentencia();
      sentencia_compuestaP();
      return;
    } else {
      return;
    }
  }

  // <sentencia if> :
  //      TPALRES_IF <expresion> TPALRES_THEN <sentencia> <sentencia if'>
  public void sentencia_if() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPALRES_IF) {
      leerToken();
      expresion();
      if (TActual.tipo == Token.TPALRES_THEN) {
        leerToken();
        sentencia();
        sentencia_ifP();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba la palabra reservada 'then'.", TActual.nlinea, TActual);
      }
    } else {
      throw new ExcepASintatico("Se esperaba la palabra reservada 'if'.", TActual.nlinea, TActual);
    }
  }

  // <sentencia if'> :
  //      TPALRES_ELSE <sentencia> |
  //      lambda
  public void sentencia_ifP() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPALRES_ELSE) {
      leerToken();
      sentencia();
      return;
    } else {
      return;
    }
  }

  // <sentencia while> :
  //      TPALRES_WHILE <expresion> TPALRES_DO <sentencia>
  public void sentencia_while() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPALRES_WHILE) {
      leerToken();
      expresion();
      if (TActual.tipo == Token.TPALRES_DO) {
        leerToken();
        sentencia();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba la palabra reservada 'do'.", TActual.nlinea, TActual);
      }
    } else {
      throw new ExcepASintatico("Se esperaba la palabra reservada 'while'.", TActual.nlinea, TActual);
    }
  }

  /*--------------------------------------------------------------*/
  //      ***********DECLARACION DE PROCEDIMIENTOS***********
    /*--------------------------------------------------------------*/
  // <declaracion de procedimiento> :
  //      <encabezado de procedimiento><bloque>
  public void declaracion_de_procedimiento() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPALRES_PROCEDURE) {
      encabezado_de_procedimiento();
      bloque();
      return;
    } else {
      throw new ExcepASintatico("Se esperaba la palabra reservada 'procedure'.", TActual.nlinea, TActual);
    }
  }

  // <bloque> :
  //      <parte de defincion de constantes>
  //      <parte de defincion de tipos>
  //      <parte de declaracion de variables>
  //      <parte de declaracion de funciones y procedimientos>
  //      <parte de sentencias>
  public void bloque() throws ExcepALexico, IOException, ExcepASintatico {
    parte_de_definicion_de_constantes();
    parte_de_definicion_de_tipos();
    parte_de_declaracion_de_variables();
    parte_de_declaracion_de_funciones_y_procedimientos();
    parte_de_sentencias();
    return;
  }

  // <encabezado de procedimiento> :
  //      TPALRES_PROCEDURE TIDENTIFICADOR <encabezado de procedimiento'> TPUNTO_Y_COMA
  public void encabezado_de_procedimiento() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPALRES_PROCEDURE) {
      leerToken();
      if (TActual.tipo == Token.TIDENTIFICADOR) {
        leerToken();
        encabezado_de_procedimientoP();
        if (TActual.tipo == Token.TPUNTO_Y_COMA) {
          leerToken();
          return;
        } else {
          throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
        }
      } else {
        throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
      }
    } else {
      throw new ExcepASintatico("Se esperaba la palabra reservada 'procedure'.", TActual.nlinea, TActual);
    }
  }

  // <encabezado de procedimiento'> :
  //      TPARENTA <seccion de parametros formales><siguiente seccion de parametros formales> TPARENTC |
  //      lambda
  public void encabezado_de_procedimientoP() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPARENTA) {
      leerToken();
      seccion_de_parametros_formales();
      siguiente_seccion_de_parametros_formales();
      if (TActual.tipo == Token.TPARENTC) {
        leerToken();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo ')'.", TActual.nlinea, TActual);
      }
    } else {
      return;
    }
  }

  // <siguiente seccion de parametros formales> :
  //      TPUNTO_Y_COMA <seccion de parametros formales><siguiente seccion de parametros formales> |
  //      lambda
  public void siguiente_seccion_de_parametros_formales() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPUNTO_Y_COMA) {
      leerToken();
      seccion_de_parametros_formales();
      siguiente_seccion_de_parametros_formales();
      return;
    } else {
      return;
    }
  }

  // <seccion de parametros formales> :
  //      <grupo de parametros> |
  //      TPALRES_VAR <grupo de parametros>
  public void seccion_de_parametros_formales() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPALRES_VAR) {
      leerToken();
      grupo_de_parametros();
      return;
    }
    if (TActual.tipo == Token.TIDENTIFICADOR) {
      grupo_de_parametros();
      return;
    }

    throw new ExcepASintatico("Seccion de parametros formales invalida.", TActual.nlinea, TActual);
  }

  // <grupo de parametros> :
  //      TIDENTIFICADOR <siguiente grupo de parametros> TDOSPUNTOS TIDENTIFICADOR
  public void grupo_de_parametros() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TIDENTIFICADOR) {
      leerToken();
      siguiente_grupo_de_parametros();
      if (TActual.tipo == Token.TDOSPUNTOS) {
        leerToken();
        if (TActual.tipo == Token.TIDENTIFICADOR) {
          leerToken();
          return;
        } else {
          throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
        }
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo ':'.", TActual.nlinea, TActual);
      }
    } else {
      throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
    }
  }

  // <siguiente grupo de parametros> :
  //      TCOMA TIDENTIFICADOR <siguiente grupo de parametros> |
  //      lambda
  public void siguiente_grupo_de_parametros() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TCOMA) {
      leerToken();
      if (TActual.tipo == Token.TIDENTIFICADOR) {
        leerToken();
        siguiente_grupo_de_parametros();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
      }
    } else {
      return;
    }
  }

  // <parte de definicion de constantes> :
  //      TPALRES_CONST <definicion de constante> TPUNTO_Y_COMA <siguiente definicion de constante> |
  //      lambda
  public void parte_de_definicion_de_constantes() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPALRES_CONST) {
      leerToken();
      definicion_de_constante();
      if (TActual.tipo == Token.TPUNTO_Y_COMA) {
        leerToken();
        siguiente_definicion_de_constante();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
      }
    } else {
      return;
    }
  }

  // <siguiente definicion de constante> :
  //      <defincion de constante> TPUNTO_Y_COMA <siguiente definicion de constante> |
  //      lambda
  public void siguiente_definicion_de_constante() throws ExcepASintatico, ExcepALexico, IOException {
    if (TActual.tipo == Token.TIDENTIFICADOR) {
      definicion_de_constante();
      if (TActual.tipo == Token.TPUNTO_Y_COMA) {
        leerToken();
        siguiente_definicion_de_constante();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
      }
    } else {
      return;
    }
  }

  // <parte de definicion de tipos> :
  //      TPALRES_TYPE <definicion de tipo> TPUNTO_Y_COMA <siguiente definicion de tipo> |
  //      lambda
  public void parte_de_definicion_de_tipos() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPALRES_TYPE) {
      leerToken();
      definicion_de_tipo();
      if (TActual.tipo == Token.TPUNTO_Y_COMA) {
        leerToken();
        siguiente_definicion_de_tipo();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
      }
    } else {
      return;
    }
  }

  // <siguiente definicion de tipo> :
  //      <defincion de tipo> TPUNTO_Y_COMA <siguiente definicion de tipo> |
  //      lambda
  public void siguiente_definicion_de_tipo() throws ExcepASintatico, ExcepALexico, IOException {
    if (TActual.tipo == Token.TIDENTIFICADOR) {
      definicion_de_tipo();
      if (TActual.tipo == Token.TPUNTO_Y_COMA) {
        leerToken();
        siguiente_definicion_de_tipo();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
      }
    } else {
      return;
    }
  }

  // <parte de declaracion de variables> :
  //      TPALRES_VAR <declaracion de variable> TPUNTO_Y_COMA <siguiente declaracion de variable> |
  //      lambda
  public void parte_de_declaracion_de_variables() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPALRES_VAR) {
      leerToken();
      declaracion_de_variable();
      if (TActual.tipo == Token.TPUNTO_Y_COMA) {
        leerToken();
        siguiente_declaracion_de_variable();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
      }
    } else {
      return;
    }
  }

  // <siguiente declaracion de variable> :
  //      <declaracion de variable> TPUNTO_Y_COMA <siguiente declaracion de variable> |
  //      lambda
  public void siguiente_declaracion_de_variable() throws ExcepASintatico, ExcepALexico, IOException {
    if (TActual.tipo == Token.TIDENTIFICADOR) {
      declaracion_de_variable();
      if (TActual.tipo == Token.TPUNTO_Y_COMA) {
        leerToken();
        siguiente_declaracion_de_variable();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
      }
    } else {
      return;
    }
  }

  // <parte de declaracion de funciones y procedimientos> :
  //      <siguiente declaracion de procedimiento o funcion>
  public void parte_de_declaracion_de_funciones_y_procedimientos() throws ExcepALexico, IOException, ExcepASintatico {
    siguiente_declaracion_de_procedimiento_o_funcion();
    return;
  }

  // <siguiente declaracion de procedimiento o funcion> :
  //      <declaracion de procedimiento o funcion> TPUNTO_Y_COMA <siguiente declaracion de procedimiento o funcion> |
  //      lambda
  public void siguiente_declaracion_de_procedimiento_o_funcion() throws ExcepALexico, IOException, ExcepASintatico {
    if ((TActual.tipo == Token.TPALRES_FUNCTION)
            || (TActual.tipo == Token.TPALRES_PROCEDURE)) {
      declaracion_de_procedimiento_o_funcion();
      if (TActual.tipo == Token.TPUNTO_Y_COMA) {
        leerToken();
        siguiente_declaracion_de_procedimiento_o_funcion();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
      }
    } else {
      return;
    }
  }

  // <declaracion de procedimiento o funcion> :
  //      <declaracion de procedimiento> |
  //      <declaracion de funcion>
  public void declaracion_de_procedimiento_o_funcion() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPALRES_PROCEDURE) {
      declaracion_de_procedimiento();
      return;
    }
    if (TActual.tipo == Token.TPALRES_FUNCTION) {
      declaracion_de_funcion();
      return;
    }
    throw new ExcepASintatico("Declaracion de procedimiento o funcion invalida.", TActual.nlinea, TActual);
  }

  // <parte de sentencias> :
  //      <sentencia compuesta>
  public void parte_de_sentencias() throws ExcepALexico, IOException, ExcepASintatico {
    sentencia_compuesta();
    return;
  }

  /*--------------------------------------------------------------*/
  // 		***********DECLARACION DE PROCEDIMIENTOS***********
    /*--------------------------------------------------------------*/
  // <declaracion de funcion> :
  //      <encabezado de funcion><bloque>
  public void declaracion_de_funcion() throws ExcepALexico, IOException, ExcepASintatico {
    encabezado_de_funcion();
    bloque();
    return;
  }

  // <encabezado de funcion> :
  //      TPALES_FUNCTION TIDENTIFICADOR <encabezado de funcion'> TDOSPUNTOS TIDENTIFICADOR TPUNTO_Y_COMA
  public void encabezado_de_funcion() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPALRES_FUNCTION) {
      leerToken();
      if (TActual.tipo == Token.TIDENTIFICADOR) {
        leerToken();
        encabezado_de_funcionP();
        if (TActual.tipo == Token.TDOSPUNTOS) {
          leerToken();
          if (TActual.tipo == Token.TIDENTIFICADOR) {
            leerToken();
            if (TActual.tipo == Token.TPUNTO_Y_COMA) {
              leerToken();
              return;
            } else {
              throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
            }
          } else {
            throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
          }
        } else {
          throw new ExcepASintatico("Se esperaba el simbolo ':'.", TActual.nlinea, TActual);
        }
      } else {
        throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
      }
    } else {
      throw new ExcepASintatico("Se esperaba la palabra reservada 'function'.", TActual.nlinea, TActual);
    }
  }

  // <encabezado de funcion'> :
  //      TPARENTA <seccion de parametros formales><siguiente seccion de parametros formales> TPARENTC |
  //      lambda
  public void encabezado_de_funcionP() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPARENTA) {
      leerToken();
      seccion_de_parametros_formales();
      siguiente_seccion_de_parametros_formales();
      if (TActual.tipo == Token.TPARENTC) {
        leerToken();
        return;
      } else {
        throw new ExcepASintatico("Se esperaba el simbolo ')'.", TActual.nlinea, TActual);
      }
    } else {
      return;
    }
  }

  /*--------------------------------------------------------------*/
  //              ***********PROGRAMAS***********
    /*--------------------------------------------------------------*/
  // <programa> :
  //      TPALRES_PROGRAM TIDENTIFICADOR TPUNTO_Y_COMA <bloque> TPUNTO
  public void programa() throws ExcepALexico, IOException, ExcepASintatico {
    if (TActual.tipo == Token.TPALRES_PROGRAM) {
      leerToken();
      if (TActual.tipo == Token.TIDENTIFICADOR) {
        leerToken();
        if (TActual.tipo == Token.TPUNTO_Y_COMA) {
          leerToken();
          bloque();
          if (TActual.tipo == Token.TPUNTO) {
            leerToken();
            return;
          } else {
            throw new ExcepASintatico("Se esperaba el simbolo '.'.", TActual.nlinea, TActual);
          }
        } else {
          throw new ExcepASintatico("Se esperaba el simbolo ';'.", TActual.nlinea, TActual);
        }
      } else {
        throw new ExcepASintatico("Se esperaba un identificador.", TActual.nlinea, TActual);
      }
    } else {
      throw new ExcepASintatico("Se esperaba la palabra reservada 'program'.", TActual.nlinea, TActual);
    }
  }

  /*--------------------------------------------------------------*/
  // 			***********GRAMATICA***********
    /*--------------------------------------------------------------*/
  // Si no lee el token que marca el final de archivo luego del ultimo
  // punto del codigo fuente, se levanta una excepcion.
  public void eof() throws ExcepASintatico {
    if (TActual.tipo == Token.TEOF) {
      return;
    }
    throw new ExcepASintatico("Se esperaba el final del archivo, pero se encontro algo mas...", TActual.nlinea, TActual);
  }
}
