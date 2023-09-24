package Baekjoon.Gold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

//2023.09.23
//골드1 미네랄2 + boj2933 미네랄 동일 코드
//단순 구현인데 클러스터를 어떻게 쓰느냐가 중요
public class boj18500 {

	public static int R, C, N, H;
	public static int[][] map;
	public static int[][] dxdy = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		R = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		map = new int[R][C];
		for(int i =0; i< R; i++) {
			String temp = br.readLine();
			for(int j = 0; j < C; j++) {
				if(temp.charAt(j) == '.') map[i][j] = 0;
				else map[i][j] = 1;
			}
		}
		N = Integer.parseInt(br.readLine());
		st = new StringTokenizer(br.readLine());
		for(int n = 1; n <= N; n++) {
			//n이 홀수이면 왼->오 , 짝수이면 오->왼
			int temp = Integer.parseInt(st.nextToken()); //막대를 던진 높이
			H = R - temp; //row 
			if(n % 2 == 1) { //n이 홀수일때
				mineral(0, C, 1);
			}else { //짝수일때
				mineral(C-1, -1, -1);
			}
		}
		for(int i = 0; i< R; i++) {
			for(int j =0;j < C; j++) {
				if(map[i][j] == 0) {
					System.out.print('.');
				}else {
					System.out.print('x');
				}
			}
			System.out.println();
		}

	}
	private static void mineral(int start, int end, int type) {
		
		//1. 막대가 날아간다.
		while(start!=end) {
			if(map[H][start] == 1) {
				map[H][start] = 0; //미네랄이 파괴된다.
				// 남은 클러스터 분리.
				divcluster();
				break;
			}
			start += type;
		}
		
	}
	private static void divcluster() {
		
		int idx = 2;
		int[][] temp = new int[R][C];
		for(int i = 0; i< R; i++) {
			for(int j =0; j< C; j++) {
				temp[i][j] = map[i][j];
			}
		}
		boolean[][] visited = new boolean[R][C];
		for(int r = 0; r < R; r++) { //위에 있는 군집
			for(int c = 0; c < C; c++) {
				if(temp[r][c] == 1 && !visited[r][c]){
					map[r][c] = idx;
					Queue<int[]> queue = new LinkedList<int[]>();
					Queue<int[]> group = new LinkedList<int[]>();
					queue.add(new int[] {r, c});
					group.add(new int[] {r, c});
					visited[r][c] = true;
					while(!queue.isEmpty()) {
						int[] arr = queue.poll();
						for(int d = 0; d < 4; d++) {
							int nr = arr[0] + dxdy[d][0];
							int nc = arr[1] + dxdy[d][1];
							if( isRange(nr, nc) && !visited[nr][nc] && temp[nr][nc] == 1) {
								visited[nr][nc] = true;
								map[nr][nc] = idx;
								queue.add(new int[] {nr, nc});
								group.add(new int[] {nr, nc});
							}
						}
					}

					//중력에 의해 내려갈 수 있는지 확인
					int groupsize = group.size();
					int mindir = 0; //중력에 의해 움직일 수 있는 최소 칸수
					for(int i =0; i< groupsize; i++) {
						int[] to = group.poll();
						int dir = 1;
						while(true) {
							if(!isRange(to[0]+dir, to[1]) || map[to[0]+dir][to[1]] == 1) {
								break;
							}
							dir++;
						}
						if(i == 0) mindir = dir;
						if(mindir > dir) mindir = dir;
						group.add(to);
					}

					//mindir 만큼 아래로 움직임
					for(int i = 0; i< groupsize; i++) {
						int[] loc = group.poll();
						map[loc[0]+mindir-1][loc[1]] = 1;
						group.add(loc);
					}
					while(!group.isEmpty()) {
						int[] loc = group.poll();
						if(map[loc[0]][loc[1]] == idx) {
							map[loc[0]][loc[1]] = 0;
						}
					}
					idx++;
				}
			}
		}
		
	}
	public static boolean isRange(int r, int c) {
		if(r >= 0 && r < R && c >= 0 && c < C) return true;
		return false;
	}
	

}
