package carsharing.view;

import carsharing.model.Car;

import java.util.List;

public class CarListView implements View {
    private final List<Car> list;

    public CarListView(List<Car> list) {
        this.list = list;
    }

    @Override
    public String getInput() {
        return null;
    }

    @Override
    public void display() {
        if (list.isEmpty()) {
            displayEmptyListError();
        } else {
            System.out.println("\nCar list:");
            for (int i = 0; i < list.size(); i++) {
                System.out.println(i + 1 + ". " + list.get(i).getName());
            }
        }
    }

    private void displayEmptyListError() {
        System.out.println("\nThe car list is empty!");
    }
}
