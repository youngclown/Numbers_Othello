package com.chat.number.service;

import com.chat.number.type.CheckerBoardRangeType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class GamePlayService {
  public Map<CheckerBoardRangeType, Integer> CHECKERBOARD;

  @PostConstruct
  public void init() {
    CHECKERBOARD = new HashMap<>();
    int CHECKERBOARD_SIZE = 6; // 가로 세로 체커보드
    CHECKERBOARD.put(CheckerBoardRangeType.LEFT_UP,-(CHECKERBOARD_SIZE+1));
    CHECKERBOARD.put(CheckerBoardRangeType.CNETER_UP,-(CHECKERBOARD_SIZE));
    CHECKERBOARD.put(CheckerBoardRangeType.RIGHT_UP,-(CHECKERBOARD_SIZE-1));
    CHECKERBOARD.put(CheckerBoardRangeType.CNETER_LEFT,-1);
    CHECKERBOARD.put(CheckerBoardRangeType.CNETER_RIGHT,1);
    CHECKERBOARD.put(CheckerBoardRangeType.LEFT_DOWN,(CHECKERBOARD_SIZE-1));
    CHECKERBOARD.put(CheckerBoardRangeType.CNETER_DOWN,(CHECKERBOARD_SIZE));
    CHECKERBOARD.put(CheckerBoardRangeType.RIGHT_DOWN,(CHECKERBOARD_SIZE+1));
  }

  public void gamePlay () {

  }
}
