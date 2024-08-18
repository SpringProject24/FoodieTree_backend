package org.nmfw.foodietree.domain.review.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
public enum Hashtag {
 SPECIAL_MENU("특별한 메뉴가 있어요"),
 VEGETARIAN_MENU("채식 메뉴가 있어요"),
 GOOD_MENU_COMBINATION("메뉴 구성이 알차요"),
 GOOD_VALUE("가성비 좋아요"),
 FRESH_INGREDIENTS("재료가 신선해요"),
 TASTY_FOOD("음식이 맛있어요"),
 FRIENDLY_STAFF("직원이 친절해요"),
 WANT_TO_REVISIT("재방문 하고 싶어요"),
 CLEAN_STORE("매장이 청결해요"),
 GOOD_PACKAGING("포장 상태가 좋아요"),
 FAST_SERVICE("빠르게 수령했어요"),
 HIGH_QUALITY_FOOD("음식 퀄리티가 좋아요"),
 EASY_TO_EAT("편하게 먹기 좋아요"),
 HOT_FOOD("따뜻하게 먹었어요"),
 PLEASANT_SURPRISE("의외의 발견");

 private final String keyword;

 Hashtag(String keyword) {
  this.keyword = keyword;
 }

 @Override
 public String toString() {
  return keyword;
  }
}

