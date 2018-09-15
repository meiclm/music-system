$(function () {

    //登陆板块和注册板块切换
    $(".loginBtn").click(function () {
        loginShow();
    });

    $(".registerBtn").click(function () {
        registerShow();
    });

    //输入框的值改变时触发
    $("#r-username").on("input", function (e) {
        //获取input输入的值
        if (e.delegateTarget.value.length < 6 || e.delegateTarget.value.length > 12) {
            $(this).siblings("span").html("用户名长度太短！").css(
                {"color": "red", "font-size": "12px", "font-weight": "blod"});
        } else {
            $(this).siblings("span").html("");
        }
    });

    $("#r-password").on("input", function (e) {
        if (e.delegateTarget.value.length < 6) {
            $(this).siblings("span").html("密码长度太短！").css(
                {"color": "red", "font-size": "12px", "font-weight": "blod"});
        } else {
            $(this).siblings("span").html("");
        }
    });

    //提交表单前判断用户名，密码，邮箱会自动判断，文件格式
    $("#register-button").click(function () {

        if ($("#r-username").val().length < 6 || $("#r-username").val().length > 12) {
            $("#r-username").siblings("span").html("用户名长度太短！").css(
                {"color": "red", "font-size": "12px", "font-weight": "blod"});
            return false;
        } else {
            $(this).siblings("span").html("");
        }

        console.log($("#r-password").val().length);
        if ($("#r-password").val().length < 6) {
            $("#r-password").siblings("span").html("密码长度太短！").css(
                {"color": "red", "font-size": "12px", "font-weight": "blod"});
            return false;
        } else {
            $(this).siblings("span").html("");
        }
        //判断文件格式
        var file = $("#pic-file").val();
        console.log(file);
        if (file != "") {
            console.log(file.toLowerCase());
            var exec = (/[.]/.exec(file)) ? /[^.]+$/.exec(file.toLowerCase()) : '';
            console.log(exec);
            if (exec == "jpg" || exec == "png") {
                $("input#pic-file").siblings("span").html("");
            }else {
                $("input#pic-file").siblings("span").html("文件格式不对，请上传jpg或者png文件!").css(
                    {"color": "red", "font-size": "12px", "font-weight": "blod"});
                return false;
            }
        }

        $("form#register-action").attr("action",$(this).attr("register_uri"));
        return true;
    });
    //注册信息返回的时候，来到注册表单
    if ($(".register-txt").html().length == 0) {
        loginShow();
    } else {
        registerShow();
    }

    function loginShow() {
        $(".loginBtn").addClass("current");
        $(".registerForm").addClass("hide");
        $(".registerBtn").removeClass("current");
        $(".loginForm").removeClass("hide");
    }

    function registerShow() {
        $(".registerBtn").addClass("current");
        $(".loginForm").addClass("hide");
        $(".loginBtn").removeClass("current");
        $(".registerForm").removeClass("hide");
    }

});

