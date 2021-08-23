package com.spring.myweb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.myweb.command.ReplyVO;
import com.spring.myweb.reply.service.IReplyService;
import com.spring.myweb.util.PageVO;

@RestController
@RequestMapping("/reply")
public class ReplyController {
	
	@Autowired
	private IReplyService service;
	
	//댓글 등록
	@PostMapping("/replyRegist")
	public String replyRegist(@RequestBody ReplyVO vo) {
		System.out.println("댓글 등록 요청이 들어옴!");
		
		service.replyRegist(vo);
		
		return "regSucess";
	}
	
	/*
	//일반 댓글 목록 (페이징 x)
	@GetMapping("/getList/{bno}")
	public List<ReplyVO> getList(@PathVariable int bno) {
		
		List<ReplyVO> list = service.getList(bno);
		System.out.println("댓글 개수: " + list.size());
		
		return list;
	}
	*/
	
	//페이징이 추가된 댓글 목록
	@GetMapping("/getList/{bno}/{pageNum}")
	public Map<String, Object> getList(@PathVariable int bno, @PathVariable int pageNum) {
		
		//1. 화면에 더보기 버튼 배치, 더보기 버튼 클릭 이벤트
		//2. getList 메서드가 (글번호, 페이지번호)를 매개값으로 받습니다.
		//3. Mapper 인터페이스에 각각 다른 값을 전달하기 위해 Map을 쓰던지 @Param 아노테이션 사용
		//4. sql문을 페이징 쿼리로 변경합니다.
		//5. 레스트 방식은 화면에 필요한 값을 여러 개 보낼 때, 리턴에 Map이나 VO형식으로 
		//필요한 데이터를 한번에 담아 처리.
		
		PageVO vo = new PageVO();
		vo.setPageNum(pageNum); //화면에서 전달되는 페이지 번호
		vo.setCountPerPage(10); //댓글은 한 화면에 10개씩
		
		List<ReplyVO> list = service.getList(vo, bno); //댓글 데이터
		int total = service.getTotal(bno); //전체 댓글 개수.
		System.out.println("전체 댓글 개수(con): " + total);
		
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("total", total);
		
		return map;
		
	}
	
	@PostMapping("/update")
	public String update(@RequestBody ReplyVO vo) {
		//비밀번호 확인
		int result = service.pwCheck(vo);
		
		if(result == 1) { //비밀번호가 맞는 경우
			service.update(vo);
			return "modSuccess";
		} else { //비밀번호가 틀린 경우
			return "pwFail";
		}
		
	}
	
	@PostMapping("/delete")
	public String delete(@RequestBody ReplyVO vo) {
		
		int result = service.pwCheck(vo);
		
		if(result == 1) {
			service.delete(vo.getRno());
			return "delSuccess";
		} else {
			return "pwFail";
		}
		
	}
	
	
	

}















