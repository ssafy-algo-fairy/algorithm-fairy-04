package rtaeho.week02.B1300;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        long k = Long.parseLong(br.readLine());

        long left = 1;
        long right = k;
        long answer = 0;

        while (left <= right) {
            // i행의 j열은 i * j니까 i번째 행은 i, 2*i, 3*i 이런식
            // 이 중 mid 이하인 애들은 i * j <= mid 이고
            // j <= mid / i 이니까 j의 최대는 mid / i

            long mid = (left + right) / 2;
            long count = 0;

            // i행에 mid 이하인 애들은 mid / i개
            // 어차피 N보다는 못 커지니까 min으로 잘라줌
            for (int i = 1; i <= N; i++) {
                count += Math.min(mid / i, N);
            }

            if (count < k) {
                left = mid + 1;
            } else {
                answer = mid;
                right = mid - 1;
            }
        }

        System.out.print(answer);
    }
}
