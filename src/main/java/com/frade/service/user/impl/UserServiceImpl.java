package com.frade.service.user.impl;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.frade.common.FilePath;
import com.frade.common.ResultCode;
import com.frade.dto.user.UserProfileDTO;
import com.frade.dto.user.UserSignDTO;
import com.frade.service.user.UserService;
import com.frade.util.SHA256Encryptor;

@Service
public class UserServiceImpl implements UserService{

	@Override
	public int userLogin(UserSignDTO userSignDTO) {
		
		// 로그인 확인용 아이디 비밀번호 아이디:test 비번:1234
		
		if("test".equals(userSignDTO.getUserId()) && "1234".equals(userSignDTO.getUserPw()) ) {
			
			return 1;
			
		}
		
		
		return -1;
	}

	@Override
	public boolean checkUserId(String userId) {
		
		if("test".equals(userId)) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean checkUserNick(String userNick) {
		 if ("홍명보".equals(userNick)) {
		        return true;
		    }

		    return false;
	}
	
	@Override
	public boolean checkUserEmail(String userEmail) {
		
		if("test@test.com".equals(userEmail)) {
			return true;
		}
		
		return false;
	}

	@Override
	public ResultCode userSignup(UserSignDTO userSignDTO) {

	    boolean idResult = checkUserId(userSignDTO.getUserId());
	    	
	    if(idResult) {
	        return ResultCode.DUP_ID;
	    }

	    boolean nickResult = checkUserNick(userSignDTO.getUserNick());

	    if(nickResult) {
	        return ResultCode.DUP_NICK;
	    }

	    boolean emailResult = checkUserEmail(userSignDTO.getUserEmail());
	    
	    if(emailResult) {
	        return ResultCode.DUP_EMAIL;
	    }

	    System.out.println("회원가입 정보 : " + userSignDTO);

	    return ResultCode.SUCCESS;
		}

	@Override
	public ResultCode updateUserProfile(UserProfileDTO userProfileDTO, 
			MultipartFile profilePhoto, boolean defaultPhoto, boolean passwordChange) {
		
		//프로필 수정정보 확인용
		System.out.println("service 프로필 수정 정보: " + userProfileDTO);
		
		 // 비밀번호 변경 처리
	   if(passwordChange) {

	        try {

	            // 임시 DB 비밀번호
	            // 나중에 DAO에서 암호화된 비밀번호를 조회하도록 변경
	            String dbPw = SHA256Encryptor.encrypt("1234");

	            // 현재 비밀번호 확인
	            boolean pwMatch = SHA256Encryptor.matches(
	                    userProfileDTO.getCurrentPw(),
	                    dbPw);

	            if(pwMatch == false) {
	                return ResultCode.CURRENT_PW_NOT_MATCH;
	            }

	            // 새 비밀번호 암호화
	            String encryptedNewPw =
	                    SHA256Encryptor.encrypt(
	                            userProfileDTO.getNewPw());

	            userProfileDTO.setNewPw(encryptedNewPw);

	            System.out.println("암호화된 새 비밀번호 : "
	                    + encryptedNewPw);

	            // 나중에 DAO 비밀번호 수정 처리

	        } catch(NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	    }
		
		
		 String uploadPath = "D:/fileStorage_Frade" + FilePath.USER_PROFILE_PATH;
		 
		 

		    File uploadFolder = new File(uploadPath);

		    // 폴더가 없으면 생성
		    if(!uploadFolder.exists()) {
		        uploadFolder.mkdirs();
		    }

		    String userNum = String.valueOf(userProfileDTO.getUserNum());

		    String[] extensions = {".png", ".jpg", ".jpeg"};

		    // 기본 이미지로 변경하는 경우
		    if(defaultPhoto) {

		        // 기존 프로필 사진 삭제
		        for(String ext : extensions) {

		            File oldFile = new File(uploadFolder, userNum + ext);

		            if(oldFile.exists()) {
		                oldFile.delete();
		            }
		        }

		        // DB 연결 후에는 프로필 사진값을 null 또는 기본값으로 수정
		        userProfileDTO.setUserPhoto(null);

		        // 확인용
		        System.out.println("기본 이미지로 변경");
		    }

		    // 새 프로필 사진을 선택한 경우
		    if(profilePhoto != null && !profilePhoto.isEmpty()) {

		        // 원래 파일명
		        String originalFileName = profilePhoto.getOriginalFilename();

		        // 확장자 가져오기
		        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

		        // 프로필 사진 확장자 확인
		        if(!extension.equalsIgnoreCase(".png")
		                && !extension.equalsIgnoreCase(".jpg")
		                && !extension.equalsIgnoreCase(".jpeg")) {

		            return ResultCode.INVALID_PROFILE_FILE;
		        }

		        // MIME 타입 확인
		        String contentType = profilePhoto.getContentType();

		        if(contentType == null || (!contentType.equals("image/png")
		                && !contentType.equals("image/jpeg"))) {

		            return ResultCode.INVALID_PROFILE_FILE;
		        }

		        // 기존 프로필 사진 삭제
		        for(String ext : extensions) {

		            File oldFile = new File(uploadFolder, userNum + ext);

		            if(oldFile.exists()) {
		                oldFile.delete();
		            }
		        }

		        // 유저번호 + 확장자로 저장
		        String saveFileName = userProfileDTO.getUserNum() + extension;

		        try {

		            // 실제 저장 위치
		            File saveFile = new File(uploadFolder, saveFileName);

		            // 파일 저장
		            profilePhoto.transferTo(saveFile);

		            // 나중에 DB에 저장할 파일명
		            userProfileDTO.setUserPhoto(saveFileName);

		            // 확인용
		            System.out.println("저장된 프로필 사진: " + userProfileDTO.getUserPhoto());

		        } catch(IOException e) {
		            e.printStackTrace();
		            
		            return ResultCode.PROFILE_FILE_SAVE_FAIL;
		        }
		    }

		    return ResultCode.SUCCESS;
		}

	@Override
	public ResultCode deleteUser(int userNum) {
		
		//확인용
		System.out.println("회원 탈퇴 요청 suerNum: "+ userNum);
		
		return ResultCode.SUCCESS;
	}

	@Override
	public UserProfileDTO getUserProfile(int userNum) {
		
		//정보 확인용 하드코딩
		UserProfileDTO userProfileDTO = new UserProfileDTO();
		
		userProfileDTO.setUserNum(userNum);
		userProfileDTO.setUserNick("개미하이");
		userProfileDTO.setUserPhoto("1.png");
		userProfileDTO.setUserPortfolioIsPublic(0);
		userProfileDTO.setUserRegistedDate(LocalDateTime.of(2026, 9, 1, 5, 30));
		
		return userProfileDTO;
	}

	
	
	

	
	

	
	

	
	

}
