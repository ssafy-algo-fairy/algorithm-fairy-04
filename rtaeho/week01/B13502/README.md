### 📌 문제 정보
- **번호**: B13502
- **제목**: 단어퍼즐 2
- **난이도**: Gold 2
- **분류**: 트라이, DFS, 백트래킹

---

### 💡 접근 방식

5×5 보드에서 8방향 DFS로 단어를 탐색하고, Trie로 가지치기

핵심 과제는 **dict.txt(24830개 단어)를 소스코드 65536B 제한 안에 넣는 것**

---

### 🔹 1단계 - 소스코드 크기 문제

dict.txt를 그냥 문자열 배열로 넣으면 `code too large` 에러

→ **Trie 직렬화 + Raw DEFLATE 압축 + Base64 인코딩**으로 해결

---

### 🔹 2단계 - Trie 직렬화

단어 목록을 먼저 Trie로 빌드한 뒤 DFS로 직렬화

공통 prefix가 한 번만 저장되기 때문에 단순 단어 목록보다 압축률이 훨씬 높음

```
포맷: [isEnd(1bit) | childCount(7bit)] [char] [child...] ...
```

```python
def serialize(node, buf):
    children = sorted([(k, v) for k, v in node.items() if k != '\x00'])
    is_end = '\x00' in node
    buf.append((is_end << 7) | len(children))
    for c, child in children:
        buf.append(ord(c))
        serialize(child, buf)
```

---

### 🔹 3단계 - Raw DEFLATE + Base64

gzip 대신 Raw DEFLATE (RFC 1951) 사용 → 헤더/체크섬이 없어서 더 작음

```python
compressor = zlib.compressobj(9, zlib.DEFLATED, -15)  # -15 = Raw DEFLATE
compressed = compressor.compress(bytes(buf)) + compressor.flush()
encoded = base64.b64encode(compressed).decode()
```

| 단계 | 크기 |
|------|------|
| 단어 목록 원본 | ~200KB |
| Trie 직렬화 | 129,893 B |
| Raw DEFLATE 압축 | 47,740 B |
| Base64 인코딩 | 63,656 B |

---

### 🔹 4단계 - Java에서 복원

```java
static void insertDict() throws Exception {
    String encoded = "인코딩 된 문자열";

    byte[] data = Base64.getDecoder().decode(encoded);
    Inflater inflater = new Inflater(true);  // true = Raw DEFLATE
    inflater.setInput(data);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buf = new byte[4096];
    while (!inflater.finished()) {
        baos.write(buf, 0, inflater.inflate(buf));
    }
    inflater.end();

    deserialize(baos.toByteArray(), new int[]{0}, trie.root);
}

static void deserialize(byte[] data, int[] pos, Node node) {
    int header = data[pos[0]++] & 0xFF;
    node.isLast = (header & 0x80) != 0;
    int childCount = header & 0x7F;
    for (int i = 0; i < childCount; i++) {
        char c = (char)(data[pos[0]++] & 0xFF);
        Node child = new Node();
        node.child.put(c, child);
        deserialize(data, pos, child);
    }
}
```

---

### 🔹 5단계 - DFS

`startsWith`로 가지치기하면서 탐색, 찾은 단어는 `HashSet`에 저장해 중복 방지

```java
static void dfs(String word, int r, int c) {
    if (trie.search(word)) {
        found.add(word);
    }

    if (!trie.startsWith(word)) {
        return;
    }

    visited[r][c] = true;
    for (int i = 0; i < 8; i++) {
        int nr = r + dr[i];
        int nc = c + dc[i];
        if (nr < 0 || nr >= 5 || nc < 0 || nc >= 5 || visited[nr][nc]) continue;
        dfs(word + map[nr][nc], nr, nc);
    }
    visited[r][c] = false;
}
```

---

### 💻 핵심 코드

```java
static class Trie {
    Node root = new Node();

    public void insert(String word) {
        Node current = root;
        for (char c : word.toCharArray()) {
            current.child.putIfAbsent(c, new Node());
            current = current.child.get(c);
        }
        current.isLast = true;
    }

    public boolean search(String word) {
        Node current = root;
        for (char c : word.toCharArray()) {
            if (!current.child.containsKey(c)) return false;
            current = current.child.get(c);
        }
        return current.isLast;
    }

    public boolean startsWith(String prefix) {
        Node current = root;
        for (char c : prefix.toCharArray()) {
            if (!current.child.containsKey(c)) return false;
            current = current.child.get(c);
        }
        return true;
    }
}
```

---

### ⏳ 복잡도 분석

- **시간**: O(25 × 8^L) — L은 최대 단어 길이, 실제로는 Trie 가지치기로 훨씬 빠름
- **공간**: O(Trie 노드 수) — 24830개 단어 기준 수십만 노드

---

### ⚠️ 어려웠던 점

그냥 자바로는 풀지 마세요 쓰레기