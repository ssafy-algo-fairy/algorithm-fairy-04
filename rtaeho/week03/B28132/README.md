### 📌 문제 정보

- **번호:** 28132
- **제목:** 기벡을 안배운다고?
- **난이도:** Gold 4
- **분류:** 수학, 해시를 사용한 집합과 맵, 정수론, 유클리드 호제법

---

### 📝 문제 요약

N개의 2차원 벡터가 주어질 때, 수직인 벡터 쌍의 수를 구하는 문제  
두 벡터의 내적이 0이면 수직  
영벡터는 모든 벡터와 수직

---

### 💡 접근 방식

> 벡터 쌍마다 내적을 직접 계산하면 O(N²)로 시간 초과 → HashMap으로 방향이 일치하는 벡터를 묶어서 O(N) 처리  
> `(x, y)` 에 수직인 벡터는 `(y, -x)` 방향임을 이용해 이미 등록된 벡터 중 수직 방향인 것의 개수를 즉시 조회  
> 영벡터는 내적이 항상 0이므로 별도 카운트로 관리

---

### 🔹 1단계 – GCD 정규화

같은 방향의 벡터를 동일한 키로 묶기 위해 GCD로 나눠 단위 방향 벡터로 정규화

```
(2, 4) → gcd=2 → (1, 2)
(3, 6) → gcd=3 → (1, 2)  ← 같은 키
(-1, -2) → gcd=1 → (-1, -2)  ← 반대 방향, 다른 키
```

```java
long common = gcd(x, y);
x /= common;
y /= common;
```

---

### 🔹 2단계 – 수직 방향 조회

`(x, y)`에 수직인 벡터 방향은 `(y, -x)` 또는 `(-y, x)`  
두 방향 모두 map에서 조회해 누적

```
현재 벡터: (1, 2)
수직 방향1: (2, -1)
수직 방향2: (-2, 1)

map에 (2, -1)이 3개 있으면 → answer += 3
```

```java
String target = y + "," + (-x);
String target2 = (-y) + "," + x;

answer += map.getOrDefault(target, 0);
answer += map.getOrDefault(target2, 0);
```

조회 후에 현재 벡터를 map에 등록 → 자기 자신과의 쌍 방지

---

### 🔹 3단계 – 영벡터 처리

`(0, 0)`은 내적이 항상 0 → 지금까지 등장한 모든 벡터와 수직

```
i=0: (1,2)   ← 일반
i=1: (3,4)   ← 일반
i=2: (0,0)   ← 영벡터: 앞에 일반 2개 + 영벡터 0개 → answer += 2
i=3: (0,0)   ← 영벡터: 앞에 일반 2개 + 영벡터 1개 → answer += 3
```

일반 벡터 처리 시에도 `zeroCount`만큼 answer에 추가

```java
// 영벡터 등장 시
answer += (i - zeroCount); // 앞에 나온 일반 벡터 수
answer += zeroCount;       // 앞에 나온 영벡터 수
zeroCount++;

// 일반 벡터 처리 시
answer += zeroCount;       // 영벡터와의 쌍
```

---

### 💻 핵심 코드

```java
long common = gcd(x, y);
x /= common;
y /= common;

String target = y + "," + (-x);
String target2 = (-y) + "," + x;

answer += map.getOrDefault(target, 0);
answer += map.getOrDefault(target2, 0);
answer += zeroCount;

String current = x + "," + y;
map.put(current, map.getOrDefault(current, 0) + 1);
```

---

### ⏳ 복잡도 분석

- **시간 복잡도:** `O(N log M)` (M = 좌표 최댓값)
  - 벡터마다 GCD 계산 O(log M), HashMap 조회/삽입 O(1) 평균
- **공간 복잡도:** `O(N)`
  - HashMap에 최대 N개의 방향 벡터 저장

---

### ⚠️ 어려웠던 점

- 수직 방향을 `(y, -x)` 한 가지만 확인했다가 틀렸습니다. `(-y, x)`도 될 수 있어서 두 방향 모두 조회해야 했습니다.

- 일반 벡터 처리 루틴에서도 `zeroCount`를 더해줘야 한다는 것을 처음엔 놓쳤고, 영벡터 등장 시에도 이전 영벡터들과의 쌍을 따로 세야 한다는 점을 놓쳤습니다.
