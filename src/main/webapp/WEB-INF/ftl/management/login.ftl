<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>圖書管理系統</title>
    <link rel="stylesheet" href="/resources/layui/css/layui.css">
    <style>
        body{
            background-color: #F2F2F2;
        }
        .oa-container{
            /*background-color: white;*/
            position: absolute;
            width: 400px;
            height: 350px;
            top: 50%;
            left: 50%;
            padding: 20px;
            margin-left: -200px;
            margin-top: -175px;
        }
        #username,#password{
            text-align: center;
            font-size: 24px;
        }
    </style>
</head>
<body>
<div class="oa-container">
    <h1 style="text-align: center;margin-bottom: 20px">圖書管理系統</h1>
    <form class="layui-form">
        <div class="layui-form-item">
            <input type="text" id="username" lay-verify="required" name="username" placeholder="請輸入用戶名" autocomplete="off" class="layui-input" >
        </div>

        <div class="layui-form-item">
            <input type="password" id="password" lay-verify="required" name="password" placeholder="請輸入密碼" autocomplete="off" class="layui-input" >
        </div>
        <div class="layui-form-item">
            <button class="layui-btn layui-btn-fluid" lay-submit lay-filter="login">登錄</button>
        </div>
    </form>
</div>
<script src="/resources/layui/layui.all.js"></script>
<script>
    // 表單提交事件
    layui.form.on("submit(login)" , function(formdata){//data參數包含了當前表單的數據
        console.log(formdata);
        //發送ajax請求進行登錄校驗
        layui.$.ajax({
            url : "/management/check_login",
            data : formdata.field, //提交表單數據
            type : "post",
            dataType : "json" ,
            success : function(json){
                console.log(json);
                if(json.code == "0"){ //登錄校驗成功
                    // layui.layer.msg("登錄成功");
                    //跳轉url
                    window.location.href=json.redirect_url;
                }else{
                    layui.layer.msg(json.message);
                }
            }
        })
        return false;//submit提交事件返回true則表單提交,false則阻止表單提交
    })
</script>
</body>
</html>