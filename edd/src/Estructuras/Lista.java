package edd.src.Estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Comparator;
// iterador
//next

public class Lista<T> implements Collection<T>{

    // Clase Nodo
    private class Nodo {
        public T elemento;
        public Nodo anterior;
        public Nodo siguiente;

        public Nodo(T elemento){
            this.elemento = elemento;
        }
    }

    // Iterador
    private class Iterador implements IteradorLista<T> {
        public Nodo anterior;
        public Nodo siguiente; 

        public Iterador(){
            siguiente = cabeza;
        }

        @Override public boolean hasNext(){
            return siguiente != null;
        }

        @Override public T next(){
            if(!hasNext())
                throw new NoSuchElementException();
            T regresar = siguiente.elemento;
            
            this.anterior = this.siguiente ;
            this.siguiente=siguiente.siguiente;
            return regresar;

        }
        
        @Override
        public boolean hasPrevious() {
            return anterior != null;
        }
        
        @Override
        public T previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();
            T regresar = anterior.elemento;

            this.siguiente = this.anterior;
            this.anterior = anterior.anterior;
            return regresar;

        }

        @Override
        public void start(){
            this.anterior = null;
            this.siguiente = cabeza;
        }
        
        @Override
        public void end() {
            this.anterior = ultimo;
            this.siguiente = null;
        }
        
    }

    private Nodo cabeza;
    private Nodo ultimo;
    private int longi;
    
    /**
     * Agrega un elemento a la lista.
     * 
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *                                  <code>null</code>.
     */
    @Override
    public void add(T elemento){
        if(elemento == null){
            throw new IllegalArgumentException("El elemento es null");
        }
        agregaFinal(elemento);
    }
    
    
    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("El elemento es null");
        }
        Nodo nuevo = new Nodo(elemento);
        if (cabeza == null) {
            this.cabeza = this.ultimo = nuevo;
        } else {
            this.cabeza.anterior = nuevo;
            nuevo.siguiente = this.cabeza;
            this.cabeza = nuevo;
        }
        longi++;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("El elemento es null");
        }
        Nodo nuevo = new Nodo(elemento);
        if(cabeza == null){
            this.cabeza = this.ultimo = nuevo;
        }
        else{
            this.ultimo.siguiente = nuevo;
            nuevo.anterior = this.ultimo;
            this.ultimo = nuevo;
        }
        longi++;
    }

    private Nodo buscaElemento(T elemento){
        Nodo n = cabeza;
        while(n !=null){
            if (elemento.equals(n.elemento)) {
                return n;
            }
            n=n.siguiente;
        }
        return null;
    }

    /**
     * Elimina un elemento de la lista.
     * 
     * @param elemento el elemento a eliminar.
     */ 
    public boolean delete(T elemento){
        if(elemento == null)
            return false;
        Nodo n = buscaElemento(elemento);
        if(n==null){
            return false;
        }
        if(longi == 1){
            empty();
            return true;
        }
        if (n == cabeza) {
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
            longi --;
            return true;
        }
        if (n == ultimo) {
            ultimo = ultimo.anterior;
            ultimo.siguiente = null;
            longi --;
            return true;
        }
        n.siguiente.anterior = n.anterior;
        n.anterior.siguiente = n.siguiente;
        longi --;
        return true;
    }    



    /**
     * Regresa un elemento de la lista. (Ultimo)
     * y lo elimina.
     * 
     * @return El elemento a sacar.
     */
    public T pop(){
        T valor = ultimo.elemento;
	//Si solo hay un elemento en la lista
	//Agregado por que pop no funcionaba cuando habia un solo elemento
	if(longi == 1)
	{
	    empty();
	    return valor;
	}
        ultimo = ultimo.anterior;
        ultimo.siguiente = null;
        longi --;
        return valor;
    }

    /**
     * Regresa el número de elementos en la lista.
     * 
     * @return el número de elementos en la lista.
     */
    public int size(){
        return longi;
    }

    /**
     * Nos dice si un elemento está contenido en la lista.
     * 
     * @param elemento el elemento que queremos verificar si está contenido en
     *                 la lista.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean contains(T elemento){
        if(buscaElemento(elemento) == null){
            return false;
        }
        return true;
    }

    /**
     * Vacía la lista.
     * 
     */
    public void empty(){
        cabeza =ultimo= null;
        longi = 0;
    }

    /**
     * Nos dice si la lista es vacía.
     * 
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    public boolean isEmpty(){
        return longi == 0;
    }

    

    /**
     * Regresa una copia de la lista.
     * 
     * @return una copia de la lista.
     */
    public Lista<T> clone() {
        Lista<T> nueva = new Lista<T>();
        Nodo nodo = cabeza;
        while (nodo != null) {
            nueva.add(nodo.elemento);
            nodo = nodo.siguiente;
        }
        return nueva;
    }

    /**
     * Nos dice si la coleccion es igual a otra coleccion recibida.
     * 
     * @param coleccion la coleccion con el que hay que comparar.
     * @return <tt>true</tt> si la coleccion es igual a la coleccion recibido
     *         <tt>false</tt> en otro caso.
     */
    public boolean equals(Collection<T> coleccion){
        // lo vemos en clase
        if(coleccion instanceof Lista) {
            return true;
        }
        return false;
    }


    
    /**
     * Metodo que invierte el orden de la lista .
     * 
     * Este metodo funciona creando un iterador al inicio y uno al inicio de la lista
     * y va intercambiando los elementos entre los nodos correspondientes.
     * Puesto que itera por cada nodo hasta llegar a la mitad de la lista (n/2), tiene un
     * tiempo de ejecucion O(n).
     * Puesto que solo crea en total 4 objetos (dos iteradores, un contador y un objeto temporal
     * para guardar datos temporales), entonces tiene un espacio de O(1).
     * 
     */
    public void reverse() {
        // Tu codigo aqui
	if(isEmpty()){return;}
	
	Iterador it = new Iterador();
	Iterador aux = new Iterador();
	T temp = cabeza.elemento;
	int contador = longi/2;
	    
	it.start();
	aux.end();
	aux.previous();

	//Basicamente hay un iterador al inicio y uno al final.
	//Van intercambiando elementos e iterando hacia el centro
	
	while(contador>0)
	{
	    temp = it.siguiente.elemento;
	    it.siguiente.elemento = aux.siguiente.elemento;
	    aux.siguiente.elemento = temp;

	    it.next();
	    aux.previous();
	    contador--;
	}
	
    }

    /**
     * Regresa una representación en cadena de la coleccion.
     * 
     * @return una representación en cadena de la coleccion.
     * a -> b -> c -> d
     */
    public String toString(){
        // Tu codigo aqui
	
	String s =  "";
        Iterador it = new Iterador();
	it.start();
	while(it.hasNext())
	{
	    s += it.next();
	    //System.out.println(s);
	    if(it.hasNext())
	    {
		s += " ";
	    }
	}
	//System.out.println("String");
	//System.out.println(s);
	//System.out.println("Retorno");
        return s;
    }

    /**
     * Junta dos listas siempre y cuando sean del mismo tipo.
     * 
     */
    public void append(Lista<T> lista) {
        // Tu codigo aqui
	if(lista.isEmpty()){return;}
	
        Nodo aux = ultimo;
	aux.siguiente = lista.cabeza;
	aux.anterior = ultimo.anterior;
	
	lista.cabeza.anterior = aux;
	
	aux.anterior.siguiente = aux;
	
	ultimo = lista.ultimo;
	
        longi += lista.size();
    }

    /**
     * Regresa un entero con la posicion del elemento.
     * Solo nos importara la primera aparición del elemento
     * Empieza a contar desde 0.
     * 
     * @param elemento elemento del cual queremos conocer la posición.
     * @return entero con la posicion del elemento
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public int indexOf(T elemento) {
        // Tu codigo aqui
	if(elemento == null)
	{
	    System.out.println("Elemento nulo");
	    return -1;
	}
	IteradorLista it = iteradorLista();
	int cont = 0;

	while(it.next() != elemento && it.hasNext())
	{
	    cont++;
	}
	//si se llega al final y no se encontro
	if(cont >= longi-1)
	{
            throw new IllegalArgumentException("No se encontro el elemento " + elemento);
	}
	else
        {
	    return cont;
	}

    }

    /** Obtiene al elemento en el indice indicado
     *@param i - Indice del objeto que se desea obtener
     *@return objeto en el indice indicado
     */
    public T getElementAt(int i)
    {
	
	if(i < 0 ||i >= longi)
	{
	    //System.out.println("No se encontro el indice " + i);
	    //System.out.println(longi);
	    throw new IllegalArgumentException("Indice fuera de rango " + i);
	    //return null;
	}
	else
	{
	    //System.out.println("Buscando " + i);
	    Iterador it = new Iterador();
	    it.start();
	    T temp = it.next();
	    i--;
	    while(i >= 0)
	    {
		//System.out.println(temp);
		temp = it.next();
		i--;
	    }
	    return temp;
	}
    }

    /**Elimina un elemento en un indice dado
     *@param i - Indice cuyo elemento será eliminado
     */
    public void deleteElementAt(int i)
    {
	if(i < 0 ||i >= longi)
	{
	    //System.out.println("No se encontro el indice " + i);
	    //System.out.println(longi);
	    throw new IllegalArgumentException("Indice fuera de rango " + i);
	    //return null;
	}
	 //Si es el unico elemento, vacia la lista
	else if(longi == 1)
	{
	    empty();
	    return;
	}
	else if (i==0)
	{
	    cabeza = cabeza.siguiente;
	    cabeza.anterior = null;
	    longi--;
	}
	else if(i == longi-1)
	{
	    pop();
	}
	else
	{
	    Iterador it = new Iterador();
	    while(i >= 0)
	    {
		it.next();
		i--;
	    }
	    it.siguiente.anterior = it.siguiente.anterior.anterior;
	    it.siguiente.anterior.siguiente = it.siguiente;
	    longi--;
	}
    }
    
    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor que cero, el elemento se agrega al inicio de la
     * lista. Si el índice es mayor o igual que el número de elementos en la
     * lista, el elemento se agrega al fina de la misma. En otro caso, después
     * de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * 
     * @param i        el índice dónde insertar el elemento. Si es menor que 0 el
     *                 elemento se agrega al inicio, y si es mayor o igual que el
     *                 número
     *                 de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *                                  <code>null</code>.
     */
    public void insert(int i, T elemento) {
        // Tu codigo aqui
	if(i <= 0)
	{
	    agregaInicio(elemento);
	    longi++;
	    return;
	}
	else if(i >= longi)
	{
	    agregaFinal(elemento);
	    longi++;
	    return;
	}
	else
	{
	    Iterador it = new Iterador();
	    it.start();
	    while(i > 0)
	    {
		i--;
		it.next();
	    }
	    Nodo nuevo = new Nodo(elemento);
	    nuevo.siguiente = it.siguiente;
	    nuevo.anterior = it.anterior;

	    it.anterior.siguiente = nuevo;
	    it.anterior = nuevo;
	    longi++;
	}
        return ;
    }

    /** Mezcla dos listas alternando elementos de ambas. Acutalmente solo funciona
     * correctamente cuando la lista a insertar es menor a la lista actual.
     * @param lista - lista doblemente ligada a insertar
     * 
     * La funcion mezcla dos listas de forma alternada mediante la modificacion de
     * las referencias de los nodos ("apuntadores").
     * El bloque principal while tiene dos iteradores, uno por cada lista, y se ejecuta hasta
     * llegar al ultimo elemento de cualquiera de las dos listas, modificando los apuntadores necesarios.
     * Posteriormente un ultimo bloque "cierra" la lista corta, modificando los apuntadores de su cola
     * para que quede completamente integrado en la otra lista. Este bloque es de tiempo constante.
     *
     * Puesto que el while se ejecuta hasta llegar al ultimo elemento de cualquiera de las dos listas, mas
     * un tiempo constante para cerrar la cola de la lista corta, tiene un tiempo de ejecución de O(min(n,m)). 
     * 
     * La cunión crea unicamente dos iteradores, uno por cada lista, por lo que el espacio
     * será de O(1).
     */
    private void mezclaAlternada(Lista<T> lista){

	if(lista.isEmpty())
	{
	    return;
	}
	
	//Iteradores. it1 para el elemento local, it2 para la lista de los parametros
	Iterador ita = new Iterador();
	ita.start();
	Iterador itb = (Iterador)lista.iteradorLista();
	itb.start();
	//No asigna bien los nodos anteriores, por lo que si se usa la funcion reverse() sobre la lista,
	// cuando b es mayor a A, el orden de algunos objetos esta mal
	while(ita.siguiente.siguiente != null && itb.siguiente.siguiente != null)
	{
	    //System.out.println("1");
	    ita.siguiente.anterior = itb.siguiente.anterior;
	    ita.next();
	    
	    //System.out.println("2");
	    ita.siguiente.anterior.siguiente = itb.siguiente;
	    
	    //System.out.println("3");
	    itb.siguiente.anterior = ita.siguiente.anterior;
	    
	    //System.out.println("4");
	    ita.siguiente.anterior = itb.siguiente;
	    
	    //System.out.println("5");
	    itb.next();
	    itb.siguiente.anterior.siguiente = ita.siguiente;
	    longi++;
	    
	}
	//Actualmente solo fucniona bien cuando la lista A es mayor o igual que la B
	// por lo que si A>=B, reverse funciona del todo correcto.
	if(itb.siguiente.siguiente == null)
	{
	    itb.siguiente.anterior = ita.siguiente;
	    itb.siguiente.siguiente = ita.siguiente.siguiente;
	    ita.next();
	    ita.siguiente.anterior.siguiente = itb.siguiente;
	    ita.siguiente.anterior = itb.siguiente;
	    longi++;
	    return;
	}
    }
    
    // Merge Sort
    public Lista<T> mergeSort(Comparator<T> comparador){
        if(longi == 1 || longi == 0){
            return clone();
        }
        Lista<T> izq = new Lista<T>();
        Lista<T> der = new Lista<T>();

        int mitad = longi/2;
        Nodo aux = cabeza;
        while(aux != null && mitad --!= 0 ){ 
            izq.add(aux.elemento);
            aux = aux.siguiente;
        }
        while(aux != null){
            der.add(aux.elemento);
            aux = aux.siguiente;
        }
        //System.out.println("izq: " + izq.toString());
        //System.out.println("der: " + der.toString());
        izq = izq.mergeSort(comparador);
        der = der.mergeSort(comparador);
        return merge(izq, der, comparador);
    }

    //Merge
    public Lista<T> merge(Lista<T> izq, Lista<T> der, Comparator<T> comparador){
        Lista<T> resultado = new Lista<T>();
        Nodo auxIzq = izq.cabeza;
        Nodo auxDer = der.cabeza;
        while (auxIzq != null && auxDer != null ) {
            if(comparador.compare(auxIzq.elemento, auxDer.elemento) < 0){
                resultado.add(auxIzq.elemento);
                auxIzq = auxIzq.siguiente;
            }
            else{
                resultado.add(auxDer.elemento);
                auxDer = auxDer.siguiente;
            }
        }
        if (auxIzq == null) {
            while (auxDer != null) {
                resultado.add(auxDer.elemento);
                auxDer = auxDer.siguiente;
            }
        }else{
            while (auxIzq != null) {
                resultado.add(auxIzq.elemento);
                auxIzq = auxIzq.siguiente;
            }
        }
	//System.out.println("Resultado: " + resultado);
       return resultado;
    } 

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**Cambia el valor de un elemento en una indice dado
     *@param i - Indice del valor a cambiar
     *@param n - valor nuevo
     */
    public void setElementAt(int i, T n)
    {
	if(i < 0 ||i >= longi)
	{
	    //System.out.println("No se encontro el indice " + i);
	    //System.out.println(longi);
	    throw new IllegalArgumentException("Indice fuera de rango " + i);
	    //return null;
	}
	else
	{
	    //System.out.println("Buscando " + i);
	    Iterador it = new Iterador();
	    it.start();
	    while(i > 0)
	    {
	        it.next();
		i--;
	    }
	    it.siguiente.elemento = n;
	}
    }

    /**Intercambia el valor de dos elementos en dos indices dados
     *@param n - indice del primer elemento
     *@param m - indice del segundo elemento
     */
    public void swap(int n, int m)
    {
	if(n < 0 ||n >= longi || m < 0 || m >= longi)
	{
	    //System.out.println("No se encontro el indice " + i);
	    //System.out.println(longi);
	    throw new IllegalArgumentException("Indice fuera de rango " + m);
	    //return null;
	}
	else
        {
	    T temp = getElementAt(m);
	    setElementAt(m, getElementAt(n));
	    setElementAt(n, temp);
	}
    }
}
