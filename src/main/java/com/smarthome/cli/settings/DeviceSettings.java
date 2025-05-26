package com.smarthome.cli.settings;

import com.smarthome.cli.utils.MenuUtils;
import com.smarthome.cli.utils.ScreenUtils;
import com.smarthome.cli.utils.SelectionResult;
import com.smarthome.core.model.devices.base.SmartDevice;
import com.smarthome.core.model.devices.impl.CoffeeMachine;
import com.smarthome.core.model.devices.impl.Lightbulb;
import com.smarthome.core.model.devices.impl.Outlet;

import java.util.Scanner;

public class DeviceSettings {
    private final Scanner scanner;
    private final LightbulbSettings lightbulbSettings;
    private final OutletSettings outletSettings;
    private final CoffeeMachineSettings coffeeMachineSettings;

    public DeviceSettings(Scanner scanner) {
        this.scanner = scanner;
        this.lightbulbSettings = new LightbulbSettings(scanner, this);
        this.outletSettings = new OutletSettings(scanner, this);
        this.coffeeMachineSettings = new CoffeeMachineSettings(scanner, this);
    }

    public void start() {
        SelectionResult selection = MenuUtils.performSelection(scanner, true);

        if (selection == null) {
            return;
        }

        SmartDevice selectedDevice = selection.getDevice();

        switch (selectedDevice) {
            case Lightbulb lightbulb -> lightbulbSettings.start(lightbulb);
            case Outlet outlet -> outletSettings.start(outlet);
            case CoffeeMachine coffeeMachine -> coffeeMachineSettings.start(coffeeMachine);
            default -> System.out.println("Invalid device type!");

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
            System.out.println("\nDevice turned off!");
        } else {
            device.turnOn();
            System.out.println("\nDevice turned on!");
        }
    }
}
