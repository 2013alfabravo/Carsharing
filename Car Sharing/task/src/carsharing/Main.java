package carsharing;

import carsharing.controller.Controller;
import carsharing.model.CarSharingDAO;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        CarSharingDAO carsharingDAO;
        if (args.length == 2 && "-databaseFileName".equals(args[0])) {
             carsharingDAO = new CarSharingDAO(args[1]);
        } else {
            carsharingDAO = new CarSharingDAO();
        }

        Controller controller = new Controller(carsharingDAO);
        controller.run();
    }
}
