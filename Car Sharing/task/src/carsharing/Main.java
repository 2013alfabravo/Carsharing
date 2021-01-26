package carsharing;

import carsharing.controller.Controller;
import carsharing.model.CarDAO;
import carsharing.model.CompanyDAO;
import carsharing.model.CustomerDAO;

import java.util.Scanner;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws ClassNotFoundException {
        CompanyDAO companyDAO;
        CarDAO carDAO;
        CustomerDAO customerDAO;
        if (args.length == 2 && "-databaseFileName".equals(args[0])) {
             companyDAO = new CompanyDAO(args[1]);
             carDAO = new CarDAO(args[1]);
             customerDAO = new CustomerDAO(args[1]);
        } else {
            companyDAO = new CompanyDAO();
            carDAO = new CarDAO();
            customerDAO = new CustomerDAO();
        }

        Controller controller = new Controller(companyDAO, carDAO, customerDAO);
        controller.run();
    }
}
