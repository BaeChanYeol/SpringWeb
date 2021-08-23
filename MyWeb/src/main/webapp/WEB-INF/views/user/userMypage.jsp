<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

	<%@ include file="../include/header.jsp" %>

	<section>
        <!--Toggleable / Dynamic Tabs긁어옴-->
        <div class="container">
            <div class="row">
                <div class="col-sm-12 col-md-10 col-lg-9 myInfo">
                    <div class="titlebox">
                        MEMBER INFO                    
                    </div>
                    
                    <ul class="nav nav-tabs tabs-style">
                        <li class="active"><a data-toggle="tab" href="#info">내정보</a></li>
                        <li><a data-toggle="tab" href="#myBoard">내글</a></li>
                        <li><a data-toggle="tab" href="#menu2">Menu 2</a></li>
                    </ul>
                    <div class="tab-content">
                        <div id="info" class="tab-pane fade in active">
 
                            <p>*표시는 필수 입력 표시입니다</p>
                            <form action="<c:url value='/user/userUpdate'/>" method="post" id="updateForm">
                            <table class="table">
                                <tbody class="m-control">
                                    <tr>
                                        <td class="m-title">*ID</td>
                                        <td><input class="form-control input-sm" name="userId" value="${userInfo.userId}" readonly></td>
                                    </tr>
                                    <tr>
                                        <td class="m-title">*이름</td>
                                        <td><input class="form-control input-sm" name="userName" value="${userInfo.userName}" readonly></td>
                                    </tr>
                                    <tr>
                                        <td class="m-title">*비밀번호</td>
                                        <td><input type="password" class="form-control input-sm" name="userPw" id="userPw" ></td>
                                    </tr>
                                    <tr>
                                        <td class="m-title">*비밀번호확인</td>
                                        <td><input type="password" class="form-control input-sm" id ="pwCheck"></td>
                                    </tr>
                                    <tr>
                                        <td class="m-title">*E-mail</td>
                                        <td>
                                            <input class="form-control input-sm" name="userEmail1" id ="userEmail1" value="${userInfo.userEmail1}">@
                                            <select class="form-control input-sm sel" name="userEmail2" id ="userEmail2">
                                                <option value="naver.com" ${userInfo.userEmail2 == '@naver.com' ? 'selected' : ''}>naver.com</option>
                                                <option value="gmail.com" ${userInfo.userEmail2 == '@gmail.com' ? 'selected' : ''}>gmail.com</option>
                                                <option value="daum.net" ${userInfo.userEmail2 == '@daum.net' ? 'selected' : ''}>daum.net</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="m-title">*휴대폰</td>
                                        <td>
                                            <select class="form-control input-sm sel" name="userPhone1" id="userPhone1">
                                                <option ${userInfo.userPhone1 == '010' ? 'selected' : ''}>010</option>
                                                <option ${userInfo.userPhone1 == '011' ? 'selected' : ''}>011</option>
                                                <option ${userInfo.userPhone1 == '017' ? 'selected' : ''}>017</option>
                                                <option ${userInfo.userPhone1 == '018' ? 'selected' : ''}>018</option>
                                            </select>
                                            <input class="form-control input-sm" name="userPhone2" id="userPhone1" value="${userInfo.userPhone2}">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="m-title">*우편번호</td>
                                        <td><input class="form-control input-sm" name="addrZipNum" id="addrZipNum"value="${userInfo.addrZipNum}" readonly>
                                        	<button type="button" class="btn btn-primary" id="addBtn" onclick="searchAddress()">주소찾기</button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="m-title">*주소</td>
                                        <td><input class="form-control input-sm add" name="addrBasic" id="addrBasic" value="${userInfo.addrBasic}"></td>
                                    </tr>
                                    <tr>
                                        <td class="m-title">*상세주소</td>
                                        <td><input class="form-control input-sm add" name="addrDetail" id="addrDetail" value="${userInfo.addrDetail}"></td>
                                    </tr>
                                </tbody>
                            </table>
                            </form>

                            <div class="titlefoot">
                                <button class="btn" id="updateBtn">수정</button>
                                <button class="btn">목록</button>
                            </div>
                        </div>
                        <!-- 첫번째 토글 끝 (내 정보, 내가 쓴 글들을 탭으로 관리) -->
                        <div id="myBoard" class="tab-pane fade">
                            <p>*내 게시글 관리</p>
                            <form>
                            <table class="table">
                                <thead>
                                    <tr>
                                        <td>번호</td>
                                        <td>제목</td>
                                        <td>작성일</td>
                                    </tr>
                                </thead>
                                <tbody>
                                	<c:forEach var="vo" items="${userInfo.userBoardList}">
	                                    <tr>
	                                        <td>${vo.bno}</td>
	                                        <td>
	                                        	<a href="<c:url value='/freeBoard/freeDetail?bno=${vo.bno}' />">${vo.title}</a>
	                                        </td>
	                                        <td>${vo.regdate}</td>
	                                    </tr>      
                                    </c:forEach>   
                                </tbody>
                            </table>
                            </form>
                        </div>
                        <!-- 두번째 토글 끝 -->
                        <div id="menu2" class="tab-pane fade">
                            <h3>Menu 2</h3>
                            <p>Some content in menu 2.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <%@ include file="../include/footer.jsp" %>
    
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    
    <script>
    
 
    function searchAddress() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('addrZipNum').value = data.zonecode;
                document.getElementById("addrBasic").value = addr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("addrDetail").focus();                             
            }
        }).open();
    }
    
    $(function() {
        
    	var pw = document.getElementById("userPw");
        pw.onkeyup = function(){
            var regex = /^[A-Za-z0-9+]{8,16}$/;
             if(regex.test(document.getElementById("userPw").value )) {
                document.getElementById("userPw").style.borderColor = "green";
            } else {
                document.getElementById("userPw").style.borderColor = "red";
            }
        }
    	
    	
    	$('#updateBtn').click(function() {
			/* console.log($('.form-control.input-sm')); */
     		if($('#userPw').val() === '' || $('#pwCheck').val() === '' || $('#uesrEmail1').val() === '' || $('#uesrEmail2').val() === '' || $('#addrZipNum').val() === '' || $('#addrBasic').val() === '' || $('#addrDetail').val() === ''){
 				alert('*표시는 필수 항목입니다.');
				return;
			}else if($('#userPw').val() !== $('#pwCheck').val()){
				alert('비밀번호가 서로 다릅니다.');
				return;
			}else{
			 	if(confirm('수정하시겠습니까?')){
					$('#updateForm').submit(); 		 		
			 	}else{
			 		return;
			 	}
			}			
		
		});
		
    	const msg = "${msg}";
    	if(msg === 'updateSuccess'){
    		alert("회원정보가 수정되었습니다.");
    	}
    
    });
    
    
    
    
    </script>
    
    
    
    
    
