package edd.src.Estructuras;

import edd.src.Estructuras.Utilidad;

public class Tablero
{
    private int[] casilla = new int[5];
    private int casillaVacia = 2;
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
	    //turno del jugador actual
	    turno(turnoJugador);
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

    private void turnoCOM()
    {
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

    /**Funcion para determinar cual jugador comienza el juego
     *@return integer con el jugador a iniciar (-1: jugador 2, 1: jugador 1);
     */
    private int tirarMoneda()
    {
	int resultado = Utilidad.randomRange(0,1);
	if(resultado == 1){return -1;}
	else return 1;
    }
}
