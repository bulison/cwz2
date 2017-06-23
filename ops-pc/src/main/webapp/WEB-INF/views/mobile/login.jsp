<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>login.html</title>
    <script src="/ops/resources/jquery/js/jquery.min.js"></script>
</head>
<body >

        <p class="title">用户登录</p>
        <p class="int"><input id="account" type="text" placeholder="请输入账号"></p>
        <p class="int"><input id="pwd" type="password" placeholder="请输入密码"></p>
       <button onclick="login() "  value="登陆" name="登陆">登陆</button>

</div>


<script type="text/javascript">
    function login() {
        //alert("inside makeRequest()");
        var settings = {
            headers: {
                "authorization" : localStorage.getItem("Authorization"),
                "pragma": "no-cache",
                "content-type": "application/json;charset=UTF-8",
                "expires": "0"
            },
            async      : true,
            crossDomain: true,
            type: "POST",
            data: "{'password': '123456', 'rememberMe': true, 'username': '0111'}",
            url: "/ops/api/authenticate",
            dataType: "JSON",

            complete: function(e, xhr, settings){
                //alert(e.status)
                console.log(e);
                if(e.status == 200){
                  //  alert(200)
                }else if(e.status == 304){
               //     alert(304)
                }else{
//
                }
            }


        };
        $.ajax(settings);
    }

</script>
</body>

</html>