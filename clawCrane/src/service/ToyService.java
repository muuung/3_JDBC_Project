package service;

import static common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;

import dao.ToyDAO;
import vo.Toy;

public class ToyService {
	
	private ToyDAO dao = new ToyDAO();
	
	/**
	 * 현재 포인트 조회
	 * @return confirmPoint
	 * @throws Exception
	 */
	public int confirmPoint() throws Exception {
		Connection conn = getConnection();

		int confirmPoint = dao.confirmPoint(conn);
		
		close(conn);

		return confirmPoint;
	}

	/**
	 * 인형뽑기 : 포인트 사용
	 * @return usePoint
	 * @throws Exception
	 */
	public int usePoint() throws Exception {
		Connection conn = getConnection();
		
		int usePoint = dao.usePoint(conn);
		
		if(usePoint > 0) commit(conn);
		else           rollback(conn);
		
		close(conn);
		
		return usePoint;
	}

	/**
	 * 인형뽑기 : 총 인형 개수
	 * @return toyNumber
	 * @throws Exception
	 */
	public int toyNumber() throws Exception {
		Connection conn = getConnection();
		
		int toyNumber = dao.toyNumber(conn);
		
		close(conn);
		
		return toyNumber;
	}

	/**
	 * 인형뽑기 : 인형 수량 확인
	 * @param ran
	 * @return selectToyStock
	 * @throws Exception
	 */
	public int selectToyStock(int ran) throws Exception {
		Connection conn = getConnection();
		
		int selectToyStock = dao.selectToyStock(conn, ran);
		
		close(conn);
		
		return selectToyStock;
	}
	
	/**
	 * 인형뽑기 : 뽑은 인형 이름 조회
	 * @param ran
	 * @return catchToy
	 * @throws Exception
	 */
	public String catchToy(int ran) throws Exception {
		Connection conn = getConnection();
		
		String catchToy = dao.catchToy(conn, ran);
		
		close(conn);
		
		return catchToy;
	}

	/**
	 * 인형뽑기 : 뽑은 인형 중복 확인
	 * @param toyName
	 * @return catchExists
	 * @throws Exception
	 */
	public int catchExists(String toyName) throws Exception {
		Connection conn = getConnection();
		
		int catchExists = dao.catchExists(conn, toyName);
		
		close(conn);
		
		return catchExists;
	}

	/**
	 * 인형뽑기 : 처음 뽑은 인형
	 * @param toyName
	 * @throws Exception
	 */
	public int catchInsert(String toyName) throws Exception {
		Connection conn = getConnection();
		
		int catchInsert = dao.catchInsert(conn, toyName);
		
		if(catchInsert > 0) commit(conn);
		else                rollback(conn);
		
		close(conn);
		
		return catchInsert;
	}

	/**
	 * 인형뽑기 : 뽑은 인형 수량 확인
	 * @param toyName
	 * @return catchToyStock
	 * @throws Exception
	 */
	public int catchToyStock(String toyName) throws Exception {
		Connection conn = getConnection();
		
		int catchToyStock = dao.catchToyStock(conn, toyName);
		
		close(conn);
		
		return catchToyStock;
	}
	
	/**
	 * 인형뽑기 : 중복으로 뽑은 인형
	 * @param toyName
	 * @throws Exception
	 */
	public int catchUpdate(String toyName) throws Exception {
		Connection conn = getConnection();
		
		int catchUpdate = dao.catchUpdate(conn, toyName);
		
		if(catchUpdate > 0) commit(conn);
		else                rollback(conn);
		
		close(conn);
		
		return catchUpdate;
	}

	/**
	 * 전체 인형 목록 보기
	 * @return toyList
	 * @throws Exception
	 */
	public List<Toy> selectAllToy() throws Exception {
		Connection conn = getConnection();
		
		List<Toy> toyList = dao.selectAllToy(conn);
		
		close(conn);
		
		return toyList;
	}
	
	/**
	 * 뽑은 인형 목록 보기
	 * @return toyList
	 * @throws Exception
	 */
	public List<Toy> selectToy() throws Exception {
		Connection conn = getConnection();
		
		List<Toy> toyList = dao.selectToy(conn);
		
		close(conn);
		
		return toyList;
	}

	/**
	 * 기계 안에 인형 넣기 : 넣을 인형 중복 확인
	 * @param toyName
	 * @return insertExists
	 * @throws Exception
	 */
	public int insertExists(String toyName) throws Exception {
		Connection conn = getConnection();

		int insertExists = dao.insertExists(conn, toyName);
		
		close(conn);

		return insertExists;
	}
		
	/**
	 * 기계 안에 인형 넣기 : 새 인형 넣기
	 * @param toyName
	 * @param toyStock
	 * @return insertToy
	 * @throws Exception
	 */
	public int insertToy(String toyName, int toyStock) throws Exception {
		Connection conn = getConnection();
		
		int insertToy = dao.insertToy(conn, toyName, toyStock);
		
		if(insertToy > 0) commit(conn);
		else              rollback(conn);
		
		close(conn);
		
		return insertToy;
	}
	
	/**
	 * 기계 안에 인형 넣기 : 넣을 수 있는 인형 수량
	 * @param toyName
	 * @return validToyStock
	 * @throws Exception
	 */
	public int validToyStock(String toyName) throws Exception {
		Connection conn = getConnection();
		
		int validToyStock = dao.validToyStock(conn, toyName);
		
		close(conn);
		
		return validToyStock;
	}

	/**
	 * 기계 안에 인형 넣기 : 기존 인형 수량 추가
	 * @param toyName
	 * @param toyStock
	 * @return updateToyStock
	 * @throws Exception
	 */
	public int updateToyStock(String toyName, int toyStock) throws Exception {
		Connection conn = getConnection();
		
		int updateToyStock = dao.updateToyStock(conn, toyName, toyStock);
		
		if(updateToyStock > 0) commit(conn);
		else                   rollback(conn);
		
		close(conn);
		
		return updateToyStock;
	}
	
	/**
	 * 현재 지갑 사정 보기
	 * @return selectPoint
	 * @throws Exception
	 */
	public int selectPoint() throws Exception {
		Connection conn = getConnection();
		
		int selectPoint = dao.selectPoint(conn);
		
		close(conn);
		
		return selectPoint;
	}	
	
	/**
	 * 용돈벌기
	 * @return savePoint
	 * @throws Exception
	 */
	public int savePoint(int point) throws Exception {
		Connection conn = getConnection();
		
		int savePoint = dao.savePoint(conn, point);
		
		if(savePoint > 0) commit(conn);
		else		      rollback(conn);
		
		close(conn);
		
		return savePoint;
	}
}