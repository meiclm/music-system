
$(function () {
    console.log("加载前："+$(".word").html()+",,"+$(".word").attr("txt_url"));
    if($(".word").attr("txt_url")!=null){
        console.log($(".word").attr("txt_url"));
        var htmlObj=$.ajax({url:'http://localhost:8080/upload/'+$(".word").attr("txt_url"),
            async:false,
            dataType: "html",
            scriptCharset: "script"
        });
        $(".word pre").html(htmlObj.responseText);
        console.log("加载后："+$(".word pre").html())
    }

    // $(".ico-zan .zan-pic").toggle(function () {
    //     $.ajax({url:$(this).attr("zan_uri"),async:true,success:function (data) {
    //         if (data==-1){
    //             alert("请先登陆")
    //         }else {
    //             $("#zan-count").html(data);
    //             $(".ico-zan .zan-pic").addClass("current");
    //         }
    //         }});
    // },function () {
    //     $.ajax({url:$(this).attr("remove_zan"),async:true,success:function (d) {
    //             if(d!=-1){
    //                 $("#zan-count").html(d);
    //                 $(".ico-zan .zan-pic").removeClass("current");
    //             }else {
    //                 alert("请先登陆");
    //             }
    //         }});
    //
    // });
    addOrReduce("div.ico-zan .zan-pic","#zan-count");
    addOrReduce("div.ico-zan .cai-pic","#cai-count");
    function addOrReduce(ev,count) {
        $(ev).toggle(function () {
            $.ajax({url:$(this).attr("add_uri"),async:true,success:function (data) {
                    if (data==-1){
                        alert("请先登陆")
                    }else {
                        $(count).html(data);
                        $(ev).addClass("current");
                    }
                }});
        },function () {
            $.ajax({url:$(this).attr("remove_zan"),async:true,success:function (d) {
                    if(d!=-1){
                        $(count).html(d);
                        $(ev).removeClass("current");
                    }else {
                        alert("请先登陆");
                    }
                }});

        });
    }

//判断该歌曲是否是当前用户喜欢的歌
    if($(".ico-zan #like").attr("like_value")!=null){
        $(".ico-zan #like").addClass("like-current");
        console.log("喜欢");
    }

});








