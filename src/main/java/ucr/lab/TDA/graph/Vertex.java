package ucr.lab.TDA.graph;

import ucr.lab.TDA.list.SinglyLinkedList;

public class Vertex {
    public Object data;
    private boolean visited; //para los recorridos DFS, BFS
    public SinglyLinkedList edgesList; //lista de aristas

    //Constructor
    public Vertex(Object data){
        this.data = data;
        this.visited = false;
        this.edgesList = new SinglyLinkedList();
    }

    public Vertex(Object data, SinglyLinkedList edgesList) {
        this.data = data;
        this.visited = false;
        this.edgesList = edgesList;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public String toString() {
        return data+"";
    }
}
