package Baekjoon.Gold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

//2023.09.28
//골드2 Maaaaaaaaaze
//순열 + 중복순열 + BFS
public class boj16985 {

	public static int[][][] map = new int[5][5][5];
	public static int[][][] temp = new int[5][5][5];
	public static boolean[][][] isvisited = new boolean[5][5][5];
	public static int[] idx = new int[5];
	public static boolean[] idxcheck = new boolean[5];
	public static int ans = Integer.MAX_VALUE;
	public static int a, b, c, d, e;
	public static int[][] dxdy = {{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}};
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		for(int i=0; i< 5; i++) {
			for(int j = 0; j< 5; j++) {
				st = new StringTokenizer(br.readLine());
				for(int k = 0; k < 5; k++) {
					map[i][j][k] = Integer.parseInt(st.nextToken());
				}
			}
		}

		for(a =0 ; a < 4; a++) {
			for(b = 0; b < 4; b++) {
				for(c = 0; c < 4; c++) {
					for(d =0; d < 4; d++) {
						for(e = 0; e < 4; e++) { //판 회전시키기 - 중복순열
							//쌓는 순서 정하기 - 순열
							idx = new int[5];
							idxcheck = new boolean[5];
							Permutation(0);
						}
					}
				}
			}
		}
		if(ans == Integer.MAX_VALUE) {
			System.out.println(-1);
		}else {
			System.out.println(ans);
		}

	}
	private static void Permutation(int cnt) {
		
		if(cnt == 5) {
			RotationIdx(a, b, c, d, e, idx);
			//이 조건 반드시 들어가야 함. 시작점과 끝점이 모두 1일 경우에만 이동 가능
			if(temp[0][0][0] == 1 && temp[4][4][4] == 1) {
				BFS();
			}
			return;
		}
		
		for(int i = 0; i < 5; i++) {
			if(!idxcheck[i]) {
				idx[cnt] = i;
				idxcheck[i] = true;
				Permutation(cnt+1);
				idxcheck[i] = false;
			}
		}
	}
	private static void BFS() {
		
		isvisited = new boolean[5][5][5];
		Queue<int[]> queue = new LinkedList<int[]>();
		queue.add(new int[] {0, 0, 0, 0}); //x, y, z, dis
		isvisited[0][0][0] = true;
		boolean flag = false;
		int cnt = 0;
		while(!queue.isEmpty()) {
			int[] arr = queue.poll();
			if(arr[0] == 4 && arr[1] == 4 && arr[2] == 4) {
				flag = true;
				cnt = arr[3];
				break;
			}
			for(int d= 0; d < 6; d++) {
				int nr = arr[0] + dxdy[d][0];
				int nc = arr[1] + dxdy[d][1];
				int nz = arr[2] + dxdy[d][2];
				if(nr >= 0 && nr < 5 && nc >= 0 && nc < 5 && nz >= 0 && nz < 5
						&& !isvisited[nr][nc][nz] && temp[nr][nc][nz] == 1) {
					queue.add(new int[] {nr, nc, nz, arr[3]+1});
					isvisited[nr][nc][nz] = true;
				}
			}
		}
		
		if(flag) {
			if(ans > cnt) ans = cnt;
		}
	}
	private static void RotationIdx(int a, int b, int c, int d, int e, int[] f) {
		//temp 인덱스, 쌓는 판의 숫자, 얼만큼 회전
		rotate(0, f[0], a);
		rotate(1, f[1], b);
		rotate(2, f[2], c);
		rotate(3, f[3], d);
		rotate(4, f[4], e);
	}
	public static void rotate(int idx, int pan, int cnt) {
		//idx번 째 판을 얼만큼 시계방향으로 회전시킬지
		if(cnt == 0) {
			for(int i =0; i< 5; i++) {
				for(int j =0; j< 5; j++) {
					temp[idx][i][j] = map[pan][i][j];
				}
			}
		}else if(cnt == 1) {
			for(int i =0; i< 5; i++) {
				for(int j =0; j< 5; j++) {
					temp[idx][j][4-i] = map[pan][i][j];
				}
			}
		}else if(cnt == 2) {
			for(int i =0; i< 5; i++) {
				for(int j =0; j< 5; j++) {
					temp[idx][4-i][4-j] = map[pan][i][j];
				}
			}
		}else if(cnt == 3) {
			for(int i =0; i< 5; i++) {
				for(int j =0; j< 5; j++) {
					temp[idx][4-j][i] = map[pan][i][j];
				}
			}
		}
	}

}
