$(".downloadBtn").click(function () {
    $.ajax({url:$(this).attr("download_url"),async:true,success:function (m) {
            console.log(m);
            if (m==0){
                $(".downloadBtn").attr({"download":$(".downloadBtn").attr("download_value"),
                    "href":'http://localhost:8080/upload/'+$(".downloadBtn").attr("download_suf")});
                console.log("?download");
            }else if (m==1){
                alert("你的等级不足8级，不能下载！");
            }else {
                alert("请先登陆！");
            }
        }})
});

//喜欢操作
$(".ico-zan #like").click(function () {
    console.log($(".ico-zan #like").hasClass("like-current"));

    if ($(".ico-zan #like").hasClass("like-current")){
        $.ajax({url:$(this).attr("re_like"),async:true,success:function (data) {
                console.log("remove");
                if (data==0){
                    console.log("0");
                    $(".ico-zan #like").removeClass("like-current");
                }else if (data==-1){
                    alert("请先登陆");
                    $("#userLogin-btn").css("display","inline").attr("href");
                }else {
                    console.log("1");
                    $(".ico-zan #like").addClass("like-current");
                }
            }});
    }else {
        $.ajax({url:$(this).attr("like_url"),async:true,success:function (data) {
                console.log("add");
                if (data==0){
                    console.log("0")
                    $(".ico-zan #like").addClass("like-current");
                }else if (data==-1){
                    console.log("-1")
                    $("#userLogin-btn").css("display","inline").attr("href");
                }else {
                    console.log("1");
                    $(".ico-zan #like").removeClass("like-current");
                }
            }});
    }
});
