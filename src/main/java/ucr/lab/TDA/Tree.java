package ucr.lab.TDA;

public interface Tree {
    //devuelve el número de elementos en el árbol
    int size() throws TreeException;
    //private int size(BTreeNode nodo)

    //anula el árbol
    void clear();

    //true si el árbol está vacío
    boolean isEmpty();

    //true si el elemento existe en el árbol
    boolean contains(Object element) throws TreeException;
    //private boolean binarySearch(BTreeNode node, Object element)

    //inserta un elemento en el árbol
    void add(Object element) throws TreeException;
    //private BtreeNode add(BtreeNode node, Object element)

    //suprime un elemento del árbol
    //Caso 1. El nodo a suprimir no tiene hijos
    //Caso 2. El nodo a suprimir solo tiene un hijo
    //Caso 3. El nodo a suprimir tiene dos hijos
    void remove(Object element) throws TreeException;
    //private BTreeNode remove(BTreeNode node, Object element)

    //devuelve la altura de un nodo (el número de ancestros)
    int height(Object element) throws TreeException;
    //private int height(BTreeNode node, Object element)

    //devuelve la altura del árbol (altura máxima de la raíz a
    //cualquier hoja del árbol)
    int height() throws TreeException;
    //private int height(BTreeNode node)

    //devuelve el valor mínimo contenido en el árbol
    Object min() throws TreeException;
    //private Object min(BTreeNode node)

    //devuelve el valor máximo contenido en el árbol
    Object max() throws TreeException;
    //private Object max(BTreeNode node)

    //recorre el árbol de la forma: nodo-hijo izq-hijo der,
    //para mostrar todos los elementos existentes
    String preOrder() throws TreeException;
    //private String preOrder(BTreeNode node)

    //recorre el árbol de la forma: hijo izq-nodo-hijo der,
    //para mostrar todos los elementos existentes
    String inOrder() throws TreeException;
    //private String inOrder(BTreeNode node)

    //recorre el árbol de la forma: hijo izq-hijo der-nodo,
    //para mostrar todos los elementos existentes
    String postOrder() throws TreeException;
    //private String postOrder(BTreeNode node)
    BTreeNode getRoot();
    boolean isBalanced();
}
