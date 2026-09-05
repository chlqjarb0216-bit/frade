package com.frade.service.user.impl;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.frade.common.FilePath;
import com.frade.common.ResultCode;
import com.frade.dao.user.UserDAO;
import com.frade.dto.user.UserDTO;
import com.frade.dto.user.UserLoginDTO;
import com.frade.dto.user.UserProfileDTO;
import com.frade.dto.user.UserSessionDTO;
import com.frade.dto.user.UserSignupDTO;
import com.frade.service.user.UserService;
import com.frade.util.SHA256Encryptor;

@Service
public class UserServiceImpl implements UserService{
	
	
	@Autowired
	UserDAO userDAO;

	@Override
	public UserSessionDTO userLogin(UserLoginDTO userLoginDTO) {

		// 로그인 확인용 임시 데이터
		if ("test123".equals(userLoginDTO.getUserId()) && "test1234!!".equals(userLoginDTO.getUserPw())) {

			return new UserSessionDTO(1, // userNum
					"개미하이", // userNick
					"1.png" // userPhoto
			);
		}

		return null;
	}

	@Override
	public boolean checkUserId(String userId) {
		
	    // DB에서 같은 아이디를 사용하는 회원 수 조회
	    int count = userDAO.countUserId(userId);

	    // 1명 이상이면 중복
	    return count > 0;
	}

	@Override
	public boolean checkUserNick(String userNick) {
		
	    // DB에서 같은 닉네임을 사용하는 회원 수 조회
	    int count = userDAO.countUserNick(userNick);

	    // 1명 이상이면 중복
	    return count > 0;
	}
	
	@Override
	public boolean checkUserEmail(String userEmail) {
		
	    // DB에서 같은 이메일을 사용하는 회원 수 조회
	    int count = userDAO.countUserEmail(userEmail);

	    // 1명 이상이면 중복
	    return count > 0;
	}

	@Override
	@Transactional
	public ResultCode userSignup(UserSignupDTO userSignupDTO) {

	    boolean idResult = checkUserId(userSignupDTO.getUserId());

	    if(idResult) {
	        return ResultCode.DUP_ID;
	    }

	    boolean nickResult = checkUserNick(userSignupDTO.getUserNick());

	    if(nickResult) {
	        return ResultCode.DUP_NICK;
	    }

	    boolean emailResult = checkUserEmail(userSignupDTO.getUserEmail());

	    if(emailResult) {
	        return ResultCode.DUP_EMAIL;
	    }

	    // 비밀번호 암호화
	    String encryptedPw = null;

	    try {

			encryptedPw = SHA256Encryptor.encrypt(userSignupDTO.getUserPw());

	    } catch(NoSuchAlgorithmException e) {

	        e.printStackTrace();

	        return ResultCode.PASSWORD_ENCRYPT_FAIL;
	    }

	    
	    int userNum = userDAO.getNextUserNum();

	    // Controller용 DTO → DB용 DTO 변환
	    // 회원가입용 생성자 사용
	    UserDTO userDTO = new UserDTO(
	    		userNum,
	            userSignupDTO.getUserId(),       // userId
	            userSignupDTO.getUserNick(),     // userNick
	            userSignupDTO.getUserEmail(),    // userEmail
	            encryptedPw                      // userPw
	    );

		System.out.println("DB에 전달할 회원가입 정보 : " + userDTO);
		
		// T_USER 테이블에 회원정보 저장
		int userResult = userDAO.saveUser(userDTO);

		// INSERT가 정상적으로 1건 처리되지 않은 경우
		if (userResult != 1) {
			return ResultCode.FAIL;
		}

		// T_CASH 테이블에 가입한 회원의 자금정보 생성
		// T_USER에 넣었던 것과 같은 userNum 사용
		int cashResult = userDAO.saveUserCash(userNum);

		// INSERT가 정상적으로 1건 처리되지 않은 경우
		if (cashResult != 1) {
			return ResultCode.FAIL;
		}    

	    return ResultCode.SUCCESS;
	}


	@Override
	public ResultCode updateUserProfile(
	        UserProfileDTO userProfileDTO) {

	    // 프로필 수정정보 확인용
	    System.out.println(
	            "service 프로필 수정 정보: "
	            + userProfileDTO);


	    // DTO에 담겨서 넘어온 프로필 수정 정보
	    MultipartFile profilePhoto =
	            userProfileDTO.getProfilePhoto();

	    boolean defaultPhoto =
	            userProfileDTO.isDefaultPhoto();

	    boolean passwordChange =
	            userProfileDTO.isPasswordChange();


	    /*
	     * 현재 프로필 정보 조회
	     * 현재는 getUserProfile()의 임시 데이터 사용
	     * DB 연결 후 DAO 조회 결과 사용
	     */
	    UserProfileDTO currentProfile =
	            getUserProfile(
	                    userProfileDTO.getUserNum());

	    // 현재 프로필 사진
	    String oldProfilePhoto =
	            currentProfile.getUserPhoto();


	    /*
	     * DB에 전달할 값
	     * 모든 처리가 끝난 후
	     * 마지막에 UserDTO 생성
	     */

	    // 비밀번호를 변경하지 않으면 null
	    String encryptedNewPw = null;

	    // 사진 변경이 없으면 기존 사진명 유지
	    String newProfilePhoto =
	            oldProfilePhoto;


	    /*
	     * 비밀번호 변경 처리
	     */
	    if(passwordChange) {

	        try {

	            // 임시 DB 비밀번호
	            // 나중에 DAO에서 암호화된 비밀번호 조회
				String dbPw = SHA256Encryptor.encrypt("test1234!!");

	            // 현재 비밀번호 확인
				boolean pwMatch = SHA256Encryptor.matches(userProfileDTO.getCurrentPw(), dbPw);

	            if(pwMatch == false) {
	                return ResultCode.CURRENT_PW_NOT_MATCH;
	            }

	            // 새 비밀번호 암호화
				encryptedNewPw = SHA256Encryptor.encrypt(userProfileDTO.getNewPw());

				System.out.println("암호화된 새 비밀번호 : " + encryptedNewPw);

	        } catch(NoSuchAlgorithmException e) {
	            e.printStackTrace();
	            return ResultCode.PASSWORD_ENCRYPT_FAIL;
	        }
	    }


	    /*
	     * 프로필 사진 저장 경로
	     */
	    String uploadPath =
	            FilePath.FILE_ABSOLUTE_STORE_PATH
	            + FilePath.USER_PROFILE_PATH;

	    File uploadFolder =
	            new File(uploadPath);

	    // 폴더가 없으면 생성
	    if(!uploadFolder.exists()) {
	        uploadFolder.mkdirs();
	    }


	    /*
	     * 기본 프로필로 변경
	     */
	    if(defaultPhoto) {

	        // 기존 프로필 사진 삭제
	        if(oldProfilePhoto != null) {

	            File oldFile =
	                    new File(
	                            uploadFolder,
	                            oldProfilePhoto);

	            if(oldFile.exists()) {
	                oldFile.delete();
	            }
	        }

	        // DB에는 null
	        newProfilePhoto = null;

	        System.out.println(
	                "기본 이미지로 변경");
	    }


	    /*
	     * 새 프로필 사진 선택
	     */
	    else if(profilePhoto != null
	            && !profilePhoto.isEmpty()) {

	        String originalFileName =
	                profilePhoto.getOriginalFilename();

	        String extension =
	                originalFileName.substring(
	                        originalFileName.lastIndexOf("."));


	        // 확장자 확인
	        if(!extension.equalsIgnoreCase(".png")
	                && !extension.equalsIgnoreCase(".jpg")
	                && !extension.equalsIgnoreCase(".jpeg")) {

	            return ResultCode.INVALID_PROFILE_FILE;
	        }


	        // MIME 타입 확인
	        String contentType =
	                profilePhoto.getContentType();

	        if(contentType == null
	                || (!contentType.equals("image/png")
	                && !contentType.equals("image/jpeg"))) {

	            return ResultCode.INVALID_PROFILE_FILE;
	        }


	        // 유저번호 + 확장자로 저장
	        String saveFileName =
	                userProfileDTO.getUserNum()
	                + extension;

	        try {

	            File saveFile =
	                    new File(
	                            uploadFolder,
	                            saveFileName);

	            // 새 프로필 사진 저장
	            profilePhoto.transferTo(saveFile);


	            /*
	             * 새 사진 저장 성공 후
	             * 기존 사진과 파일명이 다르면 기존 사진 삭제
	             */
	            if(oldProfilePhoto != null
	                    && !oldProfilePhoto.equals(
	                            saveFileName)) {

	                File oldFile =
	                        new File(
	                                uploadFolder,
	                                oldProfilePhoto);

	                if(oldFile.exists()) {
	                    oldFile.delete();
	                }
	            }


	            // DB에 저장할 새 파일명
	            newProfilePhoto =
	                    saveFileName;

	            System.out.println(
	                    "저장된 프로필 사진: "
	                    + newProfilePhoto);

	        } catch(IOException e) {

	            e.printStackTrace();

	            /*
	             * 사진 저장 실패
	             * → 기본 프로필 처리
	             */
	            newProfilePhoto = null;

	            // 기존 프로필 사진도 삭제
	            if(oldProfilePhoto != null) {

	                File oldFile =
	                        new File(
	                                uploadFolder,
	                                oldProfilePhoto);

	                if(oldFile.exists()) {
	                    oldFile.delete();
	                }
	            }

	            System.out.println(
	                    "프로필 사진 저장 실패"
	                    + " → 기본 프로필로 변경");
	        }
	    }

	    userProfileDTO.setUserPhoto(newProfilePhoto);

	    /*
	     * 모든 처리 완료 후
	     * DB용 UserDTO 생성
	     */
	    UserDTO userDTO = new UserDTO(
	            userProfileDTO.getUserNum(),
	            userProfileDTO.getUserNick(),
	            encryptedNewPw,
	            userProfileDTO.getUserPortfolioIsPublic(),
	            newProfilePhoto
	    );


	    System.out.println(
	            "DB에 전달할 프로필 수정 정보 : "
	            + userDTO);

	    // 나중에 DAO
	    // userDAO.updateUserProfile(userDTO);

	    return ResultCode.SUCCESS;
	}
	
	

	@Override
	public ResultCode deleteUser(int userNum) {
		
		//확인용
		System.out.println("회원 탈퇴 요청 userNum: "+ userNum);
		
		return ResultCode.SUCCESS;
	}

	@Override
	public UserProfileDTO getUserProfile(int userNum) {

	    // 임시 DB 조회 결과
	    UserProfileDTO userProfileDTO = new UserProfileDTO(
	            userNum,
	            "개미하이",
	            "1.png",
	            0,
	            LocalDateTime.of(2026, 9, 1, 5, 30)
	    );

	    return userProfileDTO;
	}

	
	
	

	
	

	
	

	
	

}
