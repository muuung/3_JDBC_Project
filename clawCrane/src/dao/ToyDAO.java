package dao;

import static common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import vo.Toy;

public class ToyDAO {
	
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private Properties prop;
	
	public ToyDAO() {
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("toy-query.xml"));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 현재 포인트 조회
	 * @param conn
	 * @return confirmPoint
	 * @throws Exception
	 */
	public int confirmPoint(Connection conn) throws Exception {
		int confirmPoint = 0;
		
		try {
			String sql = prop.getProperty("selectPoint");
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				confirmPoint = rs.getInt(1);
			}
			
		} finally {
			close(rs);
			close(stmt);
		}
		
		return confirmPoint;
	}

	/**
	 * 인형뽑기 : 포인트 사용
	 * @param conn
	 * @return usePoint
	 * @throws Exception
	 */
	public int usePoint(Connection conn) throws Exception {
		int usePoint = 0;
		
		try {
			String sql = prop.getProperty("usePoint");
			
			stmt = conn.createStatement();
			
			usePoint = stmt.executeUpdate(sql);
			
		} finally {
			close(stmt);
		}
		
		return usePoint;
	}

	/**
	 * 인형뽑기 : 총 인형 개수
	 * @param conn
	 * @return toyNumber
	 * @throws Exception
	 */
	public int toyNumber(Connection conn) throws Exception {
		int toyNumber = 0;
		
		try {
			String sql = prop.getProperty("toyNumber");
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				toyNumber = rs.getInt(1);
			}
			
		} finally {
			close(rs);
			close(stmt);
		}
		
		return toyNumber;
	}

	/**
	 * 인형뽑기 : 인형 수량 확인
	 * @param conn
	 * @param ran
	 * @return selectToyStock
	 * @throws Exception
	 */
	public int selectToyStock(Connection conn, int ran) throws Exception {
		int selectToyStock = 0;
		
		try {
			String sql = prop.getProperty("selectToyStock");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, ran);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				selectToyStock = rs.getInt("TOY_STOCK");
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return selectToyStock;
	}	

	/**
	 * 인형뽑기 : 뽑은 인형 이름 조회
	 * @param conn
	 * @param ran
	 * @return catchToy
	 * @throws Exception
	 */
	public String catchToy(Connection conn, int ran) throws Exception {
		String catchToy = "";
		
		try {
			String sql = prop.getProperty("catchToy");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, ran);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				catchToy = rs.getString("TOY_NAME");
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return catchToy;
	}

	/**
	 * 인형뽑기 : 뽑은 인형 중복 확인
	 * @param conn
	 * @param toyName
	 * @return catchExists
	 * @throws Exception
	 */
	public int catchExists(Connection conn, String toyName) throws Exception {
		int catchExists = 0;
		
		try {
			String sql = prop.getProperty("catchExists");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, toyName);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				catchExists = rs.getInt(1);
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return catchExists;
	}

	/**
	 * 인형뽑기 : 처음 뽑은 인형
	 * @param conn
	 * @param toyName
	 * @return catchInsert
	 * @throws Exception
	 */
	public int catchInsert(Connection conn, String toyName) throws Exception {
		int catchInsert = 0;
		
		try {
			String sql = prop.getProperty("catchInsert");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, toyName);
			
			catchInsert = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return catchInsert;
	}

	/**
	 * 인형뽑기 : 뽑은 인형 수량 확인
	 * @param conn
	 * @param toyName
	 * @return catchToyStock
	 * @throws Exception
	 */
	public int catchToyStock(Connection conn, String toyName) throws Exception {
		int catchToyStock = 0;
		
		try {
			String sql = prop.getProperty("catchToyStock");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, toyName);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				catchToyStock = rs.getInt("TOY_STOCK");
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return catchToyStock;
	}	
	
	/**
	 * 인형뽑기 : 중복으로 뽑은 인형
	 * @param conn
	 * @param toyName
	 * @return catchUpdate
	 * @throws Exception
	 */
	public int catchUpdate(Connection conn, String toyName) throws Exception {
		int catchUpdate = 0;
		
		try {
			String sql = prop.getProperty("catchUpdate");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, toyName);
			
			catchUpdate = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
			
		}
		return catchUpdate;
	}

	/**
	 * 전체 인형 목록 보기
	 * @param conn
	 * @return toyList
	 * @throws Exception
	 */
	public List<Toy> selectAllToy(Connection conn) throws Exception {
		Toy toy = null;
		List<Toy> toyList = new ArrayList();
		
		try {
			String sql = prop.getProperty("selectAllToy");
			
			listAdd(conn, sql, toy, toyList);
			
		} finally {
			close(rs);
			close(stmt);
		}
		
		return toyList;
	}
	
	/**
	 * 뽑은 인형 목록 보기
	 * @param conn
	 * @return toyList
	 * @throws Exception
	 */
	public List<Toy> selectToy(Connection conn) throws Exception {
		Toy toy = null;
		List<Toy> toyList = new ArrayList();
		
		try {
			String sql = prop.getProperty("selectToy");
			
			listAdd(conn, sql, toy, toyList);
			
		} finally {
			close(rs);
			close(stmt);
		}
		
		return toyList;
	}

	/**
	 * 인형 목록 추가
	 * @param conn
	 * @param sql
	 * @param toy
	 * @param toyList
	 * @return toyList
	 * @throws Exception
	 */
	public List<Toy> listAdd(Connection conn, String sql, Toy toy, List<Toy> toyList) throws Exception {
		stmt = conn.createStatement();
		
		rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			toy = new Toy();
			toy.setToyName(rs.getString("TOY_NAME"));
			toy.setToyStock(rs.getInt("TOY_STOCK"));
			toyList.add(toy);
		}
		
		return toyList;
	}
	
	/**
	 * 기계 안에 인형 넣기 : 넣을 인형 중복 확인
	 * @param conn
	 * @param toyName
	 * @return insertExists
	 * @throws Exception
	 */
	public int insertExists(Connection conn, String toyName) throws Exception {
		int insertExists = 0;
		
		try {
			String sql = prop.getProperty("insertExists");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, toyName);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				insertExists = rs.getInt(1);
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}

		return insertExists;
	}

	/**
	 * 기계 안에 인형 넣기 : 새 인형 넣기
	 * @param conn
	 * @param toyName
	 * @param toyStock
	 * @return insertToy
	 * @throws Exception
	 */
	public int insertToy(Connection conn, String toyName, int toyStock) throws Exception {
		int insertToy = 0;
		
		try {
			String sql = prop.getProperty("insertToy");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, toyName);
			
			pstmt.setInt(2, toyStock);
			
			insertToy = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return insertToy;
	}	

	/**
	 * 기계 안에 인형 넣기 : 넣을 수 있는 인형 수량
	 * @param conn
	 * @param toyName
	 * @return validToyStock
	 * @throws Exception
	 */
	public int validToyStock(Connection conn, String toyName) throws Exception {
		int validToyStock = 0;
		
		try {
			String sql = prop.getProperty("validToyStock");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, toyName);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				validToyStock = rs.getInt("TOY_STOCK");
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return validToyStock;
	}

	/**
	 * 기계 안에 인형 넣기 : 기존 인형 수량 추가
	 * @param conn
	 * @param toyName
	 * @param toyStock
	 * @return updateToyStock
	 * @throws Exception
	 */
	public int updateToyStock(Connection conn, String toyName, int toyStock) throws Exception {
		int updateToyStock = 0;
		
		try {
			String sql = prop.getProperty("updateToyStock");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, toyStock);
			pstmt.setString(2, toyName);
			
			updateToyStock = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return updateToyStock;
	}
	
	/**
	 * 현재 지갑 사정 보기
	 * @param conn
	 * @return selectPoint
	 * @throws Exception
	 */
	public int selectPoint(Connection conn) throws Exception {
		int selectPoint = 0;
		
		try {
			String sql = prop.getProperty("selectPoint");
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				selectPoint = rs.getInt("POINT");
			}
			
		} finally {
			close(rs);
			close(stmt);
		}
		
		return selectPoint;
	}	
	
	/**
	 * 용돈벌기
	 * @param conn
	 * @return savePoint
	 * @throws Exception
	 */
	public int savePoint(Connection conn, int point) throws Exception {
		int savePoint = 0;
		
		try {
			String sql = prop.getProperty("savePoint");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, point);
			
			savePoint = pstmt.executeUpdate();	
			
		} finally {
			close(pstmt);
		}
		
		return savePoint;
	}
}