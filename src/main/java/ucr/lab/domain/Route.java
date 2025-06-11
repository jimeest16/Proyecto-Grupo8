package ucr.lab.domain;
import ucr.lab.TDA.graph.EdgeWeight;
import java.util.ArrayList;
import java.util.List;

public class Route {
    private int originAirportCode;
    private List<EdgeWeight> destinations;

    public Route() {
        this.destinations = new ArrayList<>();
    }

    public Route(int originAirportCode) {
        this.originAirportCode = originAirportCode;
        this.destinations = new ArrayList<>();
    }

    public Route(int originAirportCode, List<EdgeWeight> destinations) {
        this.originAirportCode = originAirportCode;
        this.destinations = destinations;
    }

    public int getOriginAirportCode() {
        return originAirportCode;
    }

    public void setOriginAirportCode(int originAirportCode) {
        this.originAirportCode = originAirportCode;
    }

    public List<EdgeWeight> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<EdgeWeight> destinations) {
        this.destinations = destinations;
    }

    @Override
    public String toString() {
        return "Route{" +
                "originAirportCode=" + originAirportCode +
                ", destination=" + destinations +
                '}';
    }
}
