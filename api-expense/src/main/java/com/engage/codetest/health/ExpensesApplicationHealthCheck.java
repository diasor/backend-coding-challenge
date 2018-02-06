package com.engage.codetest.health;
import com.codahale.metrics.health.HealthCheck;
import com.engage.codetest.services.ExpenseService;

public class ExpensesApplicationHealthCheck extends HealthCheck{
    private static final String HEALTHY = "The Expenses Service is healthy use.";
    private static final String UNHEALTHY = "The Expenses Service is not healthy. ";
    private static final String MESSAGE_PLACEHOLDER = "{}";

    public ExpensesApplicationHealthCheck() {
    }

    @Override
    public Result check() throws Exception {

        String checkHealth = ExpenseService.performHealthCheck();

        if (checkHealth == null) {
            return Result.healthy(HEALTHY);
        } else {
            return Result.unhealthy(UNHEALTHY + MESSAGE_PLACEHOLDER, checkHealth);
        }
    }
}