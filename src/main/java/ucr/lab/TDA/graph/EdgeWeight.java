package ucr.lab.TDA.graph;


public class EdgeWeight {

    // atributos
    private Object edge;
    private Object weight;

    public EdgeWeight(Object edge, Object weight) {
        this.edge = edge;
        this.weight = weight;
    }

    public Object getEdge() {
        return edge;
    }

    public void setEdge(Object edge) {
        this.edge = edge;
    }

    public Object getWeight() {
        return weight;
    }

    public void setWeight(Object weight) {
        this.weight = weight;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EdgeWeight)) return false;
        EdgeWeight other = (EdgeWeight) obj;
        return (this.edge == null ? other.edge == null : this.edge.equals(other.edge));
    }

    @Override
    public int hashCode() {
        return edge == null ? 0 : edge.hashCode();
    }

    @Override
    public String toString() {
        if (weight == null) {
            return String.format("Edge = %s", edge);
        } else {
            return String.format("Edge = %s | Weight = %s", edge, weight);
        }
    }


}
