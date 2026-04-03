import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N, a, b;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();

		while (true) {
			StringTokenizer st = new StringTokenizer(br.readLine());

			N = Integer.parseInt(st.nextToken());
			if (N == 0) {
				System.out.println(sb);
				return;
			}
			a = Integer.parseInt(st.nextToken());
			b = Integer.parseInt(st.nextToken());

			long hare = 0;
			long tortoise = 0;

			while (true) {
				hare = calc(calc(hare));
				tortoise = calc(tortoise);

				// 1. 두개의 포인터가 처음으로 만나는 순간 체크
				if (hare == tortoise) {
					tortoise = 0;
					// 2. 둘이 한칸씩 움직여서 처음 만나는 곳 찾기
					while (hare != tortoise) {
						hare = calc(hare);
						tortoise = calc(tortoise);
					}
					break;
				}
			}

			// 3. 길이 구하기
			int cnt = 0;
			while (true) {
				hare = calc(hare);
				cnt++;
				if (hare == tortoise) {
					sb.append(N - cnt).append("\n");
					break;
				}
			}
		}
	}

	// a * x^2 + b mod N
	static long calc(long current) {
		long x2 = (current * current) % N;
		long ax2 = (a * x2) % N;

		return (ax2 + b) % N;
	}
}


