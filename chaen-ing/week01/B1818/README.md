## **🔍 문제 요약**

- 책이 대충 꽂혀있음
- 이거를 최소 횟수로 옮겨서 1 ~ N번으로 정렬
- 1 ≤ N ≤ 200,000

---

## **💡문제 접근 / 풀이 전략**

- 가장 긴 수열이 있다면 어차피 그거 앞뒤로 채워야하는거 아닌가? → 앞뒤로 채우면서 수열이 더 길어졌는지 체크? 하면 안되지 → 수열끼리 이어야할듯? → 가장긴수열 길이 찾고 빼면 되려나
1. **DP : 시간초과**

    - O(N^2) : 200,000 ^ 2
2. **이분탐색**
    - 배열에서 들어갈 위치 찾기
        - `Arrays.binarySearch(memo, 0, len, x)`
        - 들어갈 위치를 반환함. 만약 없다면 그 값을 음수로 반환
        - ex) [10, 20, 30] 에 15가 들어간다면 -2로 반환
    - 찾은 위치에 값을 덮어씌움 : 항상 더 작은 값이 들어옴
        - ex) [1, 5]에 2가 들어온다면 2로 바꾸기

          → 2인 경우가 더 수열이 길어질 확률이 높으므로


---

## **✅ 코드 & 소요 시간**

```java
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
```

37324 / 364

---

## **✍️ 회고**

- 이분탐색으로 하는 방법 찾아보고 풀었숨돠
- 덮어쓰는게 이해가 안갔었음