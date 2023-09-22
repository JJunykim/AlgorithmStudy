package Baekjoon.Silver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

//2023.09.22
//실버1 안전 영역
//BFS 사용
public class boj2468 {

	public static int[][] dxdy = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		int maxheight = 1;
		int[][] map = new int[N][N];
		for(int i = 0; i< N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j< N; j++) {
				int temp = Integer.parseInt(st.nextToken());
				map[i][j] = temp;
				if(temp > maxheight) maxheight = temp; //가장 높은 위치 
			}
		}
		int ans = 1;
		for(int H = 1; H <= maxheight; H++) {
			boolean[][] check = new boolean[N][N];
			int cnt = 0; // 가능한 영역의 갯수
			for(int i = 0; i< N; i++) {
				for(int j =0; j < N; j++) {
					if(map[i][j] > H && !check[i][j]) {
						//영역을 세기 시작
						Queue<int[]> queue = new LinkedList<int[]>();
						check[i][j] = true;
						queue.add(new int[] {i, j});
						while(!queue.isEmpty()) {
							int[] arr = queue.poll();
							for(int d = 0; d < 4; d++) {
								int nr = arr[0] + dxdy[d][0];
								int nc = arr[1] + dxdy[d][1];
								if(nr >= 0 && nr < N && nc >= 0 && nc < N 
										&& !check[nr][nc] && map[nr][nc] > H) {
									 queue.add(new int[] {nr, nc});
									 check[nr][nc] = true;
								}
							}
						}
						cnt += 1; //영역의 갯수 카운팅
					}
				}
			}
			if(ans < cnt) ans = cnt;
		}
		System.out.println(ans);

	}

}
