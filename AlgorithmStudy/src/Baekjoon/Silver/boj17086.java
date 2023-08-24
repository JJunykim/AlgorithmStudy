package Baekjoon.Silver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

//실버2 아기 상어2
//BFS 사용
public class boj17086 {

	public static int N, M;
	public static int[][] map;
	public static boolean[][] isvisited;
	public static int[][] dxdy = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
	
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		map = new int[N][M];
		isvisited = new boolean[N][M];
		for(int i = 0; i< N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j =0; j< M; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		int ans = 0;
		for(int i = 0; i < N; i++) {
			for(int j =0; j< M; j++) {
				if(map[i][j] == 0) {
					int temp = BFS(i, j);
					if(temp > ans) ans = temp;
					
				}
			}
		}
		System.out.println(ans);
			
	}
	public static int BFS(int r, int c) {
		
		isvisited = new boolean[N][M];
		isvisited[r][c] = true;
		Queue<int[]> queue = new LinkedList<int[]>();
		queue.add(new int[] {r, c, 0});
		boolean flag = false;
		int dis = 0;
		
		while(!queue.isEmpty()) {
			int[] temp = queue.poll();
			dis = temp[2] + 1;
			for(int d = 0; d < 8; d++) {
				int nr = temp[0] + dxdy[d][0];
				int nc = temp[1] + dxdy[d][1];
				if(nr >= 0 && nr < N && nc >= 0 && nc < M && !isvisited[nr][nc] ) {
					 if(map[nr][nc] == 1) {
						 flag = true;
						 break;
					 }
					 if(map[nr][nc] == 0) {
						 queue.add(new int[] {nr, nc, temp[2] + 1});
						 isvisited[nr][nc] = true;
					 }
				}
				
			}
			if(flag) break;
		}
		
		return dis;
	}

}
