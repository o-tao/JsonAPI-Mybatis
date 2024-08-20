$(document).ready(() => {
    console.log("Ready!");

    $( "form" ).on( "submit", function( event ) {
        event.preventDefault(); // form이 가고 있는 이벤트 정지
        console.log($("#userNm").val(), $("#userPwd").val());

        let params = {
            userNm : $("#userNm").val(),
            userPwd : $("#userPwd").val()
        }

        console.log(params);
        // {params} == {params: params}

        $.post("http://lh:80/login", params)
            .done(res => {
                console.log(res)
                if(res.status) { // 로그인 성공시
                    alert("환영합니다.") // 알림 메세지
                    $("#content1").addClass("d-none"); // d-none 추가시켜 로그인창 없애기
                    $("#content2").removeClass("d-none"); // d-none 삭제시켜 회원정보창 띄우기

                    $("nav li").eq(0).addClass("d-none"); // 로그인버튼 없애기
                    $("nav li").eq(1).removeClass("d-none"); // 로그아웃버튼 띄우기
                    $("nav li").eq(2).removeClass("d-none"); // 회원정보버튼 띄우기

                } else {
                    alert("사용자 정보가 일치하지 않습니다.")
                }
            })
            .fail(res => console.log(res));


//      ajax -> axios  
        // $.ajax({
        //     type: "POST",
        //     url: "http://lh:80/login",
        //     data: JSON.stringify(params),
        //     dataType: "json",
        //     contentType: "application/json; charset=UTF-8"
        // })
        // .done(res => console.log(res))
        // .fail(res => console.log(res));

    });

});
