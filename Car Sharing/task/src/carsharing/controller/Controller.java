package carsharing.controller;

import carsharing.model.Car;
import carsharing.model.CarDAO;
import carsharing.model.Company;
import carsharing.model.CompanyDAO;
import carsharing.view.*;

import java.util.List;

public class Controller {
    private View view;
    private final CompanyDAO companyDAO;
    private final CarDAO carDAO;

    public Controller(CompanyDAO companyDAO, CarDAO carDAO) {
        this.companyDAO = companyDAO;
        this.carDAO = carDAO;
    }

    public void run() {
        companyDAO.createTable();
        carDAO.createTable();
        displayMainMenu();
    }

    private void displayMainMenu() {
        while (true) {
            view = new MainMenuView();
            view.display();
            String input = view.getInput();
            if ("1".equals(input)) {
                displayManagerActions();
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

    private void addCompany() {
        view = new NewCompanyView();
        view.display();
        String name = view.getInput();
        companyDAO.addCompany(name);
    }

    private void displayCompanies() {
        List<Company> companies = companyDAO.getAllCompanies();
        view = new CompanyListView(companies);
        view.display();

        if (companies.isEmpty()) {
            return;
        }

        int index = Integer.parseInt(view.getInput()) - 1;
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
        view = new NewCarView();
        view.display();
        String name = view.getInput();
        carDAO.addCar(name, company.getId());
    }

    private void displayCars(Company company) {
        List<Car> cars = carDAO.getCarsByCompanyId(company.getId());
        view = new CarListView(cars);
        view.display();
    }
}
