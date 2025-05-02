package ucr.lab.domain;

public class Node {
    public Object data;
//    public Node prev; //apuntador al nodo anterior
 public Node next; //apuntador al nodo siguiente
public String priority;

    //Constructor 1
    public Node(Object data) {
        this.data = data;
      this.next = null; //puntero al sgte nodo es nulo por default
    }

    public Node() {
        this.next = null;
    }

    public Node(Object data, String priority){
        this.data = data;
        this.next = null;
        this.priority=priority;

    }
}
