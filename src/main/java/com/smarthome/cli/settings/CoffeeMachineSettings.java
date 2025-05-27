package com.smarthome.cli.settings;

import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.core.model.devices.base.CoffeeType;
import com.smarthome.core.model.devices.base.CupSize;
import com.smarthome.core.model.devices.impl.CoffeeMachine;

import java.util.Scanner;

public class CoffeeMachineSettings {
    private final Scanner scanner;
    private final DeviceSettings deviceSettings;

    public CoffeeMachineSettings(Scanner scanner, DeviceSettings deviceSettings) {
        this.scanner = scanner;
        this.deviceSettings = deviceSettings;
    }

    public void start(CoffeeMachine coffeeMachine) {
        ScreenUtils.clearScreen();

        System.out.println("\n====> Coffee Machine Settings <=====\n");

        System.out.println("[1] Change device name                  ");
        System.out.println("[2] Toggle device                       ");
        System.out.println("[3] Select coffee type                  ");
        System.out.println("[4] Select cup size                     ");
        System.out.println("[5] Brew coffee                         ");
        System.out.println("[6] Refill water                        ");
        System.out.println("[b] Back to device menu                \n");

        System.out.print("Enter your choice: ");

        char choice = scanner.next().charAt(0);

        scanner.nextLine();

        switch (choice) {
            case '1' -> deviceSettings.changeName(coffeeMachine);
            case '2' -> deviceSettings.toggleDevice(coffeeMachine);
            case '3' -> selectCoffeeType(coffeeMachine);
            case '4' -> selectCupSize(coffeeMachine);
            case '5' -> brewCoffee(coffeeMachine);
            case '6' -> refillWater(coffeeMachine);
            case 'b' -> System.out.println("\nGoing back to main menu.");
            default -> System.out.println("\nInvalid choice!");
        }
    }

    private void selectCoffeeType(CoffeeMachine coffeeMachine) {
        ScreenUtils.clearScreen();

        System.out.println("\n=======> Select coffee type <========\n");

        CoffeeType[] types = CoffeeType.values();

        for (int i = 0; i < types.length; i++) {
            System.out.printf("[%d] %s%n", i + 1, types[i]);
        }

        System.out.print("\nEnter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= types.length) {
                coffeeMachine.setCoffeeType(types[choice - 1]);

                System.out.println("\nCoffee type changed to: " + types[choice - 1]);
            } else {
                System.out.println("\nInvalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid number.");
        }
    }

    private void selectCupSize(CoffeeMachine coffeeMachine) {
        ScreenUtils.clearScreen();

        System.out.println("\n========> Select cup size <=========\n");

        CupSize[] sizes = CupSize.values();

        for (int i = 0; i < sizes.length; i++) {
            System.out.printf("[%d] %s%n", i + 1, sizes[i]);
        }

        System.out.print("\nEnter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= sizes.length) {
                coffeeMachine.setCupSize(sizes[choice - 1]);

                System.out.println("\nCup size changed to: " + sizes[choice - 1]);
            } else {
                System.out.println("\nInvalid choice!");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid number.");
        }

    }

    private void brewCoffee(CoffeeMachine coffeeMachine) {
        ScreenUtils.clearScreen();

        System.out.println("\n==========> Brew coffee <===========\n");

        System.out.printf("Current settings: %s (%s)%n",
                coffeeMachine.getCoffeeType(), coffeeMachine.getCupSize());

        System.out.printf("Water level: %d%%%n", coffeeMachine.getWaterLevel());

        System.out.print("\nStart brewing? (y/n): ");

        String input = scanner.nextLine().toLowerCase();

        if (input.equals("y")) {
            coffeeMachine.brewCoffee();

            if (coffeeMachine.isBrewing()) {
                System.out.println("\nStarted brewing coffee!");
            }
        }

    }

    private void refillWater(CoffeeMachine coffeeMachine) {
        ScreenUtils.clearScreen();

        System.out.println("\n==========> Refill water <==========\n");

        System.out.printf("Current water level: %d%%%n", coffeeMachine.getWaterLevel());

        System.out.print("\nRefill water to 100%? (y/n): ");

        String input = scanner.nextLine().toLowerCase();

        if (input.equals("y")) {
            coffeeMachine.refill();

            System.out.println("\nWater refilled to 100%");
        }
    }
}
