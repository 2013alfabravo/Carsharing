package carsharing.controller;

import carsharing.model.Company;
import carsharing.model.DataAccessObject;
import carsharing.view.*;

import java.util.List;

public class Controller {
    private View view;
    private final View mainMenu = new MainMenuView();
    private final View managerActions = new ManagerActionsView();
    private final DataAccessObject dao;

    public Controller(DataAccessObject dao) {
        this.dao = dao;
    }

    public void run() {
        dao.createTable();
        displayMainMenu();
    }

    private void displayMainMenu() {
        while (true) {
            view = mainMenu;
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
            view = managerActions;
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
        dao.addCompany(name);
    }

    private void displayCompanies() {
        List<Company> companies = dao.getAllCompanies();
        view = new CompanyListView(companies);
        view.display();
    }
}
