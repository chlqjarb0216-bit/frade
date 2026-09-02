package com.frade.common;

//유저가 업로드한 파일 접근용 경로
//사용할때 
//	FilePath.FILE_ROOT_PATH + FilePath.USER_PROFILE_PATH + "/파일이름.확장자"
//식으로 사용
//필요하면 추가
public class FilePath {
	//파일스토리지 접속루트경로
	public static final String FILE_ROOT_PATH = "/file-storage";

	//유저 프로필 저장할 폴더
	public static final String USER_PROFILE_PATH = "/user_profile";

	//게시글 첨부파일 저장할 폴더
	public static final String POST_UPLOADFILE_PATH = "/post_uploadfiles";
}
