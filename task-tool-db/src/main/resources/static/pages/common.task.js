var securityKey = 'tasktooltest';

$(function () {
    if(localStorage.authKey){
        securityKey=localStorage.authKey;
        console.log('securityKey='+securityKey);
    }
});

/**
 * 支持的翻译语言
 * @returns {*}
 */
function getCountTimeTypes() {
    var datas = [];
    $.ajax({
        url: '/taskInfo/config?authKey=' + securityKey,
        type: 'GET',
        async: false,
        cache: false,
        success: function (res) {
            if (res.errorMsg) {
                $.messager.show({
                    title: 'Error',
                    msg: res.auth
                });
                return;
            }
            datas = res.counterTimeTypes;
        },
        error: function (returndata) {
            alert(JSON.stringify(returndata));
        }
    });
    return datas;
}