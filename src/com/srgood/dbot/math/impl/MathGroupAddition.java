package com.srgood.dbot.math.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.regex.MatchResult;

import com.srgood.dbot.math.api.IMathGroup;

public class MathGroupAddition implements IMathGroup {
    
    IMathGroup[] components;

    public MathGroupAddition(IMathGroup... exps) {
        components = exps;
    }
    
    public MathGroupAddition(Collection<IMathGroup> exps) {
        this(exps.toArray(new IMathGroup[0]));
    }
    
    public BigDecimal eval() {
        BigDecimal ret = BigDecimal.ZERO;
        
        for (IMathGroup x : components) {
            ret = ret.add(x.eval());
        }
        
        return ret;
    }

}
