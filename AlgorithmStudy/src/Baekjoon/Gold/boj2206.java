package Baekjoon.Gold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

//2023.09.28
//골드3 벽 부수고 이동하기
//BFS 나눠서 생각하기
public class boj2206 {

	public static int[][] dxdy = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		int[][] map = new int[N][M];
		for(int i = 0; i< N; i++) {
			String s = br.readLine();
			for(int j = 0; j < M; j++) {
				map[i][j] = s.charAt(j) - '0';
			}
		}
		
		
		Queue<int[]> queue = new LinkedList<>();
		queue.add(new int[] {0, 0, 1, 0}); //r, c, dis, 부신적이 있는지 유무
		boolean[][][] isvisited = new boolean[N][M][2];
		isvisited[0][0][0] = true; //벽을 안부셨을 때
		isvisited[0][0][1] = true; //벽을 부셨을 때
		int ans = -1;
		while(!queue.isEmpty()) {
			int[] arr = queue.poll();
//			System.out.println(arr[0] + " " + arr[1] + " " + arr[2]+ " "+arr[3]);
			if(arr[0] == N-1 && arr[1] == M-1) {
				ans = arr[2];
				break;
			}
			
			for(int d = 0; d < 4; d++) {
				int nr = arr[0] + dxdy[d][0];
				int nc = arr[1] + dxdy[d][1];
				if(nr >= 0 && nr < N && nc >= 0 && nc < M) {
					if(map[nr][nc] == 0) { //이동 가능 할 때
						if(arr[3] == 0 && !isvisited[nr][nc][0]) { //부신 벽이 없다.
							queue.add(new int[] {nr, nc, arr[2]+1, 0});
							isvisited[nr][nc][0] = true;
						}else if(arr[3] == 1 && !isvisited[nr][nc][1]) { //부신 벽이 있다.
							queue.add(new int[] {nr, nc, arr[2]+1, 1});
							isvisited[nr][nc][1] = true;
						}
					}else { //벽일 때 
						if(arr[3] == 0) {
							queue.add(new int[] {nr, nc, arr[2]+1, 1});
							isvisited[nr][nc][1] = true;
						}
					}
				}
			}
		}
		System.out.println(ans);
		
	}

}
