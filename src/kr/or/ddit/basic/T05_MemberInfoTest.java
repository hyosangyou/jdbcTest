package kr.or.ddit.basic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import kr.or.ddit.util.DB_Util;
import kr.or.ddit.util.DB_Util2;
import kr.or.ddit.util.DB_Util3;

/*
   회원정보를 관리하는 프로그램을 작성하는데 
   아래의 메뉴를 모두 구현하시오. (CRUD기능 구현하기)
   (DB의 MYMEMBER테이블을 이용하여 작업한다.)
   
   * 자료 삭제는 회원ID를 입력 받아서 삭제한다.
   
   예시메뉴)
   ----------------------
      == 작업 선택 ==
      1. 자료 입력         ---> insert
      2. 자료 삭제         ---> delete
      3. 자료 수정         ---> update
      4. 전체 자료 출력   ---> select
      5. 작업 끝.
   ----------------------
    
      
// 회원관리 프로그램 테이블 생성 스크립트 
create table mymember(
    mem_id varchar2(8) not null,  -- 회원ID
    mem_name varchar2(100) not null, -- 이름
    mem_tel varchar2(50) not null, -- 전화번호
    mem_addr varchar2(128)    -- 주소
);

*/
public class T05_MemberInfoTest {
   
   private Connection conn;
   private Statement stmt;
   private PreparedStatement pstmt;
   private ResultSet rs;
   
   private Scanner scan = new Scanner(System.in); 
   
   /**
    * 메뉴를 출력하는 메서드
    */
   public void displayMenu(){
      System.out.println();
      System.out.println("----------------------");
      System.out.println("  === 작 업 선 택 ===");
      System.out.println("  1. 자료 입력");
      System.out.println("  2. 자료 삭제");
      System.out.println("  3. 자료 수정");
      System.out.println("  4. 전체 자료 출력");
      System.out.println("  5. 작업 끝.");
      System.out.println("----------------------");
      System.out.print("원하는 작업 선택 >> ");
   }
   
   /**
    * 프로그램 시작메서드
    */
   public void start(){
      int choice;
      do{
         displayMenu(); //메뉴 출력
         choice = scan.nextInt(); // 메뉴번호 입력받기
         switch(choice){
            case 1 :  // 자료 입력
               insertMember();
               break;
            case 2 :  // 자료 삭제
               deleteMember(); 
               break;
            case 3 :  // 자료 수정
            	updateMember();                   
               break;
            case 4 :  // 전체 자료 출력
            	selectAllMember();
 
               break;
            case 5 :  // 작업 끝
               System.out.println("작업을 마칩니다.");
               break;
            default :
               System.out.println("번호를 잘못 입력했습니다. 다시입력하세요");
         }
      }while(choice!=5);
   }
 /**
  * 회원 정보 수정
  */
	private void updateMember() {
		boolean chk = false;
		String memId;

		do {

			System.out.println();
			System.out.println("변경할 회원 ID를 입력하세요.");
			System.out.print("회원 ID >>");
			 memId = scan.next();
			   
			   chk = getMember(memId);
			   
			   if(!chk) {
				   System.out.println("잘못된 회원 아이디입니다.");
				   System.out.println("다시 입력하세요.");
			   }
			   		 		     		   
		   }while(chk != true);
		
		   try {
			   
			   
			   conn = DB_Util.getConnection();
			   
			   int num =0;
			   System.out.println();
			   System.out.println("1. 아이디\t\t2. 이름\t\t3. 전화번호\t\t4. 주소");
			   System.out.print("변경할 정보를 선택하시오>>");
			   num =scan.nextInt();
			   System.out.print("변경할 정보를 입력하시오>>");
			   String mem = scan.next();
			   String sql = "";
			   String column = "";
			   
			   switch(num) {
			   case 1:
				   column = "mem_id";				  
				   break;
			   case 2: 
				   column = "mem_name";				  
				   break;
			   case 3:
				   column = "mem_tel";				  
				   break;
			   case 4:
				   column = "mem_addr";
				   break;
				   
			   }
			   	      
			   sql = "update mymember set "+ column + " = " + "'"+mem+"'" +"where mem_id = "+"'"+memId+"'";
			   pstmt = conn.prepareStatement(sql);
		
			   
			   int cnt = pstmt.executeUpdate();
			   
			   if(cnt > 0) {
				   System.out.println(memId + "회원 정보 변경 성공....");
			   }else {
				   System.out.println(memId + "회원 정보 변경 실패....");
			   }
				   
		   }catch (SQLException e) {
			   e.printStackTrace();
	       }finally {
	    	   disConnect();// 자원반납
	       }
		
		

}

/**
    * 전체출력
    */
   private void selectAllMember() {
	   
	   try {
		   //conn = DB_Util.getConnection();
		   conn = DB_Util3.getConnection();
		   stmt = conn.createStatement();
		   
		   String sql = "select * from mymember" ;
				      
		   rs = stmt.executeQuery(sql);
		   System.out.println("================== 전체 회원 정보 ================");
		   System.out.println("ID\t이름\t전화번호\t\t주 소");
		   while(rs.next()) {
			   System.out.println("_____________________________________________");

			   System.out.println(rs.getString("MEM_ID")+"\t"+ rs.getString("MEM_NAME") + "\t" + rs.getString("MEM_TEL") 
			   +"\t\t"+rs.getString("MEM_ADDR"));
			  
		   }
		   System.out.println("_____________________________________________");
	
			System.out.println("출력 끝 ...");
		   
		  
	   }catch (SQLException e) {
		   e.printStackTrace();
       }finally {
    	   disConnect();// 자원반납
       }
	
	   

	
}

/**
    * 자료삭제 
    */
   private void deleteMember() {
	  
	   boolean chk = false;
	   String memId;
	   
	   do {
		   System.out.println();
		   System.out.println("삭제할 회원 ID를 입력하세요.");
		   System.out.print("회원 ID >>");
           memId = scan.next();
		   
		   chk = getMember(memId);
		   
		   if(!chk) {
			   System.out.println("잘못된 회원 아이디입니다.");
			   System.out.println("다시 입력하세요.");
		   }
		   		 		     		   
	   }while(chk != true);
	   
	   scan.nextLine(); // 버퍼 지우기
	   
	   try {
		   conn = DB_Util.getConnection();
		   
		   String sql = "delete mymember where mem_id = " + "'"+memId+"'" ;
				      
		   pstmt = conn.prepareStatement(sql);
	
		   
		   int cnt = pstmt.executeUpdate();
		   
		   if(cnt > 0) {
			   System.out.println(memId + "회원 삭제 작업 성공....");
		   }else {
			   System.out.println(memId + "회원 삭제 작업 실패....");
		   }
			   
	   }catch (SQLException e) {
		   e.printStackTrace();
       }finally {
    	   disConnect();// 자원반납
       }
	
	
}

//회원을 추가하는 메서드
   private void insertMember() {
	   
	   boolean chk = false;
	   String memId;
	   do {
		   
		   System.out.println();
		   System.out.println("추가할 회원 정보를 입력하세요.");
		   System.out.print("회원 ID >>");
		   memId = scan.next();
		   
		   chk = getMember(memId);
		   
		   if(chk) {
			   System.out.println("회원ID가 " + memId + "인 회원은 이미 존재합니다.");
			   System.out.println("다시 입력하세요.");
		   }
		   		 		     		   
	   }while(chk == true);
	   
	   System.out.print("회원 이름 >> ");
	   String memName = scan.next();
	   System.out.print("회원 전화번호 >> ");
	   String memTel = scan.next();
	   
	   scan.nextLine(); // 버퍼 지우기
	   
	   System.out.print("회원 주소 >> ");
	   String memAddr = scan.nextLine();
	   
	   try {
		   conn = DB_Util.getConnection();
		   
		   String sql = "insert into mymember(mem_id, mem_name, mem_tel, mem_addr)"
				       + " values (?, ?, ?, ?)";
		   pstmt = conn.prepareStatement(sql);
		   pstmt.setString(1, memId);
		   pstmt.setString(2, memName);
		   pstmt.setString(3, memTel);
		   pstmt.setString(4, memAddr);
		   
		   int cnt = pstmt.executeUpdate();
		   
		   if(cnt > 0) {
			   System.out.println(memId + "회원 추가 작업 성공....");
		   }else {
			   System.out.println(memId + "회원 추가 작업 실패....");
		   }
			   
	   }catch (SQLException e) {
		   e.printStackTrace();
       }finally {
    	   disConnect();// 자원반납
       }
	
	
}
   
/**
 * 회원ID를 이용하여 회원이 있는지 알려주는 메서드
 * @param memId
 * @return
 */
private boolean getMember(String memId) {
	
	boolean check = false;
	
	try {
		conn = DB_Util.getConnection();
		String sql = "Select count(*) as cnt from mymember"
				+ " where mem_id = ? ";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, memId);
		
		rs = pstmt.executeQuery();
		
		int cnt = 0;
		if(rs.next()) {
			cnt = rs.getInt("cnt");
		}if(cnt > 0) {
			check = true;
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}finally {
		disConnect();// 자원 반납
	}
	
	return check;
}

/**
 * 사용한 자원 반납
 */
private void disConnect() {
	if(rs != null) try {rs.close();}catch(SQLException e) {}
	if(stmt != null) try {stmt.close();}catch(SQLException e) {}
	if(pstmt != null) try {pstmt.close();}catch(SQLException e) {}
	if(conn != null) try {conn.close();}catch(SQLException e) {}

}

public static void main(String[] args) {
      T05_MemberInfoTest memObj = new T05_MemberInfoTest();
      memObj.start();
   }

}





