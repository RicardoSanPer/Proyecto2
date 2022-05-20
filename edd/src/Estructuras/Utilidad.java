/**Clase de utilidad
 *@author Ricardo Sánchez Pérez
 *@version 1.1
 *
 *Clase con funciones de utilidad general como recibir numeros, strings y bools
 *de la consola, imprimir archivos de texto, etc.
 */

package edd.src.Estructuras;
import java.util.Scanner;
import java.lang.Math;
import java.util.InputMismatchException;
import java.io.*;

public class Utilidad
{
    private static Scanner input = new Scanner(System.in);

    /**Función que captura un entero. Si es un numero invalido, solicita un numero de nuevo.
     *@return un entero
     */
    public static int getUserInt()
    {
	int numero = 0;
	boolean success = true;
	do
        { 
	    try
	    {
		numero = input.nextInt();
		success = true;
	    }
	    catch (Exception ex)
	    {
		System.out.println("Parece que ese no es un numero valido.");
		success = false;
	    }
	    input.nextLine();
	    
	}while(!success);

	return numero;
    }

    /**Funcion que captura una linea como string.
     *@return String
     */
    public static String getUserLine()
    {
        String texto = "";
	boolean success = true;
	do
        { 
	    try
	    {
		texto = input.nextLine();
		success = true;
	    }
	    catch (Exception ex)
	    {
		System.out.println("Parece que algo salio mal.");
		success = false;
	    }
	    
	}while(!success);

	return texto;
    }

    /**Funcion que captura un boolean de la consola.
     *@return boolean
     */
    public static boolean getUserBool()
    {
	System.out.printf("[S/1: Si, N/0: No]: ");
	String texto = getUserLine();
	texto = texto.toUpperCase();
	texto = texto.trim();

	if(texto.matches("^(Y|S|1)")){return true;}
	if(texto.matches("^(N|0)")){return false;}
	else{
	    return false;
	}
    }

    

    /**Genera un número entero aleatorio dentro de un rango
     *@param min - Limite inferior del rango
     *@param max - Limite superior del rango
     *@return entero aleatorio dentro del rango.
     */
    public static int randomRange(int min, int max)
    {
	return (int) Math.round(Math.random() * max + min);
    }

    /** Envuelve un entero para que se encuentre dentro de un rango. Si el rango es de 1 a 10, y el numero ingresado
     *es por ejemplo 12, lo envuelve dentro del rango para que sea 2.
     *@param i - Entero a envolver
     *@param min - limite inferior del rango
     *@param max - limite superior del rango
     *@return entero dentro del rango
     */
    public static int wrapInteger(int i, int min, int max)
    {
	int diff = max - min;
	if(i < min)
	{
	    while(i < min)
	    {
		i += diff;
	    }
	}
	if(i > max)
	{
	    while(i > max)
	    {
		i -= diff;
	    }
	}
	return i;
    }

    /**Obtiene un entero dentro de un rango. Si esta fuera de rango, pregunta de nuevo.
     *@param min - Limite inferior del rango (inclusivo)
     *@param max - Limite superior del rango (inclusivo)
     *@return entero dentro del rango
     */
    public static int getRange(int min, int max)
    {
	int n = getUserInt();
	while(n < min || n > max)
	{
	    System.out.println("Por favor ingresa un numero dentro del rango " + min + " a " + max );
	    n = getUserInt();
	}
	return n;
    }

    /**Pregunta por un entero mayor o igual a 0
     *@return entero mayor o igual a o0
     */
    public static int getPositive()
    {
	int n = getUserInt();
	while(n < 0)
	    {
		System.out.println("Por favor ingresa un numero mayor o igual a cero:");
		n = getUserInt();
	    }
	return n;
    }

    /**Espera a que el usuario ingrese enter para continuar
     */
    public static void waitInput()
    {
	System.out.println("Ingresa ENTER para continuar");
	input.nextLine();
    }

    /**Lee e imprime los contenidos de un archivo de texto
     *@param nombre - nombre del archivo
     */
    public static void leerArchivo(String nombre)
    {
	try
	{
	    File obj = new File(nombre);
	    Scanner lector = new Scanner(obj);
	    while(lector.hasNextLine())
	    {
		String linea = lector.nextLine();
		System.out.println(linea);
	    }
	    lector.close();
	}
	catch(FileNotFoundException e)
	{
	    System.out.println("No se encontró el archivo");
	    e.printStackTrace();
	}
    }

    /**Regresa el valor absoluto de un valor
     *@param n - numero cuyo valor absoluto se quiere saber
     *@return valor absoluto
     */
    public static int absoluto(int n)
    {
	n *= (n<0)? -1 : 1;
	return n;
    }
}
