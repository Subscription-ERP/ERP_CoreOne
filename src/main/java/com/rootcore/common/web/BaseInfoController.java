package com.rootcore.common.web;

import org.springframework.http.ResponseEntity;     // ★ API 응답용
import org.springframework.web.bind.annotation.GetMapping;  // ★ GET 요청 맵핑
import org.springframework.web.bind.annotation.PathVariable; // ★ Path 파라미터
import org.springframework.web.bind.annotation.RequestMapping; // ★ URL prefix
import org.springframework.web.bind.annotation.RestController; // ★ REST 컨트롤러 선언

import com.rootcore.common.service.BaseInfoService;
import com.rootcore.common.vo.BaseInfoVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor  // ★ 생성자 자동 생성
@RestController
@RequestMapping("/api/com/baseinfo")
public class BaseInfoController {

    private final BaseInfoService baseInfoService;

    @GetMapping("/{companyCode}")
    public ResponseEntity<BaseInfoVO> getCompany(@PathVariable String companyCode) {
        return ResponseEntity.ok(baseInfoService.getCompany(companyCode));
    }
}