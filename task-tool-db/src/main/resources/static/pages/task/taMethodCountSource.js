$('#search-btn').click(function () {
    doSearch();
});
$('#reset-btn').click(function () {
    $('#search-form')[0].reset();
});

$(function () {
    var beforeDay = 7;
    initStartEndTime(beforeDay);
    var methodCountId=app.getQueryString('methodCountId');
    if(methodCountId){
        $('#query_methodCountId').val(methodCountId);
    }
    var dataMethodId=app.getQueryString('dataMethodId');
    if(dataMethodId){
        $('#query_dataMethodId').val(dataMethodId);
    }
    var timeType=app.getQueryString('timeType');
    if(timeType){
        $('#query_timeType').val(timeType);
    }
    var startTime=app.getQueryString('startTime');
    if(timeType){
        $('#query_startTime').val(startTime);
    }
    var endTime=app.getQueryString('endTime');
    if(timeType){
        $('#query_endTime').val(endTime);
    }
    doSearch();
});

function doSearch() {
    var url = "/task/taMethodCountSource/list2";
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
    $('#dlg').dialog('open').dialog('setTitle', 'New taMethodCountSource');
    $('#fm').form('clear');
    $('#taMethodCountSource_status').combobox('select', '1');
    //$("#taMethodCountSource_remark").val('test');
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
        var entityName = 'taMethodCountSource';
        $('#fm').form('clear');
        var obj = getRowData(entityName, row);
        $('#dlg').dialog('open').dialog('setTitle', 'Edit taMethodCountSource');
        $('#fm').form('load', obj);
    }

}

function onSave() {
    var row = $('#dg').datagrid('getSelected');
    if (row) {
        if (row.canModify == 0) {
            $.messager.show({
                title: 'Error',
                msg: '???????????????????????????'
            });
            return;
        }
    }

    var url = '/task/taMethodCountSource/save';
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
                        title: '????????????',
                        msg: result.errorMsg
                    });
                    doSearchReload();    // reload the user data
                } else {
                    $.messager.show({
                        title: '????????????:' + result.resultCode,
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
        $.messager.confirm('????????????', '?????????????????????????', function (r) {
            if (r) {
                $.ajax({
                    type: 'post',
                    data: {id: row.id},
                    headers: app.headers,
                    dataType: 'json',
                    url: '/task/taMethodCountSource/delete',
                    success: function (data, textStatus, jqXHR) {
                        if (data.success) {
                            $.messager.show({    // show error message
                                title: '????????????',
                                msg: '????????????'
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
            title: '????????????',
            msg: '??????????????????????????????'
        });
    }
}