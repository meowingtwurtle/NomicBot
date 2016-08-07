package com.srgood.dbot.math.impl;

import java.math.BigDecimal;
import java.util.Arrays;

import com.srgood.dbot.math.api.IMathGroup;
import com.srgood.dbot.math.api.IMathHandler;

public enum MathHandler implements IMathHandler {

    INSTANCE;

    public IMathGroup parse(String exp) {
        System.out.println("parse called for: " + exp);

        exp = exp.trim().replaceAll("\\s+", "");

        if (!(exp.contains("+") || exp.contains("-") || exp.contains("*") || exp.contains("/"))) {
            return new MathGroupBasic(new BigDecimal(exp));
        }

        if (!exp.contains("(") && !exp.contains(")")) {
            return parseNoParens(exp);
        } else {
            return parseWithParens(exp);
        }
    }

    private IMathGroup parseWithParens(String exp) {
        return null;
    }

    private IMathGroup parseNoParens(String exp) {
        // a*b*c/d+c
        // AddGroup(MathGroupMultiply(new MathGroupBasic(a), "b", "c/d"), "")

        try {

            String[] topLevelComponents;

            int mode = 0;

            if (exp.contains("+")) {
                System.out.println("Splitting on +");
                topLevelComponents = exp.split("\\+");
                mode = 0;
            } else if (exp.contains("-")) {
                System.out.println("Splitting on -");
                topLevelComponents = exp.split("-");
                mode = 1;
            } else if (exp.contains("*")) {
                System.out.println("Splitting on *");
                topLevelComponents = exp.split("*");
                mode = 2;
            } else if (exp.contains("/")) {
                System.out.println("Splitting on /");
                topLevelComponents = exp.split("/");
                mode = 3;
            } else {
                topLevelComponents = new String[] { exp };
                mode = 4;
            }

            IMathGroup[] retParams = new IMathGroup[topLevelComponents.length];

            for (int i = 0; i < topLevelComponents.length; i++) {
                System.out.println("Parsing sub-component");
                retParams[i] = parse(topLevelComponents[i]);
            }

            System.out.println("retParams: " + Arrays.deepToString(retParams));

            IMathGroup ret;

            if (mode == 0) {
                System.out.println("Returning Addition");
                ret = new MathGroupAddition(retParams);
            } else if (mode == 1) {
                System.out.println("Returning Subtraction");
                ret = new MathGroupSubtraction(retParams);
            } else if (mode == 2) {
                System.out.println("Returning Multiplication");
                ret = new MathGroupMultiplication(retParams);
            } else if (mode == 3) {
                System.out.println("Returning Division");
                ret = new MathGroupDivision(retParams);
            } else {
                System.out.println("Returning Basic");
                ret = new MathGroupBasic(retParams[0]);
            }

            System.out.println("Returning: " + ret);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
