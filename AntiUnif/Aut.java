package AntiUnif;
import AUExceptions.NonGroundException;
import Terms.Term;

public class Aut implements Cloneable{
    private Term left  = null;
    private Term right = null;
    private Term label = null;
    public Aut(Term l, Term r, Term la ) throws NonGroundException {
        if(!l.vars().isEmpty()) throw new NonGroundException("Terms in AUTs must be ground");
        if(!r.vars().isEmpty()) throw new NonGroundException("Terms in AUTs must be ground");
        this.left = l;
        this.right = r;
        this.label = la; 
    }
    public Term left(){return left;}
    public Term right(){return right;}
    public Term label(){return label;}
    
    @Override
    public String toString() {
        return String.format("%s =_%s_= %s",this.left.toString(),this.label.toString(),this.right.toString());
    }
    @Override
    public boolean equals(Object other) {
        Aut a = (Aut) other;
        return this.left.equals(a.left) && this.right.equals(a.right) && this.label.equals(a.label);
    }
    @Override
    public int hashCode() {
        return this.left.hashCode()+31*this.right.hashCode()+this.label.hashCode();
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        try{
            return new Aut((Term) left.clone(), (Term) right.clone(),(Term) label.clone());
        } catch(CloneNotSupportedException | NonGroundException e ){
            return null;
        }
    }
}