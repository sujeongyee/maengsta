package com.maeng.main.model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;



public class MainDAO {

	private static MainDAO instance = new MainDAO();
	private MainDAO(){		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {

		}
	}
	public static MainDAO getInstance() {
		return instance;
	}
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String uid = "PRO";
	private String upw = "PRO";

	//--------------------------------------------------------

	public List<MainVO> getList(String id) {

		List<MainVO> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT U.USER_PHOTO, F.FOL_ID, P.POST_IMG, P.POST_LIKE, P.POST_TIME,P.POST_CONTENT,P.POST_NUM FROM FOLLOWING F JOIN POST P ON F.FOL_ID = P.USER_ID\r\n"
				+ "JOIN USERS U ON U.USER_ID = F.FOL_ID\r\n"
				+ "WHERE F.USER_ID = ?\r\n"
				+ "ORDER BY P.POST_TIME DESC";

		try {

			conn = DriverManager.getConnection(url,uid,upw);
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			while(rs.next()) {

				String user_photo = rs.getString("user_photo");
				String fol_id = rs.getString("fol_id");
				String post_img = rs.getString("post_img");
				String post_content = rs.getString("post_content");
				int post_like = rs.getInt("post_like"); 
				Timestamp post_time = rs.getTimestamp("post_time");
				String number = rs.getString("post_num");
				MainVO vo = new MainVO( user_photo, fol_id,post_img,post_content, post_like, post_time,number);
				list.add(vo);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("오류");
		} finally {
			try {
				conn.close();
				pstmt.close();
				rs.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return list;
	}


	// 좋아요 수 업데이트 시 / like table에 이름추가 
	public void likeUpdate(String number, String id) {
		String sql = "INSERT INTO LIKES VALUES (?, ?)";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection(url, uid, upw);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, number);
			pstmt.setString(2, id);


			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("likeUadate오류");
		} finally {
			try {
				conn.close();
				pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	// like likeUpate 실행시 post테이블의 좋아요수 +1 하는 메서드

	public void like(String number) {
		String sql = "update post set post_like = post_like + 1 where post_num = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;


		try {
			conn = DriverManager.getConnection(url, uid, upw);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, number);

			pstmt.executeUpdate();


		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("like오류");
		} finally {
			try {
				conn.close();
				pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	//좋아요 중복
	public int checkLike(String id, String number) {

		String sql = "select * from likes where user_id = ?  and post_num =  ? ";
		
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		
		try {
			conn = DriverManager.getConnection(url,uid,upw);
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, number);
			rs = pstmt.executeQuery();
			
			if(rs.next()) { // 중복
				result = 1;
				
			}else {
				result = 0;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("lkie dao 도중 오류 발생");
		} finally {
			
			try {
				conn.close();
				pstmt.close();
				rs.close();
			} catch (Exception e2) {
				
			}
			
		}
		
		return result;
	}

	public void likeDelId(String number, String id) {
		String sql = "delete from likes where post_num= ? and user_id = ? ";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DriverManager.getConnection(url, uid, upw);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, number);
			pstmt.setString(2, id);


			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("likeDelId오류");
		} finally {
			try {
				conn.close();
				pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	
	
	
	//좋아요 -1 메서드
	
		public void likeDel(String number) {
			String sql = "update post set post_like = post_like -1 where post_num =?";
			Connection conn = null;
			PreparedStatement pstmt = null;


			try {
				conn = DriverManager.getConnection(url, uid, upw);
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, number);

				pstmt.executeUpdate();


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("likeDel오류");
			} finally {
				try {
					conn.close();
					pstmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
	}




}