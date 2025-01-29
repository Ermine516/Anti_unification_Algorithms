package Matchers;
import java.util.function.Supplier;

import Aux.Pair;


public class SymbolFactory implements Supplier<Pair<String,String>>{

    private int counter =0;
    private String symbol ="";
    public SymbolFactory(String var){
        counter =0;
        symbol = var;
    }
    @Override
    public Pair<String,String> get() {
        Pair<String,String> newSym = new Pair<>(symbol,String.valueOf(counter));
        counter+=1;
        return  newSym;
       
    }
    public String getCat() {
        Pair<String,String> newSym = new Pair<>(symbol,String.valueOf(counter));
        counter+=1;
        return  newSym.left+newSym.right;
       
    }
}
