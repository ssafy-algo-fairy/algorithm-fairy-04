package B6523;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        while (true) {
            String line = br.readLine();
            StringTokenizer st = new StringTokenizer(line);
            long N = Long.parseLong(st.nextToken());

            if (N == 0) {
                break;
            }

            long a = Long.parseLong(st.nextToken());
            long b = Long.parseLong(st.nextToken());

            // 플로이드 순환 찾기 알고리즘(토끼와 거북이 알고리즘)

            // 1단계: 토끼와 거북이를 '순환 고리' 안으로 집어넣기
            // 거북이는 1칸(next), 토끼는 2칸(next-next)씩 이동
            // 순환이 있다면, 토끼는 반드시 거북이를 따라잡음
            long tortoise = next(0, a, b, N);
            long hare = next(next(0, a, b, N), a, b, N);

            while (tortoise != hare) {
                tortoise = next(tortoise, a, b, N);
                hare = next(next(hare, a, b, N), a, b, N);
            }
            // 토끼와 거북이가 만남
            // 근데 여기가 순환 시작점인지는 모름

            // 2단계: 순환의 입구(첫 번째 재방문자) 찾기
            // 수학적 원리(하단 작성)에 의해, 거북이를 시작점(0)으로 보내고 둘 다 1번씩만 이동시키면
            // 정확히 순환이 시작되는 곳에서 다시 만남
            tortoise = 0;
            while (tortoise != hare) {
                // 거북이: 시작점부터 1칸씩 (첫 방문 중)
                tortoise = next(tortoise, a, b, N);
                // 토끼: 만난 지점부터 1칸씩 (고리 도는 중)
                hare = next(hare, a, b, N);
            }
            // 여기까지 오면 이 위치가 순환의 입구
            // 거북이는 처음 온 거지만, 토끼는 한 바퀴 돌고 두번째 옴
            // 여기가 두 번 불린 사람

            // 3단계: 순환 고리의 길이(술 마시는 사람 수) 측정하기
            // 여기서부터 한 바퀴 돌면서 고리 안에 있는 사람 수 세기
            // 이 고리 안에 있는 사람들은 전부 2번씩 불리게 되어 술을 마셔야 함
            long cycleLength = 1;
            long current = next(tortoise, a, b, N);
            while (current != tortoise) {
                current = next(current, a, b, N);
                cycleLength++;
            }

            sb.append(N - cycleLength).append("\n");
        }
        System.out.print(sb);
    }

    static long next(long x, long a, long b, long N) {
        long x2 = (x * x) % N;
        long ax2 = (a * x2) % N;
        return (ax2 + b) % N;
    }
}

/*
1. 용어 정의
- L: 시작점(0)에서 순환 입구까지의 거리
- C: 순환 고리 전체의 한 바퀴 길이
- d: 순환 입구에서 토끼와 거북이가 처음 만난 지점까지의 거리
- n: 토끼가 거북이를 만나기 위해 고리를 돈 횟수

2. 제1원리: 첫 만남의 방정식 (속도 차이)
거북이가 1칸 이동할 때 토끼는 2칸 이동하므로, 둘이 만난 시점의 총 이동 거리는 다음과 같습니다.
- 거북이의 이동 거리(S): L + d
- 토끼의 이동 거리(F): 2 * S = 2(L + d)

이때 토끼는 거북이보다 순환 고리를 n바퀴 더 돌아서 같은 위치에 선 것이므로, 다음과 같은 식도 성립합니다.
- F = L + d + nC

두 식을 연립하면 다음과 같은 결론이 나옵니다.
- 2(L + d) = L + d + nC
- L + d = nC (시작부터 만남까지의 거리는 고리 길이의 배수입니다.)

3. 제2원리: 입구를 찾는 마법 (L = nC - d)
위의 식을 L에 대해 정리하면 L = nC - d 가 됩니다. 이 식의 의미가 알고리즘의 핵심입니다.
- 좌변(L): 시작점에서 입구까지 가는 거리
- 우변(nC - d): 만난 지점에서 고리를 마저 돌아 다시 입구로 돌아오는 거리

따라서 거북이를 시작점(0)으로 보내고 토끼를 만난 지점에 둔 뒤 똑같이 1칸씩 이동시키면, 거북이가 L만큼 움직여 입구에 도착할 때 토끼도 고리 안에서 L(즉, nC - d)만큼 움직여 정확히 입구 위치에 도달하게 됩니다.

4. 문제 해결과의 연결
이 수학적 원리를 문제의 조건에 대입하면 다음과 같은 결론이 나옵니다.
- 고리 밖 사람들 (L 명): 평생 딱 1번만 불리고 끝납니다. (술 안 마심)
- 고리 안 사람들 (C 명): 순환 구조 때문에 반드시 2번 불리게 됩니다. (술 당첨)
- 게임 종료: 입구에 있는 사람이 3번 불리려는 찰나(고리를 두 바퀴 돌았을 때) 모두 자리를 박차고 일어납니다.

최종 정답: 전체 인원 N - 술 마시는 사람 수 C = N - C
 */