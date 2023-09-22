package Baekjoon.Gold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

//2023.09.22
//골드2 새로운 게임
//단순 구현. 3차원 Arraylist 사용함
public class boj17780 {

	public static int N, K, ans = 1;
	public static int[][] color;
	public static ArrayList<Integer> [][] map;
	public static class Chess {
		public int r, c, d;
		
		public Chess(int r, int c, int d) {
			super();
			this.r = r;
			this.c = c;
			this.d = d;
		}
		public void updateInfo(int r, int c) {
			this.r = r;
			this.c = c;
		}
	}
	public static Chess[] chesslist;
	public static int[][] dxdy = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
	
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		color = new int[N][N];
		chesslist = new Chess[K+1];
		map = new ArrayList[N][N]; //3차원 Arraylis 배열 만듦
		for(int i = 0; i< N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j< N; j++) {
				color[i][j] = Integer.parseInt(st.nextToken());
				map[i][j] = new ArrayList<>(); 
			}
		}
	
		for(int i = 1; i <= K; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken()) - 1;
			int c = Integer.parseInt(st.nextToken()) - 1;
			int d = Integer.parseInt(st.nextToken()) - 1;
			chesslist[i] = new Chess(r, c, d);
			map[r][c].add(i);  //각 위치에 담기
		}
		
		playgame();
		if(ans <= 1000) {
			System.out.println(ans);
		}else {
			System.out.println(-1);
		}

	}

	private static void playgame() {
		
		while(ans <= 1001) {
			
			for(int idx = 1; idx <= K; idx++) { //이동해야 하는 말의 인덱스
				boolean check = false; //말이 이동했는지 판단 유무
				for(int r = 0; r < N; r++) {
					for(int c = 0; c < N; c++) {
						if(map[r][c].isEmpty()) continue;
						if(map[r][c].get(0) == idx) {
							check = true;
							//이동하려는 말이 가장 밑에 있을 때 이동을 시작한다.
							Chess A = chesslist[idx];
							int nr = r + dxdy[A.d][0];
							int nc = c + dxdy[A.d][1];
							if(nr >= 0 && nr < N && nc >= 0 && nc < N && color[nr][nc] == 0) {
								//1. 흰색일 경우
								int cnt = map[r][c].size();
								movechessfront(0, cnt, r, c, A.d);
							}else if(nr >= 0 && nr < N && nc >= 0 && nc < N && color[nr][nc] == 1) {
								//2. 빨간색일 경우
								int cnt = map[r][c].size();
								movechessback(cnt-1, 0, r, c, A.d);	
							}else { 
								//3. 파란색일 경우 + out of range
								int newd = changedir(A.d);
								nr = r + dxdy[newd][0];
								nc = c + dxdy[newd][1];//새로 움직였을 때 경로
								chesslist[map[r][c].get(0)].d = newd; //가장 밑에 있는 말의 방향바꿔주기
								if(nr >= 0 && nr < N && nc >= 0 && nc < N && color[nr][nc] == 0) {
									//1. 흰색일 경우
									int cnt = map[r][c].size();
									movechessfront(0, cnt, r, c, newd);
								}else if(nr >= 0 && nr < N && nc >= 0 && nc < N && color[nr][nc] == 1) {
									//2. 빨간색일 경우
									int cnt = map[r][c].size();
									movechessback(cnt-1, 0, r, c, newd);
								}else { 
									//3. 파란색 + out of range
									int cnt = map[r][c].size();
									for(int a = 0; a < cnt; a++) { //방향만 바꿔주기
										chesslist[map[r][c].get(a)].d = newd; //가장 밑에 있는 말의 방향바꿔주기
									}
									nr = r;
									nc = c;
								}
							}
							if(map[nr][nc].size() >= 4) {
								//쌓아 올려진 것이 4개 이상이면 끝
								return;
							}
							break; //말 한번 옮겼으면 다음 순서의 말로 이동
						}
					}
					if(check) break; //말 한번 옮겼으면 다음 순서의 말로 이동
				}
			}
			ans += 1;
			
		}
		
	}
	private static void movechessfront(int start, int end, int r, int c, int d) {
		int nr = r + dxdy[d][0];
		int nc = c + dxdy[d][1];
		for(int a = start; a < end; a++) {
			int temp = map[r][c].get(a); //옮기려는 체스 번호
			map[nr][nc].add(temp); //다 옮기고
			chesslist[temp].updateInfo(nr, nc);
		}
		map[r][c].clear(); //이동했으니까 
		
	}
	private static void movechessback(int start, int end, int r, int c, int d) {
		int nr = r + dxdy[d][0];
		int nc = c + dxdy[d][1];
		for(int a = start; a >= end; a--) {
			int temp = map[r][c].get(a); //옮기려는 체스 번호
			map[nr][nc].add(temp); //다 옮기고
			chesslist[temp].updateInfo(nr, nc);
		}
		map[r][c].clear(); //이동했으니까 
		
	}
	private static int changedir(int d) {
		int nd = 0;
		switch(d) {
		case 0:
			nd = 1; break;
		case 1:
			nd = 0; break;
		case 2:
			nd = 3; break;
		case 3:
			nd = 2; break;
		}
		return nd;
	}

}
