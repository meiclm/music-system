$(function () {
    console.log($("li.classify").length)
    $("li.classify").each(function () {
        $(this).css("cursor","pointer").click(function () {
            $.ajax({url:$(this).children().attr("data_url"),async:true,success:function (data) {
                    if (data!=null){
                        $("div.static-singer").hide();
                        $("div#nationality-result ul").html("");
                        for (let i=0;i<data.length;i++){
                            $("div#nationality-result ul").append("<li><a target='_blank' " +
                                "href='http://localhost:8080/singer/" +data[i].id+"'>" +data[i].name+"</a></li>")
                        }
                    }
                }});

        });
    });
});

