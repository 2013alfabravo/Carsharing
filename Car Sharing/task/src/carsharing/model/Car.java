package carsharing.model;

public class Car implements Listable {
    private Integer id;
    private String name;
    private Integer companyId;
    private Integer customerId;

    public Car(Integer id, String name, Integer companyId, Integer customerId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
        this.customerId = customerId;
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

    public Integer getCompanyId() {
        return companyId;
    }
}
