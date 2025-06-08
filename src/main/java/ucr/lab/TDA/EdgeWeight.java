package ucr.lab.TDA;

import static ucr.lab.utility.Util.compare;


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
    public String toString() {
        if (weight == null) {
            return String.format("Edge = %s", edge);
        } else {
            return String.format("Edge = %s | Weight = %s", edge, weight);
        }
    }


}
