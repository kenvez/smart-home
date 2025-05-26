package com.smarthome.cli.utils;

import com.smarthome.core.Main;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.house.House;
import com.smarthome.core.model.room.Room;

import java.util.*;

public class MenuUtils {
    public static void displayMainMenu() {
        System.out.println("\n=========> Smart home CLI <=========\n");

        System.out.println("[1] Manage houses                       ");
        System.out.println("[2] Manage rooms                        ");
        System.out.println("[3] Manage devices                      ");
        System.out.println("[4] Manage sensors                     ");
        System.out.println("[5] Manage rules                        ");
        System.out.println("[6] System status                       ");
        System.out.println("[7] Simulate devices                    ");
        System.out.println("[q] Quit program                      \n");

        System.out.print("Enter your choice: ");
    }

    public static void displayHousesList(Set<House> houses) {
        List<House> housesList = new ArrayList<>(houses);

        if (houses.isEmpty()) {
            System.out.println("No houses found! Please add a house first.");
            return;
        }

        int index = 1;

        for (House house : housesList) {
            System.out.printf("[%d] %s (%.6f, %.6f) (Type: %s)%n",
                    index++,
                    house.getName(),
                    house.getLatitude(),
                    house.getLongitude(),
                    house.getType()
            );
        }
    }

    public static List<Room> displayRoomsList(Set<Room> rooms) {
        List<Room> roomsList = new ArrayList<>(rooms);

        if (rooms.isEmpty()) {
            System.out.println("No rooms found in the selected house!");

            return roomsList;
        }

        int index = 1;
        for (Room room : roomsList) {
            System.out.printf("[%d] %s (Type: %s)%n",
                    index++,
                    room.getName(),
                    room.getType()
            );
        }

        return roomsList;
    }

    public static List<SmartDevice> displayDevicesList(Set<SmartDevice> devices) {
        List<SmartDevice> devicesList = new ArrayList<>(devices);

        if (devices.isEmpty()) {
            System.out.println("No devices found in the selected house!");

            return devicesList;
        }

        int index = 1;

        for (SmartDevice device : devicesList) {
            System.out.printf("[%d] %s (Status: %s)%n",
                    index++,
                    device.getName(),
                    device.getStatus()
            );
        }

        return devicesList;
    }

    public static House selectHouse(Scanner scanner) {
        List<House> houses = new ArrayList<>(Main.houses);

        ScreenUtils.clearScreen();

        System.out.println("\n==========> Select house <==========\n");

        // TODO implement exception

        if (Main.houses.isEmpty()) {
            System.out.println("No houses found! Please add a house first.");

            return null;
        }

        int index = 1;

        for (House house : houses) {
            System.out.printf("[%d] %s (%.6f, %.6f) (Type: %s)%n",
                    index++,
                    house.getName(),
                    house.getLatitude(),
                    house.getLongitude(),
                    house.getType()
            );
        }

        System.out.print("\nEnter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= houses.size()) {
                House selectedHouse = houses.get(choice - 1);

                System.out.println("Selected house: " + selectedHouse.getName());

                return selectedHouse;
            } else {
                System.out.println("\nInvalid house number!");

                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid number.");

            return null;
        }
    }

    public static Room selectRoom(Scanner scanner, House selectedHouse) {
        List<Room> rooms = new ArrayList<>(selectedHouse.getRooms());

        ScreenUtils.clearScreen();

        System.out.println("\n==========> Select room <===========\n");

        if (rooms.isEmpty()) {
            System.out.println("No rooms found! Please add a room first.");

            return null;
        }

        int index = 1;

        for (Room room : rooms) {
            System.out.printf("[%d] %s (Type: %s)%n",
                    index++,
                    room.getName(),
                    room.getType()
            );
        }

        System.out.print("\nEnter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= rooms.size()) {
                Room selectedRoom = rooms.get(choice - 1);

                System.out.println("Selected room: " + selectedRoom.getName());

                return selectedRoom;
            } else {
                System.out.println("\nInvalid room number!");

                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid number.");

            return null;
        }
    }

    public static SmartDevice selectDevice(Scanner scanner, Room selectedRoom) {
        List<SmartDevice> devices = new ArrayList<>(selectedRoom.getDevices());

        ScreenUtils.clearScreen();

        System.out.println("\n=========> Select device <==========\n");

        if (devices.isEmpty()) {
            System.out.println("No devices found! Please add a device first.");

            return null;
        }

        int index = 1;

        for (SmartDevice device : devices) {
            System.out.printf("[%d] %s (Status: %s)%n",
                    index++,
                    device.getName(),
                    device.getStatus()
            );
        }

        System.out.print("\nEnter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 && choice <= devices.size()) {
                SmartDevice selectedDevice = devices.get(choice - 1);

                System.out.println("Selected device: " + selectedDevice.getName());

                return selectedDevice;
            } else {
                System.out.println("\nInvalid device number!");

                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("\nPlease enter a valid number.");

            return null;
        }
    }

    public static SelectionResult performSelection(Scanner scanner, boolean needDevice) {
        House selectedHouse = selectHouse(scanner);

        if (selectedHouse == null) {
            return null;
        }

        Room selectedRoom = selectRoom(scanner, selectedHouse);

        if (selectedRoom == null) {
            return null;
        }

        SmartDevice selectedDevice = null;

        if (needDevice) {
            selectedDevice = selectDevice(scanner, selectedRoom);
            if (selectedDevice == null) {
                return null;
            }
        }

        return new SelectionResult(selectedRoom, selectedDevice);
    }
}
