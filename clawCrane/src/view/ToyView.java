package view;

import java.util.List;
import java.util.Scanner;

import service.ToyService;
import vo.Toy;

public class ToyView {
	
	private ToyService service = new ToyService();
	private Scanner sc = new Scanner(System.in);
	
	private int input = 0;

	public void toyMenu() {
		try {
			Thread.sleep(500);
			
			System.out.print("	빠밤");
			Thread.sleep(500);
			
			System.out.print("빠밤");
			Thread.sleep(500);
			
			System.out.print("빠밤");
			Thread.sleep(500);
			
			System.out.println("빠밤!!\n");
			Thread.sleep(1000);
			
			System.out.println("	____________________ \n"
							 + "	|__________________| \n"
							 + "	|   |              | \n"
							 + "	|   |              | \n"
							 + "	|   ¿  	           | \n"
							 + "	-------------------- \n"
							 + "	|    ○  1play 10p  | \n"
							 + "	|    |    ――――     | \n"
							 + "	-------------------- \n");
			Thread.sleep(1000);
			
			System.out.println("앗! 야생의 인형뽑기 기계와 마주쳤다!\n");
			Thread.sleep(1000);
			
			do {
				System.out.println("무엇을 할까?\n");
				
				System.out.println("1) 인형뽑기");
				System.out.println("2) 전체 인형 목록 보기");
				System.out.println("3) 뽑은 인형 목록 보기");
				System.out.println("4) 기계 안에 인형 넣기");
				System.out.println("5) 현재 지갑 사정 보기");
				System.out.println("6) 용돈벌기");
				System.out.println("7) 도망가기\n");
				
				System.out.print("▶ ");
				input = sc.nextInt();
				
				switch(input) {
				case 1: catchToy(); break;
				case 2: selectAllToy(); break;
				case 3: selectToy(); break;
				case 4: insertToy(); break;
				case 5: selectPoint(); break;
				case 6: savePoint(); break;
				case 7: System.out.println("\n성공적으로 도망쳤다!"); break;
				default: System.out.println("\n그런건 없다.\n"); Thread.sleep(500);
				}
			} while(input != 7);
			
		} catch(Exception e) {
			System.out.println("오류한테 당하고 말았다...");
		}
	}

	/**
	 * 인형뽑기
	 * 
	 *  1) POINT를 SELECT 해와서 포인트가 부족하면 뽑을 수 없게 함
	 *    if(point >= 10)
	 *    POINT는 service.selectPoint() 메소드를 사용해서 SELECT
	 *
	 *  2) POINT를 사용해서 인형을 뽑음
	 * 	  UPDATE POINT SET POINT = POINT - 10
	 * 
	 *  3) 난수(random)를 사용해서 뽑기가 성공하거나 실패하는 경우를 나눔
	 * 
	 *  4) 난수(random)를 사용해서 인형을 랜덤으로 뽑는데,
	 *    만약 TOY_STOCK = 0인 인형을 뽑았을 경우 다시 뽑음
	 *    SELECT TOY_STOCK FROM TOY WHERE TOY_NO = ?
	 *    while(true)
	 *    if(toy.getToyStock() == 0) continue;
	 *    else                       break;
	 *    
	 *  5) 뽑은 인형의 이름을 보여줌
	 *    SELECT TOY_NAME FROM TOY WHERE TOY_NO = ?
	 *    
	 *  6) 뽑은 인형이 만약 처음 뽑은 인형이라면 TOY_CATCH 테이블에 INSERT
	 *    INSERT INTO TOY_CATCH VALUES (?, DEFAULT);
	 * 
	 *  7) 뽑은 인형이 만약 이미 뽑은 인형인데
	 *    99개 이상이면 "뽑은 건 기쁘지만 더 이상 가질 수 없다."를 출력
	 *    SELECT TOY_STOCK FROM TOY_CATCH WHERE TOY_NAME = ?
	 *    if(toy.getToyStock() >= 99)
	 *    
	 *  8) 뽑은 인형이 만약 이미 뽑은 인형인데
	 *    99개 미만이면 TOY_CATCH 테이블에 뽑은 수량 UPDATE
	 *    UPDATE TOY_CATCH SET TOY_COUNT = TOY_COUNT + 1
	 * 
	 *  9) 뽑은 인형이 중복인지 아닌지 확인 -> service.exists 참고
	 * 
	 * 10) 특정 인형은 이벤트를 만들어줌 -> event(ran)
	 */
	private void catchToy() {
		try {
			System.out.println("\n당신은 [인형뽑기]를 선택했다!\n");
			Thread.sleep(300);
			
			int resultRow = 0;
			int point = service.confirmPoint();
			
			// 큰 if문 : POINT가 충분한지 구분
			if(point >= 10) {
				int usePoint = service.usePoint();
				
				// 하위 if문1 : 포인트를 성공적으로 사용했을 경우의 if문
				if(usePoint > 0) {
					int ran = (int)(Math.random() * 2);
					
					// 하위 if문2 : ran이 0이면 뽑기 성공, 1이면 뽑기 실패
					if(ran == 0) {
						int toyNumber = service.toyNumber();
						
						// while문 : 재고가 없는 인형일 경우 인형을 다시 뽑음
						while(true) {
							ran = (int)(Math.random() * toyNumber + 1);
							int toyStock = service.selectToyStock(ran);
							
							if(toyStock != 0) break;
						}
							
						String toyName = service.catchToy(ran);
						
						System.out.println("와! " + toyName + "을(를) 뽑았다!");
						Thread.sleep(500);
						
						int catchExists = service.catchExists(toyName);
						
						// 하위 if문3 : TOY_CATCH 테이블에 있는 인형인지 확인
						//              catchExists == 0 중복X
						if(catchExists == 0) {
							resultRow = service.catchInsert(toyName);
							commitPrint(resultRow);
							event(ran);
							
						} else {
							int catchToyStock = service.catchToyStock(toyName);
							
							// 하위 if문4 : 뽑은 99개 미만일 때만 수량 + 1
							if(catchToyStock >= 99) {
								System.out.println("\n뽑은 건 기쁘지만 더 이상 가질 수 없다.\n");
								Thread.sleep(500);
								
							} else {
							resultRow = service.catchUpdate(toyName);
							commitPrint(resultRow);
							event(ran);
							} // if4
						} // if3
						
					} else {
						System.out.println("집게 힘이 너무 약해서 인형을 놓치고 말았다...\n");
						Thread.sleep(500);
					} // if2
					
				} else {
					System.out.println("집게 힘이 너무 약해서 인형을 놓치고 말았다...\n");
					Thread.sleep(500);
				} // if1
				
			} else {
				System.out.println("용돈이 부족하다.\n");
				Thread.sleep(500);
			}
			
		} catch(Exception e) {
			System.out.println("\n오류한테 당하고 말았다...");
		}
	}

	/**
	 * DML COMMIT 확인 후 출력
	 * @param resultRow
	 */
	private void commitPrint(int resultRow) throws Exception {
		if(resultRow > 0) {
			System.out.println("\n인형 추가 완료!\n");
			Thread.sleep(500);
			
		} else {
			System.out.println("\n인형을 넣고 있었는데 까마귀가 물어갔다...\n");
			Thread.sleep(500);
		}
	}	
	
	/**
	 * 특정 인형 이벤트
	 * @param ran
	 */
	private void event(int ran) throws Exception {
		switch(ran) {
		case 2:
			while(true) {
				System.out.println("안아줘요 인형을 안아주시겠습니까? (예, 아니오 택1)");
				Thread.sleep(500);
				
				System.out.print("▶ ");
				String hug = sc.next();
				
				if(hug.equals("예")) {
					System.out.println("\n안아줘요의 기분이 좋아보인다!");
					Thread.sleep(500);
					
					System.out.println("\n안아줘요의 호감도가 10 상승하였습니다.\n");
					Thread.sleep(500);
					break;
					
				} else if(hug.equals("아니오")) {
					System.out.println("\n안아줘요의 기분이 다운되었다.");
					Thread.sleep(500);
					
					System.out.println("\n지구는 멸망했다.");
					Thread.sleep(500);
					
					input = 7;
					break;
					
				} else {
					System.out.println("\n그런건 없다.\n");
					Thread.sleep(500);
				}
			}
			
			break;
		}
	}

	/**
	 * 전체 인형 목록 보기
	 */
	private void selectAllToy() {
		try {
			System.out.println("\n당신은 [전체 인형 목록 보기]를 선택했다!\n");
			Thread.sleep(500);
			
			List<Toy> toyList = service.selectAllToy();
			toyList(toyList);
			
		} catch(Exception e) {
			System.out.println("\n오류한테 당하고 말았다...");
		}
	}

	/**
	 * 뽑은 인형 목록 보기
	 */
	private void selectToy() {
		try {
			System.out.println("\n당신은 [뽑은 인형 목록 보기]를 선택했다!\n");
			Thread.sleep(500);
			
			List<Toy> toyList = service.selectToy();
			toyList(toyList);
			
		} catch(Exception e) {
			System.out.println("\n오류한테 당하고 말았다...");
		}
	}

	/**
	 * 인형 목록 출력
	 */
	private void toyList(List<Toy> toyList) throws Exception {
		if(toyList.isEmpty()) {
			System.out.println("텅 비어있다...\n");
			Thread.sleep(500);
			
		} else {
			columnAlign();
			
			for(Toy toy : toyList) {
				rowAlign(toy.getToyName(), toy.getToyStock());
			}
			
			System.out.println();
			Thread.sleep(500);
		}
	}
	
	/**
	 * 열 가운데 정렬 후 출력
	 * @param text1
	 * @param text2
	 */
	private void columnAlign() {
		System.out.println("■ 인형 목록 ■");
		System.out.println("---------------------------");
	    System.out.printf("|%s|%s|\n", alignCenter("이름"), alignCenter("수량"));
	    System.out.println("---------------------------");
	}
	
	/**
	 * 행 가운데 정렬 후 출력
	 * @param text1
	 * @param number
	 */
	private void rowAlign(String text1, int number) {
		String text2 = number + "     ";
	    System.out.printf("|%s|%12s|\n", alignCenter(text1), text2);
	    System.out.println("---------------------------");
	}
	
	/**
	 * 문자열 가운데 정렬
	 * @param bringText
	 * @return text
	 */
	private String alignCenter(String bringText) {
		String text = "";
		int marginSize = (6 - bringText.length()) / 2;
		
		// 왼쪽 여백
		text = fillMargin(marginSize, text);
		
		// 홀수일 경우 & 짝수일 경우
		if(bringText.length() % 2 == 1) text += " " + bringText + " ";
		else							text += bringText;
		
		// 오른쪽 여백
		text = fillMargin(marginSize, text);
		
	    return text;
	}
	
	/**
	 * 문자열 여백 채우기
	 * @param marginSize
	 * @param text
	 * @return text
	 */
	private String fillMargin(int marginSize, String text) {
		for (int i = 0; i < marginSize; i++) {
	        text += ("  ");
	    }
		
		return text;
	}	
	
	/**
	 * 기계 안에 인형 넣기
	 * 
	 * 1) 인형 이름과 넣을 수량을 입력받음
	 * 
	 * 2) 넣을 인형이 새 인형일 경우 INSERT
	 * 
	 * 3) 넣을 인형이 이미 있는 경우
	 *    99 - TOY_STOCK >= 수량 이라면 정상적으로 UPDATE  
	 *    
	 * 4) 넣을 인형이 이미 있는 경우
	 *    99 - TOY_STOCK < 수량 이라면 넣을 수 있는 수량을 보여주고 재입력받아서 UPDATE
	 * 
	 * 5) 넣을 인형이 이미 있는 경우
	 *    99 = TOY_STOCK 이라면 "이 인형은 기계 안에 꽉 차있어서 더 넣을 수 없다..."를 출력
	 *    
	 * 6) 인형의 현재 수량은 SELECT로 조회
	 *    SELECT TOY_STOCK FROM TOY WHERE TOY_NO = ?
	 */
	private void insertToy() {
		try {
			System.out.println("\n당신은 [기계 안에 인형 넣기]를 선택했다!\n");
			Thread.sleep(500);
			
			System.out.println("어떤 인형을 넣을까? (기존 인형, 새 인형 모두 가능!)");
			System.out.print("▶ ");
			String toyName = sc.next();

			int toyStock = inputToyStock();
			int insertExists = service.insertExists(toyName);
			int resultRow = 0;
			
			// 큰 if문 : 넣을 인형 중복 검사
			if(insertExists == 0) {
				resultRow = service.insertToy(toyName, toyStock);
				commitPrint(resultRow);
				
			} else {
				int validToyStock = 99 - service.validToyStock(toyName);
				
				while(true) {
					// 하위 if문 : validToyStock == 0                  넣을 수 없음
					//             validToyStock >= toyStock           넣을 수 있음
					//             validToyStock < toyStock  재검사 후 넣을 수 있음
					if(validToyStock == 0) {
						System.out.println("\n이 인형은 기계 안에 꽉 차있어서 더 넣을 수 없다...\n");
						Thread.sleep(500);
						break;
						
					} else if(validToyStock >= toyStock) {
						resultRow = service.updateToyStock(toyName, toyStock);
						commitPrint(resultRow);
						break;
						
					} else if(validToyStock < toyStock) {
							System.out.printf("\n%s 인형은 %d개만 더 넣을 수 있다.\n", toyName, validToyStock);
							Thread.sleep(500);
							toyStock = inputToyStock();
					}
				}
			}
			
		} catch(Exception e) {
			System.out.println("\n오류한테 당하고 말았다...");
		}
	}

	/**
	 * 수량 입력
	 * @return toyStock
	 */
	private int inputToyStock() throws Exception {
		int toyStock = 0;

		while(true) {
			System.out.println("\n얼마나 넣을까? (99 이하 숫자만 입력 가능!)");
			System.out.print("▶ ");
			toyStock = sc.nextInt();
			
			if(toyStock <= 99) break;
			
			else {
				System.out.println("\n99 이하 숫자만 입력할 수 있다.");
				Thread.sleep(500);
			}
		}
		return toyStock;
	}
	
	/**
	 * 현재 지갑 사정 보기
	 */
	private void selectPoint() {
		try {
			System.out.println("\n당신은 [현재 지갑 사정 보기]를 선택했다!\n");
			Thread.sleep(500);
			
			int selectPoint = service.selectPoint();
			
			System.out.println(selectPoint + "P가 남아있다.\n");
			Thread.sleep(500);
						
		} catch(Exception e) {
			System.out.println("\n오류한테 당하고 말았다...");
		}
	}

	/**
	 * 용돈벌기
	 * 인형뽑기 기계와 가위바위보를 해서 이길 경우
	 * 묻더? 물어보고   고! 하면 point x 2
	 *                멈춰! 하면 현재 point 그대로 받기
	 * UPDATE POINT SET POINT = POINT + ?
	 */
	private void savePoint() {
		try {
			System.out.println("\n당신은 [용돈벌기]를 선택했다!\n");
			Thread.sleep(500);
			
			System.out.println("인형뽑기 기계가 가위바위보 배틀을 걸어왔다.");
			Thread.sleep(500);
			
			boolean flag = false;
			int point = 100;
			
			// do while문 : 묻더?
			do {
				System.out.printf("\n인형뽑기 기계와의 배틀에서 이기면 %dEXP와 %dP를 얻을 수 있다!\n\n", point, point);
				Thread.sleep(500);
				
				flag = false;
				
				// while문 : 이상한 입력 거르고 무승부 시 반복
				while(true) {
					System.out.println("가위, 바위, 보 중에 하나를 선택해보자.");
					System.out.print("▶ ");
					String user = sc.next();
					
					// 큰 if문 : 만약에 가위, 바위, 보 이외의 것을 내면 다시 내라고 함
					if(user.equals("가위") || user.equals("바위") || user.equals("보")) {
						int ran = (int)(Math.random() * 3 + 1);
						String com = "";
						
						switch(ran) {
						case 1: com = "가위"; break;
						case 2:	com = "바위"; break;
						case 3: com = "보"; break;
						}
						
						// 하위 if문1 : 승 & 패 & 무승부
						if(user.equals(com)) {
							System.out.println("\n무승부!\n");
							Thread.sleep(500);
							
							System.out.println("■ 재경기 ■");
							Thread.sleep(500);
							
						} else if ((user.equals("가위") && com.equals("바위")) ||
							       (user.equals("바위") && com.equals("보"))   ||
								   (user.equals("보") && com.equals("가위")))   {
							System.out.println("\n당신은 지고 말았다. 용돈을 버는데 실패했다...\n");
							Thread.sleep(500);
							break;
							
						} else {
							System.out.println("\n배틀에서 이겼다!\n");
							Thread.sleep(500);
							
							System.out.println("만약 이대로 한 판 더 해서 이기면 용돈을 2배로 받을 수 있다!");
							Thread.sleep(500);
							
							System.out.println("\n하지만 지면 얻은 용돈을 모두 잃게 되는데...");
							Thread.sleep(500);
							
							while(true) {
								System.out.println("\n묻더? (고!, 멈춰! 택1)");
								System.out.print("▶ ");
								String x2 = sc.next();
								
								// 하위 if문2 : 묻더 입력
								if(x2.equals("고!")) {
									point *= 2;
									flag = true;
									break;
									
								} else if(x2.equals("멈춰!")) {
									int savePoint = service.savePoint(point);
									
									// 하위 if문3 : 보상 냠냠
									if(savePoint > 0) {
										System.out.printf("\n%dEXP와 %dP를 획득했다!\n\n", point, point);
										Thread.sleep(500);
										break;
										
									} else {
										System.out.println("\n이겼지만 이상하게도 아무 일도 없었다...\n");
										Thread.sleep(500);
										break;
									} // if3
									
								} else {
									System.out.println("\n그런건 없다.");
									Thread.sleep(500);
								} // if2
							}
							
							break;
						}
						
					} else {
						System.out.println("\n그런건 없다.\n");
						Thread.sleep(500);
					} // if1
				}
			} while(flag);
			
		} catch(Exception e) {
			System.out.println("\n오류한테 당하고 말았다...");
		}
	}
}