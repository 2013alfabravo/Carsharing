package carsharing.controller;

import carsharing.model.*;
import carsharing.view.*;

import java.util.List;

// todo combine various entity views into a single menu view with Menu obj as an arg + add menu factory and menu enum
public class Controller {
    private static final String FAIL = "Operation failed";
    
    private View view;
    private final CarSharingDAO carsharingDAO;

    public Controller(CarSharingDAO carsharingDAO) {
        this.carsharingDAO = carsharingDAO;
    }

    public void run() {
//        carsharingDAO.dropTables();
        carsharingDAO.createCompanyTable();
        carsharingDAO.createCarTable();
        carsharingDAO.createCustomerTable();
        showMainMenu();
    }

    private void showMainMenu() {
        while (true) {
            view = new MainMenuView();
            view.display();
            String input = view.getInput();
            if ("1".equals(input)) {
                showManagerMenu();
            } else if ("2".equals(input)) {
                showCustomerList();
            } else if ("3".equals(input)) {
                addCustomer();
            } else if ("0".equals(input)) {
                break;
            }
        }
    }

    private void showManagerMenu() {
        while (true) {
            view = new ManagerActionsView();
            view.display();
            String input = view.getInput();
            if ("1".equals(input)) {
                showCompanyList();
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
        try {
            carsharingDAO.createCustomer(name);
            view.showMessage("The customer was added!");
        } catch (DAOException e) {
            view.showMessage(FAIL);
        }
    }

    private void showCustomerList() {
        List<Customer> customers;
        try {
            customers = carsharingDAO.findAllCustomers();
        } catch (DAOException e) {
            view.showMessage(FAIL);
            return;
        }

        // fixme extract showing a list and getting an index with input validation
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
        showCustomerMenu(customer);
    }

    private void showCustomerMenu(Customer customer) {
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
        if (customer.getRentedCarId() == null) {
            view.showMessage("You didn't rent a car!");
            return;
        }

        try {
            Car car = carsharingDAO.findRentedCar(customer.getId());
            Company company = carsharingDAO.findCompanyById(car.getCompanyId());
            view.showMessage("Your rented car:");
            view.showMessage(car.getName());
            view.showMessage("Company:");
            view.showMessage(company.getName());
        } catch (DAOException e) {
            view.showMessage(FAIL);
        }
    }

    private void returnCar(Customer customer) {
        if (customer.getRentedCarId() == null) {
            view.showMessage("You didn't rent a car!");
            return;
        }

        try {
            carsharingDAO.removeRentedCar(customer.getId(), customer.getRentedCarId());
            customer.setRentedCar(null);
            view.showMessage("You've returned a rented car!");
        } catch (DAOException e) {
            view.showMessage(FAIL);
        }
    }

    private void rentCar(Customer customer) {
        if (customer.getRentedCarId() != null) {
            view.showMessage("You've already rented a car!");
            return;
        }

        // fixme extract showing a list and getting an index with input validation
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

        try {
            Car car = cars.get(index);
            customer.setRentedCar(car.getId());
            carsharingDAO.addRentedCar(customer.getId(), customer.getRentedCarId());
            view.showMessage(String.format("You rented '%s'", car.getName()));
        } catch (DAOException e) {
            view.showMessage(FAIL);
        }
    }

    private void addCompany() {
        view = new NewRecordView("company");
        view.display();
        String name = view.getInput();
        try {
            carsharingDAO.createCompany(name);
            view.showMessage("The new company was added!");
        } catch (DAOException e) {
            view.showMessage(FAIL);
        }
    }

    private void showCompanyList() {
        List<Company> companies;

        try {
             companies = carsharingDAO.findAllCompanies();
        } catch (DAOException e) {
            view.showMessage(FAIL);
            return;
        }

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
        showCompanyMenu(company);
    }

    private void showCompanyMenu(Company company) {
        while (true) {
            view = new CompanyView(company);
            view.display();
            String input = view.getInput();
            if ("1".equals(input)) {
                showCarList(company);
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
        try {
            carsharingDAO.createCar(name, company.getId());
            view.showMessage("The new car was added!");
        } catch (DAOException e) {
            view.showMessage(FAIL);
        }
    }

    private void showCarList(Company company) {
        List<Car> cars = carsharingDAO.findAllCarsByCompanyId(company.getId());
        view = new ListView<>(cars, "car", false);
        view.display();
    }
}
