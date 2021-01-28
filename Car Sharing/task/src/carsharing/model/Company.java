package carsharing.model;

public class Company implements Listable {
    private Integer id;
    private String name;

    public Company(Integer id, String name) {
        this.id = id;
        this.name = name;
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
