package com.chat.number.service;

import com.chat.number.domain.GameRoom;
import com.chat.number.model.NumberOthello;
import com.chat.number.type.CheckerBoardRangeType;
import com.chat.number.type.NumberOthelloType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    final int CHECKERBOARD_SIZE = 7; // 가로 세로 체커보드
    private final Map<String, Integer> lastPlayedValue = new HashMap<>();

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

    public boolean gamePlay(GameRoom gameRoom, String numberOthello, String numberChoice, String type) {
        int i = Integer.parseInt(numberOthello);
        int value = Integer.parseInt(numberChoice);

        // 1. 턴 검사
        if (!gameRoom.getCurrentPlayer().equals(type)) {
            log.warn("Not your turn. Current player: {}, Your type: {}", gameRoom.getCurrentPlayer(), type);
            return false;
        }

        // 2. 패 검사
        Map<Integer, Integer> hand = gameRoom.getPlayerHands().get(type);
        if (hand == null || hand.getOrDefault(value, 0) <= 0) {
            log.warn("Player {} does not have number {} in hand.", type, value);
            return false;
        }

        // 3. '1' 연속 사용 금지 규칙 (예외 포함)
        if (value == 1 && lastPlayedValue.getOrDefault(type, 0) == 1) {
            // 1을 제외한 다른 카드가 있는지 확인
            boolean hasOtherCards = hand.entrySet().stream().anyMatch(entry -> entry.getKey() != 1 && entry.getValue() > 0);
            if (hasOtherCards) {
                log.warn("Player {} cannot play 1 twice in a row.", type);
                return false;
            }
        }

        // 4. 빈 칸에만 놓기
        if (!NumberOthelloType.BLANK.getValue().equals(othelloList.get(i).getType())) return false;

        // 5. 게임 로직 실행
        othelloList.get(i).setType(type);
        othelloList.get(i).setValue(value);
        lastPlayedValue.put(type, value);

        // 6. 카드 사용 처리
        hand.put(value, hand.get(value) - 1);

        // 7. 효과 적용
        applyLocalEffects(i, type, value);
        // applyGlobalEffects(type); // NOTE: 연쇄 효과는 복잡하고 버그 가능성이 있어 우선 비활성화
        // applyGlobalEffects(opponentOf(type));

        // 8. 턴 변경
        gameRoom.setCurrentPlayer(opponentOf(type));

        // 9. 게임 종료 확인
        checkGameOver(gameRoom);

        return true;
    }

    private void applyLocalEffects(int index, String type, int value) {
        init();
        int width = othelloList.get(index).getI();
        int height = othelloList.get(index).getJ();

        // invalidate out-of-range directions for border cells
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

        int count = 0;
        int sum = 0;
        ArrayList<NumberOthello> neighborsToFlip = new ArrayList<>();

        for (Map.Entry<CheckerBoardRangeType, Integer> entry : CHECKERBOARD.entrySet()) {
            if (entry.getValue() != -999) {
                NumberOthello neighbor = othelloList.get(index + entry.getValue());
                if (isNumberTile(neighbor)) {
                    count++;
                    sum += neighbor.getValue();
                    neighborsToFlip.add(neighbor);
                }
            }
        }

        if (count == value || sum == value) {
            for (NumberOthello neighbor : neighborsToFlip) {
                chageList(type, neighbor);
            }
        }
    }

    private boolean isNumberTile(NumberOthello o) {
        return NumberOthelloType.PLAYER_ONE.getValue().equals(o.getType()) ||
                NumberOthelloType.PLAYER_TWO.getValue().equals(o.getType());
    }

    private void checkGameOver(GameRoom room) {
        // 조건 1: 보드가 꽉 찼는가?
        boolean boardFull = othelloList.stream().noneMatch(o -> o.getType().equals(NumberOthelloType.BLANK.getValue()));

        // 조건 2: 한 명이라도 카드를 모두 소진했는가?
        boolean playerOneNoCards = room.getPlayerHands().get(NumberOthelloType.PLAYER_ONE.getValue()).values().stream().allMatch(v -> v == 0);
        boolean playerTwoNoCards = room.getPlayerHands().get(NumberOthelloType.PLAYER_TWO.getValue()).values().stream().allMatch(v -> v == 0);

        if (boardFull || playerOneNoCards || playerTwoNoCards) {
            room.setGameStart(false); // 게임 종료 상태로 변경
            log.info("Game Over in room {}. Board Full: {}, P1 No Cards: {}, P2 No Cards: {}", room.getRoomId(), boardFull, playerOneNoCards, playerTwoNoCards);
        }
    }

    private String opponentOf(String type) {
        if (NumberOthelloType.PLAYER_ONE.getValue().equals(type)) return NumberOthelloType.PLAYER_TWO.getValue();
        if (NumberOthelloType.PLAYER_TWO.getValue().equals(type)) return NumberOthelloType.PLAYER_ONE.getValue();
        return type;
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
        if (isNumberTile(othello)) { // 자신의 돌은 뒤집지 않음
            if (NumberOthelloType.PLAYER_ONE.getValue().equals(type)) {
                othello.setType(NumberOthelloType.PLAYER_ONE_BLOCK.getValue());
            } else if (NumberOthelloType.PLAYER_TWO.getValue().equals(type)) {
                othello.setType(NumberOthelloType.PLAYER_TWO_BLOCK.getValue());
            }
        }
    }

    public int getScore(String type) {
        int score = 0;
        if (NumberOthelloType.PLAYER_ONE.getValue().equals(type)) {
            for (NumberOthello numberOthello : othelloList) {
                if (numberOthello.getType().equals(NumberOthelloType.PLAYER_ONE.getValue())) {
                    score += numberOthello.getValue();
                } else if (numberOthello.getType().equals(NumberOthelloType.PLAYER_ONE_BLOCK.getValue())) {
                    score += 1; // 뒤집힌 돌은 1점
                }
            }
        } else if (NumberOthelloType.PLAYER_TWO.getValue().equals(type)) {
            for (NumberOthello numberOthello : othelloList) {
                if (numberOthello.getType().equals(NumberOthelloType.PLAYER_TWO.getValue())) {
                    score += numberOthello.getValue();
                } else if (numberOthello.getType().equals(NumberOthelloType.PLAYER_TWO_BLOCK.getValue())) {
                    score += 1; // 뒤집힌 돌은 1점
                }
            }
        } else {
            log.error("not found user {}", type);
        }

        return score;
    }

}