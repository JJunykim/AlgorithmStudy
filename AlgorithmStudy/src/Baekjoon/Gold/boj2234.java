package Baekjoon.Gold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

//2023.09.23
//골드3 성곽
//비트 연산자 &
public class boj2234 {

	public static int[][] dxdy = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		int[][] map = new int[M][N]; //칸막이 저장
		int[][] group = new int[M][N]; //방 인젝스 저장
		int[][] count = new int[M][N]; //방 갯수 저장
		
		for(int i =0; i< M; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j =0; j< N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		boolean[][] visited = new boolean[M][N];
		int group_cnt = 1; //1. 이 성에 있는 방의 개수
		int maxcount = 0; //2. 가장 넓은 방의 넓이
		for(int r = 0; r < M; r++) {
			for(int c = 0; c < N; c++) {
				if(!visited[r][c]) {
					Queue<int[]> queue = new LinkedList<int[]>();
					Queue<int[]> save = new LinkedList<>();
					queue.add(new int[] {r, c});
					save.add(new int[] {r, c}); //방의 넓이 세기 용
					visited[r][c] = true;
					while(!queue.isEmpty()) {
						int[] arr = queue.poll();
						for(int d = 0; d < 4; d++) {
							int nr = arr[0] + dxdy[d][0];
							int nc = arr[1] + dxdy[d][1];
							if(nr >= 0 && nr < M && nc >= 0 && nc < N && !visited[nr][nc]
									&& (((map[arr[0]][arr[1]] & (int)Math.pow(2, d)) == 0))) {
								queue.add(new int[] {nr, nc});
								save.add(new int[] {nr, nc});
								visited[nr][nc] = true;
							}
						}
					}
					
					int cnt = save.size(); //계산한 방에 있는 넓이
					if(maxcount < cnt) maxcount = cnt;
					while(!save.isEmpty()) {
						int rr = save.peek()[0];
						int cc = save.poll()[1];
						group[rr][cc] = group_cnt;
						count[rr][cc] = cnt;
					}
					
					group_cnt++;
				}
			}
		}
		int ans = 0; // 3. 하나의 벽을 제거하여 얻을 수 있는 가장 넓은 방의 크기
		for(int r = 0; r < M; r++) {
			for(int c = 0; c < N; c++) {
				for(int d = 0; d < 4; d++) {
					int nr = r + dxdy[d][0];
					int nc = c + dxdy[d][1];
					if(nr >= 0 && nr < M && nc >= 0 && nc < N 
							&& ((map[r][c] & (int)Math.pow(2, d)) / (int)Math.pow(2, d) == 1)
							&& group[r][c] != group[nr][nc]) {
						if(ans < count[r][c] + count[nr][nc]) ans = count[r][c] + count[nr][nc];
					}
						
				}
			}
		}
		
		System.out.println(group_cnt-1);
		System.out.println(maxcount);
		System.out.println(ans);
	
	}

}
