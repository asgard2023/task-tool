var submitflage = false;
function initCookies() {
    var cookie = document.cookie,
        items = cookie.split(";"),
        keys = {};
    items.forEach(function(item) {
        var kv = item.split('=');
        keys[$.trim(kv[0])] = $.trim(kv[1]);
    });
    return keys;
}

function encrypt_login(f) {
    console.log('----encrypt_login--')
    // 获取
    var url= '/frequencyLogin/rsaKey?funcCode=login';
    $.ajax({
        type:"get",
        url:url,
        success:function(data){
            console.log(data);
            // console.log(encrypedPwd);
            f(data.data);
        },
        error: function(result, status, xhr) {
            submitflage = false;
        }
    });
}

function login_req(data) {
    console.log('-----login_req--' + data)
    var user = $("#loginForm").serializeJSON();
    var logindata = {
        grant_type: 'password',
        authKey: user.authKey,
        clientId: app.clientId,
    };


    if (data) {//走加密方式
        var pwdKey = new RSAUtils.getKeyPair(data.exponent, "", data.modulus);
        logindata.authKey = RSAUtils.encryptedStringOfReversed(pwdKey, logindata.authKey);
        logindata.clientIdRsa = data.clientIdRsa;
    }

    sessionStorage.access_token='';
    $.ajax({
        type: 'post',
        url: '/systemInfo/checkKey',
        data: logindata,
        success: function (res) {
            console.log(res);
            res = JSON.parse(res);
            if (res.success) { // 登录成功
                submitflage = false;
                var tokenExpireTime=res.tokenExpireTime;
                var cookieTime = tokenExpireTime * 1000;
                sessionStorage.time = new Date().getTime();

                appUtil.setCookie("authKey", res.authKey);  // 把登录token放在cookie里面
                localStorage.authKey = user.authKey;  // 把登录用户名放在cookie里面
                setTimeout(function () {
                    // console.log(logindata,'logindata' )
                    window.location = '/index.html';
                    // _self.$router.push('');
                }, 300);
            } else {
                submitflage = false;
                showToastmessage('error', res.errorMsg);
            }

        },
        error: function (res, msg) {
            submitflage=false;
            var error=res.responseJSON;
            showToastmessage(error.error, error.errorMsg);
        }
    });
}

function loginSubmit() {
    if (submitflage) {
        console.log('-----submitflage---=' + submitflage)
        return;
    }
    submitflage = true;
    var user = $("#loginForm").serializeJSON();

    if(!user.authKey||user.authKey.length<4){
        showToastmessage('error', '账号长度不够');
        submitflage=false;
        return;
    }


    var isEncrypt=false;//是否加密账号及密码
    if(isEncrypt){
        encrypt_login(login_req);
    }
    else{
        login_req();
    }
}

$(function () {
    if (localStorage.authKey) {
        $("input[name='authKey']").val(localStorage.authKey);
    }
    $('#loginBtn').click(loginSubmit);
})