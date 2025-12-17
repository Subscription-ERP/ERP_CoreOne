package com.rootcore.hr.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rootcore.auth.service.PasswordResetService;
import com.rootcore.auth.util.PasswordUtil;
import com.rootcore.auth.vo.LoginVO;
import com.rootcore.common.util.FileStorageUtil;
import com.rootcore.hr.mapper.HrMapper;
import com.rootcore.hr.service.HrService;
import com.rootcore.hr.vo.CertificationVO;
import com.rootcore.hr.vo.DeptVO;
import com.rootcore.hr.vo.UserHistoryVO;
import com.rootcore.hr.vo.UserSearchVO;
import com.rootcore.hr.vo.UserVO;
import com.rootcore.hr.vo.WorkExperienceVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HrServiceImpl implements HrService {

	private final HrMapper hrMapper;
	private final FileStorageUtil fileStorageUtil;
	private final PasswordEncoder passwordEncoder;
	private final PasswordResetService passwordResetService;

	// 사원 		
	// 전체조회 + 검색
	@Override
	public List<UserVO> selectUserList(UserSearchVO userSearchVO) {
		return hrMapper.selectUserSearch(userSearchVO);
	}
	
	// 상세조회 - 기본사항/자격증/경력사항/이력
	@Override
	public UserVO selectUserDetail(String userId) {
		// 1) 사원 기본사항 조회
	    UserVO user = hrMapper.selectUserDetail(userId);
		
	    // 2) 없는 사원일 경우
        if (user == null) {
        	return null; 
        }
		
        // 3) 자격증/경력사항/이력 조회
        List<CertificationVO> certi = hrMapper.selectUserCertification(userId); 
        List<WorkExperienceVO> wex = hrMapper.selectUserWorkExperience(userId);
        List<UserHistoryVO> hist = hrMapper.selectUserHistory(userId);
        
		// 4) userVO안의 리스트필드에 세팅
        user.setCertificationList(certi);
        user.setWorkExperienceList(wex);
        user.setHistoryList(hist);
			
		return user;
	}
	
	// 부서조회
	@Override
	public List<DeptVO> selectDeptMaster() {
		return hrMapper.selectDeptMaster();
	}
	

	// 등록
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertUser(UserVO userVO,
			              MultipartFile userPhoto,
		                  MultipartFile userFile,
		                  List<MultipartFile> certiFiles) throws Exception {
		
		// 이메일 여부확인
		if (userVO.getEmail() == null || userVO.getEmail().isBlank()) {
		    throw new IllegalArgumentException("이메일은 필수입니다.");
		}
		
		// 파일 -------------------------------------------------------------------
		// 기본정보 - 사진
		if(userPhoto != null && !userPhoto.isEmpty()) {
			String photoPath = fileStorageUtil.store(userPhoto, "user/photo");
			userVO.setUserPhoto(photoPath);
		}
		
		// 기본정보 - 첨부파일
		if(userFile != null && !userFile.isEmpty()) {
			String userFilePath = fileStorageUtil.store(userFile, "user/file");
			userVO.setUserFile(userFilePath);
		}
		
		
		// 기본정보 + userId생성 ------------------------------------------------------
		int r1 = hrMapper.insertUserBasic(userVO);
		if(r1 == 0) return 0;
		
		String userId = userVO.getUserId();
		String companyCode = userVO.getCompanyCode();
		
		// 로그인 마스터 --------------------------------------------------------------
		// 임시 비밀번호 생성
		String tempPw = PasswordUtil.generateRandomPassword();
		// 암호화
		String encodePw = passwordEncoder.encode(tempPw);

		LoginVO loginVO = new LoginVO();
		loginVO.setCompanyCode(companyCode);
		loginVO.setUserId(userId);
		loginVO.setUserName(userVO.getUserName());
		loginVO.setDbPassword(encodePw);
		loginVO.setCreatedBy(userVO.getCreatedBy());
		
		int rLogin = hrMapper.insertLoginMaster(loginVO);
		if(rLogin == 0) return 0; 
		
		// 비밀번호 재설정 링크 이메일 발송 (사용자가 직접 비번 설정)
		boolean mailOk = passwordResetService.sendResetLink(userId, userVO.getEmail());
		if (userVO.getEmail() == null || userVO.getEmail().isBlank()) {
		    throw new RuntimeException("이메일이 없어 비밀번호 재설정 링크를 발송할 수 없습니다.");
		}
		if(!mailOk) {
		    throw new RuntimeException("비밀번호 재설정 이메일 발송 실패");
		}
		
		
		// 자격증 -------------------------------------------------------------------
		if(userVO.getCertificationList() != null && !userVO.getCertificationList().isEmpty()) {
			for(int i=0; i<userVO.getCertificationList().size(); i++) {
				CertificationVO cert = userVO.getCertificationList().get(i);
				
				cert.setUserId(userId);
				cert.setCompanyCode(companyCode);
				
				// 파일
				if(certiFiles != null && i < certiFiles.size()) {
					MultipartFile certFile = certiFiles.get(i);
					if(certFile != null && !certFile.isEmpty()) {
						String certFilePath = fileStorageUtil.store(certFile, "user/certification");
						cert.setCertiFile(certFilePath);
					}
				}
				int r2 = hrMapper.insertCertification(cert);
				if (r2 == 0) return 0;
			}
		}
		
		// 경력사항 ------------------------------------------------------------------
		if(userVO.getWorkExperienceList() != null && !userVO.getWorkExperienceList().isEmpty()) {
			for(WorkExperienceVO wex : userVO.getWorkExperienceList()) {
				wex.setUserId(userId);
				wex.setCompanyCode(companyCode);
				int r3 = hrMapper.insertWorkExprience(wex);
				if(r3 == 0) return 0;
			}
		}
		
		return 1;
	}

	
	// 사원 수정(기본사항,자격증,경력사항) + 이력 업데이트
	@Transactional
	@Override
	public int updateUser(UserVO userVO, 
			              MultipartFile userPhoto, 
			              MultipartFile userFile,
			              List<MultipartFile> certiFiles) throws Exception {
		
		// 기존 DB 데이터 조회
		UserVO origin = hrMapper.selectUserDetail(userVO.getUserId());
	    if (origin == null) {
	        return 0;
	    }
	    
	    // companyCode 
	    if (userVO.getCompanyCode() == null || userVO.getCompanyCode().isBlank()) {
	        userVO.setCompanyCode(origin.getCompanyCode());
	    }
		
	    String userId = userVO.getUserId();
	    String companyCode = userVO.getCompanyCode();
	    String actor = userVO.getUpdatedBy();
	    
	    
	    // 기본정보 ---------------------------------------------------------
	    // 사진
	    if (userPhoto != null && !userPhoto.isEmpty()) {
	        // 새 파일 업로드 → 파일 저장 후 경로 세팅
	        String photoPath = fileStorageUtil.store(userPhoto, "user/photo");
	        userVO.setUserPhoto(photoPath);
	    } else {
	        // 새 파일 안 올렸으면 기존 경로 유지
	        userVO.setUserPhoto(origin.getUserPhoto());
	    }
		
	    // 첨부파일
	    if (userFile != null && !userFile.isEmpty()) {
	        String userFilePath = fileStorageUtil.store(userFile, "user/file");
	        userVO.setUserFile(userFilePath);
	    } else {
	        userVO.setUserFile(origin.getUserFile());
	    }
		

	    // 기본정보
	    int r1 = hrMapper.updateUser(userVO);
	    if (r1 == 0) {
	        return 0;
	    }
	    
	    
	    // 자격증 및 경력사항 --------------------------------------------------
	    // 자격증, 경력사항 전체 삭제
	    hrMapper.deleteCetification(userId, companyCode);
	    hrMapper.deleteWorkExperience(userId, companyCode);
	    
	    // 자격증
	    if (userVO.getCertificationList() != null) {
	        for (int i = 0; i < userVO.getCertificationList().size(); i++) {

	            CertificationVO cert = userVO.getCertificationList().get(i);

	            // 완전 빈 행 제외
	            if (isEmptyCert(cert)) continue;

	            cert.setUserId(userId);
	            cert.setCompanyCode(companyCode);
	            cert.setCreatedBy(actor);

	            // 파일 처리
	            if (certiFiles != null && i < certiFiles.size()) {
	                MultipartFile certFile = certiFiles.get(i);
	                if (certFile != null && !certFile.isEmpty()) {
	                    String certPath = fileStorageUtil.store(certFile, "user/certification");
	                    cert.setCertiFile(certPath);
	                }
	            }
	            hrMapper.insertCertification(cert);
	        }
	    }
	    
	    // 경력사항
	    if (userVO.getWorkExperienceList() != null) {
	        for (WorkExperienceVO wex : userVO.getWorkExperienceList()) {

	            if (isEmptyWex(wex)) continue;

	            wex.setUserId(userId);
	            wex.setCompanyCode(companyCode);
	            wex.setCreatedBy(actor);

	            hrMapper.insertWorkExprience(wex);
	        }
	    }
	    
	    
	    // 이력 등록(insert) -------------------------------------------------
	    // 부서 변경시
	    boolean deptChanged =
	            !safeEquals(origin.getDept(), userVO.getDept()) ||
	            !safeEquals(origin.getJobTitle(), userVO.getJobTitle());

	    if (deptChanged) {
	            UserHistoryVO hist = new UserHistoryVO();
	            hist.setCompanyCode(companyCode);
	            hist.setUserId(userId);
	            hist.setHistType("f1");
	            hist.setApplyDate(userVO.getHireDate());
	            hist.setPrevDept(origin.getDept());
	            hist.setPrevJobTitle(origin.getJobTitle());
	            hist.setNewDept(userVO.getDept());
	            hist.setNewJobTitle(userVO.getJobTitle());
	            hist.setDeptChangeReason("수정 화면에서 변경");
	            hist.setCreatedBy(actor);

	            hrMapper.insertUserHistory(hist);
	    }
	    
	    // 급여 변경시
	    boolean salaryChanged =
	            origin.getSalary() == null && userVO.getSalary() != null ||
	            origin.getSalary() != null && !origin.getSalary().equals(userVO.getSalary());

	    if (salaryChanged) {
	            UserHistoryVO hist = new UserHistoryVO();
	            hist.setCompanyCode(companyCode);
	            hist.setUserId(userId);
	            hist.setHistType("f2");
	            hist.setApplyDate(userVO.getHireDate());
	            hist.setBaseSalary(userVO.getSalary());
	            hist.setSalaryChangeReason("수정 화면에서 변경");
	            hist.setUpdatedBy(actor);

	            hrMapper.insertUserHistory(hist);
	    }
	    
		return 1;
	}
	
	// 수정사항시 헬퍼 메서드 ----------------------------------------
	private boolean isEmptyCert(CertificationVO cert) {
	    return (cert == null ||
	        (isBlank(cert.getCertiName()) &&
	         isBlank(cert.getIssueOrgName()) &&
	         isEmptyDate(cert.getGetDate()) &&
	         isBlank(cert.getLicenseNo()) &&
	         isEmptyDate(cert.getExpireDate()) &&
	         isBlank(cert.getRemark())
	        )
	    );
	}
	private boolean isEmptyWex(WorkExperienceVO wex) {
	    return (wex == null ||
	        (isBlank(wex.getWexCompanyName()) &&
	         isBlank(wex.getWexDept()) &&
	         isBlank(wex.getWexJobTitle()) &&
	         isEmptyDate(wex.getWexHireDate()) &&
	         isEmptyDate(wex.getWexLeaveDate()) &&
	         isBlank(wex.getWexMainDuty()) &&
	         (wex.getWexSalary() == null)
	        )
	    );
	}

	private boolean isEmptyDate(Object date) {
	    return date == null;
	}

	private boolean isBlank(String s) {
	    return s == null || s.trim().isEmpty();
	}

	private boolean safeEquals(String a, String b) {
	    if (a == null && b == null) return true;
	    if (a == null || b == null) return false;
	    return a.equals(b);
	}

	
	// 비밀번호 초기화 이메일 전송
	@Override
	public boolean resendResetLink(String companyCode, String userId, String requestedBy) {
		// 1) 대상 사원 조회
	    UserVO user = hrMapper.selectUserDetail(userId);
	    if (user == null) {
	        throw new IllegalArgumentException("사원을 찾을 수 없습니다: " + userId);
	    }

	    // 2) 회사코드 안전장치 (원하시면)
	    if (companyCode != null && user.getCompanyCode() != null) {
	        if (!companyCode.equals(user.getCompanyCode())) {
	            throw new IllegalArgumentException("회사 정보가 일치하지 않습니다.");
	        }
	    }

	    // 3) 이메일 체크
	    if (user.getEmail() == null || user.getEmail().isBlank()) {
	        throw new IllegalArgumentException("이메일이 없어 비밀번호 재설정 링크를 발송할 수 없습니다.");
	    }

	    // 4) 재설정 링크 발송
	    boolean ok = passwordResetService.sendResetLink(userId, user.getEmail());
	    if (!ok) {
	        throw new RuntimeException("비밀번호 재설정 이메일 발송 실패");
	    }

	    return true;
	}



}
