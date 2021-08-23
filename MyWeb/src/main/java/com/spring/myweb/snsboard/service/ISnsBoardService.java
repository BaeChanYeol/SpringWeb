package com.spring.myweb.snsboard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.myweb.command.SnsBoardVO;

@Service
public interface ISnsBoardService {

	//등록
	void insert(SnsBoardVO vo);
	//목록
	List<SnsBoardVO> getList();
	//상세
	SnsBoardVO getDetail(int bno);
	//삭제 
	void delete(int bno);
	
	
}
