package com.smarthome.cli.menu;

import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.Room;
import com.smarthome.core.model.rules.Rule;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class RuleManager {
    private final Scanner scanner;
    private final HouseManager houseManager;
    private final DeviceManager deviceManager;
    private final Set<Rule<?, ?>> rules;

    public RuleManager(Scanner scanner, HouseManager houseManager, DeviceManager deviceManager) {
        this.scanner = scanner;
        this.houseManager = houseManager;
        this.deviceManager = deviceManager;
        this.rules = new HashSet<>();
    }

    public void manage() {
        char choice = ' ';

        while (choice != 'b') {
            ScreenUtils.clearScreen();
            displayMenu();

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
                case '4' -> {
                    executeRules();
                    ScreenUtils.pressEnterToContinue(scanner);
                }
                case 'b' -> System.out.println("Going back to main menu...");
                default -> {
                    System.out.println("Invalid choice! Please try again.");
                    ScreenUtils.pressEnterToContinue(scanner);
                }
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n==========> Manage Rules <==========\n");
        System.out.println("[1] Add rule");
        System.out.println("[2] Remove rule");
        System.out.println("[3] List rules");
        System.out.println("[4] Execute rules");
        System.out.println("[b] Back to main menu\n");
        System.out.print("Enter your choice: ");
    }

    private void addRule() {
        ScreenUtils.clearScreen();
        System.out.println("\n============> Add Rule <============\n");

        House selectedHouse = ScreenUtils.selectHouse(scanner, houseManager);
        if (selectedHouse == null) return;

        Room selectedRoom = ScreenUtils.selectRoom(scanner, selectedHouse);
        if (selectedRoom == null) return;

        System.out.println("\nSelect condition device:");
        SmartDevice conditionDevice = ScreenUtils.selectDevice(scanner, selectedRoom);
        if (conditionDevice == null) return;

        System.out.println("\nSelect action device:");
        SmartDevice actionDevice = ScreenUtils.selectDevice(scanner, selectedRoom);
        if (actionDevice == null) return;

        System.out.print("\nEnter rule name: ");
        String ruleName = scanner.nextLine();

        try {
            Predicate<SmartDevice> condition = createCondition(conditionDevice);
            Consumer<SmartDevice> action = createAction(actionDevice);

            Rule<SmartDevice, SmartDevice> rule = new Rule<>(
                    ruleName,
                    conditionDevice,
                    actionDevice,
                    condition,
                    action
            );

            rules.add(rule);
            System.out.println("\nRule created successfully!");
        } catch (Exception e) {
            System.out.println("\nError creating rule: " + e.getMessage());
        }
    }

    private void removeRule() {
        ScreenUtils.clearScreen();
        System.out.println("\n==========> Remove Rule <==========\n");

        if (rules.isEmpty()) {
            System.out.println("No rules to remove!");
            return;
        }

        List<Rule<?, ?>> rulesList = new ArrayList<>(rules);
        for (int i = 0; i < rulesList.size(); i++) {
            System.out.printf("[%d] %s%n", i + 1, rulesList.get(i));
        }

        System.out.print("\nEnter rule number to remove: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= rulesList.size()) {
                Rule<?, ?> ruleToRemove = rulesList.get(choice - 1);
                rules.remove(ruleToRemove);
                System.out.println("Rule removed successfully!");
            } else {
                System.out.println("Invalid rule number!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }

    private void listRules() {
        ScreenUtils.clearScreen();
        System.out.println("\n==========> List Rules <==========\n");

        if (rules.isEmpty()) {
            System.out.println("No rules defined!");
            return;
        }

        for (Rule<?, ?> rule : rules) {
            System.out.println(rule);
        }
    }

    private void executeRules() {
        ScreenUtils.clearScreen();
        System.out.println("\n==========> Execute Rules <==========\n");

        if (rules.isEmpty()) {
            System.out.println("No rules to execute!");
            return;
        }

        int executedRules = 0;
        for (Rule<?, ?> rule : rules) {
            if (rule.execute()) {
                System.out.printf("Rule '%s' executed successfully!%n", rule.getName());
                executedRules++;
            }
        }

        System.out.printf("%nExecuted %d rules%n", executedRules);
    }

    private Predicate<SmartDevice> createCondition(SmartDevice device) {
        ScreenUtils.clearScreen();
        System.out.println("\n=======> Create Condition <========\n");

        // Here you should implement device-specific condition creation
        // For example, for a temperature sensor, you might want to check if temperature is above/below a threshold
        // For now, we'll return a simple condition
        return d -> d.isOn();
    }

    private Consumer<SmartDevice> createAction(SmartDevice device) {
        ScreenUtils.clearScreen();
        System.out.println("\n=========> Create Action <=========\n");

        // Here you should implement device-specific action creation
        // For example, for a light bulb, you might want to turn it on/off or set brightness
        // For now, we'll return a simple action
        return d -> d.turnOn();
    }

    public Set<Rule<?, ?>> getRules() {
        return rules;
    }

}