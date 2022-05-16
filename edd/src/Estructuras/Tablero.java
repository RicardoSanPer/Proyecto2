package edd.src.Estructuras;

import edd.src.Estructuras.Utilidad;
import java.util.Iterator;

public class Tablero extends ArbolBinario<Integer>
{
    private int[] casilla = new int[5];
    private int casillaVacia = 2;

    private boolean comminmax = true;
    private boolean jugadorCOM = true;
    
    private int totalminmax = 0;
    private int posiblesganadosCOM = 0;

    private String jugador1 = "Jugador 1";
    private String jugador2 = "COM";
    /*
     *   1 ----- 2
     *   | \   / |
     *   |  \ /  |
     *   |   3   |
     *   |  / \  |
     *   | /   \ |
     *   4       5
     *
     */

    public Tablero()
    {
	casilla[0] = 1;
	casilla[1] = -1;
	casilla[2] = 0;
	casilla[3] = -1;
	casilla[4] = 1;

	raiz = new Vertice(-1);
    }

    /**Funcion principal de juego. Esta es la funcion que se llama para jugar
     */
    public void jugar()
    {
	int turnoJugador = tirarMoneda();
	while(true)
	{
	    //Mostrar tablero
	    verTablero();

	    //Turno de com
	    if(turnoJugador == -1 && jugadorCOM)
	    {
		promptCOM();
		if(comminmax){turnoCOM_MM();}
		else{turnoCOM_Azar();}
	    }

	    //turno del humano
	    else
	    {
		turno(turnoJugador);
	    }
	    
	    //cambio de turno
	    turnoJugador *= -1;

	    
	    //Determinar si aun hay jugadas posibles
	    if(!comprobarTablero(turnoJugador))
	    {
		verTablero();
		if(turnoJugador == -1)
		{
		    System.out.println(jugador1 + " ha ganado");
		}
		else
		{
		    System.out.println(jugador2 + " ha ganado");
		}
		Utilidad.waitInput();
		return;
	    }
	}
    }

    /**Pregunta si se desea cambiar el modo de juego de COM
     */
    public void promptCOM()
    {
	System.out.println("Deseas cambiar la configuración de " + jugador2);
	if(isDumb())
	{
	    System.out.println("Actual: Azar");
	}
	else
	{
	    System.out.println("Actual: MiniMax");
	}

	if(Utilidad.getUserBool())
	{
	    if(isDumb())
	    {
		setMiniMax();
	    }
	    else
	    {
		setAzar();
	    }
	}
    }

    /**Mueve la ficha de una casilla a otra.
     *@param jugador - numero del jugador cuyo turno es
     */
    public void turno(int jugador)
    {
	System.out.printf("Turno de ");
	if(jugador==-1){System.out.println(jugador2);}
	else{System.out.println(jugador1);}

	//System.out.println("El jugador tiene " + jugadasPosibles(jugador) + " jugadas posibles");
	
	//Casilla origen
	System.out.println("Ingresa la casilla de la ficha que quieres mover:");
	int casillaInicial = Utilidad.getRange(1,5)-1;

	//Comprueba si la ficha en la casilla pertenece al jugador y se puede mover (no esta encerrada)
	while(!comprobarCasilla(casillaInicial) || casilla[casillaInicial]!=jugador)
	{
	    System.out.println("No puedes mover la ficha en esta posicion. Intenta con otra");
	    casillaInicial = Utilidad.getRange(1,5)-1;
	}

	//Movimiento de la ficha
	casilla[casillaVacia]=jugador;
	casilla[casillaInicial]=0;

	//Actualiza la casilla vacia actual
	casillaVacia = casillaInicial;
    }

    /**Mueve fichas de COM al azar. En raras ocaciones mueve fichas que se su pone no deberia de mover
     * No estoy deguro de por que
     */
    private void turnoCOM_Azar()
    {
	int[] fichas = new int[2];
	int indice = 0;
	for(int i = 0; i < 5; i++)
	{
	    if(casilla[i]==-1)
	    {
		fichas[indice] = i;
		indice++;
	    }
	}

	int jugada = Utilidad.randomRange(0,1);

	//System.out.println("JUgadas: " + (fichas[0]+1) + " " + (fichas[1]+1));
	//System.out.println((fichas[jugada]+1));

	//Si la jugade no es posible
	if(!comprobarCasilla(jugada))
	{
	    jugada = Utilidad.wrapInteger((jugada+1),0,1);
	}

	
	
	//flavor text
	System.out.println("\n" + jugador2 + " tiene jalea en el CPU y no sabe lo que hace.");
	System.out.println(jugador2 + " juega la ficha en la casilla " + (fichas[jugada]+1));
	swapCasillaVacia(fichas[jugada]);
	Utilidad.waitInput();
    }
    
    /**Turno del COM usando minimax*/
    public void turnoCOM_MM()
    {
	//Com tiene dos fichas, por lo que en cada turno tiene dos posibilidades de jugadas.
	//1- Determinar las casillas de las fichas
	//Los "hijo" de la posicion actual

	totalminmax = 0;
	posiblesganadosCOM = 0;
	
        minimax(10,-1,raiz);

	//System.out.println((raiz.izquierdo.elemento+1) + " " + (raiz.derecho.elemento+1));

	//System.out.println("minimax arbol");
	System.out.printf("\n" + jugador2 + " miró 10 turnos hacia adelante en el tiempo y vio %d futuros posibles, ", totalminmax);
	System.out.println("ganando en " + posiblesganadosCOM + " de ellos.");
	//System.out.println("La mejor jugada para COM es mover la ficha en la casilla " + (raiz.elemento+1));

	swapCasillaVacia(raiz.elemento);
	System.out.println("\n" + jugador2 + " ha movido la ficha en la casilla " + (raiz.elemento+1));
	Utilidad.waitInput();
    }

    /**Funcion miniMax del juego, especifico para COM
     *
     * Minimax funciona igual a cualquier implementacion, pero en adicion hay un arbol binario
     * donde cada nodo representa la jugada optima del jugador para la configuracion del tablero que se esta
     * evaluando, siendo la raíz la jugada optima para el jugador que llamo la funcion (com) y por lo tanto
     * la jugada que se realizará.
     *
     *
     *@param profundidad - profundidad de la iteracion actual
     *@pparam jugadorMax - jugador de la iteracion actual cuyos movimientos se val a evaluar
     *@param v - vertice representando la jugada actual del jugador en la configuracion actual del tablero.
     *@return integer con la evaluacion de movimientos
     */
    private int minimax(int profundidad, int jugadorMax, Vertice v)
    {
	//Si se llega al final del arbol por produnfidad, o si por la configuracion del tablero
	//se termina el juego
	if((profundidad == 0)||!comprobarTablero(1)||!comprobarTablero(-1))
	{
	    totalminmax++;
	    if(jugadasPosibles(1)==0)
	    {
		posiblesganadosCOM++;
	    }
	    //El valor de la partida se determina por el numero de fichas que COM puede mover menos el numero
	    //de ficas que el humano puede mover, multiplicado por la profundidad actual para priorizar
	    //movimientos que logren la victoria en un menor numero de turnos.
	    return (jugadasPosibles(-1)-jugadasPosibles(1))*(profundidad+1);
	}

	//Movimientos posibles a partir de la posicion actual
	//Los "hijo" del nodo actual de minimax
	int[] fichas = new int[2];
	int indice = 0;
	for(int i = 0; i < 5; i++)
	{
	    if(casilla[i]==jugadorMax)
	    {
		fichas[indice] = i;
		indice++;
	    }
	}

	//Crea un arbol donde los vertices con las jugadas posibles.

	//Para recordar la posicion vacia de la configuracion actual
	int vaciaTemp = casillaVacia;
        v.elemento = null;
	v.derecho = new Vertice(null);
	v.izquierdo = new Vertice(null);
	//Si se esta evaluando una posible jugada del com (maximizacion)
	if(jugadorMax == -1)
	{
	    int maxEval = Integer.MIN_VALUE;

	    //Rama izquierda
	    if(comprobarCasilla(fichas[0]))
		{
		    //mover ficha al espacio vacio
		    swapCasillaVacia(fichas[0]);

		    //Evaluar minmax
		    int eval = minimax(profundidad-1, 1, v.izquierdo);

		    //Si la evaluacion minmax es mayor a la actual, la reemplaza
		    if(eval > maxEval)
		    {
			maxEval = eval;
			v.elemento = fichas[0];
		    }

		    //System.out.println(fichas[0]);
		    //System.out.println(v.elemento);
		    
		    //Backtrack
		    swapCasillaVacia(vaciaTemp);
		   
		    casillaVacia = vaciaTemp;
		}
	    //Rama derecha
	    if(comprobarCasilla(fichas[1]))
		{
		    //mover ficha al espacio vacio
		    swapCasillaVacia(fichas[1]);

		    //Evaluar minmax
		    int eval = minimax(profundidad-1, 1, v.derecho);

		    //Si la evaluacion mimmax es mayor a la actual, la reemplaza
		    if(eval > maxEval)
		    {
			maxEval = eval;
			v.elemento = fichas[1];
		    }
		    //System.out.println(fichas[0]);
		    //System.out.println(v.elemento);
		    
		    //Backtrack
		    swapCasillaVacia(vaciaTemp);
		    
		    casillaVacia = vaciaTemp;
		}
	    return maxEval;
	}
	//Si es una jugada del humano (minimizacion)
	else
	{
	    int minEval = Integer.MAX_VALUE;

	    //Para cada ficha de la configuracion actuas, es decir,
	    //para cada hijo de la iteracion actual
	    //Rama izquierda
	    if(comprobarCasilla(fichas[0]))
		{
		    //mover ficha al espacio vacio
		    swapCasillaVacia(fichas[0]);

		    //Evaluar minmax
		    int eval = minimax(profundidad-1, -1, v.izquierdo);

		    //Si la evaluacion minmax es mayor a la actual, la reemplaza
		    if(eval < minEval)
		    {
			minEval = eval;
			v.elemento = fichas[0];
		    }
		    
		    //Backtrack
		    swapCasillaVacia(vaciaTemp);
		   
		    casillaVacia = vaciaTemp;
		}
	    //Rama derecha
	    if(comprobarCasilla(fichas[1]))
		{
		    //mover ficha al espacio vacio
		    swapCasillaVacia(fichas[1]);

		    //Evaluar minmax
		    int eval = minimax(profundidad-1, -1, v.derecho);

		    //Si la evaluacion mimmax es mayor a la actual, la reemplaza
		    if(eval < minEval)
		    {
			minEval = eval;
			v.elemento = fichas[1];
		    }
		    //Backtrack
		    swapCasillaVacia(vaciaTemp);
		   
		    casillaVacia = vaciaTemp;
		}
	    
	    return minEval;
	}
	
    }

    /**Revisa si aun hay jugadas posibles en el tablero (si un jugador está atrapado)
     */
    public boolean comprobarTablero(int jugador)
    {
	boolean resultado = false;
	
	//Casilla 1
	if(casilla[0]==jugador)
	{
	    resultado = (casilla[1]==0 || casilla[3]==0||resultado);
	}
	//Casilla 2
	if(casilla[1]==jugador)
	{
	    resultado = (casilla[0]==0 || casilla[2]==0||casilla[4]==0||resultado);
	}
	//Casilla 3
	if(casilla[2]==jugador)
	{
	    resultado = (casilla[0]==0 || casilla[1]==0||casilla[3]==0||casilla[4]==0||resultado);
	}
	//casilla 4
	if(casilla[3]==jugador)
	{
	    resultado = (casilla[0]==0 || casilla[2]==0||resultado);
	}
	//casilla 5
	if(casilla[4]==jugador)
	{
	    resultado = (casilla[1]==0 || casilla[2]==0||resultado);
	}
	return resultado;
    }
    
    /**Determina si una casilla tiene jugadas posibles (es adyacente a la casilla vacia)
     *@param casillaComprobar - numero de la casilla a comprobar
     *@return boolean indicando si tiene jugadas o no.
     */
    public boolean comprobarCasilla(int casillaComprobar)
    {
	switch(casillaComprobar)
	{
	case 0: return (casilla[1]==0 || casilla[2]==0 || casilla[3]==0);
	case 1: return (casilla[0]==0 || casilla[2]==0 || casilla[4]==0);
	case 2: return (casilla[0]==0 || casilla[1]==0 || casilla[3]==0 || casilla[4]==0);
	case 3: return (casilla[0]==0 || casilla[2]==0);
	case 4: return (casilla[1]==0 || casilla[2]==0);
	default: break;
	}
	return false;
    }

    /**Imprime el tablero*/
    public void verTablero()
    {
        String[] ficha = new String[5];
	
	for(int i = 0; i < 5; i++)
	{
	    //Jugador 1 (ficha roja)
	    if(casilla[i] == 1)
	    {
		ficha[i] = "🔴";
	    }
	    //jugador 2
	    else if(casilla[i] == -1)
	    {
		ficha[i] = "🔷";
	    }
	    else
	    {
		ficha[i] = "[ ]";
	    }
	}

	System.out.printf("1:%s - - - - 2:%s\n", ficha[0], ficha[1]);
	System.out.printf("   | \\     /  |\n");
	System.out.printf("   | 3:%s     |\n", ficha[2]);
	System.out.printf("   |  /    \\  |\n");
	System.out.printf("4:%s         5:%s", ficha[3], ficha[4]);
	System.out.println("\n");
	System.out.println("  🔴: " + jugador1);
	System.out.println("  🔷: " + jugador2);
    }

    /**Obtiene el numero de jugadas/Fichas que el jugador puede mover.
     *@param jugador - Jugador a buscar numero de jugadas
     *@return numero de jugadas posibles
     */
    private int jugadasPosibles(int jugador)
    {
	int contador = 0;
	for(int i = 0; i < 5; i++)
	{
	    if(casilla[i]==jugador && comprobarCasilla(i))
	    {
		contador++;
	    }
	}
	return contador;
    }

    /**Funcion para determinar cual jugador comienza el juego
     *@return integer con el jugador a iniciar (-1: jugador 2, 1: jugador 1);
     */
    private int tirarMoneda()
    {
	int resultado = Utilidad.randomRange(0,1);
	if(resultado == 1){return -1;}
	else return 1;
    }

    /**Cambia la casilla con la vacia
     *@param n - numero de la casilla a mover
     */
    private void swapCasillaVacia(int n)
    {
	casilla[casillaVacia] = casilla[n];
	casilla[n] = 0;
	casillaVacia = n;
    }

    @Override
    public Iterator<Integer> iterator() {
        return null;
    }

    /**Establece el modo de juego de COM a usar minimax*/
    public void setMiniMax()
    {
	comminmax = true;
    }

    /**Establece el modo de juego de COM a usar movimientos al azar*/
    public void setAzar()
    {
	comminmax = false;
    }

    /**Reinicia el tablero para una nueva partida*/
    public void reiniciarTablero()
    {
	casilla[0] = 1;
	casilla[1] = -1;
	casilla[2] = 0;
	casilla[3] = -1;
	casilla[4] = 1;

	casillaVacia = 2;

	totalminmax = 0;
	posiblesganadosCOM = 0;
    }

    /**Cambia el nombre de un jugador
     *@param n - numero del jugador a cambiar
     *@param s - nombre del jugador
     */
    public void cambiarNombre(int n, String s)
    {
	if(n<1||n>2){return;}
	
	else if(n == 1)
	{
	    jugador1 = s;
	}
	else
	{
	    jugador2 = s;
	}
    }

    /**Determina si el juego es contra la computadora o un humano
     *@param boolean indicando si el oponente es humano
     */
    public boolean isCOM()
    {
	return jugadorCOM;
    }

    /**Determina si el COM esta configurado para jugar usando MiniMax o movimientos aleatorios
     *@return boolean indicando si esta usando azar
     */
    public boolean isDumb()
    {
	return !comminmax;
    }

    /**Cambia el oponente a un humano*/
    public void setHumano()
    {
	jugadorCOM = false;
    }

    /**Cambia el oponente a la computadora*/
    public void setCOM()
    {
	jugadorCOM = true;
    }

    /**Obtiene el nombre del jugador 1
     *@return string con el nombre del jugador
     */
    public String getJugador1()
    {
	return jugador1;
    }
    /**Obtiene el nombre del jugador 2
     *@return string con el nombre del jugador
     */
    public String getJugador2()
    {
	return jugador2;
    }
}
