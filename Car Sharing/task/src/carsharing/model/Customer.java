package carsharing.model;

public class Customer {
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
}
