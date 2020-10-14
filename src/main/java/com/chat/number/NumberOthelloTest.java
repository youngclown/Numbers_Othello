package com.chat.number;

import com.chat.number.model.NumberOthello;
import com.chat.number.type.NumberOthelloType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class NumberOthelloTest {
	static int DEFAULT_NUM = 6;
	static Map<String, String> edgeMap = new HashMap<>();

	public static void main(String[] args) {

		
		/*
		 * B : BLACK
		 * W : WHITE
		 * K : BLANK
		 * L : BLOCK
		 * BL : BLACK BLOCK
		 * WL : WHITE BLOCK
		 */
		ArrayList<NumberOthello> list = new ArrayList<>();
		NumberOthello othello;
		
		int width = 0;
		int height = 0;

		// 초기화 설정 후 list 에 해당 값 삽입.
		for (int i = 0; i < DEFAULT_NUM*DEFAULT_NUM; i++) {
			othello = new NumberOthello();
			othello.setPod(i);
			othello.setType(NumberOthelloType.BLANK.getValue());
			
			if ((i+1) % DEFAULT_NUM == 0) {
				othello.setI(width++);
				othello.setJ(height);
				height = 0;
			} else {
				othello.setI(width);
				othello.setJ(height++);
			}
			
			list.add(othello);
		}
		
	
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i).getType() + "("+list.get(i).getI()+":"+list.get(i).getJ()+")"+ i +" ");
			if ((i+1) % DEFAULT_NUM == 0) {
				System.out.println();
			}
		}
		
		NumberOthelloTest n = new NumberOthelloTest();
		n.init();
		n.replayList(list, "B");
		n.replayList(list, "W");
		
		Scanner input = new Scanner(System.in);
		
		int number, value;
		
		while (!"Q".equals(input.next())) {
			System.out.println("black turn : ");
			System.out.println("number : ");
			number = input.nextInt();
			System.out.println("value : ");
			value = input.nextInt();
			n.gamePlay(list, number, value, "B");
			n.replayList(list, "B");
			n.replayList(list, "W");
			
			
			for (int i = 0; i < list.size(); i++) {
				System.out.print(list.get(i).getType() + "("+list.get(i).getI()+":"+list.get(i).getJ()+")"+ i +" ");
				if ((i+1) % DEFAULT_NUM == 0) {
					System.out.println();
				}
			}
			
			System.out.println("white turn : ");
			System.out.println("number : ");
			number = input.nextInt();
			System.out.println("value : ");
			value = input.nextInt();
			n.gamePlay(list, number, value, NumberOthelloType.PLAYER_ONE.getValue());
			n.replayList(list, NumberOthelloType.PLAYER_TWO.getValue());
			n.replayList(list, "B");

			for (int i = 0; i < list.size(); i++) {
				System.out.print(list.get(i).getType() + "("+list.get(i).getI()+":"+list.get(i).getJ()+")"+ i +" ");
				if ((i+1) % DEFAULT_NUM == 0) {
					System.out.println();
				}
			}
		}
	}
	
	public void init(){
		edgeMap.put("LU","-" + (DEFAULT_NUM+1));
		edgeMap.put("U","-" + (DEFAULT_NUM));
		edgeMap.put("RU","-" + (DEFAULT_NUM-1));
		edgeMap.put("L","-1");
		edgeMap.put("R","1");
		edgeMap.put("LD","" + (DEFAULT_NUM-1));
		edgeMap.put("D","" + (DEFAULT_NUM));
		edgeMap.put("RD","" + (DEFAULT_NUM+1));
	}
	
	public void gamePlay(ArrayList<NumberOthello> list, int i, int value, String type){
		if (NumberOthelloType.BLANK.getValue().equals(list.get(i).getType())) {
			playLogic(list, i, value, type);
		} else {
			System.out.println("�ٸ� ���� �νñ� �ٶ��ϴ�.");
		}
	}

	private void playLogic(ArrayList<NumberOthello> list, int i, int value, String type) {
		init();	// �׻� �ʱ�ȭ�� �ؾ��մϴ�.
		int width = list.get(i).getI();
		int height = list.get(i).getJ();	//Height
		list.get(i).setType(type);
		int cnt = 0;
		
		if (width == 0) {
			edgeMap.put("LU","X");
			edgeMap.put("U","X");
			edgeMap.put("RU","X");
		}
		
		if (height == 0) {
			edgeMap.put("LU","X");
			edgeMap.put("L","X");
			edgeMap.put("LD","X");
		}
		
		if (width == (DEFAULT_NUM-1)) {
			edgeMap.put("LD","X");
			edgeMap.put("D","X");
			edgeMap.put("RD","X");
		}
		
		if (height == (DEFAULT_NUM-1)) {
			edgeMap.put("RU","X");
			edgeMap.put("R","X");
			edgeMap.put("RD","X");
		}
		
		/*
		 * B : BLACK
		 * W : WHITE
		 * K : BLANK
		 * N : NONE
		 * BL : BLACK BLOCK
		 * WL : WHITE BLOCK
		 */
		for (Map.Entry<String, String> entry : edgeMap.entrySet() )
		{
			if (!"X".equals(entry.getValue())) {
				System.out.println(i + Integer.parseInt(entry.getValue()));
				cnt += parseList(list.get(i + Integer.parseInt(entry.getValue())));
			}
		}
		
		if (cnt == value) {
			for (Map.Entry<String, String> entry : edgeMap.entrySet() )
			{
				if (!"X".equals(entry.getValue())) {
					chageList(type, list.get(i + Integer.parseInt(entry.getValue())));
				}
			}
		} else {
			list.get(i).setType(type);
			list.get(i).setValue(value);
		}
	}
	
	public int replayList(ArrayList<NumberOthello> list, String type){
		int score = 0;

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getValue() > 0) {
				if(type.equals(list.get(i).getType())){
					playLogic(list, i, list.get(i).getValue(), list.get(i).getType());
				}
			}
		}

		for (NumberOthello numberOthello : list) {
			if (numberOthello.getValue() > 0) {
				if (type.equals(numberOthello.getType())) {
					score += numberOthello.getValue();
				} else if ((type + "L").equals(numberOthello.getType())) {
					score++;
				}
			}
		}
		return score;
	}
	
	public int parseList(NumberOthello daduk){
		if("B".equals(daduk.getType()) || "W".equals(daduk.getType())){
			return 1;
		} else {
			return 0;
		}
	}
	
	public void chageList(String type, NumberOthello daduk){
		if("B".equals(daduk.getType()) || "W".equals(daduk.getType())){
			if ("B".equals(type)){
				daduk.setType("BL");
				daduk.setValue(-1);
			} else if ("W".equals(type)){
				daduk.setType("WL");
				daduk.setValue(-1);
			}
		}
	}
}
