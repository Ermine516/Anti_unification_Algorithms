package com.aualgs.AntiUnif;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import com.aualgs.Terms.Term;
import com.aualgs.Terms.VariableFactory;

public abstract class AntiUnif implements Function<Configuration,HashSet<Configuration>> {
    protected ArrayList<Rule> rules;
    protected VariableFactory fresh;

    public  HashSet<Configuration> apply(Configuration config){
        HashSet<Configuration> result = new HashSet<>();
        for(Rule r: rules){
            Optional<Stream<Configuration>> newConfigSTM =r.apply(config,fresh); 
            if(newConfigSTM.isPresent()){
                newConfigSTM.get().forEach(c-> result.addAll(this.apply(c)));
                return result;
            }
        }
        result.add(config);
        return result;
    } 
    public abstract Object computeGeneralizations(HashSet<Configuration> finalConfigs, Term var);
    public abstract HashSet<Generalization> minimize(HashSet<Generalization> generalizations);

    
}
