package edd.src.Estructuras;

import edd.src.Estructuras.Utilidad;

public class Proyecto
{
    static Tablero tablero = new Tablero();
    public static void main(String args[])
    {
	System.out.println("Proyecto");
        tablero.jugar();
	tablero.verTablero();
	//tablero.comprobarTablero(1);
    }
}
