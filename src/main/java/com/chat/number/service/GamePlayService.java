package com.chat.number.service;

import com.chat.number.model.NumberOthello;
import com.chat.number.type.CheckerBoardRangeType;
import com.chat.number.type.NumberOthelloType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Getter
@Setter
public class GamePlayService {
    public Map<CheckerBoardRangeType, Integer> CHECKERBOARD = new HashMap<>();
    public ArrayList<NumberOthello> othelloList = new ArrayList<>();
    final int CHECKERBOARD_SIZE = 7; // 가로 세로 체커보드

    public void init() {
        CHECKERBOARD.put(CheckerBoardRangeType.LEFT_UP, -(CHECKERBOARD_SIZE + 1));
        CHECKERBOARD.put(CheckerBoardRangeType.CNETER_UP, -(CHECKERBOARD_SIZE));
        CHECKERBOARD.put(CheckerBoardRangeType.RIGHT_UP, -(CHECKERBOARD_SIZE - 1));
        CHECKERBOARD.put(CheckerBoardRangeType.CNETER_LEFT, -1);
        CHECKERBOARD.put(CheckerBoardRangeType.CNETER_RIGHT, 1);
        CHECKERBOARD.put(CheckerBoardRangeType.LEFT_DOWN, (CHECKERBOARD_SIZE - 1));
        CHECKERBOARD.put(CheckerBoardRangeType.CNETER_DOWN, (CHECKERBOARD_SIZE));
        CHECKERBOARD.put(CheckerBoardRangeType.RIGHT_DOWN, (CHECKERBOARD_SIZE + 1));
    }


    public GamePlayService() {
        init();

        int width = 0;
        int height = 0;

        for (int i = 0; i < CHECKERBOARD_SIZE * CHECKERBOARD_SIZE; i++) {
            NumberOthello othello = new NumberOthello();
            othello.setType(NumberOthelloType.BLANK.getValue());

            if ((i + 1) % CHECKERBOARD_SIZE == 0) {
                othello.setI(width++);
                othello.setJ(height);
                height = 0;
            } else {
                othello.setI(width);
                othello.setJ(height++);
            }

            othelloList.add(othello);
        }
    }

    public boolean gamePlay(String numberOthello, String numberChoice, String type) {


        int i = Integer.parseInt(numberOthello);
        int value = Integer.parseInt(numberChoice);
        String setType = othelloList.get(i).getType();

        if (NumberOthelloType.BLANK.name().equals(setType)) return false;

        int width = othelloList.get(i).getI();
        int height = othelloList.get(i).getJ();  //Height

        // 배열 초기화 작업
        init();

        int cnt = 0;

        if (width == 0) {
            CHECKERBOARD.put(CheckerBoardRangeType.LEFT_UP, -999);
            CHECKERBOARD.put(CheckerBoardRangeType.CNETER_UP, -999);
            CHECKERBOARD.put(CheckerBoardRangeType.RIGHT_UP, -999);
        } else if (width == (CHECKERBOARD_SIZE - 1)) {
            CHECKERBOARD.put(CheckerBoardRangeType.LEFT_DOWN, -999);
            CHECKERBOARD.put(CheckerBoardRangeType.CNETER_DOWN, -999);
            CHECKERBOARD.put(CheckerBoardRangeType.RIGHT_DOWN, -999);
        }

        if (height == 0) {
            CHECKERBOARD.put(CheckerBoardRangeType.LEFT_UP, -999);
            CHECKERBOARD.put(CheckerBoardRangeType.CNETER_LEFT, -999);
            CHECKERBOARD.put(CheckerBoardRangeType.LEFT_DOWN, -999);
        } else if (height == (CHECKERBOARD_SIZE - 1)) {
            CHECKERBOARD.put(CheckerBoardRangeType.RIGHT_UP, -999);
            CHECKERBOARD.put(CheckerBoardRangeType.CNETER_RIGHT, -999);
            CHECKERBOARD.put(CheckerBoardRangeType.RIGHT_DOWN, -999);
        }

        for (Map.Entry<CheckerBoardRangeType, Integer> entry : CHECKERBOARD.entrySet()) {
            if (entry.getValue() != -999) {
                cnt += parseList(othelloList.get(i + entry.getValue()));
            }
        }

        // 놓은 값을 알아서 셋팅합니다!
        othelloList.get(i).setType(type);
        othelloList.get(i).setValue(value);

        if (cnt == value) {
            for (Map.Entry<CheckerBoardRangeType, Integer> entry : CHECKERBOARD.entrySet()) {
                if (entry.getValue() != -999) {
                    chageList(type, othelloList.get(i + entry.getValue()));
                }
            }
        }

        return true;
    }

    public int parseList(NumberOthello othello) {
        if (NumberOthelloType.PLAYER_ONE.getValue().equals(othello.getType()) ||
                NumberOthelloType.PLAYER_TWO.getValue().equals(othello.getType())) {
            return 1;
        } else {
            return 0;
        }
    }

    public void chageList(String type, NumberOthello othello) {
        if (NumberOthelloType.PLAYER_ONE.getValue().equals(othello.getType()) ||
                NumberOthelloType.PLAYER_TWO.getValue().equals(othello.getType())) {
            if (NumberOthelloType.PLAYER_ONE.getValue().equals(type)) {
                othello.setType(NumberOthelloType.PLAYER_ONE_BLOCK.getValue());
                othello.setValue(1);  // 해당 배열의 '1' 값 설정
            } else if (NumberOthelloType.PLAYER_TWO.getValue().equals(type)) {
                othello.setType(NumberOthelloType.PLAYER_TWO_BLOCK.getValue());
                othello.setValue(1);
            }
        }
    }


    /*
      PLAYER_ONE("PO"),       // 플레이1
      PLAYER_TWO("PT"),       // 플레이2
      BLANK("B"),            // 빈화면
      PLAYER_ONE_BLOCK("POB"), // 플레이1의 배경
      PLAYER_TWO_BLOCK("PTB");  // 플레이2의 배경
     */
    public int getScore(String type) {

        int score = 0;
        if (NumberOthelloType.PLAYER_ONE.getValue().equals(type)) {
            for (NumberOthello numberOthello : othelloList) {
                if (numberOthello.getType().equals(NumberOthelloType.PLAYER_ONE.getValue()) ||
                        numberOthello.getType().equals(NumberOthelloType.PLAYER_ONE_BLOCK.getValue()))
                    score += numberOthello.getValue();
            }
        } else if (NumberOthelloType.PLAYER_TWO.getValue().equals(type)) {
            for (NumberOthello numberOthello : othelloList) {
                if (numberOthello.getType().equals(NumberOthelloType.PLAYER_TWO.getValue()) ||
                        numberOthello.getType().equals(NumberOthelloType.PLAYER_TWO_BLOCK.getValue()))
                    score += numberOthello.getValue();
            }
        } else {
            log.error("not found user {}", type);
        }

        return score;
    }

}
