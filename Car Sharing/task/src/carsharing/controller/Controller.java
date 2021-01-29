package carsharing.controller;

import carsharing.model.*;
import carsharing.view.*;

import java.text.MessageFormat;
import java.util.List;

public class Controller {
    private View view;
    private final CarSharingDAO carsharingDAO;

    public Controller(CarSharingDAO carsharingDAO) {
        this.carsharingDAO = carsharingDAO;
    }

    public void run() {
        carsharingDAO.createCompanyTable();
        carsharingDAO.createCarTable();
        carsharingDAO.createCustomerTable();
        displayMainMenu();
    }

    private void displayMainMenu() {
        while (true) {
            view = new MainMenuView();
            view.display();
            String input = view.getInput();
            if ("1".equals(input)) {
                displayManagerActions();
            } else if ("2".equals(input)) {
                displayCustomers();
            } else if ("3".equals(input)) {
                addCustomer();
            } else if ("0".equals(input)) {
                break;
            }
        }
    }

    private void displayManagerActions() {
        while (true) {
            view = new ManagerActionsView();
            view.display();
            String input = view.getInput();
            if ("1".equals(input)) {
                displayCompanies();
            } else if ("2".equals(input)) {
                addCompany();
            } else if ("0".equals(input)) {
                break;
            }
        }
    }

    private void addCustomer() {
        view = new NewRecordView("customer");
        view.display();
        String name = view.getInput();
        // todo add display message to this view and show success message after dao method returns success
        carsharingDAO.createCustomer(name);
    }

    private void displayCustomers() {
        List<Customer> customers = carsharingDAO.findAllCustomers();
        view = new ListView<>(customers, "customer");
        view.display();

        if (customers.isEmpty()) {
            return;
        }

        int index = Integer.parseInt(view.getInput()) - 1;
        if (index == -1) {
            return;
        }

        Customer customer = customers.get(index);
        showCustomer(customer);
    }

    private void showCustomer(Customer customer) {
        while (true) {
            view = new CustomerView(customer);
            view.display();
            String input = view.getInput();

            if ("1".equals(input)) {
                rentCar(customer);
            } else if ("2".equals(input)) {
                returnCar(customer);
            } else if ("3".equals(input)) {
                showRentedCar(customer);
            } else if ("0".equals(input)) {
                break;
            }
        }
    }

    private void showRentedCar(Customer customer) {
        // fixme change to view
        if (customer.getRentedCarId() == null) {
            System.out.println("You didn't rent a car!");
            return;
        }
        Car car = carsharingDAO.findRentedCar(customer.getId());
        System.out.println(MessageFormat.format("You rented {0}", car.getName()));
    }

    // fixme change to view
    private void returnCar(Customer customer) {
        carsharingDAO.removeRentedCar(customer.getId(), customer.getRentedCarId());
        // todo try catch to see if the transaction is successful
        customer.setRentedCar(null);
        System.out.println("car returned");
    }

    private void rentCar(Customer customer) {
        List<Company> companies = carsharingDAO.findAllCompanies();
        view = new ListView<>(companies, "company");
        view.display();

        if (companies.isEmpty()) {
            return;
        }

        int index = Integer.parseInt(view.getInput()) - 1;
        if (index == -1) {
            return;
        }

        Company company = companies.get(index);

        List<Car> cars = carsharingDAO.findAvailableCarsByCompanyId(company.getId());
        view = new ListView<>(cars, "car");
        view.display();

        index = Integer.parseInt(view.getInput()) - 1;
        if (index == -1) {
            return;
        }

        Car car = cars.get(index);
        customer.setRentedCar(car.getId());
        carsharingDAO.addRentedCar(customer.getId(), customer.getRentedCarId());
    }

    private void addCompany() {
        view = new NewRecordView("company");
        view.display();
        String name = view.getInput();
        carsharingDAO.createCompany(name);
    }

    private void displayCompanies() {
        List<Company> companies = carsharingDAO.findAllCompanies();
        view = new ListView<>(companies, "company");
        view.display();

        if (companies.isEmpty()) {
            return;
        }

        int index = Integer.parseInt(view.getInput()) - 1;
        if (index == -1) {
            return;
        }

        Company company = companies.get(index);
        showCompany(company);
    }

    private void showCompany(Company company) {
        while (true) {
            view = new CompanyView(company);
            view.display();
            String input = view.getInput();
            if ("1".equals(input)) {
                displayCars(company);
            } else if ("2".equals(input)) {
                addCar(company);
            } else if ("0".equals(input)) {
                break;
            }
        }
    }

    private void addCar(Company company) {
        view = new NewRecordView("car");
        view.display();
        String name = view.getInput();
        carsharingDAO.createCar(name, company.getId());
    }

    private void displayCars(Company company) {
        List<Car> cars = carsharingDAO.findAllCarsByCompanyId(company.getId());
        // fixme change title to contain the entire list title, i.e. choose a ... or empty and don't print if empty
        view = new ListView<>(cars, "car");
        view.display();
    }
}
