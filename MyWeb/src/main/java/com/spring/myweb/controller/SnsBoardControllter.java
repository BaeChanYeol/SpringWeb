package com.spring.myweb.controller;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.myweb.command.SnsBoardVO;
import com.spring.myweb.command.UserVO;
import com.spring.myweb.snsboard.service.ISnsBoardService;

@Controller
@RequestMapping("/snsBoard")
public class SnsBoardControllter {

	@Autowired
	private ISnsBoardService service;

	@GetMapping("/snsList")
	public void snsList() {

	}

	@PostMapping("/upload")
	@ResponseBody
	public String upload(@RequestParam("file") MultipartFile file, @RequestParam("content") String content,
			HttpSession session) {

		try {
			UserVO vo = (UserVO) session.getAttribute("login");
			String writer = vo.getUserId();

			// 날짜별로 폴더를 생성해서 파일을 관리

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			String fileLoca = sdf.format(date);

			// 저장할 폴더 경로
			String uploadPath = "C:\\Users\\admin\\Desktop\\upload\\" + fileLoca;

			File folder = new File(uploadPath);

			if (!folder.exists()) {
				folder.mkdir(); // 폴더가 존재하지 않는다면 생성해라.
			}

			// 서버에서 저장할 파일 이름
			String fileRealName = file.getOriginalFilename();
			long size = file.getSize();

			// 파일명을 고유한 랜덤 문자로 작성.
			UUID uuid = UUID.randomUUID();

			String uuids = uuid.toString().replace("-", "");

			// 확장자
			String fileExtension = fileRealName.substring(fileRealName.lastIndexOf("."), fileRealName.length());

			System.out.println("저장할 폴더: " + uploadPath);
			System.out.println("실제 파일명: " + fileRealName);
			System.out.println("사이즈: " + size);
			System.out.println("폴더명: " + fileLoca);
			System.out.println("확장자: " + fileExtension);
			System.out.println("uuid: " + uuids);

			String fileName = uuids + fileExtension;
			System.out.println("변경해서 저장할 파일명: " + fileName);
			// 업로드한 파일을 서버컴퓨터의 지정한 경로 내에 실제로 저장
			File saveFile = new File(uploadPath + "\\" + fileName);
			file.transferTo(saveFile);

			// DB에 insert 작업을 진행.
			SnsBoardVO snsVO = new SnsBoardVO(0, writer, uploadPath, fileLoca, fileName, fileRealName, content, null);
			service.insert(snsVO);

			return "success";

		} catch (Exception e) {
			System.out.println("업로드중 에러 발생 : " + e.getMessage());
			return "fail";
		}

	}

	// 비동기 통신 후 가져올 목록
	@GetMapping("/getList")
	@ResponseBody
	public List<SnsBoardVO> getList() {
		return service.getList();
	}

	// ResponseEntity : 응답으로 변환될 정보를 모두 담은 요소들을 객체로 만들어서 반환해 줍니다.
	@GetMapping("/display") 
	public ResponseEntity<byte[]> getFile(@RequestParam("fileLoca") String fileLoca,
			@RequestParam("fileName") String fileName) {

		System.out.println("fileName: " + fileName);
		System.out.println("fileLoca: " + fileLoca);
		File file = new File("C:\\Users\\admin\\Desktop\\upload\\" + fileLoca + "\\" + fileName);
		System.out.println(file);

		ResponseEntity<byte[]> result = null;

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", Files.probeContentType(file.toPath()));
			// ResponseEntity<>(바디에 담을 내용, 헤더에 담을 내용, 상태메세지)
			// FileCopyUtills : 파일 및 스트림 복사를 위한 간단한 유틸리티 메서드와 집합체
			// file 객체 안에 있는 내용을 복사해서 byte배열 형태로 바디에 담에서 전달.
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	@GetMapping("/getDetail/{bno}")
	@ResponseBody
	public SnsBoardVO getDetail(@PathVariable int bno) {

		return service.getDetail(bno);
	}

	@PostMapping("/delete/{bno}")
	@ResponseBody
	public String delete(@PathVariable int bno, HttpSession session) {

		SnsBoardVO vo = service.getDetail(bno);
//		System.out.println(vo.getWriter());
//		System.out.println(((UserVO)session.getAttribute("login")).getUserId());
		if (session.getAttribute("login") != null) {
			if (vo.getWriter().equals(((UserVO) session.getAttribute("login")).getUserId())) {
				service.delete(bno);
				
				//파일 객체를 생성해서 지워지고 있는 게시물의 파일을 지움
				File file = new File(vo.getUploadPath() + "\\" + vo.getFilename());		
				return file.delete() ? "Success" : "fail"; //파일 삭제 메서드
			}
		}
		return "noAuth";

	}

	//다운로드 비동기처리(화면에서 클릭시 a태그를 통해 download를 타도록 처리)
	@GetMapping("/download")
	@ResponseBody
	public ResponseEntity<byte[]> downloan(@RequestParam("fileLoca") String fileLoca,
											@RequestParam("fileName") String fileName){
		
		System.out.println("fileName: " + fileName);
		System.out.println("fileLoca: " + fileLoca);
		File file = new File("C:\\Users\\admin\\Desktop\\upload\\" + fileLoca + "\\" + fileName);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			
			//응답하는 본문을 브라우저가 어떻게 표시해야 할 지 알려주는 헤더 정보입니다.
			//inline인 경우 웹페이지 화면에 표시되고, attachment인 경우 다운로드를 제공합니다.
			
			
			
			//파일명한글처리(Chrome browser) 크롬
	         //header.add("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") );
	         //파일명한글처리(Edge) 엣지 
	         //header.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
	         //파일명한글처리(Trident) IE
	         //Header.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", " "));
			
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Disposition", "attachment; filename=" + fileName);

			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	
}















