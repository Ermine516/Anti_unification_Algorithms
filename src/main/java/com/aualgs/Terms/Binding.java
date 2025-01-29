package com.aualgs.Terms;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.aualgs.AUExceptions.InvalidAppException;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Binding implements Cloneable { 
        private Term var = null;
        private Term term = null;
        public static Function<Term,Function<Binding,Binding>> update = t -> bin -> bin.updateInternal(t);
        public Binding(Term v,Term t){
            this.var = v;
            this.term = t;
        }
        public Term dom(){
            return var;
        }
        public Term ran(){
            return term;
        }
        private Binding updateInternal(Term t){
            term=t;
            return this;
        }
        public Term apply(Term input){          
            try{
                return traverse((Term) input.clone());
            }catch (CloneNotSupportedException e){
                return null;
            } catch (InvalidAppException e) {
                return null;
            }  
        }
        private Term traverse(Term t) throws InvalidAppException, CloneNotSupportedException{
                if(t.isVar() && this.var.equals(t)) return (Term) this.term.clone();
                UnaryOperator<Term> traverseCheck =  term-> {try{  return  this.traverse(term); }catch (InvalidAppException | CloneNotSupportedException e){ return null;}};
                Stream<Term> substitutedArgs = t.args().stream().map(traverseCheck);
                Stream<Term> filterTerms =  substitutedArgs.filter(term-> term!= null);
                ArrayList<Term> newargs = new ArrayList<Term>(filterTerms.collect(Collectors.toList()));
                if (newargs.size()!= t.args().size()) throw new InvalidAppException();
                try{ return new Term( (Symbol) t.head().clone(),newargs);}catch (Exception e){return null;}
        }
        @Override
        public String toString() {
            return String.format("%s <-- %s",this.dom().toString(), this.ran().toString());
        }
        @Override
        public int hashCode() {
            return this.dom().hashCode()+31*this.ran().hashCode();
        }
        @Override
        protected Object clone() throws CloneNotSupportedException {
            try{
                return new Binding((Term) var.clone(), (Term) term.clone());
            } catch(CloneNotSupportedException e){
                return null;
            }
        }
}