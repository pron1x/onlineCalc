package com.calc.onlinecalc;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Equation {

    @Id
    @GeneratedValue
    private Long id;
    private String equation;
    private double result;

    protected Equation() {}

    public Equation(String equation, double result) {
        this.equation = equation;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public String getEquation() {
        return equation;
    }

    public double getResult() {
        return result;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public String getResolvedString() {
        if(result % 1 == 0) {
            return String.format("%s = %d", equation, (int)result);
        } else {
            return String.format("%s = %f", equation, result);
        }
    }

    @Override
    public String toString() {
        return String.format("Equation[id=%d, equation=%s, result=%f]", id, equation, result);
    }
}
