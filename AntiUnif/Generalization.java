package AntiUnif;

import Terms.Substitution;
import Terms.Term;

//TODO: Finish this class

public class Generalization {
    private Term generalization;
    private Substitution left;
    private Substitution right;
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
