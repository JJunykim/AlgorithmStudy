package Codetree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
//삼성 SW 역량테스트 2023 상반기 오전 2번 문제  11:30
//토끼와 경주
public class RabbitAndRace {
	
	public static class Pair{ //좌표 저장
		int x, y;

		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	public static class Rabbit implements Comparable<Rabbit>{
		int jcnt, x, y, pid;
		
		public Rabbit(int jcnt, int x, int y, int pid) {
			this.jcnt = jcnt;
			this.x = x;
			this.y = y;
			this.pid = pid;
		}

		@Override
		public int compareTo(Rabbit o) {
			if(this.jcnt == o.jcnt) {
				if((this.x + this.y) == (o.x + o.y) ) {
					if(this.x == o.x) {
						return this.y == o.y ? this.pid - o.pid : this.y - o.y;
					}else return this.x - o.x; //행번호가 작은 토끼
				}else return (this.x + this.y) - (o.x + o.y); //2. 행+열 번호 작은 토끼 
			}else return this.jcnt - o.jcnt; //1. 점프 횟수가 적은 토끼
			
// (참고) 이렇게 표현할 수도 있다.
//			if(this.jcnt != o.jcnt) 
//		        return this.jcnt - o.jcnt;
//		    if(this.x + this.y != o.x + o.y) 
//		        return (this.x + this.y) - (o.x + o.y);
//		    if(this.x != o.x) 
//		        return this.x - o.x;
//		    if(y != o.y) 
//		        return this.y - o.y;
//		    return this.pid - o.pid;
		
		}
		public void print() {
			System.out.println("("+ x + ", " + y + ") " + jcnt + " " + pid);
		}
		
	}
	
	public static StringTokenizer st;
	public static final int MAX_N = 2000;
	public static int N, M, P, Q;
	public static long ans; //Long 타입으로 변경 필수
	public static int[] pid = new int[MAX_N + 1];
	public static int[] pw = new int[MAX_N + 1];
	//각 토끼의 현재 위치(좌표)를 기록
	public static Pair[] point = new Pair[MAX_N + 1];
	//각 토끼의 점프 횟수를 기록
	public static int[] jmpcnt = new int[MAX_N + 1];
	//각 토끼의 점수를 기록
	public static long[] result = new long[MAX_N + 1];
	//각 토끼의 pid(고유번호)를 인덱스 번호를 변환해줌 (입력 : 고유번호, 출력 : 인덱스)
	public static HashMap<Integer, Integer> pidToIdx = new HashMap<>();

	public static int[][] dxdy = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
	//200 경주진행에서 우선순위가 큰 위치 판단 -> true이면 b로 바꿔줘야 함.
	public static boolean cmp(Rabbit a, Rabbit b) {
		if(a.x + a.y != b.x + b.y) return a.x + a.y < b.x + b.y;
		if(a.x != b.x) return a.x < b.x;
		if(a.y != b.y) return a.y < b.y;
		return a.pid < b.pid;
	}
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		Q = Integer.parseInt(st.nextToken());
		for(int turn = 1; turn <= Q; turn++) {
			st = new StringTokenizer(br.readLine());
			int Order = Integer.parseInt(st.nextToken());
			if(Order == 100) {
//				System.out.println("100");
				init();
			}
			if(Order == 200) { // 200 K S
//				System.out.println("200");
				int K = Integer.parseInt(st.nextToken());
				int S = Integer.parseInt(st.nextToken());
				startRound(K, S);
			}
			if(Order == 300) { //300 pid_t L
//				System.out.println("300");
				int pid_t = Integer.parseInt(st.nextToken());
				int L = Integer.parseInt(st.nextToken());
				changeDistance(pid_t, L);
			}
			if(Order == 400){ //Order == 400
//				System.out.println("400");
				printResult();
				break; //종료
			}
		}
	}
	private static void printResult() {
		ans = 0;
		for(int i = 1 ; i <= P; i++) {
			if(ans < result[i]) ans = result[i];
		}
		System.out.println(ans);
	}
	private static void changeDistance(int pid_t, int l) {
		//고유번호가 pid인 토끼의 이동거리르 L배 해준다.
		pw[pidToIdx.get(pid_t)] *= l;
	}
	private static void startRound(int K, int S) {
		
		PriorityQueue<Rabbit> pq = new PriorityQueue<>();
		boolean[] isValid = new boolean[P+1]; //뽑힌적이 있는 토끼 체크
		for(int i = 1; i<= P; i++) {
			Rabbit newRabbit = new Rabbit(jmpcnt[i], point[i].x, point[i].y, pid[i]);
			pq.add(newRabbit);
			isValid[i] = false; //초기화
		}
		
		
		//경주를 K번 진행
		while(K-- > 0) {
		
			//우선순위 가장 높은 토끼 뽑기
			Rabbit curRabbit = pq.poll();
//			curRabbit.print();
			int id = curRabbit.pid; //i번째 토끼 고유 id
			int nextidx = pidToIdx.get(id); //인덱스 
			int dis = pw[nextidx]; //움직여야 하는 거리수
			isValid[nextidx] = true;
			
			Rabbit nextRabbit = new Rabbit(curRabbit.jcnt, 0, 0, curRabbit.pid);
			//토끼를 위로 이동
			Rabbit upRabbit = getUpRabbit(new Rabbit(curRabbit.jcnt, curRabbit.x, curRabbit.y, curRabbit.pid), dis);
//			upRabbit.print();
			if(cmp(nextRabbit, upRabbit)) nextRabbit = upRabbit;
			//토끼를 아래로 이동
			Rabbit downRabbit = getDownRabbit(new Rabbit(curRabbit.jcnt, curRabbit.x, curRabbit.y, curRabbit.pid), dis);
//			downRabbit.print();
			if(cmp(nextRabbit, downRabbit)) nextRabbit = downRabbit;
			//토끼를 오른쪽으로 이동
			Rabbit rightRabbit = getRightRabbit(new Rabbit(curRabbit.jcnt, curRabbit.x, curRabbit.y, curRabbit.pid), dis);
//			rightRabbit.print();
			if(cmp(nextRabbit, rightRabbit)) nextRabbit = rightRabbit;
			//토끼를 왼쪽으로 이동
			Rabbit leftRabbit = getLeftRabbit(new Rabbit(curRabbit.jcnt, curRabbit.x, curRabbit.y, curRabbit.pid), dis);
//			leftRabbit.print();
			if(cmp(nextRabbit, leftRabbit)) nextRabbit = leftRabbit;
			
			//nextRabbit 위치 변경
			point[nextidx] = new Pair(nextRabbit.x, nextRabbit.y);
			//jump 횟수 갱신
			nextRabbit.jcnt++;
//			nextRabbit.print();
			pq.add(nextRabbit);
			jmpcnt[nextidx]++;
			
			
			//idx번째 토끼를 제외한 나머지 P-1마리의 토끼들 점수 추가
			for(int i =1; i<= P; i++) {
				if(i == nextidx) continue;
				result[i] += (nextRabbit.x+1 + nextRabbit.y+1);
			}
//			printresult();
			
		}
		
		//K번 턴이 진행된 이후, 우선순위를 두었을 떄 가장 높은 우선순위의 토끼에 점수 S를 더해줌
		Rabbit plusRabbit = new Rabbit(0, 0, 0, 0);
		for(int i = 1; i<= P; i++) {
			if(isValid[i]) {
				if(plusRabbit.x + plusRabbit.y < point[i].x + point[i].y) {
					//우선순위 1 : 행번호 + 열번호가 큰 칸
					plusRabbit = new Rabbit(0, point[i].x, point[i].y, pid[i]);
				}else if(plusRabbit.x + plusRabbit.y == point[i].x + point[i].y){
					if(plusRabbit.x < point[i].x) {
						//우선순위 2 : 행번호가 큰 토끼
						plusRabbit = new Rabbit(0, point[i].x, point[i].y, pid[i]);
					}else if(plusRabbit.x == point[i].x) {
						if(plusRabbit.y < point[i].y ) {
							//우선순위 3 : 열번호가 큰 토끼
							plusRabbit = new Rabbit(0, point[i].x, point[i].y, pid[i]);
						}else if(plusRabbit.y == point[i].y) {
							if(plusRabbit.pid < pid[i]) {
								//우선순위 4: 고유번호가 큰 토끼
								plusRabbit = new Rabbit(0, point[i].x, point[i].y, pid[i]);
							}
						}
					}
				}
			}
			
		}
		result[pidToIdx.get(plusRabbit.pid)] += S;
		
	}

	private static Rabbit getLeftRabbit(Rabbit rabbit, int dis) {
		Rabbit leftRabbit = rabbit;
		if(rabbit.y >= dis) {
			leftRabbit.y = rabbit.y - dis;
		}else {
			dis -= (rabbit.y+1);
			dis = dis % (2*(M-1));
			if(dis < M-1) {
				leftRabbit.y = 1 + dis;
			}else {
				leftRabbit.y = (M-1) * 2 - 1- dis;
			}
		}
		return leftRabbit;
	}
	private static Rabbit getRightRabbit(Rabbit rabbit, int dis) {
		Rabbit rightRabbit = rabbit;
		if(rabbit.y + dis < M) {
			rightRabbit.y = rabbit.y + dis;
		}else {
			dis -= (M - rabbit.y);
			dis = dis % (2*(M-1));
			if(dis < (M-1)) {
				rightRabbit.y = M - 1 - (dis+1);
			}else {
				rightRabbit.y = dis - (M-2);
			}
		}
		return rightRabbit;
	}
	private static Rabbit getDownRabbit(Rabbit rabbit, int dis) {
		Rabbit downRabbit = rabbit;
		if(rabbit.x + dis < N) {
			downRabbit.x = rabbit.x + dis;
		}else {
			dis -= (N - rabbit.x);
			dis = dis % (2*(N-1));
			if(dis < (N-1)) {
				downRabbit.x = N - 1 - (dis+1);
			}else {
				downRabbit.x = dis - (N-2);
			}
		}
		return downRabbit;
	}
	private static Rabbit getUpRabbit(Rabbit rabbit, int dis) {
		Rabbit upRabbit = rabbit;
		if(rabbit.x >= dis) {
			upRabbit.x = rabbit.x - dis;
		}else {
			dis -= (rabbit.x+1);
			dis = dis % (2*(N-1));
			if(dis < N-1) {
				upRabbit.x = 1 + dis;
			}else {
				upRabbit.x = (N-1) * 2 - 1- dis;
			}
		}
		return upRabbit;
	}
	public static void init() {
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken()); //격자 크기
		P = Integer.parseInt(st.nextToken()); //P마리 토끼
		for(int i =1; i<= P; i++) {
			pid[i] = Integer.parseInt(st.nextToken());
			pw[i] = Integer.parseInt(st.nextToken());
			pidToIdx.put(pid[i], i); 
			point[i] = new Pair(0, 0);
		}
	}

}

/*
5
100 3 5 2 10 2 20 5
200 6 100
300 10 2
200 3 20
400
*/
