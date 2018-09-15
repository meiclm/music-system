$(function () {
//    首页轮播
    displayContent("li.second-title-cn");

    $("li.second-title").each(function () {
        $(this).click(function () {
            displayContent(this);
            $(this).siblings("li").removeClass("second-active");
        });
    });

    function displayContent(even) {
        $(even).addClass("second-active");
        $.ajax({
            url: $(even).attr("data_url"), async: true, success: function (data) {
                if (data != null) {
                    $(".second-ul-first").html("");
                    $(".second-ul-second").html("");
                    console.log("我的数据：" + data + "," + data.list.length);
                    for (let i = 0; i < data.list.length; i++) {
                        if (i >= 5) {
                            $(".second-ul-second").append("<li><a target='_blank' href='http://localhost:8080/display/"
                                + data.list[i].id + "'><div><img width='224' height='225' src='http://localhost:8080/upload/image/singer/"
                                + data.list[i].singer.name + ".PNG'></div>" +
                                "<div style='width: 224px;height: 20px;overflow: hidden;text-overflow: ellipsis;'>" + data.list[i].sname + "</div></a></li>");
                        } else {
                            $(".second-ul-first").append("<li><a target='_blank' href='http://localhost:8080/display/"
                                + data.list[i].id + "'><div><img width='224' height='225' src='http://localhost:8080/upload/image/singer/"
                                + data.list[i].singer.name + ".PNG'></div>" +
                                "<div style='width: 224px;height: 20px;overflow: hidden;text-overflow: ellipsis;'>" + data.list[i].sname + "</div></a></li>");
                        }
                    }
                }
            }
        });
    }

    changeDJMovieMusical("li.three-title-DJ");

    $("li.third-title").each(function () {
        $(this).click(function () {
            changeDJMovieMusical(this);
            $(this).siblings("li").removeClass("second-active");
        });
    });

    //DJ、影视、经典的切换
    function changeDJMovieMusical(str) {
        $(str).addClass("second-active");
        $.ajax({
            url: $(str).attr("data_url"), async: true, success: function (data) {
                if (data != null) {
                    $("ul.three-content").html("");
                    for (let i = 0; i < data.list.length; i++) {
                        $("ul.three-content").append("<li><a target='_blank' href='http://localhost:8080/display/"
                            + data.list[i].id + "'><div><img width='224' height='225' src='http://localhost:8080/upload/image/singer/"
                            + data.list[i].sname + ".PNG'></div>" +
                            "<div style='width: 224px;height: 20px;overflow: hidden;text-overflow: ellipsis;'>" + data.list[i].sname + "</div></a></li>");
                    }
                }
            }
        });
    }
});

