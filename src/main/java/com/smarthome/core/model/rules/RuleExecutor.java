package com.smarthome.core.model.rules;

import com.smarthome.core.logging.EventLogger;
import com.smarthome.core.model.devices.base.SmartDevice;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import com.smarthome.core.logging.EventType;

public class RuleExecutor {
    public static int executeRulesForDevice(SmartDevice device) {
        if (device == null) {
            return 0;
        }

        Set<Rule<?, ?>> rules = device.getRules();
        int executedRules = 0;

        for (Rule<?, ?> rule : rules) {
            if (evaluateAndExecuteRule(rule)) {
                executedRules++;
            }
        }

        return executedRules;
    }

    private static <T extends SmartDevice, U extends SmartDevice> boolean evaluateAndExecuteRule(Rule<T, U> rule) {
        try {
            Predicate<T> condition = rule.getCondition();
            T conditionDevice = rule.getConditionDevice();

            if (condition.test(conditionDevice)) {
                Consumer<U> action = rule.getAction();
                U actionDevice = rule.getActionDevice();

                action.accept(actionDevice);

                EventLogger.getInstance().logRuleEvent(
                        rule.toString(),
                        "CONDITIONAL",
                        conditionDevice.getParentRoom().getName(),
                        EventType.RULE_EXECUTED,
                        "Executed rule: " + rule
                );

                return true;
            }
        } catch (Exception e) {
            EventLogger.getInstance().logRuleEvent(
                    rule.toString(),
                    "CONDITIONAL",
                    "Unknown",
                    EventType.ERROR,
                    "Error executing rule: " + e.getMessage()
            );
        }

        return false;
    }
}
