package Baekjoon.Gold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;

//2023.09.24
//골드1 구슬 탈출 2
//조건 있는 BFS .. 너무 어려웠다.. 한 4시간 걸린거같네
public class boj13460 {

	public static int N, M, ans;
	public static boolean RedGoal, BlueGoal;
	public static int[] red = new int[2], blue = new int[2], hole = new int[2];
	public static char[][] map;
	public static int[][] dxdy = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
	public static void main(String[] args) throws IOException{

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		map = new char[N][M];
		for(int i =0; i< N; i++) {
			String temp = br.readLine();
			for(int j =0; j <M; j++) {
				char a = temp.charAt(j);
				if(a == 'R') {
					red = new int[] {i, j};
					map[i][j] = '.';
				}else if(a == 'B') {
					blue = new int[] {i, j};
					map[i][j] = '.';
				}else if(a == 'O'){
					hole = new int[] {i, j};
					map[i][j] = 'O';
				}else {
					map[i][j] = a;
				}
					
			}
		}
		
		//빨간공이 Hole에 들어갔을 때, 파란공이 Hole에 들어갔을 때
		RedGoal = false; BlueGoal = false;
		ans = -1;
		ballescape();
		System.out.println(ans);

		}
	private static void ballescape() {
		
		Queue<int[]> queue = new LinkedList<int[]>();
		//red r c , blue r c, idx, dir
		queue.add(new int[] {red[0], red[1], blue[0], blue[1], 0, -1});

		while(!queue.isEmpty()) {
			int[] arr = queue.poll();
			if(arr[4] > 10) { //경우가 11번이 넘어가면 종료
				return;
			}
			int beforedir = arr[5]; //이전에 움직인 경로
			for(int d = 0; d < 4; d++) {
				//좌우로 움직였던 거는 상하로만, 상하로만 움직였던 거는 좌우로만
				if(beforedir == 0 || beforedir == 1) {
					if(d == 0 || d == 1) continue;
				}
				if(beforedir == 2 || beforedir == 3) {
					if(d == 2 || d == 3) continue;
				}
				//바로 이동하려는 곳이 벽으로 막혀있으면 다음 방향으로-> 틀림!! 파란색만 움직일 수도 있는 경우가 있음
				//if(map[nr][nc] == '#') continue;
				
//				System.out.println("it's turn " + arr[4]);
				//현재 Red, Blue 위치
				RedGoal = false; BlueGoal = false; //선언을 movedir 위에 해줘야함. 빨강공 위치 = 홀 위치 때문에
				int[] Red = new int[] {arr[0], arr[1]};
				int[] Blue = new int[] {arr[2], arr[3]};
//				System.out.println("move dir : " + d);
//				System.out.println("before Red  : " + Red[0] + " " + Red[1]);
//				System.out.println("before Blue : " + Blue[0] + " " + Blue[1]);
				int[] end = movedir(Red, Blue, d);
//				System.out.println("after Red  : " + end[0] + " " + end[1]);
//				System.out.println("after Blue : " + end[2] + " " + end[3]);
		
				//Red 위치 이동 : {Red[0], Red[1]} -> {end[0], end[1]}
				//Blue 위치 이동 : {Blue[0], Blue[1]} -> {end[2], end[3]}
				//Red 위치 이동에 hole이 있는지  판단
				if(d == 0) {
					if(Range(end[1], Red[1], hole[1], hole[0], Red[0])) RedGoal = true;
					if(Range(end[3], Blue[1], hole[1], hole[0], Blue[0])) BlueGoal = true;
				}else if(d == 1) {
					if(Range(Red[1], end[1], hole[1], hole[0], Red[0])) RedGoal = true;
					if(Range(Blue[1], end[3], hole[1], hole[0], Blue[0])) BlueGoal = true;
					
				}else if(d == 2) {
					if(Range(end[0], Red[0], hole[0], hole[1], Red[1])) RedGoal = true;
					if(Range(end[2], Blue[0], hole[0], hole[1], Blue[1])) BlueGoal = true;
				}else {
					if(Range(Red[0], end[0], hole[0], hole[1], Red[1])) RedGoal = true;
					if(Range(Blue[0], end[2], hole[0], hole[1], Blue[1])) BlueGoal = true;
				}
				
				
				if(RedGoal && !BlueGoal) { //빨강이 들어가면
					ans = arr[4] + 1;
					if(ans == 11) ans = -1; //딱 11이 되는 경우에는 10번이 넘어가는 것이므로 제외해줘야 함.
					return;
				}
//				if(RedGoal && BlueGoal) return; //이거하면 안됨.. 미리 끝나는 경우들이 생김
				if(!RedGoal && !BlueGoal) {
					queue.add(new int[] {end[0], end[1], end[2], end[3], arr[4] + 1, d});

				}
			}

		}
		
	}
	private static boolean Range(int min, int max, int h1, int h2, int same) {
		
		if(min <= h1 && h1 <= max && h2 == same) return true;
		else return false;
		
	}
	private static int[] movedir(int[] rr, int[] bb, int d) {
		
		//Red먼저 옮겨보기
		int cnt = 1;
		Stack<Integer> stack = new Stack<>();
		stack.push(1); //Red는 들어감. 1:Red, 2:Blue
		int r2 = rr[0]; //마지막의 위치
		int c2 = rr[1];
		while(true) {
			r2 = rr[0] + dxdy[d][0]*cnt;
			c2 = rr[1] + dxdy[d][1]*cnt;
			if(r2 >= 0 && r2 < N && c2 >= 0 && c2 < M) {
				if(r2 == bb[0] && c2 == bb[1] && map[r2][c2] == '.') {
					//가는 길에 B를 만나면
					stack.push(2);
				}
//				System.out.println(r2 + " _ " + c2);
				if(map[r2][c2] == '#') {
					//갈 수 없는 길이면
					r2 -= dxdy[d][0];
					c2 -= dxdy[d][1];
					break;
				}
			}else break;
			cnt++; //계속 이동
		}
		boolean movetogether = (stack.size() == 2) ? true : false;
		if(movetogether) { //2개 이면 무조건 Blue가 먼저 말을 놓게 됨. BLue-> Red
//			System.out.println("movetogether");
			bb = new int[] {r2, c2};
			int ndir = 0;
			if(d == 0) ndir = 1;
			else if(d == 1) ndir = 0;
			else if(d == 2) ndir = 3;
			else ndir = 2;
			rr = new int[] {r2 + dxdy[ndir][0], c2 + dxdy[ndir][1]};
			
		}else {
			//Red만 움직여 주기
			rr = new int[] {r2, c2};
			map[rr[0]][rr[1]] = 'R';
			//Blue는 따로 움직여 주기
			int cnt2 = 1;
			int br2 = bb[0];
			int bc2 = bb[1];
			while(true) {
				br2 = bb[0] + dxdy[d][0]*cnt2;
				bc2 = bb[1] + dxdy[d][1]*cnt2;
				if(br2 >= 0 && br2 < N && bc2 >= 0 && bc2 < M) {
					if(map[br2][bc2] == '#' || map[br2][bc2] == 'R') {
						break;
					}
				}
				cnt2++;
			}
			bb = new int[] { br2 - dxdy[d][0], bc2 - dxdy[d][1]};
			map[rr[0]][rr[1]] = '.';
			
			//빨강이 위치하는 곳이 hole이여서 빨강 빠지고 파랑 빠질 수 있으니까 그 경우는 따로 조건처리해줌
			if(rr[0] == hole[0] && rr[1] == hole[1] 
					&& (rr[0]-bb[0] == dxdy[d][0]) && (rr[1]-bb[1] == dxdy[d][1])){
				BlueGoal = true;
			}
		}
	
		return new int[] {rr[0], rr[1], bb[0], bb[1]};
	}
	

}

/*
3 5
#####
#ORB#
#####

result : -1

9 9
#########
###.....#
#..#..#O#
##..R.#B#
####....#
#.....#.#
#.#.....#
##.###..#
#########

result : -1
*/