package Codetree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

//삼성 SW 역량테스트 2022 상반기 오전 1번 문제 13:30 ~15:50
//술래잡기
//SeekerGo 함수 잘 보기. 회오리 모양으로 나갔다 들어오는거 구현 방법
public class HideAndSeek {

	public static int n, m, h, k, turn = 1, ans;
	public static int[][] map;
	public static boolean[][] trees;
	public static int[] Seeker = new int[4];
	public static int diridx = 1, dirmaxcnt = 1, dircnt = 0;
	public static int[][] dxdy = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; //위오아왼
	public static Queue<int[]> Runners = new LinkedList<int[]>();
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken()); //격자 크기 
		m = Integer.parseInt(st.nextToken()); //도망자 수 
		h = Integer.parseInt(st.nextToken()); //나무 
		k = Integer.parseInt(st.nextToken());
		map = new int[n][n]; //술래가 이동하는 방향 표시
		trees = new boolean[n][n];
		for(int i =0; i< m; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken())-1;
			int c = Integer.parseInt(st.nextToken())-1;
			int d = Integer.parseInt(st.nextToken());
			Runners.add(new int[] {r, c, d});
		}
		for(int i =0; i< h; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken())-1;
			int c = Integer.parseInt(st.nextToken())-1;
			trees[r][c] = true;
		}
		ans = 0;
		Seeker = new int[] {n/2, n/2, 0, 1}; //술래 r,c,d,나가는지 들어오는지
		while(turn <= k){
			//k번에 걸쳐 술래잡기 진행
			Hide_And_Seek();
			turn++;

		}
		System.out.println(ans);
		
	}
	public static void Hide_And_Seek() {
		//도망자 1턴
		RunnerGo();
		//술래 1텀
		SeekerGo();
//		System.out.println(Seeker[0] + " " + Seeker[1] + " " + Seeker[2]+ " " + Seeker[3]);
//		System.out.println(diridx+ " " + dirmaxcnt + " " + dircnt);
		//턴을 넘기기 전에 도망자를 잡기
		CatchRunner();
	}
	private static void CatchRunner() {
		
		//Runners 돌면서 술래에게 잡힌 도망자 제거
		int catchcnt = 0;
		int minSeeker_r = Math.min(Seeker[0], Seeker[0]+dxdy[Seeker[2]][0]*2);
		int maxSeeker_r = Math.max(Seeker[0], Seeker[0]+dxdy[Seeker[2]][0]*2);
		int minSeeker_c = Math.min(Seeker[1], Seeker[1]+dxdy[Seeker[2]][1]*2);
		int maxSeeker_c = Math.max(Seeker[1], Seeker[1]+dxdy[Seeker[2]][1]*2);
		if(minSeeker_r < 0) minSeeker_r = 0;
		if(minSeeker_c < 0) minSeeker_c = 0;
		if(maxSeeker_r >= n) maxSeeker_r = n-1;
		if(maxSeeker_c >= n) maxSeeker_c = n-1;
		int runnercnt = Runners.size();
		for(int i = 0; i< runnercnt; i++) {
			int[] Runner = Runners.poll();
//			System.out.println("Runner "+ (i+1) + " " + Runner[0] + " " + Runner[1] + " "+Runner[2]);
			if(Runner[0] >= minSeeker_r && Runner[0] <= maxSeeker_r
					&& Runner[1] >= minSeeker_c && Runner[1] <= maxSeeker_c) {
				//술래의 영역안에 들어오면
				if(trees[Runner[0]][Runner[1]]) {
					Runners.add(Runner);
				}else {
					catchcnt++;
				}
			}else {
				Runners.add(Runner);
			}
		}
		if(catchcnt > 0) {
			ans += turn * catchcnt;
		}
		
	}
	private static void RunnerGo() {
	
		int Runnercnt = Runners.size();
		for(int i =0; i< Runnercnt; i++) {
			int[] Runner = Runners.poll();
			int dis = Math.abs(Runner[0]-Seeker[0]) + Math.abs(Runner[1]-Seeker[1]);
			if(dis <= 3) {
				//술래와의 거리가 3이하 이면 이동 가능.
				int nr = Runner[0] + dxdy[Runner[2]][0];
				int nc = Runner[1] + dxdy[Runner[2]][1];
				if(nr >= 0 && nr < n && nc >= 0 && nc < n) {
					//격자를 벗어나지 않는 경우
					if(Seeker[0] == nr && Seeker[1] == nc) {
						//술래가 있으면 움직이지 않는다.
						Runners.add(Runner);
					}else {
						//술래가 있지 않으면 한칸 이동한다.
						Runners.add(new int[] {nr, nc, Runner[2]});
					}
				}else {
					//격자를 벗어나는 경우
					int nd = (Runner[2] + 2) % 4; //방향을 반대로 틀어주기
					nr = Runner[0] + dxdy[nd][0];
					nc = Runner[1] + dxdy[nd][1];
					if(!(Seeker[0] == nr && Seeker[1] == nc)) {
						//해당 위치에 술래가 없으면 이동.
						Runners.add(new int[] {nr, nc, nd});
					}else { //이동 못하고 방향만 바꿔주기
						Runners.add(new int[] {Runner[0], Runner[1], nd});
					}
				}
				
			}else {
				Runners.add(Runner);
			}
		}
	}
	private static void SeekerGo() {
		//int diridx = 1, dircnt = 1;
		//새로 이동한 곳 업데이트
		Seeker[0] += dxdy[Seeker[2]][0];
		Seeker[1] += dxdy[Seeker[2]][1];
		//새로 이동한 곳의 방향 업데이트
		if(Seeker[0] == 0 && Seeker[1] == 0) {
			//(0, 0)을 만났을 때
			//방향 바꿔주기 , Maxcnt는 하나 더 클 것 5
			diridx++; 
			Seeker[2] = 2; //무조건 아래 방향이기 때문에
			Seeker[3] = -1; //들어가는 방향으로 바꾸기
			dircnt = 1; //4-4-4-3-3- 이렇게 줄어들어야 되서
			return;
		}
		if(Seeker[0] == n/2 && Seeker[1] == n/2) {
			//중점을 만났을 때
			//방향 바꿔주기
			diridx++;
			Seeker[2] = 0; //무조건 윗 방향이기 때문에
			Seeker[3] = 1;
			return;
		}
		dircnt++;
		if(dirmaxcnt == dircnt) {
			diridx++; //방향 바꿔주기
			int nd = Seeker[2] + Seeker[3];
			if(nd == -1) nd = 3;
			if(nd == 4) nd = 0;
			Seeker[2] = nd;
			dircnt = 0;
		}
		if(diridx % 2 == 1 && dircnt == 0) {
			//홀수 일 때마다 움직이는 방향 수 증가
			dirmaxcnt += Seeker[3]; //들어오는 방향, 나오는 방향에 따라 증감 결정됨
		}
		
	}

}

/*
5 3 1 60
2 4 1
1 4 2
4 2 1
2 4
*/
