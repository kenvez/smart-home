package com.smarthome.cli.utils;

import java.util.Scanner;

public class ScreenUtils {
    public static void clearScreen() {
        try {
            String operatingSystem = System.getProperty("os.name").toLowerCase();

            if (operatingSystem.contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("\n".repeat(50));
        }
    }

    public static void pressEnterToContinue(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
