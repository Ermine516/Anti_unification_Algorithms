package Algorithms.Commutative;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import Algorithms.Syntactic.SynConfig;
import Algorithms.Syntactic.SynAU;
import AntiUnif.Aut;
import AntiUnif.Configuration;
import AntiUnif.Generalization;
import AntiUnif.Rule;
import Matchers.CommutativeMatcher;
import Terms.Symbol;

public class ComAU extends SynAU {
    private ArrayList<Symbol> comSym;
    public ComAU(String var,Symbol[] com){
        super(var);
        rules.add(0,ComDecom);
        comSym = new ArrayList<Symbol>();
        for (Symbol s : com) comSym.add(s);
    }
    private  Rule ComDecom = (cf,variables) ->{
        SynConfig config = (SynConfig) cf;
        Predicate<Aut> canApp = aut -> comSym.contains(aut.left().head());
        Optional<Aut>  targetOpt = config.active().stream().filter(canApp).findFirst();
        if(!targetOpt.isPresent()) return  Optional.empty();
        Optional<Stream<Configuration>> DecomResult = Optional.empty();
        try{
            DecomResult = Decom.apply((SynConfig) config.clone(),variables);
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
    @Override
    public HashSet<Generalization> minimize(HashSet<Generalization> gens) {
            ArrayList<Symbol> comSym = new ArrayList<>();
            CommutativeMatcher matchC = new CommutativeMatcher(comSym);
            HashSet<Generalization> lggs = new HashSet<>();
            while(!gens.isEmpty()){
                Generalization cur = gens.stream().findFirst().get();
                gens.remove(cur);
                if(gens.stream().filter(t-> matchC.match(cur.generalization(),t.generalization())).findAny().isPresent()) continue;
                if(lggs.stream().filter(t-> matchC.match(cur.generalization(),t.generalization())).findAny().isPresent()) continue;
                lggs.add(cur);
            }
            return lggs;         
        }
}