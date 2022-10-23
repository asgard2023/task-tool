$('#search-btn').click(function () {
    doSearch();
});
$('#reset-btn').click(function () {
    $('#search-form')[0].reset();
});

$(function () {
    var beforeDay = 7;
    initStartEndTime(beforeDay)
    doSearch();
});

function doSearch() {
    var url = "/task/taMethodCount/methodCountStatistic";
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
    var url='taMethodCountSourceDetail.html?dataMethodId='+row.dataMethodId+'&timeType='+row.timeType+'&startTime='+$('#query_startTime').val();
    //window.open('taMethodCountSource.html?methodCountId='+row.id);
    var title=row.dataMethod.code+'-'+row.timeType;
    $('#dlgSource').dialog('open').dialog('setTitle', title);
    $('#iframeSource').attr('src', url);
}