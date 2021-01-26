package carsharing.view;

import carsharing.Main;

public class NewCompanyView implements View {
    private String input;

    @Override
    public String getInput() {
        return input;
    }

    @Override
    public void display() {
        System.out.println("\nEnter the company name:");
        input = Main.scanner.nextLine().strip();
        System.out.println("The company was created!");
    }
}
