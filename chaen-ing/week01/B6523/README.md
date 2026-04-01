## **🔍 문제 요약**

- 총 N명이 원탁에 앉았다면, 각 사람의 번호는 0번부터 N-1번이 된다.
- a, b가 주어지고, 현재 선택된 사람의 번호가 x라면, 다음 사람의 번호는 ax2+b mod N
- 0번부터 시작
- 두번 걸렸을때 술마시고, 어떤 사람이 세번 걸렸다면 다 집으로간다
- N과 a, b가 주어졌을 때, 술을 마시지 않고 집으로 가는 사람의 수를 구하는 프로그램을 작성하시오.

- 2 ≤ N ≤ 10^9
- 0 ≤ a, b < N
- 첫 사람이 술을 마시기 위해 필요한 단계의 수는 10^6보다 작다.

---

## **💡문제 접근 / 풀이 전략**

- **첫번째 시도 : 메모리 초과**
    - mod 연산이므로 사이클이 있음 → 그부분에 있는 사람들의 개수만 제외하기 → BigInteger로 계산하면서 그사람들만 빼기
    - 메모리 초과의 이유가 HashMap이라고 생각함!! 아니였음 BigInteger때문..
- **두번째 시도 :** 플로이드 토끼와 거북이 알고리즘
    - 해시맵안쓰고 계산 가능 → 공간복잡도 O(1), 시간복잡도 O(N)
    - 리스트에 사이클 있는지 확인
    - 토끼는 2칸 거북이는 1칸
    - 둘이 처음으로 만나는 순간 체크
    - 이때 거북이는 시작점으로 돌려놓고 둘이 한칸씩 움직여서 처음 만나는 곳을 찾기 → 사이클 판별
    - https://eazymean.tistory.com/101

        ```java
        while (true) {
        	hare = calc(calc(hare));
        	tortoise = calc(tortoise);
        	
        	// 1. 두개의 포인터가 처음으로 만나는 순간 체크
        	if (hare.equals(tortoise)) {    
        		tortoise = BigInteger.ZERO;
        		// 2. 둘이 한칸씩 움직여서 처음 만나는 곳 찾기
        		while (!hare.equals(tortoise)) {
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
        	if (hare.equals(tortoise)) {
        		sb.append(N.subtract(BigInteger.valueOf(cnt))).append("\n");
        		break;
        	}
        }
        ```

    - 근데 이거하면서도 BigInteger쓰느라 계속 메모리 초과남
- **계산방식 변경** → BigInteger 폐기
    - `a * x^2 + b mod N`

      ⇒ `x2 = x * x mod N`

      ⇒ `ax2 = a * x2 mod N`

      ⇒ `ax2 + b mod N`


---

## **✅ 코드 & 소요 시간**

```java
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

```

16132 / 444

---

## **✍️ 회고**

- 암튼 BigInteger가 문제였고… 저 토끼와거북이 알고리즘 안쓰고 HashSet이나 Map에서 contains 체크만으로 충분히 통과가능