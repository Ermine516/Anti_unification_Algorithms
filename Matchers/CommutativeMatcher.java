package Matchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.potassco.clingo.control.Control;
import org.potassco.clingo.solving.SolveHandle;
import org.potassco.clingo.solving.SolveMode;

import Aux.Pair;
import Terms.Symbol;
import Terms.Term;

public class CommutativeMatcher implements Matcher{
    private ArrayList<Symbol> comSyms;
    private SymbolFactory predMarkingSymbols;
    private SymbolFactory placeHolderVarSymbols;
    private HashMap<Term,Pair<String,String>> VarMappings;
    public CommutativeMatcher(ArrayList<Symbol> comSyms){
        this.comSyms = comSyms;
        predMarkingSymbols = null;
        placeHolderVarSymbols = null;
        VarMappings = null;
    }

    @Override
    public boolean match(Term term, Term gnd) {
        predMarkingSymbols = new SymbolFactory("pms");
        placeHolderVarSymbols = new SymbolFactory("PHV");
        VarMappings = new HashMap<>();
        Pair<String,String> startPred = predMarkingSymbols.get();
        HashSet<Term> level = new HashSet<>();
        level.add(gnd); 
        String prog = this.generateASPProgram(term,startPred,level);
        prog += String.format(":- %s(%s,MCV),MCV!=%s.\n",startPred.left,startPred.right,gnd.toString());
        prog += String.format(":- not %s(%s,_).\n",startPred.left,startPred.right);
        Control control = new Control();
        System.out.println(prog);
        control.add(prog);
        control.ground();
        try (SolveHandle handle = control.solve(SolveMode.YIELD)) {
            if(handle.hasNext()) {
                control.close();
                return true;
            }
        }
        control.close();
        return false;
    }
    private String generateASPProgram(Term term,Pair<String,String> pred,HashSet<Term> level){
        String prog = "";
        if(term.isVar()){
            if(VarMappings.containsKey(term)){
                Pair<String,String> oldPred =VarMappings.get(term); 
                prog+= String.format(":-%s(%s,%s),%s(%s,%s),%s!=%s.\n",pred.left,pred.right,"LVP",oldPred.left,oldPred.right,"OVP","LVP","OVP");
            }
            else{
                VarMappings.put(term,pred);
                ArrayList<String> possibleInst =(ArrayList<String>) level.stream().map(t-> String.format("%s(%s,%s)", pred.left,pred.right,t.toString())).collect(Collectors.toList());
                if(!possibleInst.isEmpty())
                    prog+= String.format("1{%s}1.\n", String.join(";",possibleInst));
            }
            
            return prog;
        }
        if(term.args().isEmpty()){
            String clingoVar =term.toString();
            prog = String.format("%s(%s,%s).\n", pred.left,pred.right,clingoVar);
            return prog; 
        }
        HashSet<Term> newlevel = new HashSet<>();
        level.forEach(t-> newlevel.addAll(t.args()));
        HashMap<Term,String> clingoVars = new HashMap<>();
        HashMap<Term,Pair<String,String>> clingoPreds = new HashMap<>();
        term.args().forEach(t -> clingoVars.put(t,placeHolderVarSymbols.getCat()));
        term.args().forEach(t -> clingoPreds.put(t,predMarkingSymbols.get()));
        prog = String.format("1{%s(%s,%s(", pred.left,pred.right,term.head().name()); 
        prog += String.join(",", term.args().stream().map(t-> clingoVars.get(t)).collect(Collectors.toList()));
        if (!comSyms.contains(term.head())){
            prog += "))}1:-";
            Function<Term,String> predMaker = t-> String.format("%s(%s,%s)",clingoPreds.get(t).left,clingoPreds.get(t).right,clingoVars.get(t));
            prog += String.join(",", term.args().stream().map(predMaker).collect(Collectors.toList()));
            prog += ".\n";
            Function<Term,String> recCall = t-> this.generateASPProgram(t, clingoPreds.get(t),newlevel);
            prog += String.join("", term.args().stream().map(recCall).collect(Collectors.toList()));
            return prog;
        }
        else{   
            prog += "));";
            prog += String.format("%s(%s,%s(", pred.left,pred.right,term.head().name()); 
            prog += String.join(",", term.args().reversed().stream().map(t-> clingoVars.get(t)).collect(Collectors.toList()));
            prog += "))}1:-";
            Function<Term,String> predMaker = t-> String.format("%s(%s,%s)",clingoPreds.get(t).left,clingoPreds.get(t).right,clingoVars.get(t));
            prog += String.join(",", term.args().stream().map(predMaker).collect(Collectors.toList()));
            prog += ".\n";
            Function<Term,String> recCall = t-> this.generateASPProgram(t, clingoPreds.get(t),newlevel);
            prog += String.join("", term.args().stream().map(recCall).collect(Collectors.toList()));
            return prog;
        }
  
    }
    
}
