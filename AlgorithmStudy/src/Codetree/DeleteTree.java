package Codetree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

//삼성 SW 역량테스트 2022 상반기 오후 2번 문제  13:25~14:52
//나무박멸
public class DeleteTree {
	
	public static class Tree implements Comparable<Tree>{
		public int r; int c; int capa;
		public Tree(int r, int c, int capa) {
			this.r = r;
			this.c = c;
			this.capa = capa;
		}
		@Override
		public int compareTo(Tree o) { //3. 나무 제초제 뿌릴 떄 쓰려고
			if(this.capa == o.capa) 
				return (this.r == o.r) ? this.c - o.c : this.r - o.r; 
			else return o.capa - this.capa; //this-o 오름차순
		}
		
	}
	public static int n, m, k, c, ans;
	public static boolean flag;
	public static int[][] map, acid;
	public static ArrayList<Tree> treelist = new ArrayList<>();
	public static int[][] dxdy = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
	public static int[][] cross = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken()); //박멸이 진행되는 년 수
		k = Integer.parseInt(st.nextToken()); //제초제의 확산 범위
		c = Integer.parseInt(st.nextToken()) + 1; //제초제가 남아있는 년 수 
		map = new int[n][n];
		acid = new int[n][n]; //제초제 저장
		for(int i = 0; i< n; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j< n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				if(map[i][j] > 0 ) treelist.add(new Tree(i, j, map[i][j]));
			}
		}
		flag = false;
		for(int i = 1; i<= m; i++) {
			deleteTree();
			if(flag) break;
		}
		System.out.println(ans);
	}
	private static void deleteTree() {
		//1. 나무가 성장한다.
		TreeGrownUp();
		//2. 번식을 진행한다.
		Propagation();
		//3. 제초제
		SpreadAcid();
		//4. 1시간 뒤 제초제 사라짐
		AcidNext();
	}
	private static void AcidNext() {
		
		treelist.clear();
		for(int i =0; i< n; i++) {
			for(int j =0; j < n; j++) {
				if(map[i][j] > 0 && acid[i][j] == 0) {
					treelist.add(new Tree(i, j, map[i][j]));
				}
				if(acid[i][j] > 0) {
					acid[i][j] -= 1;
					map[i][j] = 0; // 나무도 사라지는거 넣어줘야 함.
				}
			}
		}
		
	}
	private static void SpreadAcid() {
		
		//가장 많이 나무를 박멸시킬 수 있는 나무 구하기
		PriorityQueue<Tree> pq = new PriorityQueue<>();
		for(Tree tree : treelist) {
			// d방향으로 k만큼 전파 
			int cnt = tree.capa; //내 자리
			for(int d = 0; d < 4; d++) {
				for(int dis = 1; dis <= k; dis++) {
					int nr = tree.r + cross[d][0] * dis;
					int nc = tree.c + cross[d][1] * dis;
					if(!isRange(nr, nc) || map[nr][nc] <= 0) {
						break; //범위를 벗어나거나, 벽을 만났을 경우, 빈칸일 경우
					}
					cnt += map[nr][nc];
				}
			}
			pq.add(new Tree(tree.r, tree.c, cnt));
		}
		
		if(pq.isEmpty()) {
			flag = true;
			return;
		}
		Tree target = pq.poll();
		pq.clear();
		int[][] temp = new int[n][n];
		while(!pq.isEmpty()) {
			Tree t = pq.poll();
			temp[t.r][t.c]= t.capa;
		}

		ans += target.capa;
		acid[target.r][target.c] = c;
		for(int d = 0; d < 4; d++) {
			for(int dis = 1; dis <= k; dis++) {
				int nr = target.r + cross[d][0] * dis;
				int nc = target.c + cross[d][1] * dis;
				if(!isRange(nr, nc)) { //범위 벗어남
					break;
				}
				if(map[nr][nc] <= 0) { //벽 또는 빈칸
					if(map[nr][nc] == 0) {
						acid[nr][nc] = c; //빈칸일 경우에는 거기까지 뿌려짐
					}
					break;
				}
				acid[nr][nc] = c;
			}
		}
		
	}
	private static void Propagation() {
		
		int[][] temp = new int[n][n];
		boolean[] check = new boolean[4]; //방향 체크
		int cnt = 0;
		for(Tree tree : treelist) {
			check = new boolean[4];
			cnt = 0;
			for(int d = 0; d < 4; d++) {
				int nr = tree.r + dxdy[d][0];
				int nc = tree.c + dxdy[d][1];
				if(isRange(nr, nc) && map[nr][nc] == 0 && acid[nr][nc] == 0) {
					check[d] = true;
					cnt++;
				}
			}
			if(cnt == 0) continue;
			int plus = tree.capa / cnt;
			for(int d = 0; d < 4; d++) {
				if(check[d]) temp[tree.r+dxdy[d][0]][tree.c+dxdy[d][1]] += plus;
			}
		}
		for(int i =0; i< n; i++) {
			for(int j =0; j< n; j++) {
				map[i][j] += temp[i][j];
				if(temp[i][j] > 0) treelist.add(new Tree(i, j, temp[i][j]));
			}
		}	
	}
	private static void TreeGrownUp() {
		
		for(Tree tree : treelist) {
			for(int d = 0; d < 4; d++) {
				int nr = tree.r + dxdy[d][0];
				int nc = tree.c + dxdy[d][1];
				if(isRange(nr, nc) && map[nr][nc] > 0 && acid[nr][nc] == 0) {
					tree.capa++;
				}
			}
			map[tree.r][tree.c] = tree.capa; 
		}
		
	}
	public static boolean isRange(int r, int c) {
		if(r >= 0 && r < n && c >= 0 && c < n) return true;
		return false;
	}

}
