$("#manyOperate").click(function () {
    if ($(".check-box").prop('checked')){
        $(".check-box").prop('checked',false);
    }else {
        $(".check-box").prop('checked',true);
    }
});

//显示我喜欢列表
$("button#my-like-btn").click(function () {
    $(".like-song-list").toggle();
    console.log("显示了");
    $(".mySong-list").toggle();
    console.log("隐藏了");
});

