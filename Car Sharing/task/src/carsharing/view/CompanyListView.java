package carsharing.view;

import carsharing.model.Company;

import java.util.List;

public class CompanyListView implements View {
    private final List<Company> list;

    public CompanyListView(List<Company> list) {
        this.list = list;
    }

    @Override
    public String getInput() {
        return null;
    }

    @Override
    public void display() {
        if (list.isEmpty()) {
            displayErrorMessage();
        } else {
            System.out.println("\nCompany list:");
            list.forEach(System.out::println);
        }
    }

    private void displayErrorMessage() {
        System.out.println("The company list is empty!");
    }
}
