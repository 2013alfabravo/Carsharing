package carsharing.model;

public class Customer implements Listable {
    private Integer id;
    private String name;
    private Integer rentedCarId;

    public Customer(Integer id, String name, Integer rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCar(Integer rentedCarId) {
        this.rentedCarId = rentedCarId;
    }
}
