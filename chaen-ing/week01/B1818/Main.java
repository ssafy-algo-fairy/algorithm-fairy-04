package week01.B1818;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
	static int N;
	static int[] arr;
	static int[] lis;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		N = Integer.parseInt(br.readLine());
		arr = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();

		lis = new int[N];
		int len = 0;

		for (int x : arr) {
			int pos = Arrays.binarySearch(lis, 0, len, x);

			if (pos < 0) {
				pos = -(pos + 1);
			}

			lis[pos] = x;

			if (pos == len) {
				len++;
			}
		}

		System.out.println(N - len);
	}
}