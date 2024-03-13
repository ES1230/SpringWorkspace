<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="true"%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
</head>
<body>
<!-- 
	<h2> 실시간 대기오염 정보</h2>

	지역 : 
	<select id="location">
		<option>서울</option>
		<option>부산</option>
		<option>대전</option>
		<option>대구</option>
		<option>경기</option>
	</select>

	<button id="btn1">실시간 대기오염정보 확인</button>
	
	<button id="btn2">실시간 대기오염정보 확인(xml방식)</button>
	
	<button id="btn3">실시간 대기오염정보 확인(jsp에서 요청 보내기)</button>

	<table id="result1" border="1" align="center">
		<thead>
			<tr>
				<th>측정소명</th>
				<th>측정시간</th>
				<th>통합대기환경수치</th>
				<th>미세먼지농도</th>
				<th>일산화탄소농도</th>
				<th>이산화질소농도</th>
				<th>아황산가스농도</th>
				<th>오존농도</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</tbody>
	</table> -->
	
	
		<h2>실시간 대기오염 정보</h2>
		
		지역 :
		<select id = "location">
			<option>서울</option>
			<option>부산</option>
			<option>대전</option>
		</select>
		
		<button id = "btn1">해당 지역 대기오염 정보</button>
		<br><br>
		
		<table id = "result1" border = "1">
			<thead>
				<tr>
					<th>측정소명</th>
					<th>측정일시</th>
					<th>통합대기환경수치</th>
					<th>미세먼지농도</th>
					<th>일산화탄소농도</th>
					<th>이산화질소농도</th>
					<th>아황산가스농도</th>
					<th>오존농도</th>
				</tr>
			</thead>
			<tbody>
			
			</tbody>
		</table>
	
	
	<script>
		$(function () {
		    $("#btn1").click(function () {
		        $.ajax({
		            url: "air.do",
		            data: { location: $("#location").val() },
		            success: function (data) {
		                const itemArr = data.response.body.items;
		                let value = "";
						for (let i in itemArr) {
						    let item = itemArr[i];
						    value += `<tr>
						                <td>${item.stationName}</td>							
						                <td>${item.dataTime}</td>							
						                <td>${item.khaiValue}</td>							
						                <td>${item.pm10Value}</td>							
						                <td>${item.coValue}</td>							
						                <td>${item.no2Value}</td>							
						                <td>${item.so2Value}</td>							
						                <td>${item.o3Value}</td>	
						              </tr>`;
						}
		                $("#result1 tbody").html(value);
		            }
		        });
		    });
		});
	</script>

	<script>
	 	$(function(){
	 		/*		
			let $selectedLocation = $("#location");
			//선택된 지역 가져오기
			
			$("#btn1").click(function(){
				$.ajax({
					url : "air.do",
					data : { location : $selectedLocation.val() },
					success : function(data){ // json형태로 파싱된 데이터 
						//console.log(data);
						let value = "";
						let {items} = data.response.body;
						
						for( let air of items ){
							value += `<tr>
										<td>${air.stationName}</td>							
										<td>${air.dataTime}</td>							
										<td>${air.khaiValue}</td>							
										<td>${air.pm10Value}</td>							
										<td>${air.coValue}</td>							
										<td>${air.no2Value}</td>							
										<td>${air.so2Value}</td>							
										<td>${air.o3Value}</td>							
									</tr>`;
						}
						$("#result1>tbody").html(value);
					}
				})
			})		
			 */
			$("#btn2").click(function(){
				$.ajax({
					url : "air2.do",
					data : { location : $selectedLocation.val() },
					success : function(data){ //data -> xml형식의 데이터
						// find() - 내가 선택한 요소의 모든 후손요소 찾기
						let $items = $(data).find("item");
						let value = "";
						$items.each(function(index,item){
							value += `<tr>
							<td>${$(item).find("stationName").text()}</td>
							<td>${$(item).find("dataTime").text()}</td>
							<td>${$(item).find("khaiValue").text()}</td>
							<td>${$(item).find("pm10Value").text()}</td>
							<td>${$(item).find("coValue").text()}</td>
							<td>${$(item).find("no2Value").text()}</td>
							<td>${$(item).find("so2Value").text()}</td>
							<td>${$(item).find("o3Value").text()}</td>
							</tr>`;
						})
						$("#result1 tbody").html(value);
					},
					error : function(req,err,res){
						console.log("btn 2오류");
						console.log(req,err,res);
					}
				})
			})
		
			$("#btn3").click(function(){
				$.ajax({
					//localhost:8081/opendata/ 이렇게 해서 보내면 됨
					url : "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty",
					type : 'get',
					data : { /* 인코딩말고 디코딩 서비스키로 넣으면 됨 */
						serviceKey : '3F%2FEdNsr06%2BDP1GY76%2FsC8VUVBhpog%2Fv0MWvZBFQpfvulKzyjEh7coEuVg7xUHvagNBM6HIiXzsGAuRD5la5aA%3D%3D',
						sidoName : $("#location").val(),
						returnType : 'json',
						numOfRows : '30'
					}, 
					success : function(data){
						console.log(data);
						
						let value = "";
						let {items} = data.response.body;
						
						for( let air of items ){
							value += `<tr>
										<td>${air.stationName}</td>							
										<td>${air.dataTime}</td>							
										<td>${air.khaiValue}</td>							
										<td>${air.pm10Value}</td>							
										<td>${air.coValue}</td>							
										<td>${air.no2Value}</td>							
										<td>${air.so2Value}</td>							
										<td>${air.o3Value}</td>							
									</tr>`;
						}
						$("#result1>tbody").html(value);
						
						
					},
					error : function(error,req,res){
						console.log(error,req,res);
					}
				})
			})
			
			
		})
	</script>



</body>
</html>