package carsharing.view;

import carsharing.Main;
import carsharing.model.Customer;

public class CustomerView implements View {
    private static final Menu menu = new Menu()
            .addItem("Rent a car", "1")
            .addItem("Return a rented car", "2")
            .addItem("My rented car", "3")
            .addItem("Back", "0");

    private final Customer customer;

    public CustomerView(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String getInput() {
        while (true) {
            String input = Main.scanner.nextLine().strip();
            if (menu.isValidKey(input)) {
                return input;
            } else {
                displayErrorMessage();
            }
        }
    }

    @Override
    public void display() {
        System.out.println();
        menu.show();
    }

    private void displayErrorMessage() {
        System.out.println("Please enter a valid number.");
    }
}
