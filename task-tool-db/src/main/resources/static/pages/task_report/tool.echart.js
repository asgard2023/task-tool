function getDataTypes(data, fieldName){
    var types='';
    for(var d in data){
        var value=data[d][fieldName];
        if(types.indexOf(value+',')<0){
            types+=value+',';
        }
    }
    if(types.indexOf(',')>0){
        types = types.substring(0, types.length-1);
    }

    return types.split(',');
}


function echartBarShow(data, title, typeCodeField, typeValueField){
    var dataTypes=getDataTypes(data.rows, typeCodeField);
    var dataValues=getDataTypes(data.rows, typeValueField);
    var option = {
        title: {
            text: title
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                data: dataTypes,
                axisTick: {
                    alignWithLabel: true
                }
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: 'Direct',
                type: 'bar',
                barWidth: '60%',
                data: dataValues
            }
        ]
    };

    option && myChart.setOption(option);
}