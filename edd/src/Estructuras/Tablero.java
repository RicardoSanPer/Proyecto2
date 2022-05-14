package edd.src.Estructuras;

import edd.src.Estructuras.Utilidad;

public class Tablero
{
    private int[] casilla = new int[5];
    private int casillaVacia = 2;

    private int totalminmax = 0;
    private int posiblesganadosCOM = 0;
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
    }

    public void jugar()
    {
	int turnoJugador = tirarMoneda();
	while(true)
	{
	    //Mostrar tablero
	    verTablero();

	    //Turno de com
	    if(turnoJugador == -1)
	    {
		turnoCOM_MM();
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
		System.out.println("El jugador " + turnoJugador + " ha perdido");
		return;
	    }
	}
    }

    /**Mueve la ficha de una casilla a otra.
     *@param jugador - numero del jugador cuyo turno es
     */
    public void turno(int jugador)
    {
	System.out.printf("Turno de ");
	if(jugador==-1){System.out.println("COM");}
	else{System.out.println("Humano");}

	System.out.println("El jugador tiene " + jugadasPosibles(jugador) + " jugadas posibles");
	
	//Casilla origen
	System.out.println("Ingresa la casilla de la ficha que quieres mover:");
	int casillaInicial = Utilidad.getRange(1,5)-1;

	//Comprueba si la ficha en la casilla pertenece al jugador y se puede mover (no esta encerrada)
	while(!comprobarCasilla(casillaInicial) || casilla[casillaInicial]!=jugador)
	{
	    System.out.println("No puedes mover la ficha en esta posicion. Intenta con otra");
	    casillaInicial = Utilidad.getRange(1,5)-1;
	}

	//casilla destino
	System.out.println("Ingresa la casilla a la que quieres mover la ficha");
	int casillaDestino = Utilidad.getRange(1,5)-1;

	//Comprueba que la casilla destino sea la casilla vacia.
	while(casillaDestino!=casillaVacia)
	{
	    System.out.println("Esta casilla no esta vacia. Intenta con otra.");
	    casillaDestino = Utilidad.getRange(1,5)-1;
	}

	//Movimiento de la ficha
	casilla[casillaDestino]=jugador;
	casilla[casillaInicial]=0;

	//Actualiza la casilla vacia actual
	casillaVacia = casillaInicial;
    }

    /**Turno del COM usando minimax*/
    public void turnoCOM_MM()
    {
	//Com tiene dos fichas, por lo que en cada turno tiene dos posibilidades de jugadas.
	//1- Determinar las casillas de las fichas
	//Los "hijo" de la posicion actual

	totalminmax = 0;
	posiblesganadosCOM = 0;
	
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

	
	//Primer paso recursivo de miniMax
	int maxEval = Integer.MIN_VALUE;
	int jugada = 0;

	//Para recordar la posicion vacia de la configuracion actual
	int vaciaTemp = casillaVacia;
	
	//Para cada hijo de la iteracion actual
	for(int i = 0; i < 2; i++)
	{
	    //Si se puede realizar una jugada
	    if(comprobarCasilla(fichas[i]))
	    {
		//mover ficha al espacio vacio
		casilla[fichas[i]]=0;
		casilla[vaciaTemp]=-1;
		casillaVacia = fichas[i];
		
		//Evaluar minmax
		int eval = minimax(10, 1);
		jugada = (eval>maxEval)? i : jugada;
		maxEval = (eval>maxEval)?eval:maxEval;
		
		//Backtrack
		casilla[fichas[i]]=-1;
		casilla[vaciaTemp]=0;
		casillaVacia = vaciaTemp;
		
	    }
	}

	//flavor text
	System.out.println("COM miró hacia adelante en el tiempo y vio " + totalminmax + " futuros posibles");
	System.out.println("COM gana en " + posiblesganadosCOM + " de ellos.");
	System.out.println("La mejor jugada para COM es mover la ficha en la casilla " + (fichas[jugada]+1));

	swapCasillaVacia(fichas[jugada]);
	System.out.println("\nCOM ha movido la ficha en la casilla " + (fichas[jugada]+1));
	Utilidad.waitInput();
    }

    /**Funcion miniMax del juego, especifico para COM
     *@param profundidad - profundidad de la iteracion actual
     *@pparam jugadorMax - jugador de la iteracion actual cuyos movimientos se val a evaluar
     *@return integer con la evaluacion de movimientos
     */
    private int minimax(int profundidad, int jugadorMax)
    {
	//Si se llega al final del arbol por produnfidad, o si por la configuracion del tablero
	//se termina el juego
	if((profundidad == 0)||!comprobarTablero(1)||!comprobarTablero(-1))
	{
	    totalminmax++;
	    
	    //System.out.println(jugadasPosibles(jugadorMax) + " "  +jugadasPosibles(jugadorMax*-1));

	    //verTablero();
	    //System.out.println("Minmax = " + (jugadasPosibles(jugadorMax)-jugadasPosibles(jugadorMax*-1)));
	    //Utilidad.waitInput();

	    if(jugadasPosibles(1)==0)
	    {
		posiblesganadosCOM++;
	    }
	    
	    return jugadasPosibles(-1)-jugadasPosibles(1);
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

	//Para recordar la posicion vacia de la configuracion actual
	int vaciaTemp = casillaVacia;

	//Si se esta evaluando una posible jugada del com
	if(jugadorMax == -1)
	{
	    int maxEval = Integer.MIN_VALUE;
	    //Para cada hijo de la iteracion actual
	    for(int i = 0; i < 2; i++)
	    {
		//Si se puede realizar una jugada
		if(comprobarCasilla(fichas[i]))
		{
		    //mover ficha al espacio vacio
		    casilla[fichas[i]]=0;
		    casilla[vaciaTemp]=-1;
		    casillaVacia = fichas[i];

		    //Evaluar minmax
		    int eval = minimax(profundidad-1, 1);
		    maxEval = (eval>maxEval)?eval:maxEval;

		    //Backtrack
		    casilla[fichas[i]]=-1;
		    casilla[vaciaTemp]=0;
		    casillaVacia = vaciaTemp;
		    
		}
	    }
	    return maxEval;
	}
	//Si es una jugada del humano
	else
	{
	    int minEval = Integer.MAX_VALUE;
	    //Por cada hijo de la iteracion actual
	    for(int i = 0; i < 2; i++)
	    {
		//si se puede realizar una jugada
		if(comprobarCasilla(fichas[i]))
		{
		    //mover ficha al espacio vacio
		    casilla[fichas[i]]=0;
		    casilla[vaciaTemp]=1;
		    casillaVacia = fichas[i];

		    //Evaluar minimax
		    int eval = minimax(profundidad-1, -1);
		    minEval = (eval < minEval)?eval:minEval;

		    //Backtrack
		    casilla[fichas[i]]=1;
		    casilla[vaciaTemp]=0;
		    casillaVacia = vaciaTemp;
		}
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
	System.out.printf("   | 3:%s    |\n", ficha[2]);
	System.out.printf("   |  /    \\  |\n");
	System.out.printf("4:%s         5:%s", ficha[3], ficha[4]);
	System.out.println("\n");
	System.out.println("  🔴: Jugador");
	System.out.println("  🔷: COM");
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
}
