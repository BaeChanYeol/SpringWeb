package com.spring.myweb.freeboard;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spring.myweb.command.FreeBoardVO;
import com.spring.myweb.freeboard.mapper.IFreeBoardMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/config/db-config.xml")
public class FreeBoardMapperTest {
	
	@Autowired
	private IFreeBoardMapper mapper;
	
	@Test
	public void registTest() {
		for(int i=1; i<=320; i++) {
			FreeBoardVO vo = new FreeBoardVO();
			vo.setTitle("테스트 글쓰기 " + i);
			vo.setWriter("김테스트 " + i);
			vo.setContent("테스트 글쓰기 내용입니다. " + i);
			mapper.regist(vo);
		}
	}
	
	@Test
	public void getListTest() {
//		mapper.getList().forEach(vo -> System.out.println(vo));
	}
	
	@Test
	public void updateTest() {
		FreeBoardVO vo = new FreeBoardVO();
		vo.setTitle("수정한 제목");
		vo.setContent("수정한 내용입니다.");
		vo.setBno(1);
		mapper.update(vo);
	}
	
	

}










