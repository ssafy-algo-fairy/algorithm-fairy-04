### 📌 문제 정보

- **번호:** 1036
- **제목:** 36진수
- **난이도:** Gold 2
- **분류:** 수학, 구현, 그리디 알고리즘, 문자열, 임의 정밀도 / 큰 수 연산

---

### 📝 문제 요약

N개의 36진수 숫자(0~9, A~Z)가 주어질 때, K개의 자릿값(digit)을 선택해 해당 자릿값을 모두 Z로 교체.
교체 후 N개의 수의 합이 최대가 되도록 하는 문제.

---

### 💡 접근 방식

> 각 자릿값(0~35)을 Z(35)로 바꿀 때 합의 증가량을 미리 계산하고, 이득이 큰 K개를 그리디로 선택.
> 처음에는 실제 교체 후 합을 다시 구하려 했으나, "이득 합산" 방식이 훨씬 단순함을 파악.

---

### 🔹 1단계 – 원래 합 계산

각 36진수 문자열을 10진수 BigInteger로 변환 후 합산.

```
"A3Z" → 10×36² + 3×36¹ + 35×36⁰ = 12960 + 108 + 35 = 13103
```

Horner's method 활용:

```java
BigInteger val = BigInteger.ZERO;
for (char c : num.toCharArray()) {
    val = val.multiply(BASE).add(BigInteger.valueOf(getValue(c)));
}
```

---

### 🔹 2단계 – digit별 교체 이득 계산

digit `d`를 Z(35)로 바꾸면 해당 위치마다 `(35 - d) × 36^pos` 증가.

```
"A3Z"에서 '3'(d=3)을 Z로 바꿀 때 이득:
pos = 1 → (35 - 3) × 36¹ = 32 × 36 = 1152
```

N개 숫자 전체를 순회하며 digit별로 이득 누적:

```java
int pos = len - 1 - i;
BigInteger placeValue = BASE.pow(pos);
gain[d] = gain[d].add(BigInteger.valueOf(35 - d).multiply(placeValue));
```

---

### 🔹 3단계 – 상위 K개 선택 (그리디)

`gain[0..35]`를 정렬 후 가장 큰 K개를 원래 합에 더함.

```
gain = [0, 0, ..., 1152, ..., 0]
        ↑ Z(35)는 0         ↑ 3번 digit의 이득
Arrays.sort(gain) → 오름차순
상위 K개 = gain[35], gain[34], ..., gain[36-K]
```

---

### 🔹 4단계 – 36진수 변환 출력

최종 합계를 36으로 반복 나눠 나머지를 문자로 변환 후 역순 조합.

```
13103 ÷ 36 = 363 나머지 35 → 'Z'
363   ÷ 36 = 10  나머지 3  → '3'
10    ÷ 36 = 0   나머지 10 → 'A'
→ 역순: "A3Z"
```

```java
static String to36(BigInteger n) {
    if (n.equals(BigInteger.ZERO)) return "0";
    StringBuilder sb = new StringBuilder();
    while (n.compareTo(BigInteger.ZERO) > 0) {
        int r = n.mod(BASE).intValue();
        sb.append(r < 10 ? (char)('0' + r) : (char)('A' + r - 10));
        n = n.divide(BASE);
    }
    return sb.reverse().toString();
}
```

---

### 💻 핵심 코드

```java
BigInteger[] gain = new BigInteger[36];
Arrays.fill(gain, BigInteger.ZERO);

for (String num : nums) {
    int len = num.length();
    for (int i = 0; i < len; i++) {
        int d = getValue(num.charAt(i));
        int pos = len - 1 - i;
        BigInteger placeValue = BASE.pow(pos);
        gain[d] = gain[d].add(BigInteger.valueOf(35 - d).multiply(placeValue));
    }
}

Arrays.sort(gain);
for (int i = 0; i < K; i++) {
    sum = sum.add(gain[35 - i]);
}
```

---

### ⏳ 복잡도 분석

- **시간 복잡도:** `O(N × L × log L)`
  - N개 숫자, 각 최대 L자리 순회. `BASE.pow(pos)` 연산이 자리수에 비례
- **공간 복잡도:** `O(N × L)`
  - 입력 문자열 저장 및 BigInteger 연산 공간

---

### ⚠️ 어려웠던 점

- 구현은 구현인데 BigInteger 응용 문제 느낌이네요....
