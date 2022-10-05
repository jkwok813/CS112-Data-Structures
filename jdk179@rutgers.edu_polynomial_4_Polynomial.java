package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		    Node head = new Node(0, 0, null);
		    Node current = head;
		    while (poly1 != null || poly2 != null) {
		        if (poly1 == null) {
		            // poly1 has no more, take poly2
		            Node node = new Node(poly2.term.coeff, poly2.term.degree, null);
		            current.next=node;
		            current = current.next;
			        poly2 = poly2.next;
		        } else if (poly2 == null) {
		            // poly2 has no more, take poly1
		        	Node node = new Node(poly1.term.coeff, poly1.term.degree, null);
		            current.next=node;
		            current = current.next;
		            poly1 = poly1.next;
		        } else if (poly1.term.degree > poly2.term.degree) {
		            // poly1 greater, take poly2
		            Node node = new Node(poly2.term.coeff, poly2.term.degree, null);
	                current.next=node;
	                current = current.next;
		            poly2 = poly2.next;
		        } else if (poly2.term.degree > poly1.term.degree) {
		            // poly2 greater, take poly1
		        	Node node = new Node(poly1.term.coeff, poly1.term.degree, null);
		            current.next = node;
		            current = current.next;
		            poly1 = poly1.next;
		        } else if(poly1.term.coeff+poly2.term.coeff != 0) {
		        	//if poly terms coefficient when combined is not 0, add them
		            Node node = new Node(poly1.term.coeff + poly2.term.coeff, poly1.term.degree, null);
		            current.next = node;
		            current = current.next; 
		            poly1 = poly1.next;
		            poly2 = poly2.next;
		        }
		          else if(poly1.term.coeff+poly2.term.coeff == 0) {
		        	  //if poly terms when combined is 0, go to next node for both
		        		poly1 = poly1.next;
		        		poly2 = poly2.next;
		        	}
		     
		        
		        }
		    
		    //return final
		    return head.next;
		}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		Node head = null;
		Node current = head;
		Node product = null;
		Node total = null;
		for(Node e = poly1; e !=null; e=e.next) {
			Node head2 = null;
			Node current2 = null; 
			for(Node f=poly2; f!=null; f=f.next) {
				if(head2 == null) {
					head2 = new Node(e.term.coeff * f.term.coeff, e.term.degree + f.term.degree, null);
					current2 = head2; 
					continue;
				}
				Node node = new Node(e.term.coeff * f.term.coeff, e.term.degree + f.term.degree, null);
				current2.next = node;  
				current2 = current2.next;
			}
			total = add(total, head2);
		}
		
		return total;
	}

		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		float total = 0;
		while(poly != null) {
			total += poly.term.coeff * Math.pow(x, poly.term.degree);
			poly = poly.next;
		}
		return total;
	}

	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
