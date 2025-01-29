package com.aualgs.AntiUnif;

import com.aualgs.Terms.Substitution;
import com.aualgs.Terms.Term;

//TODO: Finish this class

public class Generalization {
    private final Term generalization;
    private final Substitution left;
    private final Substitution right;
    public Generalization(Term gen, Substitution subLeft, Substitution subRight) {
        generalization = gen;
        left = subLeft;
        right = subRight;
    }
    public Term generalization(){
        return generalization;
    }
    @Override
    public String toString() {
        return "[generalization=" + generalization + ", left=" + left + ", right=" + right + "]";
    }
    
}
