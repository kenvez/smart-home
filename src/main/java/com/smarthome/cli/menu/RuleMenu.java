package com.smarthome.cli.menu;

import com.smarthome.cli.utils.*;
import com.smarthome.core.Main;
import com.smarthome.core.model.devices.base.*;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.Room;
import com.smarthome.core.model.rules.Rule;

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
        System.out.println("[4] Rule settings                       ");
        System.out.println("[b] Back to main menu                 \n");

        System.out.print("Enter your choice: ");
    }

    private void addRule() {
        ScreenUtils.clearScreen();

        System.out.println("\n============> Add rule <============\n");

        System.out.println("Select the device for the condition");

        SelectionResult conditionSelection = MenuUtils.performSelection(scanner, true);

        if (conditionSelection == null) {
            System.out.println("Rule creation cancelled.");
            return;
        }

        SmartDevice conditionDevice = conditionSelection.getDevice();

        Predicate<SmartDevice> condition = createConditionForDevice(conditionDevice);

        System.out.println("\nSelect the device for the action");

        SelectionResult actionSelection = MenuUtils.performSelection(scanner, true);

        if (actionSelection == null) {
            System.out.println("Rule creation cancelled.");
            return;
        }

        SmartDevice actionDevice = actionSelection.getDevice();

        Consumer<SmartDevice> action = createActionForDevice(actionDevice);

        System.out.print("\nEnter a description for this rule (or leave empty" +
                " for auto-description): ");

        String description = scanner.nextLine();

        Rule<SmartDevice, SmartDevice> rule;

        if (description.isEmpty()) {
            rule = new Rule<>(conditionDevice, condition, actionDevice, action);
        } else {
            rule = new Rule<>(conditionDevice, condition, actionDevice, action, description);
        }

        conditionDevice.getRules().add(rule);

        System.out.println("\nRule created successfully: " + rule);

    }

    private void removeRule() {
        ScreenUtils.clearScreen();

        System.out.println("\n==========> Remove rule <===========\n");

        System.out.println("Select the device with the rule you want to remove:");

        SelectionResult selection = MenuUtils.performSelection(scanner, true);

        if (selection == null) {
            System.out.println("Rule removal cancelled.");
            return;
        }

        SmartDevice selectedDevice = selection.getDevice();
        Set<Rule<?, ?>> rules = selectedDevice.getRules();

        if (rules.isEmpty()) {
            System.out.println("\nThis device has no rules to remove.");
            return;
        }

        System.out.println("\nRules for " + selectedDevice.getName() + ":");

        List<Rule<?, ?>> rulesList = new ArrayList<>(rules);

        for (int i = 0; i < rulesList.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + rulesList.get(i));
        }

        System.out.print("\nEnter the number of the rule to remove (0 to cancel): ");

        int choice = scanner.nextInt();

        scanner.nextLine();

        if (choice == 0) {
            System.out.println("Rule removal cancelled.");

            return;
        }

        if (choice < 1 || choice > rulesList.size()) {
            System.out.println("Invalid rule number.");

            return;
        }

        Rule<?, ?> ruleToRemove = rulesList.get(choice - 1);

        rules.remove(ruleToRemove);

        System.out.println("\nRule removed successfully.");
    }

    private void listRules() {
        ScreenUtils.clearScreen();

        System.out.println("\n===========> List rules <===========\n");

        if (Main.houses.isEmpty()) {
            System.out.println("No houses found! Please add a house first.");

            return;
        }

        boolean foundRules = false;
        int totalRules = 0;

        for (House house : Main.houses) {
            System.out.println("House: " + house.getName());

            Set<Room> rooms = house.getRooms();

            if (rooms.isEmpty()) {
                System.out.println("\tNo rooms found in this house.");

                continue;
            }

            boolean houseHasRules = false;

            for (Room room : rooms) {
                Set<SmartDevice> devices = room.getDevices();
                StringBuilder roomRules = new StringBuilder();
                boolean roomHasRules = false;
                int roomRuleCount = 0;

                if (devices.isEmpty()) {
                    continue;
                }

                for (SmartDevice device : devices) {
                    Set<Rule<?, ?>> rules = device.getRules();

                    if (!rules.isEmpty()) {
                        if (!roomHasRules) {
                            roomRules.append("\tRoom: ").append(room.getName()).append("\n");
                            roomHasRules = true;
                        }

                        roomRules.append("\t\tDevice: ").append(device.getName()).append("\n");

                        int counter = 1;

                        for (Rule<?, ?> rule : rules) {
                            roomRules.append("\t\t\t").append(counter).append(
                                    ". ").append(rule).append("\n");
                            counter++;
                            roomRuleCount++;
                        }
                    }
                }

                if (roomHasRules) {
                    houseHasRules = true;
                    System.out.print(roomRules.toString());
                    totalRules += roomRuleCount;
                }
            }

            if (houseHasRules) {
                foundRules = true;
            } else {
                System.out.println("\tNo rules found in this house.");
            }

            System.out.println();
        }

        if (!foundRules) {
            System.out.println("\tNo rules found in any house in the system.");
        } else {
            System.out.println("Total rules found: " + totalRules);
        }
    }

    private Predicate<SmartDevice> createConditionForDevice(SmartDevice device) {
        ScreenUtils.clearScreen();

        System.out.println("\n=====> Select condition type <======\n");

        System.out.println("[1] Device is ON");
        System.out.println("[2] Device is OFF");

        System.out.print("\nEnter your choice: ");

        int choice = scanner.nextInt();

        scanner.nextLine();

        return switch (choice) {
            case 1 -> SmartDevice::isOn;
            case 2 -> d -> !d.isOn();
            default -> {
                System.out.println("\nInvalid choice. Using default condition" +
                        " (device is ON).");

                yield SmartDevice::isOn;
            }
        };
    }

    private Consumer<SmartDevice> createActionForDevice(SmartDevice device) {
        ScreenUtils.clearScreen();

        System.out.println("\n=======> Select action type <=======\n");

        System.out.println("[1] Turn device ON");
        System.out.println("[2] Turn device OFF");
        System.out.println("[3] Toggle device status");

        System.out.print("\nEnter your choice: ");

        int choice = scanner.nextInt();

        scanner.nextLine();

        return switch (choice) {
            case 1 -> SmartDevice::turnOn;
            case 2 -> SmartDevice::turnOff;
            case 3 -> d -> {
                if (d.isOn()) {
                    d.turnOff();
                } else {
                    d.turnOn();
                }
            };
            default -> {
                System.out.println("\nInvalid choice. Using default action " +
                        "(turn ON).");

                yield SmartDevice::turnOn;
            }
        };
    }

}
