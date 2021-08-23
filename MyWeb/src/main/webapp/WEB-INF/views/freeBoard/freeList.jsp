<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

    <%@ include file="../include/header.jsp" %>
    
    
    <section>
        <div class="container-fluid">
            <div class="row">
                <!--lg에서 9그리드, xs에서 전체그리드-->   
                <div class="col-lg-9 col-xs-12 board-table">
                    <div class="titlebox">
                        <p>자유게시판</p>
                    </div>
                    <hr>
                    
                    <!--form select를 가져온다 -->
            	<form action="<c:url value='/freeBoard/freeList' />">
		    		<div class="search-wrap">
                       <button type="submit" class="btn btn-info search-btn">검색</button>
                       <input type="text" class="form-control search-input" name="keyword" value="${pc.paging.keyword}">
                       <select class="form-control search-select" name="condition">
                            <option value="title" ${pc.paging.condition == 'title' ? 'selected' : ''}>제목</option>
                            <option value="content" ${pc.paging.condition == 'content' ? 'selected' : ''}>내용</option>
                            <option value="writer" ${pc.paging.condition == 'writer' ? 'selected' : ''}>작성자</option>
                            <option value="titleContent" ${pc.paging.condition == 'titleContent' ? 'selected' : ''}>제목+내용</option>
                       </select>
                    </div>
		    	</form>
                   
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>번호</th>
                                <th class="board-title">제목</th>
                                <th>작성자</th>
                                <th>등록일</th>
                                <th>수정일</th>
                            </tr>
                        </thead>
                        <tbody>
                        	<c:forEach var="vo" items="${boardList}">
	                            <tr>
	                                <td>${vo.bno}</td>
	                                <td>
	                                	<a href="<c:url value='/freeBoard/freeDetail?bno=${vo.bno}&pageNum=${pc.paging.pageNum}&keyword=${pc.paging.keyword}&condition=${pc.paging.condition}' />">${vo.title}</a>
	                                	&nbsp;&nbsp;&nbsp;
	                                	<c:if test="${vo.newMark}">
	                                		<img alt="newMark" src="<c:url value='/img/icon_new.gif' />">
	                                	</c:if>
	                                </td>
	                                <td>${vo.writer}</td>
	                                <td><fmt:formatDate value="${vo.regdate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	                                <td><fmt:formatDate value="${vo.updatedate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	                            </tr>
                            </c:forEach>
                        </tbody>
                        
                    </table>


                    <!--페이지 네이션을 가져옴-->
		    <form action="<c:url value='/freeBoard/freeList' />" name="pageForm">
                    <div class="text-center">
                    <hr>
                    
                    <!-- 페이지 관련 버튼들이 ul 태그로 감싸져 있다. -->
                    <ul class="pagination pagination-sm" id="pagination">
                       <c:if test="${pc.prev}">
                       		<li><a href="#" data-pageNum="${pc.beginPage-1}">이전</a></li>
                       </c:if>
                       
                       <c:forEach var="num" begin="${pc.beginPage}" end="${pc.endPage}">
                        <li class="${pc.paging.pageNum == num ? 'active' : ''}"><a href="#" data-pageNum="${num}">${num}</a></li>
                       </c:forEach>
                       
                        <c:if test="${pc.next}">
                        	<li><a href="#" data-pageNum="${pc.endPage+1}">다음</a></li>
                        </c:if>                      
                    </ul>
                    
                    <!-- 페이지 관련 버튼을 클릭시 숨겨서 보낼 값 -->
                    <input type="hidden" name="pageNum" value="${pc.paging.pageNum}">
                    <input type="hidden" name="countPerPage" value="${pc.paging.countPerPage}">
                    <input type="hidden" name="keyword" value="${pc.paging.keyword}">
                    <input type="hidden" name="condition" value="${pc.paging.condition}">
                    
                    
                    <button type="button" class="btn btn-info" onclick="location.href='<c:url value="/freeBoard/freeRegist" />'">글쓰기</button>
                    </div>
		    </form>

                </div>
            </div>
        </div>
	</section>
	
	<%@ include file="../include/footer.jsp" %>
	
	<script>
		const msg = '${msg}';
		if(msg !== '') {
			alert(msg);
		}
		
		//사용자가 페이지 관련 버튼을 클릭했을 때 어떤 버튼이 클릭되었는지를 확인하여
		//그 버튼에 맞는 페이지 정보를 -> hidden타입으로 설정한 input page value를
		//수정해 줄 예정입니다.
		const pagination = document.getElementById('pagination');
		pagination.onclick = function(e) {
			e.preventDefault(); //버튼의 고유이벤트 속성 중지.
			
			//현재 이벤트가 발생한 요소(페이지 관련 버튼 클릭의 주체)의
			//data-pageNum의 값을 얻어서 변수에 저장하는 행위.
			const value = e.target.dataset.pagenum;
			
			//페이지 버튼들을 감싸고 있는 form태그를 name으로 지목하여
			//그 안에 있는 pageNum이라는 input태그의 value에
			//위에서 얻은 data-pageNum의 값을 삽입.
			document.pageForm.pageNum.value = value;
			document.pageForm.submit();
		}
		
	</script>
	
	
	
	
	
	
	
	
	
	
	
	
	
	



