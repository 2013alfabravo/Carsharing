package carsharing.view;

import carsharing.Main;

public class ManagerActionsView implements View {
    private static final Menu menu = new Menu().addItem("Company list", "1")
            .addItem("Create a company", "2")
            .addItem("Back", "0");

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
        menu.show();
    }
}
