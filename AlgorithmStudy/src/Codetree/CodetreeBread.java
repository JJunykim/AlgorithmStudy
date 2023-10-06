package Codetree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

//삼성 SW 역량테스트 2022 하반기 오후 1번 문제  11:20~13:00
//코드트리 빵
//BFS찾아서 한칸 가기 -> BFS 쓰면서 이동 경로를 따로 저장해야할 필요 있음 backX[][] backY[][] 사용
public class CodetreeBread {

	public static int n, m, t=1;
	public static boolean flag;
	public static int[][] map;
	public static boolean[][] isValid, isvisited;
	public static int[][] Man, ManEnd;
	public static int[][] backX, backY;
	public static int[][] dxdy = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};
	public static void main(String[] args) throws IOException{
	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		map = new int[n][n];
		isValid = new boolean[n][n];
		for(int i =0; i< n ; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j =0 ; j< n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		Man = new int[m+1][3]; //도착 여부(0/1), 현재 r,c
		ManEnd = new int[m+1][2]; //도착해야하는 최종 장소
		for(int i =1; i<= m ; i++) {
			st = new StringTokenizer(br.readLine());
				int r = Integer.parseInt(st.nextToken()) -1;
				int c = Integer.parseInt(st.nextToken()) -1;
				ManEnd[i] = new int[] {r,c};
				Man[i][0] = 1;
		}
		backX = new int[n][n];
		backY = new int[n][n];
		flag = false;
		while(true) {
			//1. 격자에 있는 사람들 모두가 움직임 DFS로 
			MoveOneStep();
			//2. 편의점 도착
			checkArrived();
			if(flag && t != 1) break;
			//3. t <= m 베이스캠프 들어감
			if(t <= m)
				InBaseCamp();
			t++; //1분 지남
//			for(int i =1; i<= m; i++) {
//				System.out.println(i+ " : " + Man[i][0] + " (" + Man[i][1] + ", " + Man[i][2]+ " )");
//			}
		}
		System.out.println(t);
	}
	private static void checkArrived() {
		//편의점에 도착했는지 보는 것
		boolean finish = true;
		for(int i = 1; i<= m; i++) {
			if(Man[i][0] == 0 && Man[i][1] == ManEnd[i][0] && Man[i][2] == ManEnd[i][1]) {
				//편의점에 도착했으면
				isValid[Man[i][1]][Man[i][2]] = true; //못움직임
				Man[i][0] = 1;
			}
		}
		for(int i = 1; i<= m; i++) {
			if(Man[i][0] == 0) finish = false;
		}
		if(finish && t > m) flag = true;
		
	}
	private static void MoveOneStep() {
		
		for(int i = 1; i<= m; i++) {
			if(Man[i][0] == 1) continue;
			MoveMan(i);
		}
		
	}
	private static void MoveMan(int idx) {
		//BFS로 한칸 이동
		isvisited = new boolean[n][n]; //BFS용 방문기록 체크
		isvisited[Man[idx][1]][Man[idx][2]] = true;
		//BFS 이전 위치 기록
		backX = new int[n][n];
		backY = new int[n][n];
		Queue<int[]> queue = new LinkedList<int[]>();
		queue.add(new int[] {Man[idx][1], Man[idx][2], 1});
		while(!queue.isEmpty()) {
			int[] arr = queue.poll();
			
			if(arr[0] == ManEnd[idx][0] && arr[1] == ManEnd[idx][1]) {
				int cx = arr[0]; int cy = arr[1];
				while(true) {
					if(backX[cx][cy] == Man[idx][1] && backY[cx][cy] == Man[idx][2]) {
						Man[idx] = new int[] {0, cx, cy};
						break;
					}
					int nextCx = backX[cx][cy];
					int nextCy = backY[cx][cy];
					cx = nextCx;
					cy = nextCy;
				}
				break;
			}
			for(int d = 0; d < 4; d++) {
				int nr = arr[0] + dxdy[d][0];
				int nc = arr[1] + dxdy[d][1];
				if(isRange(nr, nc) && !isValid[nr][nc] && !isvisited[nr][nc]) {
					isvisited[nr][nc] = true;
					queue.add(new int[] {nr, nc, arr[2]+1});
					backX[nr][nc] = arr[0];
					backY[nr][nc] = arr[1];
				}
			}
		}
		queue.clear();
		
	}
	private static void InBaseCamp() {
		//t번째 사람이 베이스 캠프에 들어감
		int[] start = new int[] {ManEnd[t][0], ManEnd[t][1]}; 
		isvisited = new boolean[n][n];  //길찾기용 visited
		isvisited[start[0]][start[1]] = true;
		Queue<int[]> queue = new LinkedList<int[]>();
		queue.add(new int[] {start[0], start[1]});
		while(!queue.isEmpty()) {
			int[] arr = queue.poll();
			if(map[arr[0]][arr[1]] == 1 && !isValid[arr[0]][arr[1]]) {
				isValid[arr[0]][arr[1]] = true;
				Man[t] = new int[] {0, arr[0], arr[1]};
				break;
			}
			for(int d = 0 ; d < 4; d++) {
				int nr = arr[0] + dxdy[d][0];
				int nc = arr[1] + dxdy[d][1];
				if(isRange(nr, nc) && !isValid[nr][nc] && !isvisited[nr][nc]) {
					isvisited[nr][nc] = true;
					queue.add(new int[] {nr, nc});
				}
			}
		}
		queue.clear();

	}
	
	private static boolean isRange(int r, int c) {
		if(r >= 0 && r < n && c >= 0 && c < n) return true;
		else return false;
	}

}
