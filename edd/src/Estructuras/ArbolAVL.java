package edd.src.Estructuras;

public class ArbolAVL<T extends Comparable<T>> extends ArbolBinarioBusqueda<T>
{
    /**Clase vertice para arboles AVL
     */
    public class VerticeAVL extends Vertice
    {
	/**Contructor por parametro*/
	public VerticeAVL(T elemento)
	{
	    super(elemento);
	}
	
	/**Metodo toString
	 *@return string con el elemento
	 */
	@Override
	public String toString()
	{
	    if(this==null)
	    {
		return "";
	    }
	    return (elemento.toString() + " H: " +  String.valueOf(altura()));
	}

	/**Compara el vertice a otro objeto
	 *@param o - Objeto a comparar
	 *@return boolean indicando si son equivalentes
	 */
	public boolean equals(Object o)
	{
	    if(o==null || getClass() !=o.getClass())
	    {
		return false;
	    }
	    @SuppressWarnings("unchecked")
	    VerticeAVL vertice = (VerticeAVL) o;

	    //Compara el elemento del vertice
	    if(!this.elemento.equals(vertice.elemento))
	    {
		return false;
	    }

	    //Si ambos tienen hijo o si ambos no tienen hijo derecho
	    if(this.hayDerecho() == vertice.hayDerecho())
	    {
		//Si no es nulo el hijo derecho, comparar si son elementos de distinto valor
		if(this.derecho != null && this.derecho.elemento != vertice.derecho.elemento)
		{
		    return false;
		}
	    }
	    //Si ambos tienen hijo o si ambos no tienen hijo derecho
	    if(this.hayIzquierdo() == vertice.hayIzquierdo())
	    {
		//Si no es nulo el hijo derecho, comparar si son elementos de distinto valor
		if(this.izquierdo != null && this.izquierdo.elemento != vertice.izquierdo.elemento)
		{
		    return false;
		}
	    }

	    return true; 
	}

	/** Regresa la altura del vertice
	 *@return altura del vertice
	 */
	public int altura()
	{
	    int alturaI = 0;
	    int alturaD = 0;
	    
	    if(hayIzquierdo())
	    {
		alturaI = izquierdo.altura();
	    }
	    if(hayDerecho())
	    {
		alturaD = derecho.altura();
	    }
	    int max = (alturaI > alturaD)? alturaI : alturaD;
	    return max;
	}
	
	
    }

    public ArbolAVL()
    {
	super();
    }

    /**Construye el arbol AVL recibiendo una colección, ordenandola y construyendo el BST correspondiente
     *@param coleccion - Coleccion de elementos a ordenad
     */
    public ArbolAVL(Collection<T> coleccion)
    {
	//super(coleccion);
	//super();
	for(T elemento : coleccion)
	    {
		this.add(elemento);
	    }	
	//System.out.println("constructor");
	
    }

    /**Construye el arbol AVL a partir de una coleccion
     *@param coleccion - coleccion de elementos
     */
    public void construirAVL(Collection<T> coleccion)
    {
	elementos =0;
	raiz = null;
	for(T elemento : coleccion)
	    {
		this.add(elemento);
	    }
	//raiz = balancearAVL(raiz);
    }

    /**Inserta un elemento de forma ordenada
     *@param elemento - elemento a agrgar
     */
    @Override
    public void add(T elemento)
    {
	if(elemento == null)
	{
	    return;
	}

	raiz = insertar(raiz, elemento);
	
	raiz = balancear(raiz);
    }

    /**Elimina un nodo con un elemento dado
     *@param elemento - elemento a eliminar
     */
    public boolean delete(T elemento)
    {
	if(elemento == null)
	{
	    return false;
	}
	int stemp = elementos;
	raiz = eliminar(raiz, elemento);
	raiz = balancear(raiz);
	if(stemp == elementos)
	{
	    return false;
	}
	return true;
    }

    
    
    /**Inserta un elemento en el subarbol y balancea mediante rotaciones
     *@param v - vertice raiz del subarbol
     *@param elemento - elemento a insertar
     *@return subarbol balanceado
     */
    private Vertice insertar(Vertice v, T elemento)
    {
	if(v == null)
	{
	    elementos++;
	    return (new Vertice(elemento));
	}
	//Si el elemento es mayor al nodo actual
	if(elemento.compareTo(v.elemento) < 0)
	{
	    v.izquierdo = insertar(v.izquierdo, elemento);
	    if(v.hayIzquierdo())
	    {
		v.izquierdo.padre = v;
	    }
	}
	//si es menor
	else if(elemento.compareTo(v.elemento)>0)
	{
	    v.derecho = insertar(v.derecho, elemento);
	    if(v.hayDerecho())
	    {
		v.derecho.padre = v;
	    } 
	}
	//Si es igual
	else
	{
	    return v;
	}

        v = balancearAVL(v);
	
	return v;
	
    }

    /**Elimina un nodo con un elemento dado
     *@param v - vertice raiz del subarbol
     *@param elemento - elemento a eliminar
     *@return subarbol con el elemento eliminado y balanceado
     */
    private Vertice eliminar(Vertice v, T elemento)
    {
	if(v == null)
	{
	    return v;
	}

	//Si el elemento es menor al nodo actual
	if(elemento.compareTo(v.elemento) < 0)
	{
	    v.izquierdo = eliminar(v.izquierdo, elemento);
	    if(v.hayIzquierdo())
	    {
		v.izquierdo.padre = v;
	    }
	}
	//Si es mayor al nodo actual
	else if(elemento.compareTo(v.elemento)>0)
	{
	    v.derecho = eliminar(v.derecho, elemento);
	    if(v.hayDerecho())
	    {
		v.derecho.padre = v;
	    }
	}
	//Si es el nodo a eliminar
	else
	{
	    //Si hay a lo mucho un hijo
	    if(!v.hayIzquierdo() || !v.hayDerecho())
	    {
		Vertice temp = new Vertice(null);
		//Si no hay izquierdo
		if(temp == v.izquierdo)
		{
		    temp = v.derecho;
		}
		//Si no hay derecho
		else
		{
		    temp = v.izquierdo;
		}

		//si no hay hijos
		if(temp==null)
		{
		    temp = v;
		    temp.padre = v.padre;
		    v = null;
		}
		else
		{
		    v = temp;
		}
	    }
	    else
	    {
		//Obtiene el vertice con el elemento mas pequeño del subarbol derecho
		Vertice temp = verticeMinimo(v.derecho);
		
		//Cambio de valores y reasignacion de padre
		v.elemento = temp.elemento;
		v.derecho = eliminar(v.derecho, temp.elemento);
		if(v.hayDerecho())
		{
		    v.derecho.padre = v;
		}
	    }
	}
	elementos--;
	//Balancear
	v = balancearAVL(v);
	return v;
    }

    /**Obtiene el nodo con el elemento más pequeño de un subarbol
     *@param v - vertice raiz del subarbol
     *@return vertice con el nodo mas pequeño
     */
    private Vertice verticeMinimo(Vertice v)
    {
	Vertice temp = v;
	while(temp.hayIzquierdo())
	{
	    temp = temp.izquierdo;
	}
	return temp;
    }

    /**Balancea el arbol
     *@param v - raiz del arbol
     *@return raiz balanceada
     */
    private Vertice balancear(Vertice v)
    {
	if(v==null)
	{
	    //System.out.println("Nulo");
	    return v;
	}
        
	v.derecho = balancearAVL(v.derecho);
	v.izquierdo = balancearAVL(v.izquierdo);

	v.derecho = balancear(v.derecho);
	v.izquierdo = balancear(v.izquierdo);
	return v;
    }
    
    /**Balancea un subarbol mediante rotaciones
     *@param v - vertice raiz del subarbol a balancear
     *@return subarbol balanceado.
     */
    public Vertice balancearAVL(Vertice v)
    {
	//System.out.println("Balanceando " + v);
	
	    
	//System.out.println("AVL BAL");
	if(v==null)
	{
	    //System.out.println("Nulo");
	    return v;
	}
	int peso = balanceNodos(v);
        T valor = v.elemento;

	//System.out.println(peso);
	
	//Si esta desbalanceado:
	//izquierda izquierda
	if(peso > 1  && valor.compareTo(v.izquierdo.elemento)<0)
	{
	    return rotarDerecha(v);
	}
	//derecha derecha
	if(peso < -1 && valor.compareTo(v.derecho.elemento)>0)
	{
	    return rotarIzquierda(v);
	}

	//izquierda derecha
	if(peso > 1 &&  valor.compareTo(v.izquierdo.elemento)>0)
	{
	    v.izquierdo = rotarIzquierda(v.izquierdo);
	    return rotarDerecha(v);
	}
	//derecha izquierda
	if(peso < -1 &&  valor.compareTo(v.derecho.elemento)<0)
	{
	    v.derecho = rotarDerecha(v.derecho);
	    return rotarIzquierda(v);
	}
	return v;
    }

    /**Obtiene la diferencia de la altura maxima del subarbol izquierdo y derecho de v.
     *Regresa un numero menor a -1 si es pesado a la derecha, y mayor a 1 si es pesado a la izquierda.
     *@param v - raiz del subarbol cuya diferencia de balance se quiere saber
     *@return diferencia de altura de los subarboles hijos.
     */
    private int balanceNodos(Vertice v)
    {
	if(v==null)
	{
	    return 0;
	}
	int d = 0;
	int i = 0;
	if(v.hayDerecho())
	{
	    d = v.derecho.altura();
	}
	if(v.hayIzquierdo())
	{
	    i = v.izquierdo.altura();
	}
	
        int diff = i - d;
	//System.out.println("Elemento " + v.elemento + " I: " +i + " D: " + d + " diff; " +diff);

	return diff;
    }

    /**Rota un subarbol a la derecha
     *@param v - raiz del subarbol a rotar
     */
    private Vertice rotarDerecha(Vertice v)
    {
	if(v == null || !v.hayIzquierdo())
	{
	    return v;
	}

	/*
	 *    padre                padre
	 *      |                    |
	 *      a(v)                 b
	 *     /  \                 /  \
	 *    b   gamma   ->       alfa a(v)
	 *   /  \                      /  \
	 *  alfa beta                beta  gamma
	 *
	 *
	 */

	//Intercambio de beta
        Vertice tempi = v.izquierdo;
	
	v.izquierdo = tempi.derecho;
	tempi.derecho = v;

	//actualiza los nodos padres
	tempi.padre = v.padre;
	v.padre = tempi;
	if(v.izquierdo != null)
	{
	    v.izquierdo.padre = v;
	}
	if(tempi.padre != null)
	{
	    if(tempi.padre.izquierdo == v.elemento)
	    {
		tempi.padre.izquierdo = tempi;
	    }
	    else
	    {
		tempi.padre.derecho = tempi;
	    }
	}
	return tempi;
    }
    /**Rota un subarbol a la izquierda
     *@param v - raiz del subarbol a rotar
     */
    private Vertice rotarIzquierda(Vertice v)
    {
	if(v == null || !v.hayDerecho())
	{
	    return v;
	}

	/*
	 *    padre                padre
	 *      |                    |
	 *      v                    b
	 *     /  \                 /  \
	 *   alfa  b   ->          v    gamma
	 *        /  \            /  \    
	 *      beta gamma      alfa  beta
	 *
	 *
	 */

	//intercambia beta
	Vertice tempd = v.derecho;
	v.derecho = tempd.izquierdo;
	tempd.izquierdo = v;

	//actualiza los padres
	tempd.padre = v.padre;
	v.padre = tempd;
	if(v.derecho != null)
	{
	    v.derecho.padre = v;
	}
	if(tempd.padre != null)
	{
	    if(tempd.padre.izquierdo == v.elemento)
	    {
		tempd.padre.izquierdo = tempd;
	    }
	    else
	    {
		tempd.padre.derecho = tempd;
	    }
	}
	return tempd;

    }

    /**String del arbol en inorden
     *@return string con los elementos en inorden y su altura
     */
    public String toIO()
    {
	return IO(raiz);
    }

    /**String del subarbol en inorden
     *@param v - raiz del subarbol
     *@return string con los elementos en inorden y con su altura.
     */
    private String IO(Vertice v)
    {
	String text = "";
	if(v == null)
	    {
		return "";
	    }
        if(v.hayIzquierdo())
	    {
		text += IO(v.izquierdo);
	    }
	//añadir la raiz del subarbol
	text += "(" + v + " H: " + v.altura() + ") ";
	//inorden del subarbol derecho
	if(v.hayDerecho())
	    {
		text += IO(v.derecho);
	    }
	return text;
    }
}
