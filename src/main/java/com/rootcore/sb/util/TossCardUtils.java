package com.rootcore.sb.util;

import java.util.Map;

public class TossCardUtils {
	
	 // 카드 코드 → 한글 카드사명 매핑 테이블
    private static final Map<String, String> CARD_CODE_MAP = Map.ofEntries(
        Map.entry("11", "국민"),
        Map.entry("15", "카카오뱅크"),
        Map.entry("21", "하나"),
        Map.entry("24", "토스뱅크"),
        Map.entry("30", "산업"),
        Map.entry("14", "비씨"),
        Map.entry("W1", "우리"),
        Map.entry("35", "전북"),
        Map.entry("36", "씨티"),
        Map.entry("37", "우체국"),
        Map.entry("38", "새마을"),
        Map.entry("39", "저축"),
        Map.entry("3A", "케이뱅크"),
        Map.entry("3C", "유니온페이"),
        Map.entry("41", "신한"),
        Map.entry("42", "제주"),
        Map.entry("46", "광주"),
        Map.entry("4M", "마스터"),
        Map.entry("4V", "비자"),
        Map.entry("51", "삼성"),
        Map.entry("51", "삼성"),
        Map.entry("4M", "마스터"),
        Map.entry("4M", "마스터")
        // 필요하면 계속 추가 가능
    );
}
