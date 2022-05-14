package edd.src.Estructuras;

public class Cola<T> extends PushPop<T>{

    /** Agrega un elemento al final de la cola
     *@param Elemento - Elemento a agregar
     */
    public void push(T elemento)
    {
	if(elemento == null)
	{
	    throw new IllegalArgumentException("Algo salio mal con el elemento");
	}
	Nodo aux = new Nodo(elemento);
	if(this.isEmpty())
	{
	    this.cabeza = aux;
	    this.ultimo = aux;
	    longi++;
	    return;
	}
	//elementos nuevos se agregan en la cola
        ultimo.siguiente = aux;
	ultimo = aux;
	longi++;
    }

    //To String
    public String toString()
    {
	if(this.isEmpty())
	{
	    return "";
	}
	String texto = this.cabeza.elemento.toString();
	Nodo it = this.cabeza;
	while(it.siguiente != null)
	{
	    texto += " " + it.siguiente.elemento.toString();
	    it = it.siguiente;
	}
	return texto;
    }

    /** Regresa una copia de la estructura
     *@return clon de la cola
     */
    public Cola<T> clone()
    {
	Cola<T> temp = new Cola<T>();
	if(this.isEmpty())
	{
	    return temp;
	}
	temp.push(this.cabeza.elemento);
	Nodo it = this.cabeza;
	while(it.siguiente != null)
	{
	    temp.push(it.siguiente.elemento);
	    it = it.siguiente;
	}
	return temp;
    }
    
}

