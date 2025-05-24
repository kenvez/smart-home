package com.smarthome.core.model.rules;

import com.smarthome.core.model.devices.base.SmartDevice;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Rule<T extends SmartDevice, U extends SmartDevice> {
    private final String id;
    private final T conditionDevice;
    private final U actionDevice;
    private final Predicate<T> condition;
    private final Consumer<U> action;
    private String name;

    public Rule(String name, T conditionDevice, U actionDevice, Predicate<T> condition, Consumer<U> action) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.conditionDevice = conditionDevice;
        this.actionDevice = actionDevice;
        this.condition = condition;
        this.action = action;
    }

    public boolean checkCondition() {
        return this.condition.test(this.conditionDevice);
    }

    public boolean execute() {
        if (checkCondition()) {
            action.accept(this.actionDevice);
            return true;
        }

        return false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Rule name cannot be null or empty");
        }

        this.name = name;
    }

    public T getConditionDevice() {
        return this.conditionDevice;
    }

    public U getActionDevice() {
        return this.actionDevice;
    }

    @Override
    public String toString() {
        return "Rule: " + name + " (if condition on " + conditionDevice.getName() + ", then action on " + actionDevice.getName() + ")";
    }
}
