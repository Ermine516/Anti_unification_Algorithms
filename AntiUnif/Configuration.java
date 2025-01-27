package AntiUnif;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import AUExceptions.NonfreshLabelException;
import Terms.Binding;
import Terms.Substitution;

import Terms.Term;

public class Configuration implements Cloneable{
    private HashSet<Aut> active;
    private HashSet<Aut> solved;
    private HashMap<Aut,HashSet<Aut>> mergeSets;

    private HashSet<Aut> delayed;
    private Substitution generalization;

    public Configuration() {
        active = new HashSet<Aut>();
        solved = new HashSet<Aut>();
        delayed = new HashSet<Aut>();
        mergeSets = new HashMap<Aut,HashSet<Aut>>();
        generalization = new Substitution();
        
    }
    public HashSet<Aut> active(){return active;}
    public HashSet<Aut> solved(){return solved;}
    public HashSet<Aut> delayed(){return delayed;}
    public Substitution generalization(){return generalization;}
    public  HashMap<Aut,HashSet<Aut>> mergeSets(){return mergeSets;}
    
    public void compose(Binding sub){
        generalization.compose(sub);          
    }
    
    public void addSolve(Aut aut){
        active.remove(aut);
        solved.add(aut);
        Stream<Aut> sameAutSTRM= mergeSets.keySet().stream().filter(a-> aut.left().equals(a.left()) && aut.right().equals(a.right()));
        Optional<Aut> existSameAut = sameAutSTRM.findAny();
        if(!existSameAut.isPresent()) mergeSets.put(aut, new HashSet<>());
        else{
            HashSet<Aut> current =mergeSets.get(existSameAut.get());
            current.add(aut);
            mergeSets.put(existSameAut.get(),current);
        }
    }
    public void add(Aut aut) throws NonfreshLabelException{
        if(this.labels().contains(aut.label())){
            throw new NonfreshLabelException("Cannot use a variable contained in the AUT!");
        }
        active.add(aut);
    }
    public HashSet<Term> labels(){
        Stream<Term> labelsActive = active.stream().map(aut -> aut.label());
        Stream<Term> labelsSolved = solved.stream().map(aut -> aut.label());
        Stream<Term> labelsDelayed = delayed.stream().map(aut -> aut.label());
        Stream<Term> labelsAS = Stream.concat(labelsActive,labelsSolved);
        Stream<Term> labelsASD = Stream.concat(labelsAS,labelsDelayed);
        return new HashSet<Term>(labelsASD.collect(Collectors.toSet()));
    }
    @Override
    public String toString(){
        BinaryOperator<String> cat= (acc,ele) -> acc+"\t"+ele+"\n";
        String  res = active.stream().map(aut-> aut.toString()).reduce("Active:\n", cat)+"Solved:\n";
        res = solved.stream().map(aut-> aut.toString()).reduce(res, cat)+"Delayed:\n";
        res = delayed.stream().map(aut-> aut.toString()).reduce(res, cat)+"Generalization:\n";
        return res+"\t"+generalization.toString();
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        Configuration result = new Configuration();
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
        delayed.stream().forEach(aut ->{
            try{
                result.delayed.add((Aut)aut.clone());
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