package com.aualgs.Algorithms.Syntactic;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BinaryOperator;

import com.aualgs.AntiUnif.Aut;
import com.aualgs.AntiUnif.Configuration;
import com.aualgs.Terms.Binding;
import com.aualgs.Terms.Substitution;


public class SynConfig extends Configuration {
    private Substitution generalization;

    public SynConfig() {
        super();
        generalization = new Substitution();
    }
    public Substitution generalization(){return generalization;}
    
    public void compose(Binding sub){
        generalization.compose(sub);          
    }

    @Override
    public String toString(){
        BinaryOperator<String> cat= (acc,ele) -> acc+"\t"+ele+"\n";
        String  res = active.stream().map(aut-> aut.toString()).reduce("Active:\n", cat)+"Solved:\n";
        res = solved.stream().map(aut-> aut.toString()).reduce(res, cat)+"Generalization:\n";
        return res+"\t"+generalization.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SynConfig result = new SynConfig();
        active.stream().forEach(aut ->{
            try{
                result.active.add((Aut)aut.clone());
            }catch(CloneNotSupportedException e){}
        });
        solved.stream().forEach(aut ->{
            try{
                result.solved.add((Aut)aut.clone());
            }catch(CloneNotSupportedException e){}
        });
        try{
            result.generalization = (Substitution) generalization.clone();
        }catch(CloneNotSupportedException e){} 
        mergeSets.keySet().stream().forEach(aut ->{
            try{
                HashSet<Aut> autMer = new HashSet<>();
                mergeSets.get(aut).stream().forEach(a->{
                    try{
                        autMer.add((Aut)a.clone());
                    }catch(CloneNotSupportedException e){}
                });
                result.mergeSets.put((Aut)aut.clone(),autMer);
            }catch(CloneNotSupportedException e){}
        }); new HashMap<Aut,HashSet<Aut>>();
        return result;
    }
}