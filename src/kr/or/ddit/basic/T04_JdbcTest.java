package kr.or.ddit.basic;
/*
    LPROD 테이블에 새로운 데이터 추가하기
    
    lprod_gu와 lprod_nm은 직접 입력받아 처리하고,
    lprod_id는 현재의 lprod_id들 중 제일 큰값보다 1증가된 값으로 한다.
    (기타사항 : lprod_gu도 중복 되는지 검사한다.);
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class T04_JdbcTest {

	public static void main(String[] args) {

		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Scanner s = new Scanner(System.in);

		try {
			// 1. 드라이버 로딩
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2. DB에 접속 (Connection객체 생성)
			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			String userId = "you";
			String password = "java";
			String sql;
			String input_gu;
			int input_id = 0;

			conn = DriverManager.getConnection(url, userId, password);
			stmt = conn.createStatement();

			while (true) {
				System.out.print("lprod_gu 값을 입력하시오>");
				input_gu = s.nextLine();

				sql = "select lprod_gu " + "from lprod ";

				rs = stmt.executeQuery(sql);

				while (rs.next()) {
					if (rs.getString("lprod_gu").equals(input_gu)) {
						System.out.println("중복된 값이 있습니다.");
					} else {
						break;
					}
				}
 
				break;
			}

			System.out.print("lprod_nm 값을 입력하시오>");
			String input_nm = s.nextLine();
			

			
			sql = "select Max(lprod_id) as lprod_id from lprod";
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				input_id = rs.getInt("lprod_id");
			}
			input_id++;
		
	

			sql = "insert into lprod" + "(lprod_id, lprod_gu, lprod_nm)" + "values(?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, input_id);
			pstmt.setString(2, input_gu);
			pstmt.setString(3, input_nm);
			
			int cnt =pstmt.executeUpdate();
			if(cnt>0) {
				System.out.println("추가완료");
			}else {
				System.out.println("추가실패");
			}

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패 !!");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
	}

}
