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
                    $('#hidden_username').val(res_data.username);
                    $('#username_txt').html(res_data.username);
                }
            }
        }
    });

    get_fav_list();

    $('#orderby').on('change', function(){
        get_fav_list();
    });

    function get_fav_list(){
        let orderby = $('#orderby').val();
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8081/hw4/FavoritesServlet',
            data : {
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
                    let htmlstr = '';
                    $.each(res_data, function(index, data){
                        let tempstr = '<div class="result-line" >\n' +
                            '            <div class="result-left">\n' +
                            '                <image src="'+data.image_url+'"></image>\n' +
                            '            </div>\n' +
                            '            <div class="result-right">\n' +
                            '                <div class="result-right-line1" key="'+data.name+'">\n' +
                            '                    <a href="#">'+data.name+'</a>\n' +
                            '                </div>\n' +
                            '                <div class="result-right-line2">\n' +
                            '                    <span>'+data.address+'</span>\n' +
                            '                </div>\n' +
                            '                <div class="result-right-line3">\n' +
                            '                    <a href="'+data.url+'">'+data.url+'</a>\n'+
                            '                </div>\n' +
                            '            </div>\n' +
                            '        </div>';
                        htmlstr += tempstr;
                    });
                    $('#fav_box').html(htmlstr);
                    $(".result-right-line1").bind('click', function(){
                        let rest_name=$(this).attr('key');
                        let detail_url = 'detail.html?rest_name='+encodeURIComponent(rest_name);
                        window.location.href=detail_url;
                    });
                }
            }
        });
    }


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
            alert('restaurant name is missing');
            return;
        }
        if(!latitude || !longitude){
            alert('latitude or longitude is missing');
            return;
        }
        let url = 'search.html?rest_name='+encodeURIComponent(rest_name)+'&latitude='+encodeURIComponent(latitude)+'&longitude='+encodeURIComponent(longitude)+'&orderby='+encodeURIComponent(orderby);
        window.location.href=url;
    });

    $("#logout").bind('click',function(){
        if(confirm('Logout now?')){
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