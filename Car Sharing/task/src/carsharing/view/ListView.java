package carsharing.view;

import carsharing.Main;
import carsharing.model.Listable;

import java.text.MessageFormat;
import java.util.List;

public class ListView<T extends Listable> implements View {
    // fixme add parameter boolean showMenu
    private static final Menu menu = new Menu().addItem("Back", "0");

    private final List<T> list;
    private final String title;

    public ListView(List<T> list, String title) {
        this.list = list;
        this.title = title;
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
            System.out.println(MessageFormat.format("\nChoose a {0}:", title));
            for (int i = 0; i < list.size(); i++) {
                System.out.println(i + 1 + ". " + list.get(i).getName());
            }
            menu.show();
        }
    }

    private void displayEmptyListError() {
        System.out.println(MessageFormat.format("\nThe {0} list is empty!", title));
    }

    private void displayInvalidIndexError() {
        System.out.println("Enter a valid index.");
    }
}
