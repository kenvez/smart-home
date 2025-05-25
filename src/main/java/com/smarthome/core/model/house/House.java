package com.smarthome.core.model.house;

import com.smarthome.core.model.room.Room;

import java.util.*;

public class House {
    private String name;
    private double latitude;
    private double longitude;
    private HouseType type;
    private final Set<Room> rooms;

    public House(String name, double latitude, double longitude, HouseType type) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.rooms = new HashSet<>();
    }

    public String getName() {
        return this.name;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public HouseType getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setType(HouseType type) {
        this.type = type;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Set<Room> getRooms() {
        return this.rooms;
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    public void removeRoom(Room room) {
        this.rooms.remove(room);
    }
}
