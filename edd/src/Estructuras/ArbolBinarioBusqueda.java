package edd.src.Estructuras;
import java.util.Iterator;
import java.util.Comparator;

public class ArbolBinarioBusqueda<T extends Comparable<T>> extends ArbolBinario<T>
{

    public ArbolBinarioBusqueda()
    {
    }
    
    /**Constructor por lista
     *@param lista - lista de elementos a partir del cual construir el arbol
     */
    public ArbolBinarioBusqueda(Collection<T> lista)
    {
	super(lista);
	//System.out.println("Binario");
    }

    /**Build unsorted. Funciona actualmente solo con enteros
     *@param lista - lista de enteros
     *
     *
     *Complejidad de O(nlogn), pues el ordenamiento de la lista toma ese tiempo, y la
     *construccion del arbol toma O(n), lo cual es menor
     *
     */
    public void buildUnsorted(Lista<T> lista)
    {
	//Crea una lista a partir de la coleccion para ordenar mas facilmente
	Lista<T> ordenado = new Lista<T>();
	//System.out.println("Lista :" +lista);
	
	//Ordena la lista. No se que tan bien funcione con datos que no sean integer
	ordenado = lista.mergeSort(new Comparator<T>(){
		@Override
		public int compare(T o1, T o2) {
		    return ((Integer)o1) - ((Integer)o2);
		}});

	//Construye el arbol con la lista ordenada
	//System.out.println("Ordenado :" + ordenado);
	
        buildSorted(ordenado);
    }
    
    /**Construye un arbol de busqueda binaria a partir de una lista ordenada
     *@param lista - lista ordenada
     *
     *
     *Tiempo de ejecucion de O(n) (explicado en la funcion construir vertices)
     */
    public void buildSorted(Lista<T> lista)
    {
	this.empty();
	elementos = 0;
	//System.out.println(lista);

	//Version O(nlogn)
	//raiz = construirVertices(lista, 0, lista.size()-1, null);

	
	IteradorLista<T> it = lista.iteradorLista();
	int n = lista.size();
	//System.out.println(lista);
	raiz = construirSorted(it, n);
    }

    /**Construye el arbol O(n)
     *@param it - iterador de la lista de elementos a partir de la cual se construye el BST
     *@param n - tamaño de la lista
     *@return vertice con el subarbol construido
     */
    private Vertice construirSorted(IteradorLista<T> it, int n)
    {
	//Si el iterador llego al final
	if(!it.hasNext())
	    {
		return null;
	    }
	//Si n es cero
	if(n<=0)
	    {
		return null;
	    }
	//Subarbol izquierdo
	Vertice izq = construirSorted(it, n/2);

	//Subarbol actual
	Vertice r = new Vertice(it.next());
	elementos++;

	r.izquierdo = izq;

	//Subarbol derecho
	r.derecho = construirSorted(it, n - (n/2) -1);

	return r;
	/**
	 * El tiempo de ejecucion es de O(n). Durante cada recursión se para como
	 * parametro el iterador de la lista, el cual se mueve unicamente una posición
	 * por cada llamada de la funcion (it.next()). Puesto que este itera por cada elemento
	 * de la lista, entonces itera n veces a lo mucho, caso en el cual termina la iteracion y
	 * la funcion.
	 * 
	 */
    }

    /**Construye el BST a partir de un arbol binario completo
     *@param arbol - arbol binario completo a convertir
     */
    public void convertBST(ArbolBinarioCompleto<T> arbol)
    {
	Iterator<T> it = arbol.iterator();
	Lista<T> lista = new Lista<T>();

	//crear una lista con todos los elementos para luego pasar a buildUnsorted
	while(it.hasNext())
	    {
		lista.add(it.next());
	    }

	buildUnsorted(lista);
    }
    
    /**Busca un elemento en el BST
     *@param elemento - elemento a buscar
     *@return boolean indicando si el elemento se encuentra o no en el arbol
     */
    public boolean search(T elemento)
    {
	//SI no hay elementos en el arbol
	if(elementos == 0)
	{
	    return false;
	}
	return search(raiz, elemento, new Comparator<T>(){
		@Override
		public int compare(T o1, T o2) {
		    return ((Integer)o1) - ((Integer)o2);
		}});
    }

    /**Busca un elemento en un vertice.
     *@param v - vertice en el que se va a buscar
     *@param elemento - elemento que se está buscando
     *@param comparador - comparador que compara el elemento con el nodo
     *@return boolean con el resultado de la busqueda
     */
    private boolean search(Vertice v, T elemento, Comparator<T> comparador)
    {
	//Si el elemento es igual al elemento del nodo, regresar true
	if(comparador.compare(v.elemento,elemento) == 0)
	    {
	    return true;
	    }

	//Buscar en el nodo derecho si el elemento a buscar es menor
	if(comparador.compare(v.elemento,elemento) < 0 && v.hayDerecho())
	    {
		return search(v.derecho, elemento, comparador);
	    }
        //Buscar en el nodo izquierdo
	else if(v.hayIzquierdo())
	    {
		return search(v.izquierdo, elemento, comparador);
	    }
	//Si ya no hay nodos que buscar, entonces el elemento no esta en el arbol
	else
	    {
		return false;
	    }

    }

    /**Inserta un elemento en el BST.
     *@param elemento - elemento a insertar
     */
    public void insert(T elemento)
    {
	raiz = insert(raiz, null, elemento, new Comparator<T>(){
		@Override
		public int compare(T o1, T o2) {
		    return ((Integer)o1) - ((Integer)o2);
		}});
    }

    /**Inserta un elemento en el BST
     *@param v - vertice del subarbol en el que se va a insertar
     *@param padre - vertice padre del subarbol
     *@param elemento - elemento a insertar
     *@param comparador - comparador para comparar elementos
     *@return subarbol con el elemento insertado
     */
    private Vertice insert(Vertice v, Vertice padre, T elemento, Comparator<T> comparador)
    {
	//Si el nodo es nulo, crear un nodo nuevo
	if(v == null)
	    {
		Vertice temp = new Vertice(elemento);
		temp.padre = padre;
		elementos++;
		return temp;
	    }
	//Si el elemento es igual a uno que ya se encuentra en el arbol
	if(comparador.compare(v.elemento, elemento)==0)
	    {
		System.out.println("El elemento " + elemento + " ya se encuentra en el arbol.");
		return v;
	    }
	//Colocar en el subarbol derecho si es mayor
	if(comparador.compare(v.elemento, elemento)<0)
	    {
	        v.derecho = insert(v.derecho, v, elemento, comparador);
	    }
	//Colocar en el subarbol izquierdo si es menor
	else
	    {
	        v.izquierdo = insert(v.izquierdo, v, elemento, comparador);
	    }
	//regresar el arbol construido
	return v;
	
    }

    /**Funcion para eliminar un elemento del BST
     *@return boolean indicando si se elimino
     */
    public boolean delete(T elemento)
    {
	if(elementos == 0 || !search(elemento))
	    {
		//System.out.println("El elemento no existe");
		return false;
	    }
	raiz = delete(raiz, elemento,  new Comparator<T>(){
		@Override
		public int compare(T o1, T o2) {
		    return ((Integer)o1) - ((Integer)o2);
		}});
	raiz.derecho.padre = raiz;
	raiz.izquierdo.padre = raiz;
	return true;
	
    }

    /**Elimina un elemento del BST.
     *@param v - vertice en cuyo subarbol se esta buscando el elemento a eliminar
     *@param  elemento - elemento a eliminar
     *@param comparador - comparador para realizar las comparaciones
     *@return subarbol con el nodo eliminado
     */
    private Vertice delete(Vertice v, T elemento, Comparator<T> comparador)
    {
	//Si se llego a una hoja sin elementos
	if(v == null){return v;}
	
	//System.out.println("Nodo actual " + v.elemento + "  elemento " + elemento);
	//Si se encontro el nodo a eliminar
	if(comparador.compare(v.elemento, elemento) == 0)
	    {
		//System.out.println("Eliminando");
		
		//Si hay un solo hijo (el derecho)
		if(v.izquierdo == null)
		    {
			return v.derecho;
		    }
		//Si no hay hijos
		else if(v.derecho == null)
		    {
			return v.izquierdo;
		    }
		//hallar el minimo del subarbol derecho y reemplazarlo en el nodo cuyo elemento se elimino
		v.elemento = minimo(v.derecho);
		//System.out.println("Eliminando " + v.elemento);

		//eliminar el nodo que tenia el minimo en el subarbol derecho
		v.derecho = delete(v.derecho, v.elemento, comparador);
		if(v.hayDerecho())
		    {
			v.derecho.padre = v;
		    }
		
		elementos--;
	    }
	//Buscar en el arbol izquierdo
	else if(comparador.compare(v.elemento, elemento) > 0)
	    {
		//System.out.println(comparador.compare(v.elemento, elemento));
		v.izquierdo = delete(v.izquierdo, elemento, comparador);
		if(v.hayIzquierdo())
		    {
			v.izquierdo.padre = v;
		    }
	    }
	//Buscar en el arbol derecho
	else
	    {
		v.derecho = delete(v.derecho, elemento, comparador);
		if(v.hayDerecho())
		    {
			v.derecho.padre = v;
		    }
	    }
	return v;
    }

    /**Obtiene el minimo de un arbol
     *@return valor minimo
     */
    private T minimo(Vertice v)
    {
	Vertice temp = v;
	while(temp.hayIzquierdo())
	    {
		temp = temp.izquierdo;
	    }
	return temp.elemento;
    }

    /**Imprime el arbol en inorden
     *@return string con los elementos en inorden
     */
    public String toStringInOrden()
    {
	return toStringIO(raiz);
    }

    /**Obtiene el inorden de un subarbol
     *@param v - vertice del subarbol a recorrer
     *@return String con los elementos en inorden
     */
    public String toStringIO(Vertice v)
    {
	String text = "";

	if(v==null)
	    {
		return "";
	    }
	//añadir inorden del subarbol izquierdo
	if(v.hayIzquierdo())
	    {
		text += toStringIO(v.izquierdo);
	    }
	//añadir la raiz del subarbol
	text += v.elemento + " ";
	//inorden del subarbol derecho
	if(v.hayDerecho())
	    {
		text += toStringIO(v.derecho);
	    }
	return text;
    }

    /**Balancea el arbol
     */
    public void balance()
    {	
	Lista<T> elementos = new Lista<T>();
	elementosInOrden(elementos, raiz);

	buildSorted(elementos);
	//System.out.println(elementos);
    }

    /**Crea una lista con los elementos de un subarbol en orden inorden
     *@param lista que contiene los elementos
     *@param v - vertice del subarbol
     *
     */
    private void elementosInOrden(Lista<T> lista, Vertice v)
    {
	//SI es verice nulo
	if(v == null)
	    {return;}
	//Agrega elementos del subarbol izquierdo
	if(v.hayIzquierdo())
	    {
		elementosInOrden(lista, v.izquierdo);
	    }
	//elemento del vertice actual
	lista.add(v.elemento);
	//Elementos del subarbol derecho
	if(v.hayDerecho())
	    {
		elementosInOrden(lista, v.derecho);
	    }
    }
    
    @Override
    public Iterator<T> iterator() {
        return null;
    }

    public void buildSorted2(Lista<T> lista)
    {
	this.empty();
	elementos = 0;
	IteradorLista<T> it = lista.iteradorLista();
	int n = lista.size();
	//System.out.println(lista);
	raiz = construirSorted(it, n);
    }

    /**Construye el arbol a partir de una lista ordenada O(nlog(N))
     *@param lista - lista ordenada
     *@param start - indice a partir del cual se buscan los elementos
     *@param end - indice final hasta el cual se buscan los elementos
     *@param p - vertice padre
     *@return vertice que contiene un subarbol BST.
     */
    private Vertice construirVertices(Lista<T> lista, int start, int end, Vertice p)
    {
	if(start > end)
	    {
		return null;
	    }
        //n = n/2;
	int medio = (start + end)/2;
	
	Vertice temp = new Vertice(lista.getElementAt(medio));
	elementos++;
	
	temp.padre = p;

	
	temp.izquierdo = construirVertices(lista, start, medio - 1, temp);
	
        
	temp.derecho = construirVertices(lista, medio+1, end, temp);
	
	    
	return temp;
	/**Tiempo de ejecución: O(nlog(n))
	 *
	 *La relación de recurrencia es T(n) = n + 2T(n/2) + c donde:
	 *n es el tiempo que toma acceder a cualquier elemento de la lista
	 *2T(n/2) se refiere a la recursión de la función, pues divide n sobre 2
	 *hasta llegar a la condicion final
	 *c son las demas operaciones.
	 *
	 *n + 2((n+2T(n/2))/2) + c = n + n + 2T(n/2)+c = 2n + 2T(n/2) + c ...
	 * = nlogn + c
	 *
	 *Con ello se obtiene un tiempo de ejecucion de O(nlog(n)). Puesto
	 */
    }
}
