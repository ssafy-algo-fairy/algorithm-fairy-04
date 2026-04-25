package rtaeho.week04.B1006;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    static final int INF = 1 << 29;
    static int N, W;
    static int[] inner, outer;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine().trim());

        while (T-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            W = Integer.parseInt(st.nextToken());

            inner = new int[N];
            outer = new int[N];

            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < N; i++)
                inner[i] = Integer.parseInt(st.nextToken());
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < N; i++)
                outer[i] = Integer.parseInt(st.nextToken());

            sb.append(solve()).append('\n');
        }
        System.out.print(sb);
    }

    static int solve() {
        if (N == 1) {
            return (inner[0] + outer[0] <= W) ? 1 : 2;
        }

        int ans = INF;

        // 케이스 1: 원형 묶음 없음
        int s0 = (inner[0] + outer[0] <= W) ? 1 : 2;
        int[][] dp1 = runDp(s0, 1, 1, 0);
        ans = Math.min(ans, dp1[N - 1][0]);

        // 케이스 2: inner[0] - inner[N-1] 원형 묶음
        if (inner[0] + inner[N - 1] <= W) {
            int[][] dp2 = runDp(1, 0, INF, INF);
            ans = Math.min(ans, dp2[N - 1][2] + 1);
        }

        // 케이스 3: outer[0] - outer[N-1] 원형 묶음
        if (outer[0] + outer[N - 1] <= W) {
            int[][] dp3 = runDp(1, INF, 0, INF);
            ans = Math.min(ans, dp3[N - 1][1] + 1);
        }

        // 케이스 4: 둘 다 원형 묶음
        if (inner[0] + inner[N - 1] <= W && outer[0] + outer[N - 1] <= W) {
            int[][] dp4 = runDp(0, INF, INF, INF);
            ans = Math.min(ans, dp4[N - 1][3] + 2);
        }

        return ans;
    }

    static int[][] runDp(int s0, int s1, int s2, int s3) {
        int[][] dp = new int[N][4];
        for (int i = 0; i < N; i++) {
            dp[i][0] = dp[i][1] = dp[i][2] = dp[i][3] = INF;
        }
        dp[0][0] = s0;
        dp[0][1] = s1;
        dp[0][2] = s2;
        dp[0][3] = s3;

        for (int i = 0; i < N - 1; i++) {
            int j = i + 1;
            int v;

            // state 0: i열 둘 다 커버
            v = dp[i][0];
            if (v < INF) {
                if (inner[j] + outer[j] <= W && v + 1 < dp[j][0])
                    dp[j][0] = v + 1;
                if (v + 2 < dp[j][0])
                    dp[j][0] = v + 2;
                if (v + 1 < dp[j][1])
                    dp[j][1] = v + 1;
                if (v + 1 < dp[j][2])
                    dp[j][2] = v + 1;
                if (v < dp[j][3])
                    dp[j][3] = v;
            }

            // state 1: outer[i] 미커버 → outer[j]와 가로 묶음 필수
            v = dp[i][1];
            if (v < INF && outer[i] + outer[j] <= W) {
                if (v + 2 < dp[j][0])
                    dp[j][0] = v + 2;
                if (v + 1 < dp[j][2])
                    dp[j][2] = v + 1;
            }

            // state 2: inner[i] 미커버 → inner[j]와 가로 묶음 필수
            v = dp[i][2];
            if (v < INF && inner[i] + inner[j] <= W) {
                if (v + 2 < dp[j][0])
                    dp[j][0] = v + 2;
                if (v + 1 < dp[j][1])
                    dp[j][1] = v + 1;
            }

            // state 3: 둘 다 미커버
            v = dp[i][3];
            if (v < INF && inner[i] + inner[j] <= W && outer[i] + outer[j] <= W) {
                if (v + 2 < dp[j][0])
                    dp[j][0] = v + 2;
            }
        }
        return dp;
    }
}