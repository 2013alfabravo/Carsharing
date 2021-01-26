package carsharing;

import carsharing.model.DataAccessObject;

import java.util.Scanner;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws ClassNotFoundException {
        DataAccessObject dao;
        if (args.length == 2 && "-databaseFileName".equals(args[0])) {
             dao = new DataAccessObject(args[1]);
        } else {
            dao = new DataAccessObject();
        }

        Controller controller = new Controller(dao);
        controller.run();
    }
}
