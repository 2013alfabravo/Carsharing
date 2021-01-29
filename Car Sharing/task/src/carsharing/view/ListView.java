package carsharing.view;

import carsharing.Main;
import carsharing.model.Listable;

import java.util.List;

public class ListView<T extends Listable> implements View {
    private static final Menu menu = new Menu().addItem("Back", "0");

    private final List<T> list;
    private final String title;
    private final boolean showMenu;

    public ListView(List<T> list, String title, boolean showMenu) {
        this.list = list;
        this.title = title;
        this.showMenu = showMenu;
    }

    public ListView(List<T> list, String title) {
        this(list, title, true);
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
            return;
        }

        emptyLine();
        showMessage(String.format("Choose a %s:", title));

        for (int i = 0; i < list.size(); i++) {
            showMessage(i + 1 + ". " + list.get(i).getName());
        }

        if (showMenu) {
            menu.show();
        }
    }

    private void displayEmptyListError() {
        emptyLine();
        showMessage(String.format("The %s list is empty!", title));
    }

    private void displayInvalidIndexError() {
        showMessage("Enter a valid index.");
    }
}
