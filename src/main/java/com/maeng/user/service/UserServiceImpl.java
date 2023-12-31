package com.maeng.user.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.maeng.board.model.BoardDAO;
import com.maeng.board.model.BoardVO;
import com.maeng.user.model.UserDAO;
import com.maeng.user.model.UserVO;

public class UserServiceImpl implements UserService {

	
	@Override
	public UserVO login(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");

		UserDAO dao = UserDAO.getInstance();
		UserVO vo =dao.login(id,pw);


		return vo;
	}

	@Override
	public int checkId(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		UserDAO dao = UserDAO.getInstance();
		int result = dao.checkId(id);
		return result;
	}
	
	
	public int join(HttpServletRequest request, HttpServletResponse response) {

		String id = request.getParameter("id");
		String nick = request.getParameter("name");
		String pw = request.getParameter("pw");
		String photo = request.getParameter("photo");
		String intro = request.getParameter("intro");

		
		UserDAO dao =UserDAO.getInstance();
		int result = dao.checkId(id);


		if(result == 1) { // 중복
			return 1;
		}else { // 가입처리
			
			UserVO vo = new UserVO(id,nick,pw,photo,intro);
			dao.join(vo);

			return 0;			
		}

	}

	@Override
	public int delete(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		UserVO vo = (UserVO)session.getAttribute("vo");
		String id = vo.getId();
		UserDAO dao = UserDAO.getInstance();

		int result = dao.delete(id);

	
		return result;
	}
	
	public UserVO getInfo(HttpServletRequest request, HttpServletResponse response) {

		//아이디가 꼭 필요
		//이전 화면에서 필요한 값을 넘겨주던 vs 회원아이디는 세션에 존재

		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("user_id");

		UserDAO dao = UserDAO.getInstance();

		UserVO vo = dao.getInfo(id);

		return vo;
	}
	
	public UserVO getInfo2(HttpServletRequest request, HttpServletResponse response) {

		

		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("search_id");

		UserDAO dao = UserDAO.getInstance();

		UserVO vo = dao.getInfo(id);

		return vo;
	}

	@Override
	public int updateInfo(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		UserVO vo = (UserVO)session.getAttribute("vo");
		String id = request.getParameter("id");
		String name = request.getParameter("nick");
		String pw = request.getParameter("pw");
		String intro = request.getParameter("intro");
		String photo = request.getParameter("photo");

		
		if(photo.equals("")) {
			photo = vo.getPhoto();
		};

		UserVO vo2 = new UserVO(id,name,pw,photo,intro);
		UserDAO dao = UserDAO.getInstance();

		int a = dao.updateInfo(vo2);


		return a;
	}
	
	@Override
    public List<BoardVO> getlist(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("user_id");
        BoardDAO dao = BoardDAO.getInstance();
        List<BoardVO> list = dao.getList(id);
        return list;
    }
	
	 public List<BoardVO> getlist2(HttpServletRequest request, HttpServletResponse response) {
	        
	        String id = request.getParameter("search_id");
	        BoardDAO dao = BoardDAO.getInstance();
	        List<BoardVO> list = dao.getList(id);
	        return list;
	    }

}
