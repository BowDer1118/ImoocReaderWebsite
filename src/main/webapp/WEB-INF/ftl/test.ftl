<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <!--引入wangEditor的JS-->
    <script src="/resources/wangEditor.min.js"></script>
</head>
<body>
<div>
    <button id="btnRead">讀取內容</button>
    <button id="btnWrite">寫入內容</button>
</div>
<!--使用div容器存放wangEditor-->
<div id="divEditor" style="width: 800px;height: 600px"></div>
<script>
    //獲得wangEditor物件
    var E=window.wangEditor;
    //透過id找到wangEditor組件，並實例化(此時尚未顯示)
    var editor=new E("#divEditor");
    //顯示wangEditor
    editor.create();

    document.getElementById("btnRead").onclick=function (){
        //獲取Editor內的HTML內容
        var content=editor.txt.html();
        alert(content);
    }

    document.getElementById("btnWrite").onclick=function (){
        var content="<li style='color:red'>我是<b>新內容</b></li>";
        //對Editor寫入HTML內容
        editor.txt.html(content);
    }

</script>
</body>
</html>