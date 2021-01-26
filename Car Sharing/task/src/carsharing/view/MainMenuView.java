package carsharing.view;

import carsharing.Main;

public class MainMenuView implements View {
    private static final Menu menu = new Menu()
            .addItem("Log in as a manager", "1")
            .addItem("Log in as a customer", "2")
            .addItem("Create a customer", "3")
            .addItem("Exit", "0");

    @Override
    public String getInput() {
        while (true) {
            String actionKey = Main.scanner.nextLine().strip();
            if (menu.isValidKey(actionKey)) {
                return actionKey;
            } else {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    @Override
    public void display() {
        System.out.println();
        menu.show();
    }
}
