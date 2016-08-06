package com.srgood.dbot.math.impl;

import java.math.BigDecimal;
import java.util.Collection;

import com.srgood.dbot.math.api.IMathGroup;

public class MathGroupDivision implements IMathGroup {

    IMathGroup[] components;

    public MathGroupDivision(IMathGroup[] exps) {
        components = exps;
    }
    
    public MathGroupDivision(Collection<IMathGroup> exps) {
        this(exps.toArray(new IMathGroup[0]));
    }

    public BigDecimal eval() {
        BigDecimal ret = components[0].eval();
        
        for (int x = 1; x < components.length; x++) {
            ret = ret.divide(components[x].eval());
        }
        
        return ret;
    }

}
