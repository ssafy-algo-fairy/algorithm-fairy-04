package B1818;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    static int N;
    static int[] arr;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        StringTokenizer st = new StringTokenizer(br.readLine());
        arr = new int[N];
        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        int[] tails = new int[N + 1];
        int size = 0;

        for (int i = 0; i < N; i++) {
            int num = arr[i];

            if (size == 0 || tails[size - 1] < num) {
                tails[size++] = num;
            } else {
                int lo = 0, hi = size;
                while (lo < hi) {
                    int mid = (lo + hi) / 2;
                    if (tails[mid] < num) lo = mid + 1;
                    else hi = mid;
                }
                tails[lo] = num;
            }
        }

        System.out.println(N - size);
    }
}