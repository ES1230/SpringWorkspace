package com.kh.opendata.model.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.junit.Test;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kh.opendata.model.vo.AirVo;

@Service
public class AirPollutionTest {

	// 한국환경공단_에어코리아_대기오염정보 중 "시도별 실시간 측정정보 조회"
	// 사이트에서 가지고온 일반인증키 값 넣기
	public static final String serviceKey = "3F%2FEdNsr06%2BDP1GY76%2FsC8VUVBhpog%2Fv0MWvZBFQpfvulKzyjEh7coEuVg7xUHvagNBM6HIiXzsGAuRD5la5aA%3D%3D";
	
	public String[] locations = {
			"전국","서울","부산","대구","인천","광주","대전","울산","경기",
			"강원","충북","충남","전북","전남","경북","경남","제주","세종"};
	
	@Test
	public void locationTestRun() throws IOException{
		for( String location : locations ) {
			testRun(location);
		}
	}
	
	public void testRun(String location) throws IOException{
		
		// OPENAPI서버로 요청할 URL주소 설정
		String url = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
		
		url += "?serviceKey="+serviceKey;
		url += "&sidoName="+ URLEncoder.encode(location,"UTF-8"); // url방식으로 인코딩 해줌, 한국어지원이 UTF-8이기 때문에 UTF-8로 설정
		url += "&returnType=json";  
		url += "&numOfRows=50"; //반환받는 결과값 개수
		
		
		// 1. 요청하고자 하는 url주소를 매개변수로 전달하면서 URL객체 생성
		URL requestUrl = new URL(url);
		
		// 2. 생성된 URL을 통해, httpUrlConnection객체 생성
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection(); 
										  //반환형이 URLConnection이어서 HttpURLConnection으로 다운캐스팅
		
		// 3. 전송방식 설정
		urlConnection.setRequestMethod("GET");
		
		// 4. 요청 주소에 적힌 openapi 서버로 요청 보내기
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); //성능향상, 입력 혹은 출력 향상을 위한 보조스트림
											  //인풋스트림과 연결해줄 기반스트림 만들기
		String responseText = ""; //반환받을 값 입력해주기
		
		String line; 
		
		while( (line=br.readLine()) != null ) {
			System.out.println(line);
			responseText += line;
		}
		
		//System.out.println(responseText);
		
		//responseText에서 원하는 데이터만 추출하여 vo클래스로 전환
		JsonObject totalObj = JsonParser.parseString(responseText).getAsJsonObject(); 
							//responsetext에서 내가 원하는 데이터만 추출해서 담아주는 코드
		//System.out.println(totalObj);
		//{"response":{"body":{"totalCount":6,"items":[{"so2Grade":"1","coFlag":null,"khaiValue":"219","so2Value":"0.004","coValue":"0.9","pm10Flag":null,"o3Grade":"1","pm10Value":"102","khaiGrade":"3","sidoName":"세종","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2024-02-01 11:00","coGrade":"1","no2Value":"0.025","stationName":"신흥동","pm10Grade":"2","o3Value":"0.023"}} 
		//이런식으로 담겨있음
		
		JsonObject response = totalObj.getAsJsonObject("response");
		
		JsonObject body = response.getAsJsonObject("body");
			
		int totalCount = body.get("totalCount").getAsInt();
		
		JsonArray items = body.getAsJsonArray("items");
		
		
		ArrayList<AirVo> list = new ArrayList();
		
		if( items != null) {
			for(int i=0; i<items.size(); i++) {
				JsonObject obj = items.get(i).getAsJsonObject();
				
				AirVo air = new AirVo();
				
				air.setStationName(obj.get("stationName").getAsString());
				
				if(!obj.get("dataTime").isJsonNull()) {
					air.setDataTime(obj.get("dataTime").getAsString());
					air.setCoValue(obj.get("coValue").getAsString());
					air.setKhaiValue(obj.get("khaiValue").getAsString());
					air.setPm10Value(obj.get("pm10Value").getAsString());
					air.setSo2Value(obj.get("so2Value").getAsString());
					air.setNo2Value(obj.get("no2Value").getAsString());
					air.setO3Value(obj.get("o3Value").getAsString());
				}
				list.add(air);
			}
		}
		
		for(AirVo a : list) {
			System.out.println(a);
		}
		
		br.close();
		urlConnection.disconnect();
		
	}
	
	
}
