package com.srgood.dbot.math.impl;

import java.math.BigDecimal;

import com.srgood.dbot.math.api.IMathGroup;

public class MathGroupBasic implements IMathGroup {
    
    private BigDecimal value;

    public MathGroupBasic(BigDecimal value) {
        this.value = value;
    }

    @Override
    public BigDecimal eval() {
        return value;
    }

}
