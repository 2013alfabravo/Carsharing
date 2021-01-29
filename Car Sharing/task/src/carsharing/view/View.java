package carsharing.view;

public interface View {
    String getInput();
    void display();

    default void showMessage(String message) {
        System.out.println(message);
    }

    default void emptyLine() {
        System.out.println();
    }
}
