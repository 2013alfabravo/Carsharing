package carsharing.view;

import carsharing.Main;

public class NewCarView implements View {
    private String input;

    @Override
    public String getInput() {
        return input;
    }

    @Override
    public void display() {
        System.out.println("\nEnter the car name:");
        input = Main.scanner.nextLine().strip();
        System.out.println("The car was added!");
    }
}
