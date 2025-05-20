package com.smarthome.cli;

import java.util.Scanner;

public class CLI {
    private final Scanner scanner;

    public CLI() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        char choice = ' ';

        while (choice != 'q') {
            displayMainMenu();

            choice = scanner.next().charAt(0);

            switch (choice) {
                case '1' -> manageHouses();
                case '2' -> manageRooms();

                default -> displayMainMenu();
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("---------> Smart Home CLI <---------\n");

        System.out.println("[1] Manage houses                     ");
        System.out.println("[2] Manage rooms                      ");
        System.out.println("[3] Manage devices                    ");
        System.out.println("[4] Manage rules                      ");
        System.out.println("[5] Display system status             ");
        System.out.println("[6] Simulate devices                  ");
        System.out.println("[q] Quit program                      ");

        System.out.print("Enter your choice: ");
    }

    private void manageHouses() {
        System.out.println("-");
    }

    private void manageRooms() {
        System.out.println("-");
    }

    private void manageDevices() {
        System.out.println("-");
    }
}
