package B1036;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;

public class Main {
    static int N;
    static String[] nums;
    static int K;
    static final BigInteger BASE = BigInteger.valueOf(36);

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        nums = new String[N];
        for (int i = 0; i < N; i++) {
            nums[i] = br.readLine();
        }
        K = Integer.parseInt(br.readLine());

        BigInteger sum = BigInteger.ZERO;
        for (String num : nums) {
            BigInteger val = BigInteger.ZERO;
            for (char c : num.toCharArray()) {
                val = val.multiply(BASE).add(BigInteger.valueOf(getValue(c)));
            }
            sum = sum.add(val);
        }

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

        System.out.println(to36(sum));
    }

    static String to36(BigInteger n) {
        if (n.equals(BigInteger.ZERO)) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();

        while (n.compareTo(BigInteger.ZERO) > 0) {
            int r = n.mod(BASE).intValue();
            sb.append(r < 10 ? (char) ('0' + r) : (char) ('A' + r - 10));
            n = n.divide(BASE);
        }
        return sb.reverse().toString();
    }

    static int getValue(char c) {
        return c >= '0' && c <= '9' ? c - '0' : c - 'A' + 10;
    }
}
