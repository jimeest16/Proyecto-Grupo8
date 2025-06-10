package ucr.lab.TDA;

import static ucr.lab.utility.Util.compare;


public class Vertex {
    public Object data;
    public boolean visited;
    public SinglyLinkedList edgesList; // lista de aristas

    public Vertex(Object data) {
        this.data = data;
        this.visited = false;
        this.edgesList = new SinglyLinkedList();
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public SinglyLinkedList getEdgesList() {
        return edgesList;
    }

    public void setEdgesList(SinglyLinkedList edgesList) {
        this.edgesList = edgesList;
    }

    public boolean isVisited() {
        return visited;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vertex) {
            Vertex other = (Vertex) obj;
            return compare(this.data, other.data) == 0;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(data);
    }
}
