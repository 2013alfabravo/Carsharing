package carsharing.view;

import carsharing.Main;
import carsharing.model.Company;

public class CompanyView implements View {
    private static final Menu menu = new Menu()
            .addItem("Car list", "1")
            .addItem("Create a car", "2")
            .addItem("Back", "0");

    private final Company company;

    public CompanyView(Company company) {
        this.company = company;
    }

    @Override
    public String getInput() {
        while (true) {
            String input = Main.scanner.nextLine().strip();
            if (menu.isValidKey(input)) {
                return input;
            } else {
                showMessage("Please enter a valid number.");
            }
        }
    }

    @Override
    public void display() {
        emptyLine();
        showMessage(String.format("'%s' company", company.getName()));
        menu.show();
    }

}
