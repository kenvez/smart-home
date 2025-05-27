package com.smarthome.core.model.rules;

import com.smarthome.core.model.devices.base.SmartDevice;

import java.util.function.Consumer;
import java.util.function.Predicate;


public class Rule<T extends SmartDevice, U extends SmartDevice> {
    private final T conditionDevice;
    private final Predicate<T> condition;
    private final U actionDevice;
    private final Consumer<U> action;
    private final String description;


    public Rule(T conditionDevice, Predicate<T> condition, U actionDevice,
                Consumer<U> action, String description) {
        this.conditionDevice = conditionDevice;
        this.condition = condition;
        this.actionDevice = actionDevice;
        this.action = action;
        this.description = description;
    }

    public Rule(T conditionDevice, Predicate<T> condition, U actionDevice,
                Consumer<U> action) {
        this.conditionDevice = conditionDevice;
        this.condition = condition;
        this.actionDevice = actionDevice;
        this.action = action;
        this.description = "";
    }

    @Override
    public String toString() {
        return description.isEmpty()
                ? "If condition on " + conditionDevice.getName() + " then action on " + actionDevice.getName()
                : description;
    }

    public Consumer<U> getAction() {
        return action;
    }

    public Predicate<T> getCondition() {
        return condition;
    }

    public T getConditionDevice() {
        return conditionDevice;
    }

    public U getActionDevice() {
        return actionDevice;
    }
}
