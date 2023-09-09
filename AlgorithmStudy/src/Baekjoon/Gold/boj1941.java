package Baekjoon.Gold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

//2023.09.09
//골드3 소문난 칠공주
//조합 사용
public class boj1941 {

	public static boolean[][] student = new boolean[5][5];
	public static int[][] check = new int[5][5];
	public static int[][] dxdy = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
	public static int[] choose = new int[7];
	public static int ans = 0, count = 0;
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		for(int i = 0 ; i < 5; i++) {
			st = new StringTokenizer(br.readLine());
			String temp = st.nextToken();
			for(int j = 0; j < 5; j++) {
				if(temp.charAt(j) == 'S') {
					student[i][j] = true;
				}
			}
		}
		
		//25개에서 7개를 뽑음
		combination(0, 0);
		System.out.println(ans);

	}
	public static void combination(int cnt, int start) {
		
		if(cnt == 7) {
			
			if(checkprincess()) {
				ans++;
			}
			return;
		}
		for(int i = start; i < 25; i++) {
			choose[cnt] = i;
			combination(cnt+1, i+1);
		}
				
	}
	
	public static boolean checkprincess() {
		
		for(int i=0; i< 5; i++) {
			for(int j =0; j< 5; j++) {
				check[i][j] = 0;
			}
		}
		Queue<int[]> queue = new LinkedList<>();
		int cntS = 0; //true
		int cntY = 0; //false
		//경우의 수를 배열에 대입하기
		for(int i = 0; i < 7; i++) {
			int idx = choose[i];
			int r = idx / 5;
			int c = idx % 5;
			check[r][c] = (student[r][c] ? 1 : 2);
			// true 이면 1, 아니면 2, 없으면 0
		}
		
		int[] a1 = new int[] {choose[0]/5, choose[0]%5};
		queue.add(a1);
		if(check[a1[0]][a1[1]] == 1) cntS++;
		else cntY++;
		check[a1[0]][a1[1]] = 0;
		
		while(!queue.isEmpty()) {
			int r = queue.peek()[0];
			int c = queue.poll()[1];
			for(int d = 0; d <4; d++) {
				int nr = r + dxdy[d][0];
				int nc = c + dxdy[d][1];
				if(nr >= 0 && nr < 5 && nc >= 0 && nc < 5 && check[nr][nc] >= 1) {
					queue.add(new int[] {nr, nc});
					if(check[nr][nc] == 1) cntS++;
					else cntY++;
					check[nr][nc] = 0;
				}
			}
		}
		if(cntS + cntY == 7 && cntS >= 4) {
			return true;
		}else return false;
	}

	

}
