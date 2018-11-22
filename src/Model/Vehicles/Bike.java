package Model.Vehicles;

public class Bike extends Vehicle{

    private final String typeOfBike;
    private final String color;
    private final int tireWidth; // szerokość opony
    private final int sizeOfWheele; //wielkosc kola np 26

    /**
     *
     * @param id KEY in database, is like EL "3D33"
     * @param name
     * @param course
     * @param availability
     * @param typeOfBike
     * @param color
     * @param tireWidth
     * @param sizeOfWheele
     * @param pricePerDay
     */
    public Bike(String id, String name, int course, boolean availability, String typeOfBike, String color, int tireWidth, int sizeOfWheele, int pricePerDay) {
        super(id, name, course, availability, pricePerDay);
        this.typeOfBike = typeOfBike;
        this.color = color;
        this.tireWidth = tireWidth;
        this.sizeOfWheele = sizeOfWheele;
    }

    public String getTypeOfBike() {
        return typeOfBike;
    }

    public String getColor() {
        return color;
    }

    public int getTireWidth() {
        return tireWidth;
    }

    public int getSizeOfWheele() {
        return sizeOfWheele;
    }

    @Override
    public String toString() {
        return "{" +
                super.toString() +
                ", typ = " + typeOfBike +
                ", kolor = " + color +
                ", szerokość opon = " + tireWidth +
                ", rozmiar kola = " + sizeOfWheele +
                '}';
    }
}
