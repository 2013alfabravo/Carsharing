package carsharing.model;

public class Customer {
    private int id;
    private String name;
    private int rentedCarId;

    public Customer(int id, String name, int rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }
}
