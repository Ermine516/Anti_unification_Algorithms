package Algorithms;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import AntiUnif.Aut;
import AntiUnif.Configuration;
import AntiUnif.Rule;
import Terms.Symbol;

public class ComAU extends SynAU {
    private ArrayList<Symbol> comSym;
    public ComAU(String var,Symbol[] com){
        super(var);
        rules.add(0,ComDecom);
        comSym = new ArrayList<Symbol>();
        for (Symbol s : com) comSym.add(s);
    }
    private  Rule ComDecom = (config,variables) ->{
        Predicate<Aut> canApp = aut -> comSym.contains(aut.left().head());
        Optional<Aut>  targetOpt = config.active().stream().filter(canApp).findFirst();
        if(!targetOpt.isPresent()) return  Optional.empty();
        Optional<Stream<Configuration>> DecomResult = Optional.empty();
        try{
            DecomResult = Decom.apply((Configuration) config.clone(),variables);
        }catch(CloneNotSupportedException e){}
        Aut target = targetOpt.get();
        Collections.reverse(target.right().args());
        Optional<Stream<Configuration>>  DecomResult2 = Decom.apply(config,variables);
        Stream<Configuration> result1  = (DecomResult.isPresent())? DecomResult.get(): Stream.empty();
        Stream<Configuration> result2  = (DecomResult2.isPresent())? DecomResult2.get(): Stream.empty();
        Builder<Configuration> res = Stream.builder();
        result1.forEach(c -> res.add(c));
        result2.forEach(c -> res.add(c));
        return Optional.of(res.build());
    };
}