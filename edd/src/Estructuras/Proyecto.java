package edd.src.Estructuras;

import edd.src.Estructuras.Utilidad;

public class Proyecto
{
    static Tablero tablero = new Tablero();

    static int seleccion = 0;
    
    public static void main(String args[])
    {
	menuPrincipal();
    }

    private static void menuPrincipal()
    {
	while(true)
	{
	    System.out.println("========== Encerrado ==========");
	    System.out.println(" 1. Jugar");
	    System.out.println(" 2. Configurar juego");
	    System.out.println(" 3. Como jugar");
	    System.out.println(" 4. Salir");

	    tablero.reiniciarTablero();
	    
	    elegir(1,4);
	    switch(seleccion)
	    {
	    case 1:
		tablero.jugar();
	        break;
	    case 2:
		configurar();
		break;
	    case 3:
		Utilidad.leerArchivo("./src/Estructuras/instrucciones.txt");
		Utilidad.waitInput();
		break;
	    case 4: return;
	    default: break;
	    }
	}
    }

    private static void configurar()
    {
	while(true)
	{
	    System.out.println("========== Configurar ==========");
	    System.out.println("Jugador 1: " + tablero.getJugador1());
	    System.out.println("Jugador 2: " + tablero.getJugador2());
	    System.out.printf("\n 1. Cambiar oponente (Actual: ");
	    if(tablero.isCOM())
	    {
		System.out.println("COM)");
	    }
	    else
	    {
		System.out.println("Humano)");
	    }
	    System.out.printf(" 2. Cambiar modo de COM (Actual: ");
	    if(!tablero.isDumb())
	    {
		System.out.println("miniMax)");
	    }
	    else
	    {
		System.out.println("Azar)");
	    }
	    System.out.println(" 3. Cambiar nombres");
	    System.out.println(" 4. Regresar");
	    
	    elegir(1,4);
	    switch(seleccion)
	    {
	    case 1:
		if(tablero.isCOM())
		{
		    tablero.setHumano();
		}
		else
		{
		    tablero.setCOM();
		}
		break;
	    case 2:
		if(tablero.isDumb())
		{
		    tablero.setMiniMax();
		}
		else
		{
		    tablero.setAzar();
		}
		break;
	    case 3:
		System.out.println("Ingresa el numero del jugador cuyo nombre quieres cambiar:");
		int j = Utilidad.getRange(1,2);
		System.out.println("Ingresa el nombre para el jugador " + j + ":");
		String n = Utilidad.getUserLine();
		tablero.cambiarNombre(j,n);
		break;
	    case 4:
		return;
	    default:break;
	    }
	}
    }

    private static void elegir(int min, int max)
    {
	System.out.println("Elige una opcion:");
	seleccion = Utilidad.getRange(min,max);
    }
}
