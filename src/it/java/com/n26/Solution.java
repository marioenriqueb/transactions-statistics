package com.n26;

import java.util.TreeSet;

import org.junit.Assert;

public class Solution {
	
	public static void main(String[] args) {
		int[] num = {-1, -2};
		Assert.assertEquals(1, solution(num));
		
		int[] num2 = {1, 3, 6, 4, 1, 2};
		Assert.assertEquals(5, solution(num2));
		
		int[] num3 = {};
		Assert.assertEquals(5, solution(num3));
	}
	
	public static int solution(int[] A) {
        TreeSet<Integer> arbol = new TreeSet<Integer>();
        
        for(int element: A) {
            arbol.add(element);
        }
        
        int counter = 1;
        
        while (!arbol.isEmpty()) {
            int first = arbol.pollFirst();
            if (first > counter) {
                return counter;
            } else if (first == counter) {
                counter++;
            }
        }
        
        return counter;
    }
}
