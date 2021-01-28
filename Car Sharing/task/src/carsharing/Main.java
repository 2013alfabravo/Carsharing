package carsharing;

import carsharing.controller.Controller;
import carsharing.model.CarSharingDAO;
import carsharing.model.DataSource;

import java.util.Scanner;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws ClassNotFoundException {
        DataSource dataSource;

        if (args.length == 2 && "-databaseFileName".equals(args[0])) {
            dataSource = new DataSource(args[1]);
        } else {
            dataSource = new DataSource();
        }

        CarSharingDAO carsharingDAO = new CarSharingDAO(dataSource);
        Controller controller = new Controller(carsharingDAO);
        controller.run();
    }
}
