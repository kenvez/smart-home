package com.smarthome.core;

import com.smarthome.cli.CLI;
import com.smarthome.core.model.house.House;

import java.util.*;

public class Main {
    public static Set<House> houses = new HashSet<>();

    public static void main(String[] args) {
        CLI cli = new CLI();
        cli.start();
    }
}
