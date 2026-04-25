package B13502;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.Inflater;

public class Main {
    static class Node {
        HashMap<Character, Node> child;
        boolean isLast;

        public Node() {
            child = new HashMap<>();
            isLast = false;
        }
    }

    static class Trie {
        Node root;

        public Trie() {
            root = new Node();
        }

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
                if (!current.child.containsKey(c)) {
                    return false;
                }
                current = current.child.get(c);
            }
            return current.isLast;
        }

        public boolean delete(Node node, String word, int index) {
            char c = word.charAt(index);
            if (!node.child.containsKey(c)) {
                return false;
            }
            Node cur = node.child.get(c);
            index++;
            if (index == word.length()) {
                if (!cur.isLast) {
                    return false;
                }
                cur.isLast = false;

                if (cur.child.isEmpty()) {
                    node.child.remove(c);
                }
            } else {
                if (!delete(cur, word, index)) {
                    return false;
                }
                if (cur.child.isEmpty() && !cur.isLast) {
                    node.child.remove(c);
                }
            }
            return true;
        }

        public boolean startsWith(String prefix) {
            Node current = root;
            for (char c : prefix.toCharArray()) {
                if (!current.child.containsKey(c)) {
                    return false;
                }
                current = current.child.get(c);
            }
            return true;
        }
    }

    static char[][] map = new char[5][5];
    static boolean[][] visited = new boolean[5][5];
    static int[] dr = {-1, -1, -1, 0, 1, 1, 1, 0};
    static int[] dc = {-1, 0, 1, 1, 1, 0, -1, -1};
    static Trie trie = new Trie();
    static Set<String> found = new HashSet<>();

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        insertDict();

        for (int i = 0; i < 5; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 5; j++) {
                map[i][j] = st.nextToken().charAt(0);
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                dfs(map[i][j] + "", i, j);
            }
        }
        System.out.println(found.size());
    }

    static void insertDict() throws Exception {
        String encoded = "인코딩 된 문자열";

        byte[] data = Base64.getDecoder().decode(encoded);
        Inflater inflater = new Inflater(true);
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
            char c = (char) (data[pos[0]++] & 0xFF);
            Node child = new Node();
            node.child.put(c, child);
            deserialize(data, pos, child);
        }
    }

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
            if (nr < 0 || nr >= 5 || nc < 0 || nc >= 5 || visited[nr][nc]) {
                continue;
            }
            dfs(word + map[nr][nc], nr, nc);
        }
        visited[r][c] = false;
    }

    static boolean deleteWord(String word) {
        return trie.delete(trie.root, word, 0);
    }
}
