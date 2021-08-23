package com.spring.myweb.util.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.spring.myweb.command.UserVO;

public class BoardAuthHandler extends HandlerInterceptorAdapter {

	// 화면에서 변경, 수정, 삭제가 일어날 때, writer값을 넘겨주도록 처리
	// 게시글 수정, 삭제에 대한 권한 처리 핸들러
	// 세션값과 writer(작성자) 정보가 같다면 컨트롤러를 실행.
	// 그렇지 않다면 '권한이 없습니다.' 출력
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		UserVO vo = (UserVO) session.getAttribute("login");

		if (vo != null) {

			if (request.getParameter("writer").equals(vo.getUserId())) {
				return true;
			}
		
		}
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pw = response.getWriter();
		String html = "<script>alert('권한이 없습니다.'); history.back();</script>";
		pw.print(html);
		pw.flush();
		return false;

	}

}
