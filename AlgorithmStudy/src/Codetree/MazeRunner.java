package Codetree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

//삼성 SW 역량테스트 2023 하반기 오후 1번 문제  23:27 - 01:40
//메이즈 러너
//Matrix 일부 정사각형을 90회전 시키는 방법. 원점 기준으로 돌리고 -> 좌표 이동
public class MazeRunner {

	public static int N, M, K, turn=0, ans = 0;
	public static boolean flag = false;
	public static int[][] map, Runners;
	public static int[] Exit = new int[2];
	public static int[][] dxdy = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; 
	public static void main(String[] args) throws IOException{

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken()); //미로의 크기
		M = Integer.parseInt(st.nextToken()); //참가자 수
		K = Integer.parseInt(st.nextToken()); //게임 시간
		map = new int[N][N];
		Runners = new int[M][3]; //살아있는지(1 or 0), r, c
		for(int i =0; i< N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j< N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		for(int i =0; i< M; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken()) -1;
			int c = Integer.parseInt(st.nextToken()) -1;
			Runners[i] = new int[] {1, r, c};
		}
		st = new StringTokenizer(br.readLine());
		Exit[0] = Integer.parseInt(st.nextToken()) -1;
		Exit[1] = Integer.parseInt(st.nextToken()) -1;
		
		while(turn < K) {
			MazeRunner();
			if(flag) break;
			turn++;
		}
		System.out.println(ans);
		//문제의 시작이 (1, 1)부터 시작이어서 +1 해줘야 함.
		System.out.println((Exit[0]+1) + " " + (Exit[1]+1));
		
	}
	private static void MazeRunner() {
		
		//1.모든 참가자들이 한칸식 움직임
		MoveOneStep();
		//종료 코드 추가. 모든 참가자가 탈출에 성공했을 때, 게임 끝
		if(AllEscape()) {
			flag = true;
			return;
		}
		//2.미로가 회전
		RotateMaze();

	}
		
	private static boolean AllEscape() {
		
		boolean temp = true;
		for(int i = 0; i < M; i++) {
			if(Runners[i][0] == 1) {
				temp = false; //한명이라도 남아있으면
			}
		}
		return temp;
	}
	private static void RotateMaze() {
		
		//가장 작은 정사각형 길이 잡기
		int D = Integer.MAX_VALUE;
		boolean[][] exist = new boolean[N][N];
		for(int i = 0; i < M; i++) {
			if(Runners[i][0] == 1) {
				exist[Runners[i][1]][Runners[i][2]] = true;
				int dr = Math.abs(Runners[i][1] - Exit[0]) + 1; //r차이
				int dc = Math.abs(Runners[i][2] - Exit[1]) + 1; //c차이
				int dmax = Math.max(dr, dc); //출구와 본인을 포함하는 가장 큰 정사각형
				if( D > dmax) D = dmax;
			}
		}
		
		boolean Rflag = false;
		boolean Eflag = false;
		boolean REflag = false;
		int[] target = new int[2]; //기준 타겟 점
		for(int r =0; r <= N-D; r++) {
			for(int c= 0; c <= N-D; c++) {
				//가장 좌상단 좌표 (r, c)
				Rflag = false;
				Eflag = false;
				for(int i = r; i < r+D; i++) {
					for(int j = c; j < c+D; j++) {
						if(exist[i][j]) Rflag = true;
						if(Exit[0] == i && Exit[1] == j) Eflag = true;
						if(Rflag && Eflag) break;
					}
					if(Rflag && Eflag) {
						REflag = true;
						target = new int[] {r, c};
						break;
					}
				}
				if(REflag) break;
			}
			if(REflag) break;
		}
		
		
		//시계방향으로 90도 회전
		int[][] temp = new int[N][N]; //배열 임시 저장
		int[] Next = new int[2]; //출구가 이동하는 곳
		for(int i = 0; i< D; i++) {
			for(int j =0; j< D; j++) {
				if(Exit[0] == (i+target[0]) && Exit[1] == j+target[1]) {
					Next = new int[] {j+target[0], D-1-i+target[1]};
				}
				temp[j+target[0]][D-1-i+target[1]] = map[i+target[0]][j+target[1]];
			}
		}

		//회전된 벽은 내구도가 씩 깎임
		for(int i = target[0]; i< target[0]+D; i++) {
			for(int j = target[1]; j< target[1] + D; j++) {
				map[i][j] = temp[i][j];
				if(map[i][j] > 0) map[i][j] -= 1;
			}
		}
		//참가자도 따로 돌려주기
		for(int i =0 ; i < M; i++) {
			if(Runners[i][0] == 1) {
				if(Runners[i][1] >= target[0] && Runners[i][1] < target[0]+D
						&& Runners[i][2] >= target[1] && Runners[i][2] < target[1]+D) {
					//정사각형에 포함되어 있으면,
					int r = Runners[i][1] - target[0];
					int c = Runners[i][2] - target[1];
					Runners[i][1] = c + target[0];
					Runners[i][2] = D-1-r+target[1];
				}
			}
		}
		//출구도 따로 돌려주기
		Exit = new int[] {Next[0], Next[1]};
		
	}
	private static void MoveOneStep() {
		
		for(int idx =0; idx< M; idx++) {
			if(Runners[idx][0] == 0) continue; //이미 탈출한 참가자라서
			
			int[] R = Runners[idx];
			int mindis = Integer.MAX_VALUE; //최단거리
			int present = Math.abs(R[1]-Exit[0]) + Math.abs(R[2]-Exit[1]); //현재머물러 있던 칸과 출구까지 거리
			boolean[] check = new boolean[4]; //최단거리 후보가 되는 방향 체크
			for(int d = 0; d < 4; d++) {
				int nr = R[1] + dxdy[d][0];
				int nc = R[2] + dxdy[d][1];
				if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue; //범위 벗어남
				if(map[nr][nc] > 0) continue; //벽임
				int temp = Math.abs(nr-Exit[0]) + Math.abs(nc-Exit[1]);
				if(temp > present) continue; //움직인 칸의 최단거리가 멀면 out
				mindis = Math.min(mindis, temp);
				check[d] = true;
			}
			
			//움직일 수 있는 최단 거리가 있다면, (2개 이상도 포함)
			if(mindis < Integer.MAX_VALUE) {
				for(int d = 0; d < 4; d++) {
					if(check[d]) {
						int nr = R[1] + dxdy[d][0];
						int nc = R[2] + dxdy[d][1];
						if(Math.abs(nr-Exit[0]) + Math.abs(nc-Exit[1]) == mindis) {
							Runners[idx][1] = nr;
							Runners[idx][2] = nc; //경로 업데이트
							if(nr == Exit[0] && nc == Exit[1]) {
								Runners[idx][0] = 0; //출구 도달시, 즉시 탈출
							}
							break;
						}
					}
				}
				ans++; //이동한 거리 더해주기
			}
			
		}
		
	}

}
