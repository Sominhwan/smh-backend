package kr.co.smh.module.weather.model;

import lombok.Getter;

@Getter
public enum WindTypeVO {
  N0(0, "북"),
  NNE(1, "북북동"),
  NE(2, "북동"),
  ENE(3, "동북동"),
  E(4, "동"),
  ESE(5, "동남동"),
  SE(6, "남동"),
  SSE(7, "남남동"),
  S(8, "남"),
  SSW(9, "남남서"),
  SW(10, "남서"),
  WSW(11, "서남서"),
  W(12, "서"),
  WNW(13, "서북서"),
  NW(14, "북서"),
  NNW(15, "북북서"),
  N16(16, "북");

  private int code; // 풍향 코드
  private String name; // 풍향 종류

  private WindTypeVO(int code, String name) {
    this.code = code;
    this.name = name;
  }

  public static WindTypeVO value(int value) {
    for (WindTypeVO type : WindTypeVO.values()) {
      if (type.getCode() == value) {
        return type;
      }
    }
    return null;
  }
}
