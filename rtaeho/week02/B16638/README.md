### 📌 문제 정보
- **번호:** 16638
- **제목:** 괄호 추가하기 2
- **난이도:** Gold 1
- **분류:** 구현, 브루트포스, 백트래킹

---

### 💡 접근 방식

> 수식에 괄호를 추가해 결과의 최댓값을 구하는 문제입니다.
> 괄호 안에는 연산자가 하나만 들어갈 수 있고, 중첩 괄호는 불가합니다.
> 곱하기가 더하기/빼기보다 우선순위가 높습니다.

> 백트래킹으로 괄호를 칠 연산자 조합을 모두 탐색하고,
> 각 조합에 대해 중위 표기법 계산(연산자 우선순위 스택)으로 결과를 구했습니다.

---

### 🔹 1단계 – 괄호 조합 생성 (백트래킹)

수식 `3+8*7-9*2`에서 연산자는 인덱스 1, 3, 5, 7에 위치

```
인덱스: 0 1 2 3 4 5 6 7 8
문자:   3 + 8 * 7 - 9 * 2
```

- 연산자 `i`를 선택하면 → `(숫자 연산자 숫자)` = `i-1, i, i+1`을 괄호로 감쌈
- 선택한 괄호와 겹치지 않으려면 다음 선택은 `i+4`부터
- 선택하지 않으면 `i+2`로 이동

```java
static void makeParentheses(int idx) {
    if (idx >= N) {
        // 문자열 만들고 계산
        return;
    }

    if (idx + 2 < N) {  // 감쌀 수 있을 때만
        selected[idx] = true;
        makeParentheses(idx + 4);  // 겹치지 않게 건너뜀
        selected[idx] = false;
    }
    makeParentheses(idx + 2);  // 선택 안 함
}
```

예: `(3+8)*7-(9*2)` → `selected[1]=true, selected[7]=true`

---

### 🔹 2단계 – 괄호 포함 수식 문자열 생성

`selected[i]`가 true인 연산자 기준으로 앞뒤 숫자까지 묶어서 괄호 삽입

```java
int i = 0;
while (i < N) {
    if (i + 2 < N && selected[i + 1]) {
        sb.append('(');
        sb.append(equation.charAt(i));      // 숫자
        sb.append(equation.charAt(i + 1));  // 연산자
        sb.append(equation.charAt(i + 2));  // 숫자
        sb.append(')');
        i += 3;
    } else {
        sb.append(equation.charAt(i));
        i++;
    }
}
```

---

### 🔹 3단계 – 중위 표기법 계산 (연산자 우선순위 스택)

괄호와 `*, +, -` 우선순위를 모두 처리하는 표준 스택 계산기

```
우선순위: ( = 0,  +/- = 1,  * = 2
```

- 숫자 → operandStack에 push
- `(` → operatorStack에 push
- `)` → `(`를 만날 때까지 연산 수행
- 연산자 → 스택 top의 우선순위가 >= 현재 연산자이면 먼저 계산

⚠️ **반복문 종료 후 스택에 남은 연산자도 전부 처리해야 함**

```java
while (!operatorStack.isEmpty()) {
    char operator = operatorStack.pop();
    int operand2 = operandStack.pop();
    int operand1 = operandStack.pop();
    operandStack.push(applyOperator(operand1, operand2, operator));
}
```

---

### ⏳ 복잡도 분석

- **시간 복잡도:** `O(2^(N/2) × N)`
    - 연산자 최대 9개, 각각 선택/비선택 → 최대 약 2^5 = 32가지 조합
    - 각 조합마다 O(N) 문자열 생성 + O(N) 계산
- **공간 복잡도:** `O(N)`
    - 스택과 문자열 빌더

---

### ⚠️ 어려웠던 점

- 괄호 안에 연산자 하나만 들어갈 수 있는 줄 모르고 한참 고민했습니다.
- `calculate`에서 반복문 끝난 뒤 스택에 남은 연산자 계산 까먹었습니다.
- `idx + 2 < N` 범위 체크 안 해서 IndexOutOfBoundsException 났습니다.