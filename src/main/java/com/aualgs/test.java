package com.aualgs;
import java.util.ArrayList;
import java.util.HashSet;

import com.aualgs.AUExceptions.InvalidAppException;
import com.aualgs.AUExceptions.InvalidSymbolException;
import com.aualgs.AUExceptions.NonGroundException;
import com.aualgs.AUExceptions.NonfreshLabelException;
import com.aualgs.Algorithms.Syntactic.SynConfig;
import com.aualgs.Algorithms.Commutative.ComAU;
import com.aualgs.Algorithms.Syntactic.SynAU;
import com.aualgs.AntiUnif.Aut;
import com.aualgs.AntiUnif.Configuration;
import com.aualgs.AntiUnif.Generalization;
import com.aualgs.Terms.Binding;
import com.aualgs.Terms.Substitution;
import com.aualgs.Terms.Symbol;
import com.aualgs.Terms.Term;

public class test {
    public test(){}
      // a
	public static Term a() throws InvalidAppException, InvalidSymbolException{
        Symbol syma = new Symbol(0,"a");
        try{ return new Term(syma,new ArrayList<>());}catch (Exception e){return null;}
    }
    // b
    public static Term b() throws InvalidAppException, InvalidSymbolException{
        Symbol symb = new Symbol(0,"b");
        try{ return new Term(symb,new ArrayList<>());}catch (Exception e){return null;}
    }
    public static Term c() throws InvalidAppException, InvalidSymbolException{
        Symbol symb = new Symbol(0,"c");
        try{ return new Term(symb,new ArrayList<>());}catch (Exception e){return null;}
    }
    // x
	public static Term x() throws InvalidAppException, InvalidSymbolException{
        Symbol symx = new Symbol(0,"x",true);
        try{ return new Term(symx,new ArrayList<>());}catch (Exception e){return null;}
    }   
    public static Term y() throws InvalidAppException, InvalidSymbolException{
        Symbol symy = new Symbol(0,"y",true);
        try{ return new Term(symy,new ArrayList<>());}catch (Exception e){return null;}
    }   
    // f(a)
    public static Term af() throws InvalidAppException, InvalidSymbolException{
        Symbol symf = new Symbol(1,"f");
        ArrayList<Term> args = new ArrayList<>();
        args.add(a());
       try{ return new Term(symf,args);}catch (Exception e){return null;}
    }    
    public static Term bf() throws InvalidAppException, InvalidSymbolException{
        Symbol symf = new Symbol(1,"f");
        ArrayList<Term> args = new ArrayList<>();
        args.add(b());
       try{ return new Term(symf,args);}catch (Exception e){return null;}
    }    
    public static Term xf() throws InvalidAppException, InvalidSymbolException{
        Symbol symf = new Symbol(1,"f");
        ArrayList<Term> args = new ArrayList<>();
        args.add(x());
        try{ return new Term(symf,args);}catch (Exception e){return null;}
    }
    public static Term yf() throws InvalidAppException, InvalidSymbolException{
        Symbol symf = new Symbol(1,"f");
        ArrayList<Term> args = new ArrayList<>();
        args.add(y());
        try{ return new Term(symf,args);}catch (Exception e){return null;}
    }

    public static Term aafg() throws InvalidAppException, InvalidSymbolException{
        Symbol symg = new Symbol(2,"g");
        ArrayList<Term> args = new ArrayList<>();
        args.add(a());
        args.add(af());
        try{ return new Term(symg,args);}catch (Exception e){return null;}
    } 
    public static Term bag() throws InvalidAppException, InvalidSymbolException{
        Symbol symg = new Symbol(2,"g");
        ArrayList<Term> args = new ArrayList<>();
        args.add(b());
        args.add(a());
        try{ return new Term(symg,args);}catch (Exception e){return null;}
    } 
    public static Term abagg() throws InvalidAppException, InvalidSymbolException{
        Symbol symg = new Symbol(2,"g");
        ArrayList<Term> args = new ArrayList<>();
        args.add(a());
        args.add(bag());
        try{ return new Term(symg,args);}catch (Exception e){return null;}
    } 
    public static Term cag() throws InvalidAppException, InvalidSymbolException{
        Symbol symg = new Symbol(2,"g");
        ArrayList<Term> args = new ArrayList<>();
        args.add(c());
        args.add(a());
        try{ return new Term(symg,args);}catch (Exception e){return null;}
    } 
    public static Term cagcg() throws InvalidAppException, InvalidSymbolException{
        Symbol symg = new Symbol(2,"g");
        ArrayList<Term> args = new ArrayList<>();
        args.add(cag());
        args.add(c());
        try{ return new Term(symg,args);}catch (Exception e){return null;}
    } 
    public static Term abfg() throws InvalidAppException, InvalidSymbolException{
        Symbol symg = new Symbol(2,"g");
        ArrayList<Term> args = new ArrayList<>();
        args.add(a());
        args.add(bf());
        try{ return new Term(symg,args);}catch (Exception e){return null;}
    } 
    public static Term abfgfbg() throws InvalidAppException, InvalidSymbolException{
        Symbol symg = new Symbol(2,"g");
        ArrayList<Term> args = new ArrayList<>();
        args.add(abfg());
        args.add(bf());
        try{ return new Term(symg,args);}catch (Exception e){return null;}
    } 
    public static Term aafgfag() throws InvalidAppException, InvalidSymbolException{
        Symbol symg = new Symbol(2,"g");
        ArrayList<Term> args = new ArrayList<>();
        args.add(aafg());
        args.add(af());
        try{ return new Term(symg,args);}catch (Exception e){return null;}
    } 
    public static Term xxfg() throws InvalidAppException, InvalidSymbolException{
        Symbol symg = new Symbol(2,"g");
        ArrayList<Term> args = new ArrayList<>();
        args.add(x());
        args.add(xf());
        try{ return new Term(symg,args);}catch (Exception e){return null;}
    } 
    public static Term xyfg() throws InvalidAppException, InvalidSymbolException{
        Symbol symg = new Symbol(2,"g");
        ArrayList<Term> args = new ArrayList<>();
        args.add(x());
        args.add(yf());
        try{ return new Term(symg,args);}catch (Exception e){return null;}
    } 
    public static void varTest()  throws NonfreshLabelException,NonGroundException, InvalidAppException, InvalidSymbolException{
            String str = String.format("The term  %s has variables %s ",xxfg(),xxfg().vars());
            System.out.println(str);
            str = String.format("The term  %s has variables %s ",x(),x().vars());
            System.out.println(str);
            str = String.format("The term  %s has variables %s ",xf(),xf().vars());
            System.out.println(str);
            str = String.format("The term  %s has variables %s ",xyfg(),xyfg().vars());
            System.out.println(str);
    }
    public static void SubTest()  throws NonfreshLabelException,NonGroundException, InvalidAppException, InvalidSymbolException{
        Binding one = new Binding(x(),aafg());
        Binding two = new Binding(y(),xf());
        Substitution sub = new Substitution();
        sub.union(one);
        sub.union(two);
        String str = String.format("applying { %s } to %s results in %s",one,xyfg(),one.apply(xyfg()));
        System.out.println(str);
        str = String.format("applying %s to %s results in %s",sub,xyfg(),sub.apply(xyfg()));
        System.out.println(str);
        sub = new Substitution();
        sub.union(two);
        sub.compose(one);
        str = String.format("applying %s to %s results in %s",sub,xyfg(),sub.apply(xyfg()));
        System.out.println(str);
    }
    public static void autTest()  throws NonfreshLabelException,NonGroundException, InvalidAppException, InvalidSymbolException{
        Aut autOne = new Aut(abfgfbg(),aafgfag(),x());
        String str = String.format("The following is an AUT %s",autOne);
        System.out.println(str);
    }
    public static void testSynAU() throws NonfreshLabelException,NonGroundException, InvalidAppException, InvalidSymbolException{
        SynConfig config = new SynConfig();
        Aut autOne = new Aut(abfgfbg(),aafgfag(),x());
        Aut auttwo = new Aut(abfg(),aafg(),y());
        config.add(autOne);
        config.add(auttwo);
        System.out.println(config);
        System.out.println();
        SynAU alg = new SynAU("x");
        HashSet<Configuration> results = alg.apply(config);
        HashSet<Generalization> lggs = alg.computeGeneralizations(results,x());
        System.out.println( "The Computed generalizations of " + autOne + " are as follows:\n");
        for(Generalization c: lggs)
            System.out.println(c.toString()+"\n");  
    }
    public static void testComAU() throws NonfreshLabelException,NonGroundException, InvalidAppException, InvalidSymbolException{
        SynConfig config = new SynConfig();
        Aut autOne = new Aut(abfgfbg(),aafgfag(),x());
        Aut auttwo = new Aut(abfg(),aafg(),y());
        config.add(autOne);
        config.add(auttwo);
        System.out.println(config);
        System.out.println();
        Symbol[] commutative = {new Symbol(2,"g")};
        ComAU alg = new ComAU("x",commutative);
        HashSet<Configuration> results = alg.apply(config);
        HashSet<Generalization> lggs = alg.minimize(alg.computeGeneralizations(results,x()));
        System.out.println( "The lggs of " + autOne + " are as follows:\n");
        for(Generalization c: lggs)
            System.out.println("\t"+ c.toString()+"\n");   
        lggs = alg.minimize(alg.computeGeneralizations(results,y()));
        System.out.println( "The lggs of " + auttwo + " are as follows:\n");
            for(Generalization c: lggs)
                System.out.println("\t"+ c.toString()+"\n");  

    }
    public static void testComAUEasy() throws NonfreshLabelException,NonGroundException, InvalidAppException, InvalidSymbolException{
        SynConfig config = new SynConfig();
        Aut autOne = new Aut(abfg(),aafg(),x());
        config.add(autOne);
        System.out.println(config);
        System.out.println();
        Symbol[] commutative = {new Symbol(2,"g")};
        ComAU alg = new ComAU("x",commutative);
        HashSet<Configuration> results = alg.apply(config);
        HashSet<Generalization> lggs = alg.minimize(alg.computeGeneralizations(results,x()));
        System.out.println( "The lggs of " + autOne + " are as follows:\n");
        for(Generalization c: lggs)
            System.out.println(c.toString()+"\n");   

    }

    public static void testComAUFinite() throws NonfreshLabelException,NonGroundException, InvalidAppException, InvalidSymbolException{
        SynConfig config = new SynConfig();
        Aut autOne = new Aut(cagcg(),abagg(),x());
        config.add(autOne);
        System.out.println(config);
        System.out.println();
        Symbol[] commutative = {new Symbol(2,"g")};
        ComAU alg = new ComAU("x",commutative);
        HashSet<Configuration> results = alg.apply(config);
        HashSet<Generalization> lggs = alg.minimize(alg.computeGeneralizations(results,x()));
        System.out.println( "The lggs of " + autOne + " are as follows:\n");
        for(Generalization c: lggs)
            System.out.println("\t"+ c.toString()+"\n");   

    }
}
