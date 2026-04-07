# BOJ 16638 - 괄호 추가하기 2

## 📌 문제 설명

+, -, *, 1자리수 음이 아닌 정수로만 이루어진 수식에 임의로 괄호 추가해서 계산하기

## 💡 해결 아이디어

1. n <= 19이니 연산자 개수는 최대 9개..
2. 괄호끼리는 겹쳐지지 않으니 괄호끼리의 순서는 상관없겠군
3. 괄호, *, +/- 순서로 계산하면 되겠군
4. 괄호 씌우는거 완탐해봤자 2^9네? 바로 완전탐색
5. 계산한 숫자들을 하나로 처리 => union-find


## 🧠 코드 해설

```java
			int[] num = new int[n/2+1];
			for (int j = 0; j < n/2+1; j++) {
				num[j] = input[2*j] - '0';
			}
```

숫자 저장

```java
			p = new int[n/2+1];
			for (int j = 0; j < n/2+1; j++) {
				p[j] = j;
			}
```

union-find를 위한 parent배열 생성

```java
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
```

우선순위 배열 생성  
*면 우선순위 1, +/-면 우선순위 2  
비트마스킹으로 괄호를 씌울 곳 골라서 0  
단 괄호가 연속해서 나오면 안되기 때문에 연속한 경우 continue

```java
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
```

우선순위 순서대로 계산  
계산한 숫자 2개는 union-find로 묶기

## 🚀 느낀점

그냥 별로 안어려운 문제였던 것 같다...