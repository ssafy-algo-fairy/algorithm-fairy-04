### 📌 문제 정보
- **번호**: B1939
- **제목**: 중량제한
- **난이도**: Gold 3
- **분류**: 이분탐색, BFS

---

### 💡 접근 방식

운반할 수 있는 최대 중량을 이분탐색으로 찾고, 해당 중량으로 이동 가능한지 BFS로 검증

---

### 🔹 1단계 - 이분탐색 범위 설정

간선 가중치의 최솟값~최댓값을 탐색 범위로 설정

```java
min = Math.min(min, C);
max = Math.max(max, C);
```

---

### 🔹 2단계 - BFS로 경로 검증

`mid` 중량 이상인 간선만 사용해서 start → end 도달 가능한지 확인

```java
for (Edge edge : graph[cur]) {
    if (!visited[edge.to] && edge.weight >= weight) {
        visited[edge.to] = true;
        q.offer(edge.to);
    }
}
```

---

### 🔹 3단계 - 이분탐색

도달 가능하면 `result` 갱신 후 더 큰 값 탐색, 불가능하면 더 작은 값 탐색

```java
if (bfs(mid)) {
    result = mid;
    low = mid + 1;
} else {
    high = mid - 1;
}
```

---

### 💻 핵심 코드

```java
static int binarySearch(int low, int high) {
    int result = 0;
    while (low <= high) {
        int mid = (low + high) / 2;
        if (bfs(mid)) {
            result = mid;
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }
    return result;
}
```

---

### ⏳ 복잡도 분석

- **시간**: O(M × log W) — W는 가중치 범위, BFS마다 O(M), 이분탐색 O(log W)
- **공간**: O(N + M)

---

### ⚠️ 어려웠던 점

다익스트라로 해보려고 했는데, 시간초과 날 거 같았습니다.