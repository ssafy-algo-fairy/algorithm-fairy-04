package rtaeho.week03.B28132;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        Map<String, Integer> map = new HashMap<>();
        long zeroCount = 0;
        long answer = 0;

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            long x = Long.parseLong(st.nextToken());
            long y = Long.parseLong(st.nextToken());

            if (x == 0 && y == 0) {
                // 0벡터가 나오면 앞서 나온 쌍 다 수직
                answer += i;
                zeroCount++;
                continue;
            }

            long common = gcd(x, y);
            x /= common;
            y /= common;

            // 정규화 해서 법선벡터 찾기
            String target = y + "," + (-x);
            String target2 = (-y) + "," + x;

            answer += map.getOrDefault(target, 0);
            answer += map.getOrDefault(target2, 0);
            answer += zeroCount; // 영벡터랑 이루는 쌍 추가

            String current = x + "," + y;
            map.put(current, map.getOrDefault(current, 0) + 1);
        }

        System.out.print(answer);
    }

    static long gcd(long a, long b) {
        while (b != 0) {
            a %= b;
            long temp = a;
            a = b;
            b = temp;
        }
        return Math.abs(a);
    }
}
