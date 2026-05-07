package B16638;

import java.io.*;
import java.util.*;

public class Main {
	static int[] p;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int n = Integer.parseInt(br.readLine());
		
		char[] input = br.readLine().toCharArray();

		int max = Integer.MIN_VALUE;
		
		for (int i = 0; i < (1<<n/2); i++) {
			int[] num = new int[n/2+1];
			for (int j = 0; j < n/2+1; j++) {
				num[j] = input[2*j] - '0';
			}
			
			p = new int[n/2+1];
			for (int j = 0; j < n/2+1; j++) {
				p[j] = j;
			}
			
			int[] priority = new int[n/2];
			for (int j = 0; j < n/2; j++) {
				priority[j] = input[2*j+1] == '*' ? 1 : 2;
			}
			
			for (int j = 0; j < n/2; j++) {
				if ((i & (1<<j)) != 0) {
					if (j != 0 && priority[j-1] == 0) continue;
					priority[j] = 0;
				}
			}
			
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < n/2; k++) {
					if (priority[k] == j) {
						int a = num[find(k)];
						int b = num[find(k+1)];
						union(k, k+1);
						num[find(k)] = calc(a, b, input[2*k+1]);
					}
				}
			}	
			max = Math.max(max, num[find(0)]);
		}
		System.out.print(max);
	}
	
	public static void union(int a, int b) {
		a = find(a);
		b = find(b);
		p[a] = b;
	}
	
	public static int find(int a) {
		if (a == p[a]) return a;
		return p[a] = find(p[a]);
	}
	
	public static int calc(int a, int b, char c) {
		if (c == '+') return a+b;
		if (c == '-') return a-b;
		if (c == '*') return a*b;
		return 0;
	}
}
