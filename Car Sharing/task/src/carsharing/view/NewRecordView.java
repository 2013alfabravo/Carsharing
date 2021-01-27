package carsharing.view;

import carsharing.Main;

import java.text.MessageFormat;

public class NewRecordView implements View {
    private String input;
    private final String title;

    public NewRecordView(String title) {
        this.title = title;
    }

    @Override
    public String getInput() {
        return input;
    }

    @Override
    public void display() {
        System.out.println(MessageFormat.format("\nEnter the {0} name:", title));
        input = Main.scanner.nextLine().strip();
        System.out.println(MessageFormat.format("The {0} was created!", title));
    }
}
