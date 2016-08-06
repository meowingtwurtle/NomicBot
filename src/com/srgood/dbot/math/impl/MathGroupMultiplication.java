package com.srgood.dbot.math.impl;

import java.math.BigDecimal;
import java.util.Collection;

import com.srgood.dbot.math.api.IMathGroup;

public class MathGroupMultiplication  implements IMathGroup{
    
    IMathGroup[] components;

    public MathGroupMultiplication(IMathGroup... exps) {
        components = exps;
    }
    
    public MathGroupMultiplication(Collection<IMathGroup> exps) {
        this(exps.toArray(new IMathGroup[0]));
    }
    
    public BigDecimal eval() {
        BigDecimal ret = BigDecimal.ZERO;
        
        for (IMathGroup x : components) {
            ret = ret.multiply(x.eval());
        }
        
        return ret;
    }

}
