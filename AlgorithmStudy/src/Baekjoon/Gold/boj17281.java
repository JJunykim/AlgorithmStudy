package Baekjoon.Gold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

//2023.09.24
//골드4 야구공 이모티콘
//브루트포스 알고리즘, 완전탐색
public class boj17281 {

	public static int N, target_idx, ans, score; //이닝 수
	public static int[] arr = new int[10]; //순서 인덱스
	public static boolean[] used = new boolean[10];
	public static boolean[] check = new boolean[4];
	public static int[][] map;
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken()); //이닝
		map = new int[N][10]; //선수들의 이닝 기록
		for(int i =0; i< N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j =1; j <= 9; j++){
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		arr[4] = 1; //4번째에는 1번타자
		used[1] = true; //1번 타자는 사용되었음.
		ans = 0; //가장 많은 득점
		permutation(1);
		System.out.println(ans);

	}
	private static void permutation(int idx) {
		
		if(idx == 10) {
			//이닝 수 만큼 경기 시작
			startbaseball();
			if(score > ans) ans = score;
			return;
		}
		
		for(int i = 2; i <= 9; i++) {
			if(!used[i]) {
				arr[idx] = i;
				used[i] = true;
				if(idx+1 == 4) permutation(idx+2);
				else permutation(idx+1);
				used[i] = false;
			}
		}
	}
	private static void startbaseball() {
		
		target_idx = 1; //첫번째 순서부터 시작, 실제 경기자는 arr[target_idx]
		score = 0; //해당 경기의 점수
		for(int i = 0; i< N; i++) {
			inning(i); //i번째 inning 경기
		}
		
	}
	private static void inning(int idx) {
		
		int out = 0;
		check = new boolean[4]; //1루, 2루, 3루 초기화
		while(true) {
			//arr[target_idx]이 경기를 시작
			int capa = map[idx][arr[target_idx]]; //i번째 이닝의 타겟의 능력치
			if(capa == 0) { //아웃일 때
				out += 1; //아웃이 하나 증가
			}else { //안타~홈런일 때
				gocapa(capa);
			}
						
			if(target_idx+1 == 10) target_idx = 0;
			target_idx++;
			if(out == 3) break;
		}
	
	}
	private static void gocapa(int capa) {

		//기존 루에 있던 타자들 옮기기
		for(int i = 3; i>= 1 ; i--) {
			if(check[i]) {
				check[i] = false;
				if(i + capa >= 4) { //루 밖으로 이동
					score++;
				}else { //루 이동
					check[i+capa] = true;
				}
			}
		}
		//본인주자 옮기기
		if(capa == 4) score++; //홈런이면 본인주자까지 
		else check[capa] = true; //타자위치

	}

}
