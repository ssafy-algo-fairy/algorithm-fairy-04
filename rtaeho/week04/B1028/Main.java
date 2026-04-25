package rtaeho.week04.B1028;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int R = Integer.parseInt(st.nextToken());
        int C = Integer.parseInt(st.nextToken());

        int[][] g = new int[R + 2][C + 2];
        for (int i = 1; i <= R; i++) {
            String line = br.readLine();
            for (int j = 1; j <= C; j++) {
                g[i][j] = line.charAt(j - 1) - '0';
            }
        }

        int[][] lu = new int[R + 2][C + 2]; // 왼쪽위
        int[][] ru = new int[R + 2][C + 2]; // 오른쪽위
        int[][] ld = new int[R + 2][C + 2]; // 왼쪽아래
        int[][] rd = new int[R + 2][C + 2]; // 오른쪽아래

        // 왼쪽위
        for (int i = 1; i <= R; i++) {
            for (int j = 1; j <= C; j++) {
                lu[i][j] = g[i][j] == 1 ? lu[i - 1][j - 1] + 1 : 0;
            }
        }
        // 오른쪽위
        for (int i = 1; i <= R; i++) {
            for (int j = C; j >= 1; j--) {
                ru[i][j] = g[i][j] == 1 ? ru[i - 1][j + 1] + 1 : 0;
            }
        }

        // 왼쪽아래
        for (int i = R; i >= 1; i--) {
            for (int j = 1; j <= C; j++) {
                ld[i][j] = g[i][j] == 1 ? ld[i + 1][j - 1] + 1 : 0;
            }
        }

        // 오른쪽아래
        for (int i = R; i >= 1; i--) {
            for (int j = C; j >= 1; j--) {
                rd[i][j] = g[i][j] == 1 ? rd[i + 1][j + 1] + 1 : 0;
            }
        }

        int ans = 0;

        for (int r = 1; r <= R; r++) {
            for (int c = 1; c <= C; c++) {
                // 왼쪽아래와 오른쪽아래 중에 작은 값을 기준으로 진행
                int maxKtop = Math.min(ld[r][c], rd[r][c]);
                for (int k = maxKtop; k > ans; k--) {
                    int bottomRow = r + 2 * (k - 1);
                    if (bottomRow > R) {
                        continue;
                    }
                    if (lu[bottomRow][c] >= k && ru[bottomRow][c] >= k) {
                        ans = k;
                        break;
                    }
                }
            }
        }

        System.out.println(ans);
    }
}
