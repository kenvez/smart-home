package com.smarthome.cli.menu;

import com.smarthome.cli.utils.*;
import com.smarthome.core.model.devices.base.*;
import com.smarthome.core.model.room.*;
import com.smarthome.core.model.house.*;
import com.smarthome.core.model.rules.*;
import com.smarthome.core.model.devices.impl.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RuleMenu {
    private final Scanner scanner;

    public RuleMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        char choice = ' ';

        while (choice != 'b') {
            ScreenUtils.clearScreen();
            display();

            choice = scanner.next().charAt(0);

            scanner.nextLine();

            switch (choice) {
                case '1' -> {
                    addRule();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '2' -> {
                    removeRule();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case '3' -> {
                    listRules();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case 'b' -> {
                    System.out.println("\nGoing back to main menu.");
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                default -> {
                    System.out.println("\nInvalid choice! Please try again.");
                    ScreenUtils.pressEnterToContinue(scanner);
                }
            }
        }
    }

    private void display() {
        System.out.println("\n===========> Rule menu <============\n");

        System.out.println("[1] Add rule                            ");
        System.out.println("[2] Remove rule                         ");
        System.out.println("[3] List rules                          ");
        System.out.println("[b] Back to main menu                 \n");

        System.out.print("Enter your choice: ");
    }

    private void addRule() {
        SelectionResult selection = MenuUtils.performSelection(scanner, true);

        if (selection == null) {
            return;
        }

        SmartDevice selectedDevice = selection.getDevice();

        ScreenUtils.clearScreen();

        System.out.println("\n============> Add rule <============\n");
    }

    private void removeRule() {
        SelectionResult selection = MenuUtils.performSelection(scanner, true);

        if (selection == null) {
            return;
        }

        SmartDevice selectedDevice = selection.getDevice();

        ScreenUtils.clearScreen();

        System.out.println("\n==========> Remove rule <===========\n");

    }

    private void listRules() {

    }
}
