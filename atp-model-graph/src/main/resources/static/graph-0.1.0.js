var j_board = {};	//js画布对象
var s_board= {};	//svg画布对象

$(document).ready(function(){
    //=======================================================svg代码==================================================================//
    //see https://svgjs.com
    var j_board = $('#g-board');
    var s_board = SVG('g-board').size(3000, 1000);	
                
    $('div[draggable="true"]').draggable({
        helper:'clone',
        cursor: 'crosshair',				
        drag:function(event, ui){					
            var leftX = $('.g-left-panel').width();
            var topY = $('.g-unit-list-header').height();					
            //console.debug('left:' + (ui.offset.left - leftX) + ',top:' + (ui.offset.top - topY));
        },
        stop:function(event, ui){
            if(inBoard($(this).left, $(this).top)){
                var leftX = $('.g-left-panel').width();
                var topY = $('.g-unit-list-header').height();
                //未加滚动条
                var x = ui.offset.left - leftX;						
                var y = ui.offset.top - topY;
                var group = s_board.group().attr('class', 'g-unit-group');
                group.attr('transform', 'translate(' + x + ' ' + y +')');
                group.rect(219, 32).addClass('algorithm g-unit-body');
                group.rect(31, 30).addClass('g-unit-icon-container g-unit-icon-container-algorithm').attr('x','1').attr('y','1');					
                group.text($(this).text()).addClass('g-unit-name').attr('x', '109.5').attr('y', '-2');
                group.circle(5).addClass("g-joint-point").attr('cx', '109.5').attr('cy', '32');
                group.draggable({x:x,y:y});						
                var $group = $('#' + group.attr('id'));
            }
        }
    });								
                
    //动态绑定事件
    j_board.on('click', 'g', function(event){
        var g = SVG.get($(this).attr('id'));
        //g.draggable({});
    })
    //删除元素
    j_board.on('keydown', 'g', function(event){				
        if(event.keyCode==46){
            SVG.get($(this).attr('id')).remove();
        }
    });
    //点画线
    j_board.on('mousedown', '.g-joint-point', function(event){	
        var $g = $(this).parent();
        var g = SVG.get($g.attr('id'));
        //图元固定,记录正在画图的元素
        g.fixed();
        var path = g.path();
        path.addClass('g-temp-connection').stroke({ dasharray:'4', color: '#3582f8', width: 1});
        //记录正在画线的图元和路径
        j_board.attr("g", g.attr('id'));
        j_board.attr("p", path.attr('id'));
    });

    //画布监控鼠标move事件
    j_board.on('mousemove', function(event){
        if(j_board.attr('p')!=null){
            var $g = $('#' + j_board.attr('g'));
            var path = SVG.get(j_board.attr('p'));
            var x = event.pageX - 550;
            var y = event.pageY - 150;
            path.attr('d','M' + 110 + ' ' + 33 + 'L' + x + ' ' + y + '');
        }
    });

    //画布监控鼠标up事件
    j_board.on('mouseup', function(event){
        if(j_board.attr('g')!=null && j_board.attr('p')!=null){
            var g = SVG.get(j_board.attr('g'));
            g.draggable();
            var p = SVG.get(j_board.attr('p'));
            //p.remove();

            j_board.removeAttr('g').removeAttr('p');
        }
    });
    
    //==============================================设计器、代码视图============================================================//
    $(".textarea").initTextarea();
    $('.g-code-view').hide();
    $('#btnCode').click(function(){
        $('.g-board-container').toggle();
        $('.g-code-view').toggle();
        if($('#btnCode').text()=='切换为代码视图'){
            $('#btnCode').text('切换为设计视图')
        }else{
            $('#btnCode').text('切换为代码视图')
        }				
    });
})	

//判断是否在设计区域内
function inBoard(x, y){
    return true;
}