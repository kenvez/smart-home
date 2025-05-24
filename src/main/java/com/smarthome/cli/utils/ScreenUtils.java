package com.smarthome.cli.utils;

import com.smarthome.cli.menu.DeviceManager;
import com.smarthome.cli.menu.HouseManager;
import com.smarthome.cli.menu.RoomManager;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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

    public static Room selectRoom(Scanner scanner, House house) {
        ScreenUtils.clearScreen();

        System.out.println("\n==========> Select room <===========\n");

        Set<Room> rooms = house.getRooms();

        if (rooms.isEmpty()) {
            System.out.println("No rooms found! Please add a room first.");

            ScreenUtils.pressEnterToContinue(scanner);

            return null;
        }

        List<Room> roomsList = new ArrayList<>(rooms);
        int index = 1;

        for (Room room : roomsList) {
            System.out.printf("[%d] %s (Type: %s)%n",
                    index++,
                    room.getName(),
                    room.getType()
            );
        }

        System.out.print("\nEnter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= roomsList.size()) {
                Room selectedRoom = roomsList.get(choice - 1);

                System.out.println("Selected room: " + selectedRoom.getName());

                return selectedRoom;
            } else {
                System.out.println("\nInvalid room number!");

                ScreenUtils.pressEnterToContinue(scanner);

                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid number.");

            ScreenUtils.pressEnterToContinue(scanner);

            return null;
        }
    }

    public static SmartDevice selectDevice(Scanner scanner, Room room) {
        ScreenUtils.clearScreen();

        System.out.println("\n==========> Select room <===========\n");

        Set<SmartDevice> devices = room.getDevices();

        if (devices.isEmpty()) {
            System.out.println("No rooms found! Please add a room first.");

            ScreenUtils.pressEnterToContinue(scanner);

            return null;
        }

        List<SmartDevice> devicesList = new ArrayList<>(devices);
        int index = 1;

        for (SmartDevice device : devicesList) {
            System.out.printf("[%d] %s (Status: %s)%n",
                    index++,
                    device.getName(),
                    device.getStatus()
            );
        }

        System.out.print("\nEnter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= devicesList.size()) {
                SmartDevice selectedDevice = devicesList.get(choice - 1);

                System.out.println("Selected device: " + selectedDevice.getName());

                return selectedDevice;
            } else {
                System.out.println("\nInvalid device number!");

                ScreenUtils.pressEnterToContinue(scanner);

                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid number.");

            ScreenUtils.pressEnterToContinue(scanner);

            return null;
        }
    }
}
