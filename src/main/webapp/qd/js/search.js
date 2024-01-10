$(document).ready(function(){
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8081/hw4/LoginServlet',
        data : {
            type: 'logstatus',
        },
        success: function(res){
            console.log(res);
            //let res_json = JSON.parse(res);
            if(res.code != 0){
                alert(res.errmsg);
            }
            else{
                //success
                if(res.data == 'nologin'){
                    //alert('nologin');
                    $('#nav-nologin-box').show();
                    $('#nav-login-box').hide();
                }
                else{
                    let res_data = JSON.parse(res.data);
                    $('#nav-nologin-box').hide();
                    $('#nav-login-box').show();
                    console.log(res_data);
                }
            }
        }
    });
    let sp = new URLSearchParams((window.location.search));
    let rest_name = sp.get('rest_name');
    let latitude = sp.get('latitude');
    let longitude = sp.get('longitude');
    let orderby = sp.get('orderby');
    $('#rest_name').val(rest_name);
    $('#latitude').val(latitude);
    $('#longitude').val(longitude);
    $("input:radio[value='"+orderby+"']").prop('checked', true);
    //$('#rest_name_txt').html(rest_name);

    $.ajax({
        type: 'GET',
        url: 'http://localhost:8081/hw4/RestSearch',
        data : {
            name: rest_name,
            latitude: latitude,
            longitude: longitude,
            orderby: orderby,
        },
        success: function(res){
            console.log(res);
            //let res_json = JSON.parse(res);
            if(res.code != 0){
                alert(res.errmsg);
            }
            else{
                //success
                let res_data = JSON.parse(res.data);
                console.log(res_data);
                let htmlstr = 'Result for "<span id="rest_name_txt">'+rest_name+'</span>"\n' +
                    '        <hr />';
                $.each(res_data, function(index, data){
					let detail_url = 'detail.html?rest_name='+encodeURIComponent(data.name);
                    let tempstr = '<div class="result-line" >\n' +
                        '            <div class="result-left">\n' +
                        '               <a href="'+detail_url+'"> <image src="'+data.image_url+'"></image></a>\n' +
                        '            </div>\n' +
                        '            <div class="result-right">\n' +
                        '                <div class="result-right-line1" key="'+data.name+'">\n' +
                        '                    <a href="#">'+data.name+'</a>\n' +
                        '                </div>\n' +
                        '                <div class="result-right-line2">\n' +
                        '                    <span>'+data.address+'</span>\n' +
                        '                </div>\n' +
                        '                <div class="result-right-line3">\n' +
                        '                    <a href="'+data.url+'">'+data.url+'</a>\n' +
                        '                </div>\n' +
                        '            </div>\n' +
                        '        </div>';
                    htmlstr += tempstr;
                });
                $('#result-box').html(htmlstr);
                $(".result-right-line1").bind('click', function(){
                    let rest_name=$(this).attr('key');
                    let detail_url = 'detail.html?rest_name='+encodeURIComponent(rest_name);
                    window.location.href=detail_url;
                });
            }
        }
    });


    $("#search").bind('click',function(){

        let orderby = $('input[name="orderby"]:checked').val();
        console.log(orderby);
        let rest_name = $('#rest_name').val();
        let latitude = $('#latitude').val();
        let longitude = $('#longitude').val();
        if(!orderby){
            alert('please choose orderby');
            return;
        }
        if(!rest_name || !latitude || !longitude){
            alert('param is missing');
            return;
        }
        let url = 'search.html?rest_name='+encodeURIComponent(rest_name)+'&latitude='+encodeURIComponent(latitude)+'&longitude='+encodeURIComponent(longitude)+'&orderby='+encodeURIComponent(orderby);
        window.location.href=url;
    });

    $("#logout").bind('click',function(){
        if(confirm('are you sure logout')){
            $.ajax({
                // xhrFields: {
                //     withCredentials: true
                // },
                // crossDomain: true,
                type: 'GET',
                url: 'http://localhost:8081/hw4/LoginServlet',
                data : {
                    type: 'logout',
                },
                success: function(res){
                    console.log(res);
                    //let res_json = JSON.parse(res);
                    if(res.code != 0){
                        alert(res.errmsg);
                    }
                    else{
                        //success
                        //alert("bye");
                        //let res_data = JSON.parse(res.data);
                        //console.log(res_data);

                        window.location.href='index.html';
                    }
                }
            });
        }

    });
});