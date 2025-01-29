package com.aualgs.Terms;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.aualgs.AUExceptions.InvalidAppException;

public class Substitution implements Cloneable { 
        private ArrayList<Binding> subs = null;
        private HashMap<Term,Binding> matcher;
        public Substitution(){
            subs = new ArrayList<Binding>();
            matcher = new HashMap<Term,Binding>();
        }
        public Substitution(Collection<Binding> bins){
            subs = new ArrayList<Binding>(bins);
            subs.forEach(b ->  matcher.put(b.dom(), b));
        }
        public void compose(Binding b){
            if(this.ranVar().contains(b.dom())){
                matcher.replaceAll((_,bin)-> Binding.update.apply(b.apply(bin.ran())).apply(bin));
                subs = new ArrayList<>();
                matcher.keySet().stream().forEach(v-> subs.add(matcher.get(v)));
            }
            if(!this.dom().contains(b.dom()))
                this.union(b);            
        }
        public void union(Binding b){
            this.subs.add(b);
            this.matcher.put(b.dom(), b);
        }
        public HashSet<Term> dom(){
            Stream<Term> varOnly = subs.stream().map(bin -> bin.dom()).distinct();
            return new HashSet<Term>(varOnly.collect(Collectors.toSet()));
        }
        public HashSet<Term> ran(){
            Stream<Term> ranOnly = subs.stream().map(bin -> bin.ran()).distinct();
            return new HashSet<Term>(ranOnly.collect(Collectors.toSet()));
        }
        public HashSet<Term> ranVar(){
            Stream<Term> ranOnly = subs.stream().map(bin -> bin.ran());
            Stream<Stream<Term>> varsets = ranOnly.map(term -> term.vars().stream());
            Optional<Stream<Term>> unionVarSets = varsets.reduce((ele, acc) -> Stream.concat(acc,ele));
            return (unionVarSets.isPresent())? (HashSet<Term>)unionVarSets.get().collect(Collectors.toSet()): new HashSet<>();
        }
        public Term apply(Term input){          
            try{
                return traverse((Term) input.clone());

            }catch (CloneNotSupportedException | InvalidAppException e){
                return null;
            }  
        }
        private Term traverse(Term t) throws InvalidAppException,CloneNotSupportedException{
            if(t.isVar() && this.dom().contains(t)) return matcher.get(t).apply(t);
            UnaryOperator<Term> traverseCheck =  term-> {
                try{  
                    return  this.traverse(term); 
                }catch (InvalidAppException | CloneNotSupportedException e){ 
                    return null;}
                };
            Stream<Term> substitutedArgs = t.args().stream().map(traverseCheck);
            Stream<Term> filterTerms =  substitutedArgs.filter(term-> term!= null);
            ArrayList<Term> newargs = new ArrayList<Term>(filterTerms.collect(Collectors.toList()));
            if (newargs.size()!= t.args().size()) throw new InvalidAppException();
            try{ 
                return new Term( (Symbol) t.head().clone(),newargs);
            }catch (Exception e){
                return null;
            }
        }
        @Override
        public String toString() {
            Stream<Object> converted = this.subs.stream().map(bins-> bins.toString());
            String[] asStr = converted.toArray(String[]::new);
            return String.format("{ %s }",String.join(" , ",asStr));
        }
        @Override 
        public boolean equals(Object other){
            // finish this code!!!
            return true;
        }
        @Override
        public int hashCode() {
            IntStream one = IntStream.range(0, this.subs.size());
            int summed = one.map(i -> 31*this.subs.get(i).hashCode()).sum(); 
            return summed;
        }
        @Override
        public Object clone() throws CloneNotSupportedException {
            Substitution result = new Substitution();
            subs.stream().forEach(bin ->{
                try{
                    Binding newBin = (Binding) bin.clone();
                    result.subs.add((Binding) bin.clone());
                    result.matcher.put(newBin.dom(),newBin);

                } catch(CloneNotSupportedException e){}
            });
            return result;
        }
}