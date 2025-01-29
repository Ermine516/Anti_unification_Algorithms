package com.aualgs;

import com.aualgs.AUExceptions.InvalidAppException;
import com.aualgs.AUExceptions.InvalidSymbolException;
import com.aualgs.AUExceptions.NonGroundException;
import com.aualgs.AUExceptions.NonfreshLabelException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try{
            //test.testSynAU();
            //test.testComAUEasy();
             test.testComAUFinite();
           
	    }catch (InvalidAppException | InvalidSymbolException | NonGroundException | NonfreshLabelException e){
            System.err.println("some problem");
        }    
    }
}
