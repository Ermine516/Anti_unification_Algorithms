package Terms;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import AUExceptions.InvalidAppException;

import java.util.function.IntPredicate;
import java.util.function.UnaryOperator;


public class Term implements Cloneable{
    private Symbol sym= null ;
    private ArrayList<Term> args = null;

    public Term(Symbol sym, ArrayList<Term> args) throws InvalidAppException {
        this.sym = sym;
        this.args = args;
        if (sym.arity() != args.size())
            throw new InvalidAppException(String.format("%s conflicts with size %d!", this.sym.toString(),args.size()));
    }
    public Symbol head(){
        return sym;
    }
    public ArrayList<Term>  args(){
        return args;
    }
    public boolean isVar(){
        return this.sym.isVar();
    }
    public HashSet<Term> vars(){
        if(this.isVar()){
            HashSet<Term> ret = new HashSet<Term>();
            ret.add(this);
            return ret;
        }
        Stream<Stream<Term>> zero = this.args.stream().map(term -> term.vars().stream());
        Optional<Stream<Term>> varSetsConcat = zero.reduce((ele, acc) -> Stream.concat(acc,ele));
        return (varSetsConcat.isPresent())? (HashSet<Term>) varSetsConcat.get().collect(Collectors.toSet()): new HashSet<Term>();
        
    }
    @Override
    public String toString() {
        Stream<Object> converted = this.args.stream().map(term-> term.toString());
        String[] asStr = converted.toArray(String[]::new);
        String result =  String.join(",",asStr);
        return String.format((this.sym.arity()>0)?"%s(%s)":"%s",this.sym.name(), result);
    }
    @Override
    public int hashCode() {
       IntStream one = IntStream.range(0, this.sym.arity());
       int summed = one.map(i -> this.args.get(i).hashCode()).sum(); 
       return this.sym.hashCode()+ 31*summed;
    }
    
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Term)) return false;
        if(!(this.sym.equals(((Term) other).head()))) return false;
        if(this.sym.arity()==0) return true;
        //check is the arguments of the terms are position-wise equal
        IntStream argspositions = IntStream.range(0, this.sym.arity());
        IntPredicate checkArgsZipNonEquality = i -> ((this.args().get(i).equals(((Term) other).args().get(i)))?1:0) == 0;
        int[] nonEqualPositions = argspositions.filter(checkArgsZipNonEquality).toArray();
        return nonEqualPositions.length>0;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        UnaryOperator<Term> cloneCheck =  t-> {try{  return (Term) t.clone(); }catch (CloneNotSupportedException e){ return null;}};
        Stream<Term> argscloned = this.args.stream().map(cloneCheck);
        Stream<Term> filterFailures =  argscloned.filter(term-> term!= null);
        ArrayList<Term> asList = (ArrayList<Term>) filterFailures.collect(Collectors.toList());
        if (asList.size()!= this.args().size()) throw new CloneNotSupportedException();
        try{
            return new Term((Symbol) this.sym.clone(),asList);
        }catch (InvalidAppException e){
            throw new CloneNotSupportedException();
        }
    }
}