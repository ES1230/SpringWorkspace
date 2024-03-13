package com.kh.opendata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class APIController {
	
    public static final String serviceKey = "3F%2FEdNsr06%2BDP1GY76%2FsC8VUVBhpog%2Fv0MWvZBFQpfvulKzyjEh7coEuVg7xUHvagNBM6HIiXzsGAuRD5la5aA%3D%3D"; 
    
    @RequestMapping(value="air.do", produces="application/json; charset=UTF-8")
    public String airPollution(@RequestParam String location) throws IOException {
    	
    	// OPENAPI서버로 요청할 URL주소 설정
		String url = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
		
		url += "?serviceKey="+serviceKey;
		url += "&sidoName="+ URLEncoder.encode(location,"UTF-8"); // url방식으로 인코딩 해줌, 한국어지원이 UTF-8이기 때문에 UTF-8로 설정
		//url += "&sidoName="+ URLEncoder.encode("서울,부산,대전","UTF-8"); // url방식으로 인코딩 해줌, 한국어지원이 UTF-8이기 때문에 UTF-8로 설정
		url += "&returnType=json";  
		//url += "&numOfRows=100"; //반환받는 결과값 개수
    	
		// 1. 요청하고자 하는 url주소를 매개변수로 전달하면서 URL객체 생성
		URL reqUrl = new URL(url);
		
		// 2. 생성된 URL을 통해, httpUrlConnection객체 생성
		HttpURLConnection urlConnection = (HttpURLConnection)reqUrl.openConnection(); 
		
		// 3. 전송방식 설정
		urlConnection.setRequestMethod("GET");
		
		// 4. 요청 주소에 적힌 openapi 서버로 요청 보내기
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
											  
		String responseText = ""; //반환받을 값 입력해주기
		String line;
		
		while( ( line = br.readLine() ) != null ) {
			responseText += line;
		}
		
		br.close();
		urlConnection.disconnect();
    			
    	return responseText; //이게 json형태로 선언된 문자열임
    }
    
    @RequestMapping(value="air2.do", produces="text/xml; charset=UTF-8")
    public String airPollution2(@RequestParam String location) throws IOException {
    	
    	// OPENAPI서버로 요청할 URL주소 설정
		String url = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
		
		url += "?serviceKey="+serviceKey;
		url += "&sidoName="+ URLEncoder.encode(location,"UTF-8"); // url방식으로 인코딩 해줌, 한국어지원이 UTF-8이기 때문에 UTF-8로 설정
		url += "&returnType=xml";  // <----- 여기만 바꿈  //
		url += "&numOfRows=50";
    	
		// 1. 요청하고자 하는 url주소를 매개변수로 전달하면서 URL객체 생성
		URL reqUrl = new URL(url);
		
		// 2. 생성된 URL을 통해, httpUrlConnection객체 생성
		HttpURLConnection urlConnection = (HttpURLConnection)reqUrl.openConnection(); 
		
		// 3. 전송방식 설정
		urlConnection.setRequestMethod("GET");
		
		// 4. 요청 주소에 적힌 openapi 서버로 요청 보내기
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
											  
		String responseText = ""; //반환받을 값 입력해주기
		String line;
		
		while( ( line = br.readLine() ) != null ) {
			responseText += line;
		}
		
		br.close();
		urlConnection.disconnect();
    			
    	return responseText;
    }
    
    
}