$('#search-btn').click(function () {
    doSearch();
});
$('#reset-btn').click(function () {
    $('#search-form')[0].reset();
});

var rowIds = '';
$(function () {
    // doSearch();
});

function dataLoader(param, success, error) {
    var url = "/taskHost/hosts?authKey="+securityKey;
    $.ajax({
        url: url,
        data: param,
        method: 'get',
        dataType: "json",
        success: function (data) {
            success(data);
        },
        error: function (e) {
            error(e);
        }
    })
}

function doSearch() {
    console.log('-----doSearch--');
    var url = "/taskHost/hosts";
    var jsonParam = $('#search-form').serializeJson();
    $('#dg').datagrid({headers: app.headers, url: url, queryParams: jsonParam});
}


function doSearchReload() {
    var jsonParam = $('#search-form').serializeJson();
    $('#dg').datagrid('reload', jsonParam);
}

function onDblClick(rowIndex, rowData) {
    //alert('---onDblClick--rowIndex='+rowIndex+' row.id='+rowData.id);
    onEdit();
}

function onClick(rowIndex, rowData) {

}

function listenerName(ex) {
    if (ex.keyCode == 13) {
        doSearch();
    }
}

$('#query_code').keydown(listenerName);
$('#query_type').keydown(listenerName);

var url;

function onAdd() {
    $('#dlg').dialog('open').dialog('setTitle', 'New 运行信息');
    $('#fm').form('clear');
    //$("#dflRole_remark").val('test');
}

function onEdit() {
    var row = $('#dg').datagrid('getSelected');
    if (row) {
        var entityName = 'taskHost';
        $('#fm').form('clear');
        $('#dlg').dialog('open').dialog('setTitle', 'Edit 运行信息');
        $('#fm').form('load', row);
    }

}

function onSave() {
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        $.messager.show({
            title: 'Error',
            msg: '请选中行'
        });
        return;
    }

    var url = '/taskHost/save?authKey='+securityKey;
    var jsonParam = $('#fm').serializeJson();
    if ($('#fm').form('validate')) {
        $.ajax({
            method: 'post',
            url: url,
            data: jsonParam,
            headers: app.headers,
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    $('#dlg').dialog('close');        // close the dialog
                    $.messager.show({
                        title: '系统消息',
                        msg: result.errorMsg
                    });
                    doSearchReload();    // reload the user data
                } else {
                    $.messager.show({
                        title: '错误消息:' + result.resultCode,
                        msg: result.errorMsg
                    });
                }
            },
            error: function (d, e) {
                $.messager.show({
                    title: e,
                    msg: d.responseJSON.errorMsg,
                    timeout: 3000,
                    showType: 'slide'
                });
            }
        });
    }
}

function onDestroy() {
    var row = $('#dg').datagrid('getSelected');
    if (row) {
        $.messager.confirm('删除确认', '你确定删除此记录?', function (r) {
            if (r) {
                $.ajax({
                    type: 'post',
                    data: {code: row.code},
                    headers: app.headers,
                    dataType: 'json',
                    url: '/taskHost/delete?authKey='+securityKey,
                    success: function (data, textStatus, jqXHR) {
                        if (data.success) {
                            $.messager.show({    // show error message
                                title: '系统消息',
                                msg: '删除成功'
                            });
                            doSearchReload();    // reload the user data
                        } else {
                            $.messager.show({    // show error message
                                title: 'Error',
                                msg: data.errorMsg
                            });
                        }
                    },
                    error(d, e) {
                        $.messager.show({    // show error message
                            title: 'Error',
                            msg: d.responseJSON.errorMsg
                        });
                    }
                });
            }
        });
    } else {
        $.messager.show({
            title: '系统提示',
            msg: '请先选择要删除的记录'
        });
    }
}
