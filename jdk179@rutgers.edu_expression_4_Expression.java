package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
	public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	StringTokenizer token = new StringTokenizer(expr, delims);
    	String str = "";
    	while(token.hasMoreTokens()) {
    		str = token.nextToken();
			int index = expr.indexOf(str);
			int size = str.length();

		if (Character.isLetter(str.charAt(0)) == true) {
			if(index+size+1>expr.length()) {
				Variable test = new Variable(str);
				vars.add(test);
				break;
			}
			else if(expr.charAt(index+size)=='[' ) {
    			Array array = new Array(str);
    			if(!arrays.contains(array)) {
    					arrays.add(new Array(str));
    				}
			}else {
				Variable var = new Variable(str);
				if(!vars.contains(var)) {
					vars.add(new Variable(str));
				}
			}	  	
    }
    	}
    
    	System.out.println(vars.toString());
    	System.out.println(arrays.toString());
    			
    	
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
	public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/ 
		System.out.println(vars);
		System.out.println(arrays);
    	Stack<Float> numbers = new Stack<Float>();
    	Stack<Float> numbersrev = new Stack<Float>();
        Stack<String> operators = new Stack<String>();
        Stack<String> operatorsrev = new Stack<String>();
        Stack<String> parenthesis = new Stack<String>();
        
    	expr = expr.replaceAll(" ", "");
    	boolean returnDelims = true;
    	StringTokenizer tok = new StringTokenizer(expr, delims, returnDelims);
    	
    	//check for parenthesis
    	int length = -1; 
    	while(tok.hasMoreElements()) {
    		String token = tok.nextToken();
			length = length + token.length();
    		System.out.println("New Check" + token);
    		
    		if(length < expr.length() && expr.charAt(length) == '(') { //parenthesis check
    			int begindex = length+1;
    			parenthesis.push(token);
    			length = length + token.length();
    			if(tok.hasMoreElements()) {
					token = tok.nextToken();
				}
    			while(!parenthesis.isEmpty()) {
    				if(expr.charAt(length) == ('(')) {
    					parenthesis.push(token);
    					length = length + token.length();
    					if(tok.hasMoreElements()) {
        					token = tok.nextToken();
    					}
    				}
    				else if(expr.charAt(length) == (')')){
    					parenthesis.pop();
    					length = length + token.length();
    					if(tok.hasMoreElements()) {
    						token = tok.nextToken();
    					}
    				}
    				
    				else {
    					length = length + token.length();
    					if(tok.hasMoreElements()) {
        					token = tok.nextToken();
    					}
    				}
    				if(length < expr.length()) { //+ - check
    				if(expr.charAt(length) == ('+') || expr.charAt(length) == ('-')){
						operators.push(token);
    				}
    				else continue;
    				}
    				if(length < expr.length()) { //* check
    					if(expr.charAt(length) == ('*')) {
    						token = tok.nextToken();
    						if(token.equals("(")) {
    		    				int begindex3 = length+1;
    		        			parenthesis.push(token);	
    		        			length = length + token.length();
    		        			if(tok.hasMoreElements()) {
    		    					token = tok.nextToken();
    		    				}
    		        			while(!parenthesis.isEmpty()) {
    		        				if(length < expr.length() && expr.charAt(length) == ('(')) {
    		        					parenthesis.push(token);
    		        					length = length + token.length();
    		        					if(tok.hasMoreElements()) {
    		            					token = tok.nextToken();
    		        					}
    		        				}
    		        				else if(length < expr.length() && expr.charAt(length) == (')')){
    		        					parenthesis.pop();
    		        					length = length + token.length();
    		        					if(tok.hasMoreElements()) {
    		        						token = tok.nextToken();
    		        					}
    		        				}
    		        				
    		        				else {
    		        					length = length + token.length();;
    		        					if(tok.hasMoreElements()) {
    		            					token = tok.nextToken();
    		        					}
    		        				}
    		        				if(length < expr.length()) {
    		        				if(expr.charAt(length) == ('+') || expr.charAt(length) == ('-')){
    		    						operators.push(token);
    		    					}
    		    					if(expr.charAt(length) == ('*')) {
    		    						
    		    					}
    		        				}
    		        			}
    		        			int endindex = length-1;
    		        			System.out.println("par recursion" + "begindex " + begindex3 +" " + "endindex " + endindex);
    		        			System.out.println(expr.substring(begindex3, endindex));
    		        			numbers.push(evaluate(expr.substring(begindex3, endindex), vars, arrays));
    		        			System.out.println("Pushed in" + numbers.peek()); 
    		        		} 
    						
    					}
    				} //end of mult paren check
    			int endindex = length-1;
    			System.out.println("par recursion" + "begindex " + begindex +" " + "endindex " + endindex);
    			System.out.println(expr.substring(begindex, endindex));
    			numbers.push(evaluate(expr.substring(begindex, endindex), vars, arrays));
    			System.out.println("Pushed in" + numbers.peek()); 
    			}
    		} //end of parenthesis check
    		
    		else if(token.charAt(0) == '+'|| token.charAt(0) == '-'){ //add or sub check
    			operators.push(token);
    			System.out.println( token + " added");
    		}
    		else if(token.equals("*")){ //mult check
    			token = tok.nextToken();
    			if(token.equals("(")) {
    				int begindex3 = length+1;
        			parenthesis.push(token);	
        			length = length + token.length();
        			if(tok.hasMoreElements()) {
    					token = tok.nextToken();
    				}
        			while(!parenthesis.isEmpty()) {
        				if(length < expr.length() && expr.charAt(length) == ('(')) {
        					parenthesis.push(token);
        					length = length + token.length();
        					if(tok.hasMoreElements()) {
            					token = tok.nextToken();
        					}
        				}
        				else if(length < expr.length() && expr.charAt(length) == (')')){
        					parenthesis.pop();
        					length = length + token.length();
        					if(tok.hasMoreElements()) {
        						token = tok.nextToken();
        					}
        				}
        				
        				else {
        					length = length + token.length();;
        					if(tok.hasMoreElements()) {
            					token = tok.nextToken();
        					}
        				}
        				if(length < expr.length()) {
        				if(expr.charAt(length) == ('+') || expr.charAt(length) == ('-')){
    						operators.push(token);
    					}
        				} //end of add loop
        			} //end of while loop 
        			int endindex = length-1;
        			System.out.println("par recursion" + "begindex " + begindex3 +" " + "endindex " + endindex);
        			System.out.println(expr.substring(begindex3, endindex));
        			numbers.push(evaluate(expr.substring(begindex3, endindex), vars, arrays));
        			System.out.println("Pushed in" + numbers.peek()); 
        		} 
    			else {	
    				if(token.contains("a") || token.contains("b") || token.contains("c") ||token.contains("d") ||token.contains("e") ||token.contains("f") ||token.contains("g") ||token.contains("h") ||token.contains("i") ||token.contains("j") ||token.contains("k") ||token.contains("l") ||token.contains("m") ||token.contains("n") ||token.contains("o") ||token.contains("p") ||token.contains("q") ||token.contains("r") ||token.contains("s") ||token.contains("t") ||token.contains("u") ||token.contains("v") ||token.contains("w") ||token.contains("x") ||token.contains("y") ||token.contains("z")) {
    	    			for(Variable v: vars){
    	    				if(v.name.equals(token)){
    	    					numbers.push((float)v.value);
    	    				}
    	    		}
    	    		}
    				else {
    					float a =  Float.parseFloat(token);
    	    			numbers.push(a);
    				}
    			float m = numbers.pop();
    			float n = numbers.pop();
    			numbers.push(m*n);
    			System.out.println("Multiplied" + numbers.peek() + "pushed in");
    			}
    		}//end of mult
    		else if(token.equals("/")){
    			token = tok.nextToken();
    			if(token.equals("(")) {
    				int begindex4 = length+1;
        			parenthesis.push(token);
        			length = length + token.length();
        			if(tok.hasMoreElements()) {
    					token = tok.nextToken();
    				}
        			while(!parenthesis.isEmpty()) {
        				if(expr.charAt(length) == ('(')) {
        					parenthesis.push(token);
        					length = length + token.length();
        					if(tok.hasMoreElements()) {
            					token = tok.nextToken();
        					}
        				}
        				else if(expr.charAt(length) == (')')){
        					parenthesis.pop();
        					length = length + token.length();
        					if(tok.hasMoreElements()) {
        						token = tok.nextToken();
        					}
        				}
        				
        				else {
        					length = length + token.length();
        					if(tok.hasMoreElements()) {
            					token = tok.nextToken();
        					}
        				}
        				if(length < expr.length()) {
        				if(expr.charAt(length) == ('+') || expr.charAt(length) == ('-')){
    						operators.push(token);
    					}
    					if(expr.charAt(length) == ('*')) {
    						token = tok.nextToken();
    						
    					}
        				}
        			}
        			int endindex = length-1;
        			System.out.println("par recursion" + "begindex4 " + begindex4 +" " + "endindex " + endindex);
        			System.out.println(expr.substring(begindex4, endindex));
        			numbers.push(evaluate(expr.substring(begindex4, endindex), vars, arrays));
        			System.out.println("Pushed in" + numbers.peek()); 
        		} 
    			else {	
    				if(token.contains("a") || token.contains("b") || token.contains("c") ||token.contains("d") ||token.contains("e") ||token.contains("f") ||token.contains("g") ||token.contains("h") ||token.contains("i") ||token.contains("j") ||token.contains("k") ||token.contains("l") ||token.contains("m") ||token.contains("n") ||token.contains("o") ||token.contains("p") ||token.contains("q") ||token.contains("r") ||token.contains("s") ||token.contains("t") ||token.contains("u") ||token.contains("v") ||token.contains("w") ||token.contains("x") ||token.contains("y") ||token.contains("z")) {
    	    			for(Variable v: vars){
    	    				if(v.name.equals(token)){
    	    					numbers.push((float)v.value);
    	    				}
    	    		}
    	    		}
    				else {
    					float a =  Float.parseFloat(token);
    	    			numbers.push(a);
    				}
    			float m = numbers.pop();
    			float n = numbers.pop();
    			numbers.push(n / m);
    			System.out.println("Divided" + numbers.peek() + "pushed in");
    			}
    			} //end of divided
    		else if(token.contains("A") || token.contains("B") || token.contains("C") ||token.contains("D") ||token.contains("E") ||token.contains("F") ||token.contains("G") ||token.contains("H") ||token.contains("I") ||token.contains("J") ||token.contains("K") ||token.contains("L") ||token.contains("M") ||token.contains("N") ||token.contains("O") ||token.contains("P") ||token.contains("Q") ||token.contains("R") ||token.contains("S") ||token.contains("T") ||token.contains("U") ||token.contains("V") ||token.contains("W") ||token.contains("X") ||token.contains("Y") ||token.contains("Z")) {
    			for(Array arr: arrays) {
    				if(arr.name.equals(token)) {
    					int endPos = 0, begCount = 1, endCount = 0;
    					length = length + token.length();
    					token = tok.nextToken();
    				
    					while(length < expr.length() && begCount != endCount) {
    	
    						System.out.println(length + token);
    						if(expr.charAt(length) == ']') {
    							endCount++;
    							length = length + token.length();
    							if (tok.hasMoreElements()) {
    								token = tok.nextToken();
    							} //end of token check
    						} //end of ] 
    						else if(expr.charAt(length) == '[') {
    							begCount++;
    							length = length + token.length();
    							if (tok.hasMoreElements()) {
    								token = tok.nextToken();
    							} //end of token check
    						}//end of [ check
    						else {
    							length = length + token.length();
    							if (tok.hasMoreElements()) {
    								token = tok.nextToken();
    							}//end of token check
    							}//end of other check
    					} //end of while loop
    					
    					endPos = length;
    					System.out.println("token length " + token.length()+1 + "endpos " + endPos);
    					System.out.println("boop" + expr.substring(token.length()+1, endPos-1));
    					numbers.push((float)arr.values[(int)evaluate(expr.substring(token.length()+1, endPos-1), vars, arrays)]);
    					System.out.println(numbers.peek() + "pushed in");
    			}
    			}
    			if(length < expr.length()) { //+ - check
    				if(expr.charAt(length) == ('+') || expr.charAt(length) == ('-')){
						operators.push(token);
    				}
    				else continue;
    				}
    		}//end of arrays
    		else if(token.contains("a") || token.contains("b") || token.contains("c") ||token.contains("d") ||token.contains("e") ||token.contains("f") ||token.contains("g") ||token.contains("h") ||token.contains("i") ||token.contains("j") ||token.contains("k") ||token.contains("l") ||token.contains("m") ||token.contains("n") ||token.contains("o") ||token.contains("p") ||token.contains("q") ||token.contains("r") ||token.contains("s") ||token.contains("t") ||token.contains("u") ||token.contains("v") ||token.contains("w") ||token.contains("x") ||token.contains("y") ||token.contains("z")) {
    			for(Variable v: vars){
    				if(v.name.equals(token)){
    					numbers.push((float)v.value);
    				}
    		}
    		}//end of vars
    		else if(token.contains("]")){
    			continue;
    		}
    		else { 
    			boolean d = true;
    			float hold = 0;
    			try {
    				hold = Float.parseFloat(token);
    				d = true; 
    			} catch (NumberFormatException nfe) {
    				d = false;
    		}
    			if(d = true) {
    				numbers.push(hold);
    				System.out.println(numbers.peek() + "num added");
    			}
    			else {
    				continue;
    			}
    	}//else numbers
    	}//end of filter while loop
    	System.out.println(numbers.peek() + "THUIAHFIOAGFHED");
    while(!numbers.isEmpty()) {   //reverses numbers
		numbersrev.push(numbers.pop());
	}
    while(!operators.isEmpty()) {   //reverses operators
		operatorsrev.push(operators.pop());
		System.out.println("Operator reversed");
	}
	while(!numbersrev.isEmpty() && !operatorsrev.isEmpty()) {
		if (operatorsrev.peek().equals("-")) {
			float c = numbersrev.pop();
			if (numbersrev.isEmpty()){
    			float whee = 0;
    			numbersrev.push(whee);
    		}
			float d = numbersrev.pop();
			numbersrev.push(c - d);
			operatorsrev.pop();
			System.out.println("Subtracted");
		}
		else if (operatorsrev.peek().equals("+")) {
    		float c = numbersrev.pop();
    		if (numbersrev.isEmpty()){
    			float whee = 0;
    			numbersrev.push(whee);
    		}
    		float d = numbersrev.pop();
    		numbersrev.push(c + d);
    		operatorsrev.pop();
    		System.out.println("Added");
    		}
		else {
			System.out.println("Error");
		}
	} //end of + - while loop
	return numbersrev.pop();
} // end of evaluate
} //last line
