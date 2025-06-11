package ucr.lab.TDA.tree;


import java.util.ArrayList;
import java.util.List;
import static ucr.lab.utility.Util.compare;

public class AVLTree implements Tree {
    public BTreeNode root; //se refiere a la raiz del arbol

    @Override
    public int size() throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return size(root);
    }

    @Override
    public BTreeNode getRoot() {
            return root;
    }

    @Override
    public boolean isBalanced() {
        return isBalanced( root);
    }

    private boolean isBalanced(BTreeNode node) {
        if (node == null) return true;
        int leftHeight = height(node.left);
        int rightHeight = height(node.right);
        if (Math.abs(leftHeight - rightHeight) > 1) return false;
        return isBalanced(node.left) && isBalanced(node.right);
    }

    private int size(BTreeNode node) {
        if (node == null) return 0;
        else return 1 + size(node.left) + size(node.right);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(Object element) throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return binarySearch(root, element);
    }

    private boolean binarySearch(BTreeNode node, Object element) throws TreeException {
        if (node == null) return false;
        else if (compare(node.data, element) == 0) return true;
        else if (compare(element, node.data) < 0)
            return binarySearch(node.left, element);
        else return binarySearch(node.right, element);
    }

    @Override
    public void add(Object element) throws TreeException {
        this.root = add(root, element, "root");
    }

    private BTreeNode add(BTreeNode node, Object element, String path) throws TreeException {
        if (node == null)
            node = new BTreeNode(element, path);
        else if (compare(element, node.data) < 0)
            node.left = add(node.left, element, path + "/left");
        else if (compare(element, node.data) > 0)
            node.right = add(node.right, element, path + "/right");

        //una vez agregado el nuevo nodo, debemos determinar si se requiere rebalanceo para siga siendo BST-AVL
        node = reBalance(node, element);
        return node;
    }

    private BTreeNode reBalance(BTreeNode node, Object element) throws TreeException {
        //debemos obtener el factor de balanceo, si es 0, -1, 1 está balanceado, si es <=-2, >=2 hay que rebalancear
        int balance = getBalanceFactor(node);

        // Caso-1. Left Left Case
        if (balance > 1 && compare(element, node.left.data) < 0) {
            node.path += "/Simple-Right-Rotate";
            return rightRotate(node);
        }
        // Caso-2. Right Right Case
        if (balance < -1 && compare(element, node.right.data) > 0) {
            node.path += "/Simple-Left-Rotate";
            return leftRotate(node);
        }
        // Caso-3. Left Right Case
        if (balance > 1 && compare(element, node.left.data) > 0) {
            node.path += "/Double-Left-Right-Rotate";
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        // Caso-4. Right Left Case
        if (balance < -1 && compare(element, node.right.data) < 0) {
            node.path += "/Double-Right-Left-Rotate";
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    //retorna el factor de balanceo del árbol a partir del nodo nado
    private int getBalanceFactor(BTreeNode node) {
        if (node == null) {
            return 0;
        } else {
            return height(node.left) - height(node.right);
        }
    }

    public BTreeNode leftRotate(BTreeNode node) {
        BTreeNode node1 = node.right;
        if (node1 != null) { //importante para evitar NullPointerException
            BTreeNode node2 = node1.left;
            //se realiza la rotacion (perform rotation)
            node1.left = node;
            node.right = node2;
        }
        return node1;
    }

    public BTreeNode rightRotate(BTreeNode node) {
        BTreeNode node1 = node.left;
        if (node1 != null) { //importante para evitar NullPointerException
            BTreeNode node2 = node1.right;
            //se realiza la rotacion (perform rotation)
            node1.right = node;
            node.left = node2;
        }
        return node1;
    }

    @Override
    public void remove(Object element) throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        root = remove(root, element);
    }

    private BTreeNode remove(BTreeNode node, Object element) throws TreeException {
        if (node != null) {
            if (compare(element, node.data) < 0)
                node.left = remove(node.left, element);
            else if (compare(element, node.data) > 0)
                node.right = remove(node.right, element);
            else if (compare(node.data, element) == 0) {
                //caso 1. es un nodo si hijos, es una hoja
                if (node.left == null && node.right == null) return null;
                //caso 2-a. el nodo solo tien un hijo, el hijo izq
                else if (node.left != null && node.right == null) {
                    return node.left;
                } //caso 2-b. el nodo solo tien un hijo, el hijo der
                else if (node.left == null && node.right != null) {
                    return node.right;
                }
                //caso 3. el nodo tiene dos hijos
                else {
                    //else if (node.left!=null&&node.right!=null) {
                    /* *
                        * El algoritmo de supresión dice que cuando el nodo a suprimir
                        * tiene 2 hijos, entonces busque una hoja del subarbol derecho
                        * y sustituya la data del nodo a suprimir por la data de esa hoja,
                        * luego elimine esa hojo
                        * */
                    Object value = min(node.right);
                    node.data = value;
                    node.right = remove(node.right, value);
                    //si el arbol no es balanceado AVL,  re-balancear para que quede como un avl
                    reBalance(node,element);
                    switch (getBalanceFactor(root) ){
                        case 0: leftRotate(node);//balanceado pero dejar como AVL
                        case 1: return node; //ya es AVL por la derecha
                        case -1: return node; //ya es AVL por la izquierda
                        case -2: return rightRotate(node); //rotación a la derecha
                        case 2: return leftRotate(node);//rotación a la izquierda
                    }
                }
            }
        }
        return node; //retorna el nodo modificado o no
    }

    @Override
    public int height(Object element) throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return height(root, element, 0);
    }

    //devuelve la altura de un nodo (el número de ancestros)
    private int height(BTreeNode node, Object element, int level) throws TreeException {
        if (node == null) return 0;
        else if (compare(node.data, element) == 0) return level;
        else return Math.max(height(node.left, element, ++level),
                    height(node.right, element, level));
    }

    @Override
    public int height() throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        //return height(root, 0); //opción-1
        return height(root); //opción-2
    }

    //devuelve la altura del árbol (altura máxima de la raíz a
    //cualquier hoja del árbol)
    private int height(BTreeNode node, int level) {
        if (node == null) return level - 1;//se le resta 1 al nivel pq no cuente el nulo
        return Math.max(height(node.left, ++level),
                height(node.right, level));
    }

    //opcion-2
    private int height(BTreeNode node) {
        if (node == null) return -1; //retorna valor negativo para eliminar el nivel del nulo
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    @Override
    public Object min() throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return min(root);
    }

    private Object min(BTreeNode node) {
        if (node.left != null)
            return min(node.left);
        return node.data;
    }

    @Override
    public Object max() throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return max(root);
    }

    private Object max(BTreeNode node) {
        if (node.right != null)
            return max(node.right);
        return node.data;
    }

    @Override
    public String preOrder() throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return preOrder(root);
    }

    //recorre el árbol de la forma: nodo-hijo izq-hijo der
    private String preOrder(BTreeNode node) {
        String result = "";
        if (node != null) {
            result = node.data + " ";
            result += preOrder(node.left);
            result += preOrder(node.right);
        }
        return result;
    }

    //recorre el árbol de la forma: nodo-hijo izq-hijo der
    private String preOrderPath(BTreeNode node) {
        String result = "";
        if (node != null) {
            result = node.data + "(" + node.path + ")" + " ";
            result += preOrderPath(node.left);
            result += preOrderPath(node.right);
        }
        return result;
    }

    @Override
    public String inOrder() throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return inOrder(root);
    }

    //recorre el árbol de la forma: hijo izq-nodo-hijo der
    private String inOrder(BTreeNode node) {
        String result = "";
        if (node != null) {
            result = inOrder(node.left);
            result += node.data + " ";
            result += inOrder(node.right);
        }
        return result;
    }

    //para mostrar todos los elementos existentes
    @Override
    public String postOrder() throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return postOrder(root);
    }

    //recorre el árbol de la forma: hijo izq-hijo der-nodo,
    private String postOrder(BTreeNode node) {
        String result = "";
        if (node != null) {
            result = postOrder(node.left);
            result += postOrder(node.right);
            result += node.data + " ";
        }
        return result;
    }

    @Override
    public String toString() {
        String result;
        try {
            result = "PreOrder: " + preOrderPath(root);
            result += "\nPreOrder: " + preOrder();
            result += "\nInOrder: " + inOrder();
            result += "\nPostOrder: " + postOrder();
        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // Algoritmo que devuelva el padre del elemento dado en un árbol de búsqueda binaria.
    public Object father(Object element) throws TreeException {
        if (isEmpty())
            throw new TreeException("Tree empty");
        return father(root, element);
    }

    private Object father(BTreeNode node, Object element) throws TreeException {
        if (node == null) return null;

        // Si el hijo izquierdo o derecho contiene el valor buscado,
        // entonces el nodo actual es su padre
        if ((node.left != null && compare(node.left.data, element) == 0) ||
                (node.right != null && compare(node.right.data, element) == 0)) {
            return node.data; // Se encontró el valor del padre
        }

        // Si el valor buscado es menor, buscamos en el subárbol izquierdo
        if (compare(element, node.data) < 0) {
            return father(node.left, element);
        } else {
            // Si es mayor, buscamos en el subárbol derecho
            return father(node.right, element);
        }
    }

    // Algoritmo que devuelva el hermano (izquierdo o derecho) del elemento dado
    public Object brother(Object element) throws TreeException {
        if (isEmpty())
            throw new TreeException("Tree empty");
        return brother(root, element);
    }

    private Object brother(BTreeNode node, Object element) throws TreeException {
        if (node == null) return null;

        // Si el hijo izquierdo es el elemento buscado
        if (node.left != null && compare(node.left.data, element) == 0) {
            return (node.right != null) ? node.right.data : null; // Devuelve hermano derecho o null
        }

        // Si el hijo derecho es el elemento buscado
        if (node.right != null && compare(node.right.data, element) == 0) {
            return (node.left != null) ? node.left.data : null; // Devuelve hermano izquierdo o null

        }

        // Continuar la búsqueda según el valor
        if (compare(element, node.data) < 0) {
            return brother(node.left, element);
        } else {
            return brother(node.right, element);
        }
    }

    // Algoritmo que devuelva los hijos (uno, dos o ninguno) del elemento dado
    public String children(Object element) throws TreeException {
        if (isEmpty())
            throw new TreeException("Tree empty");
        return children(root, element);
    }

    private String children(BTreeNode node, Object element) throws TreeException {
        if (node == null) {
            return "Elemento no encontrado";
        }

        // Si encontramos el nodo con el valor buscado
        if (compare(node.data, element) == 0) {
            String result = "";

            // Verificar hijo izquierdo
            if (node.left != null) {
                result += "Izquierdo: " + node.left.data + " ";
            }

            // Verificar hijo derecho
            if (node.right != null) {
                result += "Derecho: " + node.right.data;
            }

            return result.isEmpty() ? "Sin hijos" : result.trim();
        }


        if (compare(element, node.data) < 0) {
            return children(node.left, element);
        } else {
            return children(node.right, element);
        }
    }
    // uso de los operadores ternarios: la primer parte debe de cumplirse, la segunda no


    public boolean isAVL() {
        return switch (getBalanceFactor(root)) {
            case 0 -> false;
            case 1 -> true;
            case -1 -> true;
            default -> false;
        };
    }

    public BTreeNode search(Object element) throws TreeException {
        if (isEmpty()) {
            throw new TreeException("AVL Binary Search Tree is empty");
        }
        return search(root, element);
    }

    private BTreeNode search(BTreeNode node, Object element) {
        if (node == null) {
            return null; // Elemento no encontrado
        }
        int comparison = compare(element, node.data);
        if (comparison == 0) {
            return  node; // Elemento encontrado
        } else if (comparison < 0) {
            return search(node.left, element); // Buscar en el subárbol izquierdo
        } else {
            return search(node.right, element); // Buscar en el subárbol derecho
        }
    }

    public <T> List<T> toList() {
        List<T> list = new ArrayList<>();
        if (!isEmpty())
            preOrder(root, list);
        return list;
    }

    // Metodo auxiliar que acumula en la lista
    private <T> void preOrder(BTreeNode node, List<T> result) {
        if (node != null) {
            result.add((T) node.data); // nodo
            preOrder(node.left, result); // hijo izq
            preOrder(node.right, result); // hijo der
        }
    }
}//fin class AVL


