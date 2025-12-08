package com.rootcore.sb.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rootcore.sb.service.CompanyService;
import com.rootcore.sb.service.PaymentService;
import com.rootcore.sb.vo.CompanyVO;
import com.rootcore.sb.vo.SubscribeVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manage")
public class SubRestController {

    private final CompanyService companyService;
    private final PaymentService paymentService;

    /**
     * 관리자 - 회사 목록 조회, 구독 이력 조회
     * 예: Toast UI Grid에서 readData로 호출
     */
    
    // 회사 목록 조회
    @GetMapping("/list")
    public List<CompanyVO> getCompanyList( @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String ceoName) {
        List<CompanyVO> companyList = companyService.selectCompanyList(companyName, ceoName);
        return companyList;
    }
    // 회사 상세 조회
    @GetMapping("/companyDetail")
    public CompanyVO getCompanyDetail(@RequestParam String companyCode) {
        return companyService.selectCompanyDetail(companyCode);
    }
    // 회사 정보 수정
    @PutMapping("/company")
    public int updateCompany(@RequestBody CompanyVO vo) {
        return companyService.updateCompany(vo);
    }

    // 회사 구독 이력
    @GetMapping("/subscribes")
    public List<SubscribeVO> getCompanySubscribeHistory(@RequestParam("companyCode") String companyCode) {

        List<SubscribeVO> historyList = paymentService.selectInactiveSubListByComCode(companyCode);

        return historyList;
    }
    
	/* 회사(사용자) - 구독 상세 화면, 결제 이력 조회 */
 
    
}