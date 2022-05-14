package edd.src.Estructuras;

import edd.src.Estructuras.Utilidad;

public class Tablero
{
    private int[] casilla = new int[5];
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
	int turnoJugador = 1;
	for(int i = 0; i < 5; i++)
	{
	    verTablero();
	    turno(turnoJugador);
	    turnoJugador *= -1;
	}
    }

    /**Mueve la ficha de una casilla a otra.
     *@param jugador - numero del jugador cuyo turno es
     */
    public void turno(int jugador)
    {
	//Casilla origen
	System.out.println("Ingresa la casilla de la ficha que quieres mover:");
	int casillaInicial = Utilidad.getRange(1,5)-1;
	while(casilla[casillaInicial]!=jugador)
	{
	    System.out.println("No tienes una ficha en esa casilla. Intenta con otra");
	    casillaInicial = Utilidad.getRange(1,5)-1;
	}
	//casilla destino
	System.out.println("Ingresa la casilla a la que quieres mover la ficha");
	int casillaDestino = Utilidad.getRange(1,5)-1;
	while(casilla[casillaDestino]!=0)
	{
	    System.out.println("Esta casilla no esta vacia. Intenta con otra.");
	    casillaDestino = Utilidad.getRange(1,5)-1;
	}
	//Movimiento de la ficha
	casilla[casillaDestino]=jugador;
	casilla[casillaInicial]=0;
	
    }

    /**Revisa si aun hay jugadas posibles en el tablero (si un jugador está atrapado)
     */
    public boolean comprobarTablero()
    {
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
}
