package com.aualgs.Algorithms.Syntactic;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import com.aualgs.AUExceptions.InvalidAppException;
import com.aualgs.AUExceptions.NonGroundException;
import com.aualgs.AntiUnif.AntiUnif;
import com.aualgs.AntiUnif.Aut;
import com.aualgs.AntiUnif.Configuration;
import com.aualgs.AntiUnif.Generalization;
import com.aualgs.AntiUnif.Rule;
import com.aualgs.Terms.Binding;
import com.aualgs.Terms.Substitution;
import com.aualgs.Terms.Term;
import com.aualgs.Terms.VariableFactory;

public  class SynAU extends AntiUnif {
    public SynAU(String var){
        fresh = new VariableFactory(var);
        Rule[] temp = {Decom,Sol,Mer};
        rules = new ArrayList<>();
        for (Rule r : temp) rules.add(r);
    }
    protected static Rule Decom = (cf,variables) ->{
        SynConfig config = (SynConfig) cf;
        Predicate<Aut> canApp = aut -> aut.left().head().equals(aut.right().head());
        Optional<Aut>  targetOpt = config.active().stream().filter(canApp).findFirst();
        Builder<Configuration> result =Stream.builder();
        result.add(config);
        if(!targetOpt.isPresent()) return  Optional.empty();
        Aut target = targetOpt.get();
        config.active().remove(target);
        Stream<Term> forLabels =target.left().args().stream();
        try (Stream<Term> argsLabelsSTM = forLabels.map( _ -> variables.get())) {
            ArrayList<Term> argsLabels = (ArrayList<Term>) argsLabelsSTM.collect(Collectors.toList());
            HashSet<Aut> autResult= new HashSet<>();
            IntConsumer autConstruct = i-> { try {
                autResult.add(new Aut(target.left().args().get(i),target.right().args().get(i),argsLabels.get(i)));
                } catch (NonGroundException e) {}};
            IntStream.range(0, argsLabels.size()).forEach(autConstruct);
            config.active().addAll(autResult);
            try {
                Term genterm = new Term(target.left().head(),argsLabels);
                config.compose(new Binding(target.label(), genterm));
            } catch (InvalidAppException e) {return null;}
        }
        return  Optional.of(result.build());
    };
    protected static Rule Sol = (cf,_) ->{
        SynConfig config = (SynConfig) cf;
        Predicate<Aut> canApp = aut -> !aut.left().head().equals(aut.right().head());
        Optional<Aut>  targetOpt = config.active().stream().filter(canApp).findFirst();
        Builder<Configuration> result =Stream.builder();
        result.add(config);
        if(!targetOpt.isPresent()) return  Optional.empty();
        config.addSolve(targetOpt.get());
        return Optional.of(result.build());
    };
    protected static Rule Mer = (cf,_) ->{
        SynConfig config = (SynConfig) cf;
        Builder<Configuration> result =Stream.builder();
        result.add(config);
        if(!config.active().isEmpty()) Optional.of(result.build());
        for(Aut a: config.mergeSets().keySet()){
            config.mergeSets().get(a).stream().forEach(aut -> {
                config.solved().remove(aut);
                config.compose(new Binding(aut.label(),a.label()));
            });
            config.mergeSets().put(a,new HashSet<>());
        }
        return Optional.empty();
    };
    @Override
    public HashSet<Generalization> computeGeneralizations(HashSet<Configuration> finalConfigs,Term var) {
        HashSet<Generalization> results = new HashSet<>();
        finalConfigs.stream().forEach( cf ->{
            SynConfig config = (SynConfig) cf;
            Term gen = config.generalization().apply(var);
            HashSet<Term> genVars = gen.vars();
            Stream<Aut> solvedAut = config.solved().stream().filter(aut-> genVars.contains(aut.label()));
            Substitution subLeft = new Substitution();
            Substitution subRight = new Substitution();
            solvedAut.forEach(aut ->{
                try{
                    subLeft.union(new Binding((Term)aut.label().clone(),(Term)aut.left().clone()));
                    subRight.union(new Binding((Term)aut.label().clone(),(Term)aut.right().clone()));
                }catch(CloneNotSupportedException e){}
            });
            results.add(new Generalization(gen,subLeft,subRight));
        });
        return results;
    }
    @Override
    public HashSet<Generalization> minimize(HashSet<Generalization> gens) {
            return gens;
    }

}
