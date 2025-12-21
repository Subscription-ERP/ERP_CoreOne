package com.rootcore.sb.service;

import com.rootcore.sb.vo.CompanyVO;

import jakarta.servlet.http.HttpSession;

public interface CompanyDraftService {

    /**
     * 회사 등록 1단계: 회사정보를 검증/정규화 후 세션에 임시 저장
     */
    void saveDraft(CompanyVO requestVO, HttpSession session);

    /**
     * 세션에 저장된 회사정보 조회 (없으면 예외)
     */
    CompanyVO getDraft(HttpSession session);

    /**
     * 세션에 저장된 회사정보 삭제 (결제 완료 후 등)
     */
    void clearDraft(HttpSession session);
}
