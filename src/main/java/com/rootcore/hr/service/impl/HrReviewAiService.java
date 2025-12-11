package com.rootcore.hr.service.impl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.rootcore.hr.vo.ReviewCommentRequestVO;
import com.rootcore.hr.vo.ReviewItemScoreVO;

@Service
public class HrReviewAiService {

	private final ChatClient chatClient;
	
	public HrReviewAiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }
	
	// 인사평가 코멘트용 시스템 프롬프트
	private static final String REVIEW_COMMENT_SYSTEM_PROMPT = """
	        너는 HR 인사평가 코멘트를 작성하는 전문가야.
	        아래에 피평가자 정보와 평가 항목별 점수(A~E)가 주어질 거야.

	        - A는 매우 우수, B는 우수, C는 보통, D는 미흡, E는 매우 미흡으로 보면 돼.
	        - 점수가 높은 항목은 구체적으로 칭찬을 해주고,
	          점수가 낮은 항목은 부드럽게 개선 방향을 제안해줘.
	        - 말투는 상사가 작성하는 평가 코멘트처럼 **존댓말**을 사용해.
	        - 너무 공격적이거나 감정적인 표현은 쓰지 말고,
	          격려 + 피드백 느낌이 나도록 작성해.
	        - 문장은 3~5문장 정도의 한 단락으로 작성해.
	        - "A, B" 같은 등급 글자를 그대로 쓰지 말고,
	          자연스러운 한국어 표현(예: "탁월한 수준", "우수한 편", "보완이 필요한 부분")으로 바꿔서 써.
	        """;
	
	// 코멘트 생성 메서드
    public String generateReviewComment(ReviewCommentRequestVO vo) {

        StringBuilder sb = new StringBuilder();

        sb.append("[피평가자 정보]\n");
        sb.append("이름: ").append(vo.getUserName()).append("\n");
        sb.append("직무/직책: ").append(vo.getJobTitle()).append("\n");
        sb.append("평가기간: ").append(vo.getPeriod()).append("\n\n");

        sb.append("[평가 항목 및 점수]\n");
        for (ReviewItemScoreVO item : vo.getItems()) {
            sb.append("- 항목명: ").append(item.getItemName())
              .append(" / 설명: ").append(item.getDescription())
              .append(" / 가중치: ").append(item.getWeight())
              .append(" / 평점: ").append(item.getGrade())   // A~E
              .append("\n");
        }

        String userPrompt = sb.toString();

        // AI 호출
        return chatClient.prompt()
                .system(REVIEW_COMMENT_SYSTEM_PROMPT)
                .user(userPrompt)
                .call()
                .content()
                .trim();
    }
	
}
