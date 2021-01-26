package carsharing.view;

import carsharing.Main;
import carsharing.model.Company;

import java.util.List;

public class CompanyListView implements View {
    private static final Menu menu = new Menu().addItem("Back", "0");

    private final List<Company> list;

    public CompanyListView(List<Company> list) {
        this.list = list;
    }

    @Override
    public String getInput() {
        while (true) {
            String input = Main.scanner.nextLine().strip();
            if (input.matches("[0-9]+")) {
                int index = Integer.parseInt(input);
                if (index >= 0 && index <= list.size()) {
                    return input;
                }
            } else {
                displayInvalidIndexError();
            }
        }
    }

    @Override
    public void display() {
        if (list.isEmpty()) {
            displayEmptyListError();
        } else {
            System.out.println("\nChoose a company:");
            for (int i = 0; i < list.size(); i++) {
                System.out.println(i + 1 + ". " + list.get(i).getName());
            }
            menu.show();
        }
    }

    private void displayEmptyListError() {
        System.out.println("\nThe company list is empty!");
    }

    private void displayInvalidIndexError() {
        System.out.println("Enter a valid index.");
    }
}
