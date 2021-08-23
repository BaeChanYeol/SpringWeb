package com.spring.myweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.myweb.command.FreeBoardVO;
import com.spring.myweb.freeboard.service.IFreeBoardService;
import com.spring.myweb.util.PageCreator;
import com.spring.myweb.util.PageVO;

@Controller
@RequestMapping("/freeBoard")
public class FreeBoardController {
	
	@Autowired
	private IFreeBoardService service;
	
	//목록 화면
	@GetMapping("/freeList")
	public String freeList(PageVO vo, Model model) {
		
		PageCreator pc = new PageCreator();
		pc.setPaging(vo);
		pc.setArticleTotalCount(service.getTotal(vo));
		
		model.addAttribute("boardList", service.getList(vo));
		model.addAttribute("pc", pc);
			
		return "freeBoard/freeList";
	}
	
	//글쓰기 화면 처리
	@GetMapping("/freeRegist")
	public void freeRegist() {}
	
	//글 등록 처리
	@PostMapping("/registForm")
	public String registForm(FreeBoardVO vo, RedirectAttributes ra) {
		service.regist(vo);
		
		//등록 성공 여부를 1회용으로 전달하기 위한 ra객체의 메서드
		ra.addFlashAttribute("msg", "정상 등록 처리되었습니다.");
		
		return "redirect:/freeBoard/freeList"; //등록 후에 글 목록 요청으로 리다이렉트
	}
	
	//글 상세보기 처리
	@GetMapping("/freeDetail")
	public void getContent(@RequestParam int bno, Model model, 
						   @ModelAttribute("p") PageVO vo) {
		model.addAttribute("article", service.getContent(bno));
	}
	
	//글 수정 페이지 이동
	@GetMapping("/freeModify")
	public void modify(@RequestParam int bno, Model model) {
		model.addAttribute("article", service.getContent(bno));
	}
	
	//글 수정 처리
	@PostMapping("/freeUpdate")
	public String freeUpdate(FreeBoardVO vo, RedirectAttributes ra) {
		service.update(vo);
		ra.addFlashAttribute("msg", "updateSuccess");
		
		return "redirect:/freeBoard/freeDetail?bno=" + vo.getBno();
	}
	
	@PostMapping("/freeDelete")
	public String freeDelete(FreeBoardVO vo, RedirectAttributes ra) {
		service.delete(vo.getBno());
		
		ra.addFlashAttribute("msg", "게시글이 정상 삭제되었습니다.");
		
		return "redirect:/freeBoard/freeList";
	}
	
	

}
















