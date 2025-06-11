package ucr.lab.domain;

public class Boarding {
    private int destinationCode;
    private int reservationCode;

    public Boarding(int destinationCode, int reservationCode) {
        this.destinationCode = destinationCode;
        this.reservationCode = reservationCode;
    }

    public int getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(int destinationCode) {
        this.destinationCode = destinationCode;
    }

    public int getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(int reservationCode) {
        this.reservationCode = reservationCode;
    }

    @Override
    public String toString() {
        return "Boarding{" +
                "destinationCode=" + destinationCode +
                ", reservationCode=" + reservationCode +
                '}';
    }
}
