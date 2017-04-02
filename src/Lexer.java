/*  an instance of this class provides methods that produce a
    sequence of tokens following some Finite State Automata,
    with capability to put back tokens

    Adapted code Written by Professor Schultz
    to fit the CalcLang Project. Code taken from:
    /3210s17noon/Otter/Code/Lexer.java
*/
import com.sun.org.apache.bcel.internal.ExceptionConstants;

import java.util.*;
import java.io.*;

public class Lexer {
    public static String margin = "";

    private static String[] keywords = { "show", "newline", "msg", "input" };

    private static String[] builtInFunctions = {"sin", "sqrt", "cos", "tan"};

    // holds any number of tokens that have been put back
    private Stack<Token> stack;
    // the source of physical symbols
    private BufferedReader input;
    // one lookahead physical symbol
    private int lookahead;

    // construct a Lexer ready to produce tokens from a file
    public Lexer( String fileName ) {
        try{
            input = new BufferedReader( new FileReader( fileName ) );
        }
        catch(Exception e) {
            error("Problem opening file named [" + fileName + "]" );
        }

        stack = new Stack<Token>();
        lookahead = 0;  // indicates no lookahead symbol present
    }// constructor

    // produce the next token
    public Token getNext() throws Exception {
        if (!stack.empty()) {
            //  produce the most recently put back token
            Token token = stack.pop();
            return token;
        } else {
            // produce a token from the input source
            int state = 0;  // state of DFA
            String data = "";  // specific info for the token
            boolean done = false;
            int sym;  // holds current symbol

            do {
                sym = getNextSymbol();
                if (state == 0) {
                    if (isWhiteSpace(sym)) {
                        // stay in state 0
                    } else if (isLetter(sym)) {
                        state = 1;
                        data += (char) sym;
                    } else if(isMathSymbol(sym)){
                        state = 2;
                        data += (char) sym;
                        done = true;
                    } else if (isDigit(sym)) {
                        state = 3;
                        data += (char) sym;
                    } else if(sym == '"' ) {
                        state = 5;
                        data += (char) sym;
                    } else if(sym == ';'){
                        state = 7;// This is just comments.
                    } else if(sym == -1){
                        state = 8;
                    }
                } else if (state == 1) { //Letter or Digit stay in state 1.
                    if (isDigit(sym) || isLetter(sym)) {
                        //Stay in state 1.
                        state = 1;
                        data += (char) sym;
                    } else {
                        done = true;
                        putBackSymbol(sym);
                    }
                } else if (state == 3) { // If symbol is a isDigit. Go to stay 4 if symbol is "."
                    if(isDigit(sym)){
                        state = 3;
                        data += (char) sym;
                    } else if(sym == (char) '.'){
                        state = 4;
                        data += (char) sym;
                    } else {
                        done = true;
                        putBackSymbol(sym);
                    }

                } else if (state == 4) { // If symbol is a isDigit
                    if(isDigit(sym)) {
                        state = 4;
                        data += (char) sym;
                    } else {
                        done = true;
                        putBackSymbol(sym);
                    }
                } else if(state == 5) { //Paring of String literals, "blah"
                    data += (char) sym;
                    state = 6;

                } else if(state == 6){ //
                    if(isLetter(sym) || sym == '=' || sym == ' ' ) {
                        state = 6;
                        data += (char) sym;
                    } else if(sym == '"'){
                        state = 5;
                        done = true;
                        data += (char) sym;
                    } else {
                        done = true;
                        putBackSymbol(sym);
                    }
                } else if(state == 7) {
                    if(!(sym == 10 || sym == 13)) {
                        state = 7;
                    } else {
                        state = 0;
                    }
                } else if(state == 8) {
                    done = true;
                } else {
                    throw new Exception("Reached an unknown state: " + state);
                }

            } while (!done);

            // generate token depending on stopping state
             return generateToken(state, data);
        }
    } // getNext

    private Token generateToken (int state, String data) {
        Token token;
        if(state == 1 ){
            for (String keyword : keywords) {
                if (keyword.equals(data)) {
                    token = new Token("keyword", data);
                    return token;
                }
            }
            for (String builtInFunction : builtInFunctions) {
                if (builtInFunction.equals(data)) {
                    token = new Token("bif", data);
                    return token;
                }
            }
            token = new Token( "id", data );
            return token;
        } else if(state == 2) {
            token = new Token("mathSym", data);
            return token;
        } else if(state == 3 || state == 4) {
            token = new Token("digit", data);
            return token;
        } else if( state == 5){
           token = new Token("string", data);
            return token;
        } else if(state == 8){
            token = new Token("eof", "");
            return token;
        } else {
            error("Lexer FA halted at inappropriate state: " + state + "with data: " + data);
            return null;
        }
    }

    public Token getToken() {
      Token token = null;
       try {
           token = getNext();
           System.out.println("                                   got token: " + token);

       } catch (Exception e) {
         System.out.println("Exception caught:" + e.getMessage());
       }
        return token;
    }

    public void putBack( Token token ) {
        System.out.println( margin + "put back token " + token.toString() );
        stack.push( token );
    }

    // next physical symbol is the lookahead symbol if there is one,
    // otherwise is next symbol from file
    private int getNextSymbol() {
        int result = -1;

        if( lookahead == 0 ) {// is no lookahead, use input
            try{  result = input.read();  }
            catch(Exception e){}
        }
        else {// use the lookahead and consume it
            result = lookahead;
            lookahead = 0;
        }
        return result;
    }

    private void putBackSymbol( int sym ) {
        if( lookahead == 0 ) {// sensible to put one back
            lookahead = sym;
        }
        else {
            System.out.println("Oops, already have a lookahead " + lookahead +
                    " when trying to put back symbol " + sym );
            System.exit(1);
        }
    }// putBackSymbol


    private boolean isMathSymbol(int sym) {
        return (sym == '+') ||(sym == '-') || (sym == '=') || (sym == '*')
                || (sym == '/') || (sym == '(') || (sym == ')');
    }
    private boolean isLetter( int code ) {
        return 'a'<=code && code<='z' ||
                'A'<=code && code<='Z';
    }

    private boolean isDigit( int code ) {
        return '0'<=code && code<='9';
    }

    private boolean isWhiteSpace( int code ) {
        return code==10 || code==13 || code==32 ;
    }

    private static void error( String message ) {
        System.out.println( message );
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        System.out.print("Enter file name: ");
        Scanner keys = new Scanner( System.in );
        String name = keys.nextLine();

        Lexer lex = new Lexer( name );
        Token token;

        do{
            token = lex.getNext();
            System.out.println( token.toString() );
        }while( ! token.getKind().equals( "eof" )  );

    }

}