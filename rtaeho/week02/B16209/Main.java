package rtaeho.week02.B16209;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        StringTokenizer st = new StringTokenizer(br.readLine());

        List<Integer> negative = new ArrayList<>();
        List<Integer> positive = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            int x = Integer.parseInt(st.nextToken());
            if (x < 0) {
                negative.add(x);
            } else {
                positive.add(x);
            }
        }

        positive.sort(Collections.reverseOrder());
        Collections.sort(negative);

        Deque<Integer> posDeque = new ArrayDeque<>();
        boolean addToFront = true;
        for (int x : positive) {
            if (addToFront) {
                posDeque.addFirst(x);
            } else {
                posDeque.addLast(x);
            }
            addToFront = !addToFront;
        }

        Deque<Integer> negDeque = new ArrayDeque<>();
        addToFront = true;
        for (int x : negative) {
            if (addToFront) {
                negDeque.addFirst(x);
            } else {
                negDeque.addLast(x);
            }
            addToFront = !addToFront;
        }

        List<Integer> posList = new ArrayList<>(posDeque);
        List<Integer> negList = new ArrayList<>(negDeque);

        StringBuilder sb = new StringBuilder();

        if (negative.isEmpty()) {
            for (int x : posList) {
                sb.append(x).append(' ');
            }
        } else if (positive.isEmpty()) {
            for (int x : negList) {
                sb.append(x).append(' ');
            }
        } else {
            int posFirst = posList.get(0);
            int posLast = posList.get(posList.size() - 1);
            int negFirst = negList.get(0);
            int negLast = negList.get(negList.size() - 1);

            if (posFirst < posLast) {
                Collections.reverse(posList);
            }
            if (Math.abs(negFirst) > Math.abs(negLast)) {
                Collections.reverse(negList);
            }

            for (int x : posList) {
                sb.append(x).append(' ');
            }
            for (int x : negList) {
                sb.append(x).append(' ');
            }
        }

        System.out.println(sb.toString().trim());
    }
}
