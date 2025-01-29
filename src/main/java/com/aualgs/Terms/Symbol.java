package com.aualgs.Terms;
import java.util.ArrayList;
import java.util.function.Function;

import com.aualgs.AUExceptions.InvalidSymbolException;


public class Symbol implements Function<ArrayList<Term>,Term>, Cloneable {
   

    private int Arity=0;
    private String Name="";
    private boolean Variable = false;
    public Symbol(int arity,String name) throws InvalidSymbolException{
        this.Arity=arity;
        this.Name=name;
        this.Variable = false;
    }
    public Symbol(int arity,String name, boolean var) throws InvalidSymbolException {
        this.Arity=arity;
        this.Name=name;
        this.Variable = var;
        if(var && arity>0){
            String myStr = "A symbol with arity %d cannot  be a variable!";
            String errmsg = String.format(myStr, arity);
            throw new InvalidSymbolException(errmsg);
        }
    }
    public int arity(){
        return Arity;
    }
    public String name(){
        return Name;
    }
    public boolean isVar(){
        return Variable;
    }
    public Term apply(ArrayList<Term> Args) {
        if (this.Arity != Args.size()) return null;
        try{return new Term(this, Args);}catch (Exception e) {return null;}    
    }
    @Override
    public String toString() {
        return String.format("Symbol(%s,%d)", this.Name, this.Arity);
    }
    @Override
    public int  hashCode(){
        return Name.hashCode();
    }
    @Override
    public boolean equals(Object other) {
        Symbol sym = (Symbol) other;
        return this.Name.equals(sym.name());
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        try{
            return new Symbol(this.arity(), this.name(), this.isVar()); 
        }catch(InvalidSymbolException e){
            return null;
        }
    }
}