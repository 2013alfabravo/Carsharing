package carsharing.model;

public class Car implements Listable {
    private Integer id;
    private String name;
    private Integer companyId;
    private Boolean available;

    public Car(Integer id, String name, Integer companyId, Boolean available) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
        this.available = available;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }
}
