package carsharing.view;

import carsharing.Main;

public class NewCustomerView implements View {
    private String input;

    @Override
    public String getInput() {
        return input;
    }

    @Override
    public void display() {
        System.out.println("\nEnter the customer name:");
        input = Main.scanner.nextLine().strip();
        System.out.println("The customer was added!");
    }
}
