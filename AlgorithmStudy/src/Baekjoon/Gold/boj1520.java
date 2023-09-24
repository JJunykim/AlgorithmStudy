package Baekjoon.Gold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

//2023.09.24
//골드3 내리막 길
//DFS + DP
public class boj1520 {

	public static int M, N, ans = 0;
	public static int[][] map, dp;
	public static int[][] dxdy = {{1, 0}, {0, 1}, {0, -1}, {-1, 0}};
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		M = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());
		map = new int[M][N];
		dp = new int[M][N];
		for(int i = 0; i< M; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j< N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				dp[i][j] = -1; //dp 배열 초기화
			}
		}

		System.out.println(dfs(0, 0));

	}
	private static int dfs(int r, int c) {

		//(r, c) -> (M-1, N-1) 까지 갈 수 있는 경로의 수를 저장
		if(r == M-1 && c == N-1) {
			return 1;
		}
		if(dp[r][c] != -1) { //메모이제이션. 이미 방문했던 곳이면 계산된 값을 호출
			return dp[r][c];
		}
		dp[r][c] = 0; // 현재 위치에서 끝점까지 도달하는 경로의 개수 초기화
		for(int d = 0; d < 4; d++) {
			int nr = r + dxdy[d][0];
			int nc = c + dxdy[d][1];
			if(nr >= 0 && nr < M && nc >= 0 && nc < N 
					&& map[r][c] > map[nr][nc] ) {
				dp[r][c] += dfs(nr, nc);
			}
		}
		//위에서 0으로 초기화 해주는 이유가 사방탐색으로 갈 수 있는데가 아무데도 없으면 그 곳은 0이 되어 끊어주기위해.
		return dp[r][c];
		
	}

}
