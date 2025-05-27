package com.smarthome.cli.settings;

import com.smarthome.cli.utils.MenuUtils;
import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.cli.utils.SelectionResult;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.devices.impl.*;
import com.smarthome.core.model.room.Room;


import java.util.List;
import java.util.Scanner;

public class DeviceSettings {
    private final Scanner scanner;
    private final LightbulbSettings lightbulbSettings;
    private final OutletSettings outletSettings;
    private final CoffeeMachineSettings coffeeMachineSettings;
    private final CurtainSettings curtainSettings;
    private final DoorLockSettings doorLockSettings;
    private final TemperatureSensorSettings temperatureSensorSettings;

    public DeviceSettings(Scanner scanner) {
        this.scanner = scanner;
        this.lightbulbSettings = new LightbulbSettings(scanner, this);
        this.outletSettings = new OutletSettings(scanner, this);
        this.coffeeMachineSettings = new CoffeeMachineSettings(scanner, this);
        this.curtainSettings = new CurtainSettings(scanner, this);
        this.doorLockSettings = new DoorLockSettings(scanner, this);
        this.temperatureSensorSettings = new TemperatureSensorSettings(scanner, this);
    }

    public void start() {
        ScreenUtils.clearScreen();

        System.out.println("\n========> Device settings <=========\n");

        System.out.println("[1] Individual device                   ");
        System.out.println("[2] Group control                       ");
        System.out.println("[b] Back to device menu\n");

        System.out.print("Enter your choice: ");

        char choice = scanner.next().charAt(0);

        scanner.nextLine();

        switch (choice) {
            case '1' -> individualDevice();
            case '2' -> groupControl();
            case 'b' -> System.out.println("\nGoing back to main menu.");
            default -> System.out.println("Invalid choice!");
        }
    }

    private void individualDevice() {
        SelectionResult selection = MenuUtils.performSelection(scanner, true);

        if (selection == null) {
            return;
        }

        SmartDevice selectedDevice = selection.device();

        switch (selectedDevice) {
            case Lightbulb lightbulb -> lightbulbSettings.start(lightbulb);
            case Outlet outlet -> outletSettings.start(outlet);
            case CoffeeMachine coffeeMachine -> coffeeMachineSettings.start(coffeeMachine);
            case Curtain curtain -> curtainSettings.start(curtain);
            case DoorLock doorLock -> doorLockSettings.start(doorLock);
            case TemperatureSensor temperatureSensor -> temperatureSensorSettings.start(temperatureSensor);
            default -> System.out.println("Invalid device type!");

        }
    }

    public void groupControl() {
        ScreenUtils.clearScreen();

        System.out.println("\n=========> Group control <==========\n");

        SelectionResult selection = MenuUtils.performSelection(scanner, false);

        if (selection == null) {
            return;
        }

        Room selectedRoom = selection.room();

        if (selectedRoom.getDevices().isEmpty()) {
            System.out.println("No devices found in the selected room!");

            return;
        }

        ScreenUtils.clearScreen();

        System.out.println("\n=========> Group control <==========\n");

        System.out.println("Room: " + selectedRoom.getName() + "\n");

        System.out.println("[1] Turn ON all devices");
        System.out.println("[2] Turn OFF all devices");
        System.out.println("[3] Control all lightbulbs");
        System.out.println("[4] Control all curtains");
        System.out.println("[b] Back to device settings\n");

        System.out.print("Enter your choice: ");

        char choice = scanner.next().charAt(0);

        scanner.nextLine();

        int affectedDevices;

        switch (choice) {
            case '1' -> {
                affectedDevices = GroupControl.turnOnAllDevices(selectedRoom);

                System.out.println("\nTurned ON " + affectedDevices + " devices in " + selectedRoom.getName());
            }
            case '2' -> {
                affectedDevices = GroupControl.turnOffAllDevices(selectedRoom);

                System.out.println("\nTurned OFF " + affectedDevices + " devices in " + selectedRoom.getName());
            }
            case '3' -> controlAllLightbulbs(selectedRoom);
            case '4' -> controlAllCurtains(selectedRoom);
            case 'b' -> System.out.println("\nGoing back to main menu.");
            default -> System.out.println("\nInvalid choice!");
        }
    }

    private void controlAllLightbulbs(Room room) {
        ScreenUtils.clearScreen();

        System.out.println("\n====> Lightbulb group control <=====\n");

        List<Lightbulb> lightbulbs = GroupControl.getDevicesOfType(room, Lightbulb.class);

        if (lightbulbs.isEmpty()) {
            System.out.println("No lightbulbs found in this room!");
            return;
        }

        System.out.println("Lightbulbs in " + room.getName() + ": " + lightbulbs.size());

        System.out.println("\n[1] Turn ON all lightbulbs");
        System.out.println("[2] Turn OFF all lightbulbs");
        System.out.println("[3] Set brightness for all lightbulbs");
        System.out.println("[4] Set color for all lightbulbs");
        System.out.println("[b] Back to group control\n");

        System.out.print("Enter your choice: ");

        char choice = scanner.next().charAt(0);

        scanner.nextLine();

        int affectedDevices;

        switch (choice) {
            case '1' -> {
                affectedDevices = GroupControl.applyToDevices(
                        room,
                        device -> device instanceof Lightbulb,
                        device -> {
                            if (!device.isOn()) {
                                device.turnOn();
                            }
                        },
                        "Turned ON"
                );
                System.out.println("\nTurned ON " + affectedDevices + " lightbulbs");
            }
            case '2' -> {
                affectedDevices = GroupControl.applyToDevices(
                        room,
                        device -> device instanceof Lightbulb,
                        device -> {
                            if (device.isOn()) {
                                device.turnOff();
                            }
                        },
                        "Turned OFF"
                );
                System.out.println("\nTurned OFF " + affectedDevices + " lightbulbs");
            }
            case '3' -> {
                System.out.print("\nEnter brightness (0-100%): ");
                try {
                    int brightness = Integer.parseInt(scanner.nextLine());

                    if (brightness < 0 || brightness > 100) {
                        System.out.println("Brightness must be between 0 and 100%");

                        return;
                    }

                    affectedDevices = GroupControl.setAllLightbulbs(room, brightness / 100.0);

                    System.out.println("\nSet brightness to " + brightness + "% for " + affectedDevices + " lightbulbs");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
            case '4' -> {
                System.out.print("\nEnter hue (0-359): ");

                try {
                    double hue = Double.parseDouble(scanner.nextLine());

                    if (hue < 0 || hue >= 360) {
                        System.out.println("Hue must be between 0 and 359");

                        return;
                    }

                    affectedDevices = GroupControl.applyToDevices(
                            room,
                            device -> device instanceof Lightbulb && device.isOn(),
                            device -> ((Lightbulb) device).setHue(hue),
                            "Set hue to " + hue
                    );
                    System.out.println("\nSet hue to " + hue + " for " + affectedDevices + " lightbulbs");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
            case 'b' -> System.out.println("\nGoing back to main menu.");
            default -> System.out.println("\nInvalid choice!");
        }
    }

    private void controlAllCurtains(Room room) {
        ScreenUtils.clearScreen();

        System.out.println("\n=====> Curtain group control <======\n");

        List<Curtain> curtains = GroupControl.getDevicesOfType(room, Curtain.class);

        if (curtains.isEmpty()) {
            System.out.println("No curtains found in this room!");
            return;
        }

        System.out.println("Curtains in " + room.getName() + ": " + curtains.size());

        System.out.println("\n[1] Turn ON all curtains");
        System.out.println("[2] Turn OFF all curtains");
        System.out.println("[3] Open all curtains fully");
        System.out.println("[4] Close all curtains fully");
        System.out.println("[5] Set openness percentage for all curtains");
        System.out.println("[b] Back to group control\n");

        System.out.print("Enter your choice: ");

        char choice = scanner.next().charAt(0);

        scanner.nextLine();

        int affectedDevices;

        switch (choice) {
            case '1' -> {
                affectedDevices = GroupControl.applyToDevices(
                        room,
                        device -> device instanceof Curtain,
                        device -> {
                            if (!device.isOn()) {
                                device.turnOn();
                            }
                        },
                        "Turned ON"
                );
                System.out.println("\nTurned ON " + affectedDevices + " curtains");
            }
            case '2' -> {
                affectedDevices = GroupControl.applyToDevices(
                        room,
                        device -> device instanceof Curtain,
                        device -> {
                            if (device.isOn()) {
                                device.turnOff();
                            }
                        },
                        "Turned OFF"
                );

                System.out.println("\nTurned OFF " + affectedDevices + " curtains");
            }
            case '3' -> {
                affectedDevices = GroupControl.applyToDevices(
                        room,
                        device -> device instanceof Curtain && device.isOn(),
                        device -> ((Curtain) device).openFully(),
                        "Opened fully"
                );
                System.out.println("\nOpened " + affectedDevices + " curtains fully");
            }
            case '4' -> {
                affectedDevices = GroupControl.applyToDevices(
                        room,
                        device -> device instanceof Curtain && device.isOn(),
                        device -> ((Curtain) device).closeFully(),
                        "Closed fully"
                );
                System.out.println("\nClosed " + affectedDevices + " curtains fully");
            }
            case '5' -> {
                System.out.print("\nEnter openness percentage (0-100%): ");
                try {
                    int percentage = Integer.parseInt(scanner.nextLine());

                    affectedDevices = GroupControl.setAllCurtains(room, percentage);

                    System.out.println("\nSet openness to " + percentage + "% for " + affectedDevices + " curtains");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
            case 'b' -> System.out.println("\nGoing back to main menu.");
            default -> System.out.println("\nInvalid choice!");
        }
    }


    public void changeName(SmartDevice device) {
        ScreenUtils.clearScreen();

        System.out.println("\n=======> Change device name <=======\n");

        System.out.print("Enter new device name: ");

        String newName = scanner.nextLine();

        device.setName(newName);

        System.out.printf("\n%s name changed successfully!\n",
                device.getName());
    }

    public void toggleDevice(SmartDevice device) {
        ScreenUtils.clearScreen();

        System.out.println("\n=========> Toggle device <==========");

        if (device.isOn()) {
            device.turnOff();
            System.out.println("\nDevice turned OFF!");
        } else {
            device.turnOn();
            System.out.println("\nDevice turned ON!");
        }
    }
}
