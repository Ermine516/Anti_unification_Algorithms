package Terms;
import java.util.ArrayList;
import java.util.function.Supplier;

import AUExceptions.InvalidAppException;
import AUExceptions.InvalidSymbolException;


public class VariableFactory implements Supplier<Term>{
    private int counter =0;
    private String symbol ="";
    public VariableFactory(String var){
        counter =0;
        symbol = var;
    }
    @Override
    public Term get() {
       try {
            Symbol newSym = new Symbol(0,symbol+counter,true);
            counter+=1;
            return  new Term(newSym,new ArrayList<>());
        } catch (InvalidAppException | InvalidSymbolException e) { 
            return null;
        } 
    }
    
}
