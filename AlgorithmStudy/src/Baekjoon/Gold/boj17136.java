package Baekjoon.Gold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
//2023.09.29
//골드2 색종이 붙이기
//DFS + 백트레킹
public class boj17136 {

	public static int[][] map = new int[10][10];
	public static int[] count = new int[6]; //색종이가 각각 5개씩 있음
	public static int ans = 26;
	public static void main(String[] args) throws IOException{
	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		int Num = 0;
		for(int i=0; i< 10; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j< 10; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				if(map[i][j] == 1) Num++;
			}
		}
		if(Num == 0) {
			System.out.println(0);
			return; //종료
		}
		
		//탐색 시작 r, c, 갯수
		DFS(0, 0, 0);
			
		if(ans == 26)
			System.out.println(-1);
		else
			System.out.println(ans);

	}
	private static void DFS(int r, int c, int cnt) {
		
//		System.out.println(r + " " + c + " " + cnt);
		if(ans < cnt) { //백트레킹 역할
			return;
		}
		
		if(r == 10) { //탐색 다 끝남
			ans = Math.min(ans, cnt);
			return;
		}
		if(c == 10) { //마지막 열에 왔으면 행 바꿈
			DFS(r+1, 0, cnt);
			return;
		}
		
		if(map[r][c] == 0) { //1만 이동 가능하니까
			DFS(r, c+1, cnt);
			return;
		}
	
		for(int size = 5; size >= 1; size--) {
			if(r+size > 10 || c+size > 10 || count[size]+1 > 5) {
				continue;
			}
			boolean flag = true;
			for(int i = r ; i < r+size; i++) {
				for(int j = c; j < c+size; j++) {
					if(map[i][j] == 0) { //하나라도 0이 있으면 색종이 못붙임
						flag = false;
						break;
					}
				}
				if(!flag) break;
			}
			
			if(!flag) continue; //백트레킹 역할
			
			//색종이 붙일 수 있음.
			for(int i = r; i < r+size; i++) {
				for(int j = c; j < c+size; j++) {
					map[i][j] = 0;
				}
			}
			count[size] += 1;
			
			DFS(r, c+size, cnt+1); //열마다 가능성 측정
			
			for(int i = r; i < r+size; i++) {
				for(int j = c; j < c+size; j++) {
					map[i][j] = 1;
				}
			}
			count[size] -= 1;
			
			
		}
		
	}

}
