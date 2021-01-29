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
        emptyLine();
        showMessage(String.format("Enter the %s name:", title));
        input = Main.scanner.nextLine().strip();
    }
}
