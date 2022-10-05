package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		ArrayList<String> q = new ArrayList<String>();
		Queue<Person> queue = new Queue<Person>();
		Person[] va = new Person[g.members.length];
		if(g == null || p1 == null || p2 == null) {
			return null;
		}
		int ind1 = 0; 
		int ind2 = 0;
		//indexes p1 and p2
		while(ind1 < g.members.length) {
			System.out.println("waa");
			if(g.members[ind1].name.equals(p1)) {
				break;
			}
			else {
				ind1++;
			}
		}
		while(ind2 < g.members.length) {
			System.out.println("waaa");
			if(g.members[ind2].name.equals(p2)) {
				break;
			}
			else {
				ind2++;
			}
		}
		boolean[] visited = new boolean[g.members.length];
		visited[ind1] = true;
		queue.enqueue(g.members[ind1]);
		while(!queue.isEmpty()) {
			Person pivot = queue.dequeue();
			int num = g.map.get(pivot.name);
			visited[num] = true;
			Friend nbr = pivot.first;
			if(nbr == null) {
				return null;
			}
			while(nbr != null) {
				if(!visited[nbr.fnum]) {
					visited[nbr.fnum]= true;
					va[nbr.fnum]= pivot;
					queue.enqueue(g.members[nbr.fnum]);
					if(g.members[nbr.fnum].name.equals(p2)) {
						pivot = g.members[nbr.fnum];
						while(pivot.name.equals(p1) == false) {
							q.add(0, pivot.name);
							pivot = va[g.map.get(pivot.name)];
						}
						q.add(0, p1);
						return q;
					}
				}
				nbr = nbr.next;
			}
		}
		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		if(g == null || school == null) {
			return null;
		}
		int check = 0;
		while(check < g.members.length) {
			if(g.members[check].school.equals(school)) {
				break;
			}
			else {
				check++;
			}
		}
		if(check == 0 && !school.equals(g.members[0].school)) {
			System.out.println(g.members[0].school);
			System.out.println(school);
			System.out.println("Not in the file");
			return null;
		}
		Person start = g.members[check];
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		boolean[] visited = new boolean[g.members.length];
		ArrayList<String> result = new ArrayList<String>();
		Queue<Person> queue = new Queue<Person>();
		queue.enqueue(start);
		visited[g.map.get(start.name)] = true;
		Person n = new Person();
		Friend nbr;
		while(!queue.isEmpty()) {
			System.out.println("wawa");
			n = queue.dequeue();
			nbr = n.first;
			result.add(n.name);
			while (nbr != null) {
				if(!visited[nbr.fnum]) {
					if(g.members[nbr.fnum].school != null) {
						if(g.members[nbr.fnum].school.equals(school)) {
							queue.enqueue(g.members[nbr.fnum]);
						}
					}
					visited[nbr.fnum] = true;
				}
				nbr = nbr.next;
			}
		}
		if(!result.isEmpty() && list.isEmpty()) {
			list.add(result);
		}
		return list;
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		if (g == null) {
			return null;
		}
		ArrayList<String> connectors = new ArrayList<String>();
		boolean[] visited = new boolean[g.members.length];
		ArrayList<String> a = new ArrayList<String>();
		int[] num = new int[g.members.length];
		int[] b = new int[g.members.length];
		int i = 0;
		int count = 0;
		/////////////////////
		while(i < g.members.length) {
			if(visited[i] == false) {
				connectors = DFS(connectors, g, g.members[i], visited, count, num, a, b, true);
			}
			i++;
		}
		return  connectors;
	}
	
	private static ArrayList<String> DFS(ArrayList<String> connectors, Graph g, Person start, boolean[] visited, int count, int[] num, ArrayList<String> pre, int[] back,  boolean started){
		if(count == 11) {
			count = 1;
		}
		visited[g.map.get(start.name)] = true;
		Friend nbr = start.first;
		num[g.map.get(start.name)] = count;
		back[g.map.get(start.name)]= count; 
		while(nbr != null) {
			//if nbr hasn't been visited
			if(visited[nbr.fnum]== false) {
				count++;
				connectors = DFS(connectors, g, g.members[nbr.fnum], visited, count, num, pre, back, false);
				if(num[g.map.get(start.name)] <= back[nbr.fnum]) {
					if (!connectors.contains(start.name) && pre.contains(start.name)){
						connectors.add(start.name);
					}
					if(!connectors.contains(start.name) && started == false) {
						connectors.add(start.name);
					}
				}
				else {
					if(back[g.map.get(start.name)] > back[nbr.fnum]) {
						back[g.map.get(start.name)] = back[nbr.fnum];
					}
				}		
			pre.add(start.name);
			}
			//////////////////// If nbr has been visited already
			else {
				if (back[g.map.get(start.name)] > num[nbr.fnum]) {
					back[g.map.get(start.name)] = num[nbr.fnum];
				}
			}
			nbr = nbr.next;
		}
		return connectors;
	}
}


