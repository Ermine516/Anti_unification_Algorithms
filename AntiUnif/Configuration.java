package AntiUnif;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import AUExceptions.NonfreshLabelException;
import Terms.Term;



public abstract class Configuration  implements Cloneable  {
    protected HashSet<Aut> active;
    protected HashSet<Aut> solved;
    protected HashMap<Aut,HashSet<Aut>> mergeSets;
    public Configuration() {
        active = new HashSet<Aut>();
        solved = new HashSet<Aut>();
        mergeSets = new HashMap<Aut,HashSet<Aut>>();
        
    }
    public HashSet<Aut> active(){return active;}
    public HashSet<Aut> solved(){return solved;}
    public  HashMap<Aut,HashSet<Aut>> mergeSets(){return mergeSets;}

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
        Stream<Term> labelsAS = Stream.concat(labelsActive,labelsSolved);
        return new HashSet<Term>(labelsAS.collect(Collectors.toSet()));
    }
}
