<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>慕課書評網</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="/resources/bootstrap/bootstrap.css">
    <link rel="stylesheet" href="/resources/raty/lib/jquery.raty.css">
    <script src="/resources/jquery.3.3.1.min.js"></script>
    <script src="/resources/bootstrap/bootstrap.min.js"></script>
    <script src="/resources/art-template.js"></script>
    <!--引入raty的JS-->
    <script src="/resources/raty/lib/jquery.raty.js"></script>

    <style>
        .highlight {
            color: red !important;
        }

        a:active {
            text-decoration: none !important;
        }
    </style>


    <style>
        .container {
            padding: 0px;
            margin: 0px;
        }

        .row {
            padding: 0px;
            margin: 0px;
        }

        .col- * {
            padding: 0px;
        }
    </style>
    <!--製作Art-Template的模板-->
    <!--將html片段當作Art-Template的模板-->
    <script type="text/html" id="tpl">
        <!--動態注入bookId變數-->
        <a href="/book/{{bookId}}" style="color: inherit">
            <div class="row mt-2 book">
                <div class="col-4 mb-2 pr-2">
                    <img class="img-fluid" src="{{cover}}">
                </div>
                <div class="col-8  mb-2 pl-0">
                    <h5 class="text-truncate">{{bookName}}</h5>

                    <div class="mb-2 bg-light small  p-2 w-100 text-truncate">{{author}}</div>


                    <div class="mb-2 w-100">{{subTitle}}</div>

                    <p>
                        <span class="stars" data-score="{{evaluationScore}}" title="gorgeous"></span>
                        <span class="mt-2 ml-2">{{evaluationScore}}</span>
                        <span class="mt-2 ml-2">{{evaluationQuantity}}人已評</span>
                    </p>
                </div>
            </div>
        </a>

        <hr>
    </script>

    <!--利用Ajax向後台BookController獲取書本資訊-->
    <!--當頁面加載完成直接向伺服器發起請求-->
    <script>
        <!--指定raty的圖片目錄-->
        <!--定義一個加載更多的函數-->
        function loadMore(isReset){ //isRest=true代表從從第一頁查詢，否則按照nextPage查詢下一頁
            if(isReset==true){
                $("#nextPage").val(1);
                //清空id為bookList標籤中的資料
                $("#bookList").html("");
            }
            //獲取書籍下一頁要查詢的頁碼
            var nextPage=$("#nextPage").val();
            //獲取隱藏數值
            var categorId=$("#categoryId").val();
            var order=$("#order").val();

            //查詢該頁碼
            $.ajax({
                url: "/books",
                data: { //JS會自動將key中的變數轉換為String而value是取變數的數值
                    p:nextPage,
                    categoryId:categorId,
                    order:order
                },
                type: "get",
                dataType: "json",
                success: function(json){
                    // console.info(json);
                    //獲取第一頁的所有元素
                    var list=json.records;
                    for(var i=0;i<list.length;i++){
                        //提取每本書的訊息
                        var book=json.records[i];
                        //使用JS動態加載到圖書顯示區
                        // var html="<li>"+book.bookName+"</li>";
                        //使用Art-Template注入html片段
                        var html=template("tpl",book); //傳入模板id和要動態顯示的資料
                        // console.info(html);
                        $("#bookList").append(html);
                    }
                    <!--對所有擁有starts的span依照分數顯示星星數量-->
                    $(".stars").raty({readonly:true});
                    //狀態控制(查看是否還有資料可以顯示)
                    if(json.current<json.pages){
                        $("#nextPage").val(parseInt(json.current)+1);
                        $("#btnMore").show();
                        $("#divNoMore").hide();
                    }else{
                        $("#btnMore").hide();
                        $("#divNoMore").show();
                    }
                }
            })
        }
        $.fn.raty.defaults.path="/resources/raty/lib/images";

        $(function (){
            loadMore(true);
        })

        //用於綁定加載更多按鈕的點擊事件
        $(function (){
            //獲取加載更多按鈕的id
            $("#btnMore").click(function (){
                loadMore(false);
            })
            //對擁有category class的標籤套用事件
            $(".category").click(function (){
                //移除所有分類的紅色高亮Class
                $(".category").removeClass("highlight");
                //設定為灰色
                $(".category").addClass("text-back-50");
                //獲取當前被點擊的標籤
                $(this).addClass("highlight");
                //獲取該標籤的data數值:data-category
                var categoryId=$(this).data("category");
                $("#categoryId").val(categoryId);
                //重新加載頁面
                loadMore(true);
            })

            //對擁有order class的標籤套用事件
            $(".order").click(function (){
                //移除所有分類的紅色高亮Class
                $(".order").removeClass("highlight");
                //設定為灰色
                $(".order").addClass("text-back-50");
                //獲取當前被點擊的標籤
                $(this).addClass("highlight");
                //獲取當前標籤的data數值
                var order=$(this).data("order");
                $("#order").val(order);
                loadMore(true);
            })
        })
    </script>
</head>

<body>
    <div class="container">
        <nav class="navbar navbar-light bg-white shadow mr-auto">
            <ul class="nav">
                <li class="nav-item">
                    <a href="/bookshop">
                        <img src="https://m.imooc.com/static/wap/static/common/img/logo2.png" class="mt-1"
                            style="width: 100px">
                    </a>
                </li>

            </ul>
            <!--如果loginMember存在-->
            <#if loginMember??>
                <h6 class="mt-1">
                    <img style="width: 2rem;margin-top: -5px" class="mr-1" src="./images/user_icon.png">${loginMember.nickname}
                </h6>
            <#else>
                <a href="/login.html" class="btn btn-light btn-sm">
                    <img style="width: 2rem;margin-top: -5px" class="mr-1" src="./images/user_icon.png">登錄
                </a>
            </#if>
        </nav>
        <div class="row mt-2">


            <div class="col-8 mt-2">
                <h4>熱評好書推薦</h4>
            </div>

            <div class="col-8 mt-2">
                <span data-category="-1" style="cursor: pointer" class="highlight  font-weight-bold category">全部</span>
                |
                <#--使用FreeMarker將分類資料動態載入到畫面中-->
                <#list categoryList as category>
                    <a style="cursor: pointer" data-category="${category.categoryId}" class="text-black-50 font-weight-bold category">${category.categoryName}</a>
                    <#if category_has_next>|</#if>
                </#list>



            </div>

            <div class="col-8 mt-2">
                <span data-order="quantity" style="cursor: pointer"
                    class="order highlight  font-weight-bold mr-3">按熱度</span>

                <span data-order="score" style="cursor: pointer"
                    class="order text-black-50 mr-3 font-weight-bold">按評分</span>
            </div>
        </div>
        <div class="d-none">
            <input type="hidden" id="nextPage" value="2">
            <input type="hidden" id="categoryId" value="-1">
            <input type="hidden" id="order" value="quantity">
        </div>

        <div id="bookList">

        </div>
        <button type="button" id="btnMore" data-next-page="1" class="mb-5 btn btn-outline-primary btn-lg btn-block">
            點擊加載更多...
        </button>
        <div id="divNoMore" class="text-center text-black-50 mb-5" style="display: none;">沒有其他數據了</div>
    </div>

</body>

</html>