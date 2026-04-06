### 📌 문제 정보

- **번호:** 6523
- **제목:** 요세푸스 한 번 더!
- **난이도:** Gold 2
- **분류:** 구현, 자료구조, 해시

---

### 💡 접근 방식

> ax²+b mod N 점화식으로 다음 사람을 선택하는 게임에서 술을 마시지 않는 사람의 수를 구하는 문제입니다.  
> 처음에는 HashMap으로 방문 횟수를 관리했으나 메모리 초과가 났고,  
> 플로이드 순환 찾기 알고리즘(토끼와 거북이)으로 다시 풀었습니다.

---

### 🔹 1단계 – HashMap 접근 (메모리 초과)
```java
HashMap<Long, Integer> people = new HashMap<>();
people.merge(x, 1, Integer::sum);
```

HashMap은 엔트리당 약 80 bytes의 객체 오버헤드가 있어서  
최대 2×10^6 엔트리 × 80 bytes = 160MB → 메모리 제한 128MB 초과

---

### 🔹 2단계 – 플로이드 순환 찾기 알고리즘

수열 구조를 파악하면
```
0 → x₁ → x₂ → ... → 입구 → ... → 입구 (순환)
      ↑ 고리 밖 (L명)  ↑    고리 안 (C명)
```

- 고리 밖 사람들 → 딱 1번만 호출 → 술 안 마심
- 고리 안 사람들 → 반드시 2번 호출 → 술 마심
- 입구 사람이 3번째 호출될 때 게임 종료

따라서 **답 = N - 고리 길이(C)**

---

### 🔹 3단계 – 알고리즘 3단계

**1단계: 순환 고리 안으로 진입**

거북이는 1칸, 토끼는 2칸씩 이동  
순환이 있으면 반드시 만남
```java
long tortoise = next(0, a, b, N);
long hare = next(next(0, a, b, N), a, b, N);

while (tortoise != hare) {
    tortoise = next(tortoise, a, b, N);
    hare = next(next(hare, a, b, N), a, b, N);
}
```

**2단계: 순환 입구 찾기**

수학적으로 `L + d = nC` 가 성립하므로  
거북이를 시작점(0)으로 보내고 둘 다 1칸씩 이동시키면  
정확히 순환 입구에서 다시 만남
```java
tortoise = 0;
while (tortoise != hare) {
    tortoise = next(tortoise, a, b, N);
    hare = next(hare, a, b, N);
}
// 이 지점이 순환 입구 = 2번 호출된 사람
```

**3단계: 고리 길이 측정**

입구부터 한 바퀴 돌며 고리 안 사람 수 카운트
```java
long cycleLength = 1;
long current = next(tortoise, a, b, N);
while (current != tortoise) {
    current = next(current, a, b, N);
    cycleLength++;
}
```

---

### 💻 핵심 수학 원리
```
L: 시작점 → 순환 입구까지 거리
C: 순환 고리 길이
d: 순환 입구 → 첫 만남 지점까지 거리

거북이 이동: L + d
토끼 이동:   2(L + d) = L + d + nC

→ L + d = nC
→ L = nC - d

거북이를 0으로 보내면
  거북이: L만큼 이동 → 입구 도착
  토끼:   nC-d만큼 이동 → 입구 도착
→ 둘이 정확히 입구에서 만남
```

---

### 💻 핵심 코드
```java
static long next(long x, long a, long b, long N) {
    long x2 = (x * x) % N;
    long ax2 = (a * x2) % N;
    return (ax2 + b) % N;
}
```

중간마다 `% N` 을 해줘야 오버플로우 방지  
(`a * x * x` 는 최대 10^27로 long 초과)

---

### ⏳ 복잡도 분석

- **시간 복잡도:** `O(L + C)`
    - 문제 조건상 최대 10^6 단계
- **공간 복잡도:** `O(1)`
    - 포인터 2개만 사용, 추가 자료구조 불필요

---

### ⚠️ 어려웠던 점

- 처음에 HashMap으로 방문 횟수를 관리했는데 박싱 오버헤드 때문에 메모리 초과가 났습니다.
- 도저히 모르겠어서 찾아봤더니 플로이드 순환 찾기 알고리즘을 사용하라고 했습니다. 근데 뭔지 모릅니다.
- 2단계에서 거북이를 시작점으로 보냈을 때 왜 순환 입구에서 만나는지 수학적 원리(`L = nC - d`)를 이해하는 게 어렵습니다.