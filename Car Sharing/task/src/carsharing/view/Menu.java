package carsharing.view;

import java.util.*;

public class Menu {
    private final List<String> items = new ArrayList<>();
    private final List<String> aKeys = new ArrayList<>();

    public void show() {
        items.forEach(System.out::println);
    }

    public Menu addItem(String title, String aKey) {
        items.add(aKey + ". " + title);
        aKeys.add(aKey);
        return this;
    }

    public boolean isValidKey(String aKey) {
        return aKeys.contains(aKey);
    }
}
