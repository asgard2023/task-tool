var isSupportLocalStorage = false;

if (window.localStorage) {
    isSupportLocalStorage = true;
} else {
    console.warn('This browser does NOT support localStorage');
}

var isSupportSessionStorage = false;
if (window.sessionStorage) {
    isSupportSessionStorage = true;
} else {
    console.warn('This browser does NOT support sessionStorage');
}

function getToken() {
    var token = sessionStorage.access_token;
    // if (!token) {
    //     try{
    //         showToastmessage('error', '请先登入');
    //     }
    //     catch (e){
    //         console.log('----getToken--'+e);
    //     }
    //     return;
    // }
    return token;
}

var header_source = 1;
var header_sysType = 'pc';
var app = {
    isSupportLocalStorage: isSupportLocalStorage,
    isSupportSessionStorage: isSupportSessionStorage,
    clientId: '',
    source: header_source,
    sysType: header_sysType,
    authorToken: function () {
        return getToken();
    },
    paramToQuery: function (param, key, encode) {
        if (param == null) return '';
        var paramStr = '';
        var t = typeof (param);
        if (t == 'string' || t == 'number' || t == 'boolean') {
            paramStr += '&' + key + '=' + ((encode == null || encode) ? encodeURIComponent(param) : param);
        } else {
            for (var i in param) {
                var k = key == null ? i : key + (param instanceof Array ? '[' + i + ']' : '.' + i);
                paramStr += this.paramToQuery(param[i], k, encode);
            }
        }
        return paramStr;
    },
    getQueryString: function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null)
            return unescape(r[2]);
        return null;
    },
    getQueryObject: function () {
        let obj = {};
        // 获取url的参数
        let search = location.search
        // 干掉参数中的?
        let searchStr = search.slice(1);
        // 通过 & 符号将 字符串分割成 数组
        let searchStrArr = searchStr.split("&")
        // 遍历数组
        searchStrArr.forEach(function (v) {
            // 通过=将字符串分割成数组
            let vArr = v.split("=")
            console.log(vArr[0], "key");
            obj[vArr[0]] = vArr[1]
        });
        if (obj.startTime) {
            obj.startTime = decodeURIComponent(obj.startTime);
        }
        if (obj.endTime) {
            obj.endTime = decodeURIComponent(obj.endTime);
        }
        console.log(obj);
        return obj;
    },
    headers: {'source': header_source, 'sysType': header_sysType, 'authorization': getToken()},
    typeItemApi: '/dflBasedata/dflTypeItem/typeItems',
    getTypeItems: function (codes, lang) {
        if (!lang) {
            lang = "zh";
        }
        var url = "/dflBasedata/dflTypeItem/typeItems?codes=" + codes + "&lang=" + lang;
        var result = {};
        $.ajax({
            url: url,
            type: "get",
            async: false,
            dataType: "text",
            error: function (e) {
                alert("Error " + e);
            },
            success: function (text) {
                eval("var resp=" + text);
                console.log(resp);
                result = resp;
            }
        });
        return result;
    }
}

var appUtil =
    {
// 设置cookie
    setCookie: function (name, value, expire, path) {
        var curdate = new Date();
        var cookie = name + '=' + encodeURIComponent(value) + '; ';
        if (expire != undefined || expire == 0) {
            if (expire == -1) {
                expire = 366 * 86400 * 1000;// 保存一年
            } else {
                expire = parseInt(expire);
            }
            curdate.setTime(curdate.getTime() + expire);
            cookie += 'expires=' + curdate.toUTCString() + '; ';
        }
        path = path || '/';
        cookie += 'path=' + path;
        document.cookie = cookie;
    },

    // 获取cookie
    getCookie: function (name) {
        var re = '(?:; )?' + encodeURIComponent(name) + '=([^;]*);?';
        re = new RegExp(re);
        if (re.test(document.cookie)) {
            return decodeURIComponent(RegExp.$1);
        }
        return '';
    },
    deleteCookie: function (name) {
        this.setCookie(name, '');
    },
    //清除所有cookie函数
    clearAllCookie: function () {
        console.log('清除cookie');
        this.setCookie("access_token", '');  // 登录token
        this.setCookie("username", '');  //  登录用户名
        var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
        if (keys) {
            for (var i = keys.length; i--;)
                document.cookie = keys[i] + '=0;expires=' + new Date(0).toUTCString()
        }
    },
    getName: function () {
        return this.name;
    }
}

var Common = {
    //格式化日期,
    formatDate: function (date, format) {
        var paddNum = function (num) {
            num += "";
            return num.replace(/^(\d)$/, "0$1");
        }
        //指定格式字符
        var cfg = {
            yyyy: date.getFullYear() //年 : 4位
            , yy: date.getFullYear().toString().substring(2)//年 : 2位
            , M: date.getMonth() + 1  //月 : 如果1位的时候不补0
            , MM: paddNum(date.getMonth() + 1) //月 : 如果1位的时候补0
            , d: date.getDate()   //日 : 如果1位的时候不补0
            , dd: paddNum(date.getDate())//日 : 如果1位的时候补0
            , h: date.getHours()
            , hh: paddNum(date.getHours())  //时
            , m: date.getMinutes() //分
            , mm: paddNum(date.getMinutes()) //分
            , s: date.getSeconds() //秒
            , ss: paddNum(date.getSeconds()) //秒
        }
        format || (format = "yyyy-MM-dd hh:mm:ss");
        return format.replace(/([a-z])(\1)*/ig, function (m) {
            return cfg[m];
        });
    },
    dateParser: function (s) {
        var date = new Date(s);
        var year = date.getFullYear().toString();
        var month = (date.getMonth() + 1);
        var day = date.getDate().toString();
        var hour = date.getHours().toString();
        var minutes = date.getMinutes().toString();
        var seconds = date.getSeconds().toString();
        if (month < 10) {
            month = "0" + month;
        }
        if (day < 10) {
            day = "0" + day;
        }
        if (hour < 10) {
            hour = "0" + hour;
        }
        if (minutes < 10) {
            minutes = "0" + minutes;
        }
        if (seconds < 10) {
            seconds = "0" + seconds;
        }
        return year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds;
    },

    //EasyUI用DataGrid用日期格式化
    TimeFormatter: function (value, rec, index) {
        if (value == undefined) {
            return "";
        }
        var formatStr = "hh:mm:ss";
        return Common.formatDate(new Date(value), formatStr);
    },
    DateTimeFormatter: function (value, rec, index) {
        if (value == undefined) {
            return "";
        }
        var formatStr = "yyyy-MM-dd hh:mm:ss";
        return Common.formatDate(new Date(value), formatStr);
    },

    //EasyUI用DataGrid用日期格式化
    DateFormatter: function (value, rec, index) {
        if (value == undefined) {
            return "";
        }
        var formatStr = "yyyy-MM-dd";
        return Common.formatDate(new Date(value), formatStr);
    },
    TitleFormatter: function (value, rec, index) {
        if (value.length > 10) value = value.substr(0, 8) + "...";
        return value;
    },
    LongTitleFormatter: function (value, rec, index) {
        if (value.length > 15) value = value.substr(0, 12) + "...";
        return value;
    }
};

function initQueryDate() {
    var recentlyDate = new Date(new Date().getTime() - 7 * 24 * 3600 * 1000);//7天内
    $('#query_startTime').datetimebox('setValue', Common.DateFormatter(recentlyDate));

}

$(function () {
    $.ajaxSetup({
        headers: {
            'authorization': app.authorToken(),
            'lang': 'CN'
        }
    });
});


function loadError(r, e) {
    $.messager.show({
        title: 'Error:' + r.responseJSON.resultCode,
        msg: r.responseJSON.errorMsg
    });
}


function showToastmessage(title, msg) {
    $.messager.show({
        title: title,
        msg: msg,
        timeout: 3000,
        showType: 'slide'
    });
}


/**
 * easyui支持serializeJson
 */
if (typeof jQuery != 'undefined') {
    (function () {
        if (!window.WebUtil)
            WebUtil = {};
    })();
    WebUtil.form = {
        serializeJson: function (frm) {
            var json = {};
            frm = frm || $('body');
            if (!frm) {
                return json;
            }
            var inputs = frm.find('input[type!=button][type!=reset][type!=submit][type!=image],select,textarea');
            if (!inputs) {
                return json;
            }
            for (var index = 0; index < inputs.length; index++) {
                var input = $(inputs[index]);
                var name = input.attr('name');
                var value = input.val();
                if (name != null && $.trim(name) != '' && value != null && $.trim(value) != '') {
                    json[name] = value;
                }
            }
            return json;
        }
    };
    (function ($) {
        $.fn.serializeJson = function () {
            return WebUtil.form.serializeJson($(this));
        };
    }(jQuery));
}

function initStartEndTime(beforeDay) {
    var recentlyDate = new Date(new Date().getTime() - beforeDay * 24 * 3600 * 1000);//7天内
    // $("#query_startTime").val(Common.DateFormatter(recentlyDate));
    console.log('----initStartEndTime-beforeDay=' + beforeDay)
    $("#query_startTime").datetimebox("setValue", Common.DateFormatter(recentlyDate));
}

function getDataGridIds(dgId) {
    var rows = $("#" + dgId).datagrid("getRows");
    var ids = '';
    for (var i = 0; i < rows.length; i++) {
        ids += rows[i].id + ',';
    }
    console.log(ids);
    return ids;
}