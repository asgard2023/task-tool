$('#search-btn').click(function () {
    doSearch();
});
$('#reset-btn').click(function () {
    $('#search-form')[0].reset();
});

$(function () {
    //单位下拉框
    $("#query_timeType").combobox ({
        editable : false,
        url : "/taskInfo/config?type=timeTypes&authKey="+securityKey,//url
        valueField : "code", //相当于 option 中的 value 发送到后台的
        textField : "name"	//option中间的内容 显示给用户看的
    });

    var beforeDay = 7;
    initStartEndTime(beforeDay)
    doSearch();
});

function doSearch() {
    var url = "/task/taMethodCount/list2";
    var jsonParam = $('#search-form').serializeJson();
    $('#dg').datagrid({headers: app.headers, url: url, queryParams: jsonParam})
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
$('#query_name').keydown(listenerName);

var url;

function onAdd() {
    $('#dlg').dialog('open').dialog('setTitle', 'New TaMethodCount');
    $('#fm').form('clear');
    $('#taMethodCount_status').combobox('select', '1');
    //$("#taMethodCount_remark").val('test');
}

function getRowData(entityName, row) {
    var tmp;
    var obj = {}
    obj[entityName] = {};
    for (i in row) {
        tmp = row[i];
        obj[i] = tmp;
    }
    return obj;
}

function onEdit() {
    var row = $('#dg').datagrid('getSelected');
    if (row) {
        var entityName = 'taMethodCount';
        $('#fm').form('clear');
        var obj = getRowData(entityName, row);
        $('#dlg').dialog('open').dialog('setTitle', 'Edit TaMethodCount');
        $('#fm').form('load', obj);
    }

}

function onSave() {
    var row = $('#dg').datagrid('getSelected');
    if (row) {
        if (row.canModify == 0) {
            $.messager.show({
                title: 'Error',
                msg: '不可修改，禁止操作'
            });
            return;
        }
    }

    var url = '/task/taMethodCount/save';
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
                    data: {id: row.id},
                    headers: app.headers,
                    dataType: 'json',
                    url: '/task/taMethodCount/delete',
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

function sourceCount(){
    var row = $('#dg').datagrid('getSelected');
    if (!row) {
        if (row.canModify == 0) {
            $.messager.show({
                title: 'Warn',
                msg: '请选中行'
            });
            return;
        }
    }
    var url='taMethodCountSourceDetail.html?methodCountId='+row.id+'&startTime='+$('#query_startTime').val();
    //window.open('taMethodCountSource.html?methodCountId='+row.id);
    var title=row.dataMethod.code+'-'+row.timeType+'-'+row.timeValue;
    $('#dlgSource').dialog('open').dialog('setTitle', title);
    $('#iframeSource').attr('src', url);
}