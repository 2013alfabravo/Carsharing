package carsharing.view;

import carsharing.Main;

public class MainMenuView implements View {
    private static final Menu menu = new Menu()
            // fixme remove new lines before menu titles and add new lines at the ends of lists
            //  and before returns in getInput() where necessary
            .addItem("Log in as a manager", "1")
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
