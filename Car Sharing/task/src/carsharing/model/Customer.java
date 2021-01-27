package carsharing.model;

public class Customer implements Listable {
    private int id;
    private String name;
    private Integer rentedCarId;

    public Customer(int id, String name, Integer rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCar(int rentedCarId) {
        this.rentedCarId = rentedCarId;
    }

    public int getRentedCar() {
        return rentedCarId;
    }
}
