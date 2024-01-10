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
    let sp = new URLSearchParams(window.location.search);
    let name = sp.get('name');
    console.log(name);



    $("#search").bind('click',function(){

        let orderby = $('input[name="orderby"]:checked').val();
        console.log(orderby);
        let rest_name = $('#rest_name').val();
        let latitude = $('#latitude').val();
        let longitude = $('#longitude').val();
        if(!orderby){
            alert('Please Choose Orderby');
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

                        window.location.href='home.html';
                    }
                }
            });
        }

    });

});
