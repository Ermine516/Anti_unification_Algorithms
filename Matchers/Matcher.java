package Matchers;


import Terms.Term;

public interface Matcher {

public boolean match(Term term, Term gnd);

    
}