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
                   	$('#bt4').hide();
					$('#bt1').hide();
					$('#bt2').hide();
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
    $('#hidden_rest_name').val(rest_name);
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8081/hw4/DetailsServelt',
        data : {
            name: rest_name,
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
                let rating_html = get_star_html(res_data.rating);
                if(!res_data.price){
					res_data.price= "";
				}
                let htmlstr = res_data.name+'\n' +
                    '        <hr />';
                let tempstr = '<div class="result-line" >\n' +
                    '            <div class="result-left">\n' +
                    '                <image src="'+res_data.image_url+'"></image>\n' +
                    '            </div>\n' +
                    '            <div class="result-right">\n' +
                    '                <div class="result-right-line" >\n' +
                    '                    <span>Address: '+res_data.address+'</span>\n' +
                    '                </div>\n' +
                    '                <div class="result-right-line">\n' +
                    '                    <span>Phone No:'+res_data.phone+'</span>\n' +
                    '                </div>\n' +
                    '                <div class="result-right-line">\n' +
                    '                    <span>Cuisine: '+res_data.cuisine+'</span>\n' +
                    '                </div>\n' +
                    '                <div class="result-right-line">\n' +
                    '                    <span>Price: '+res_data.price+'</span>\n' +
                    '                </div>\n' +
                    '                <div class="result-right-line">\n' +
                    '                    <span>Rating: '+rating_html+'</span>\n' +
                    '                </div>\n' +

                    '            </div>\n' +
                    '        </div>';
                htmlstr += tempstr;
                $('#result-box').html(htmlstr);
                $(".result-right-line1").bind('click', function(){
                    let rest_name=$(this).attr('key');
                    let detail_url = 'detail.html?rest_name='+encodeURIComponent(rest_name);
                    window.location.href=detail_url;
                });
                if(res_data.isfav==2){
					$('#bt1').hide();
					$('#bt4').show();
				}
				else if(res_data.isfav==1){
					$('#bt4').hide();
					$('#bt1').show();
				
				}
				else if(res_data.isfav==3){
					$('#bt4').hide();
					$('#bt1').hide();
					$('#bt2').hide();
				
				}
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

    $("#bt1").bind('click',function(){
        let rest_name = $('#hidden_rest_name').val();
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8081/hw4/FavoritesServlet',
            data : {
                name: rest_name,
                type: 1,
            },
            success: function(res){
                console.log(res);
                //let res_json = JSON.parse(res);
                if(res.code != 0){
                    alert(res.errmsg);
                }
                else{
                    //success
                    alert("fav success");
                    //$('#bt1').prop('disabled', true);
               
					//  $('#bt1').prop('disabled', true);
					$('#bt1').hide();
					$('#bt4').show();
				
			
                    //let res_data = JSON.parse(res.data);
                    //console.log(res_data);

                    //window.location.href='index.html';
                }
            }
        });

    });
      $("#bt4").bind('click',function(){
        let rest_name = $('#hidden_rest_name').val();
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8081/hw4/FavoritesServlet',
            data : {
                name: rest_name,
                type: 2,
            },
            success: function(res){
                console.log(res);
                //let res_json = JSON.parse(res);
                if(res.code != 0){
                    alert(res.errmsg);
                }
                else{
                    //success
                    alert("remove favorite success");
                    //$('#bt1').prop('disabled', true);
               
					//  $('#bt1').prop('disabled', true);
					$('#bt4').hide();
					$('#bt1').show();
				
			
                    //let res_data = JSON.parse(res.data);
                    //console.log(res_data);

                    //window.location.href='index.html';
                }
            }
        });

    });

    $("#bt2").bind('click',function(){
        if($('#rese_box').is(':hidden')){
            $('#rese_box').show();
        }
        else{
            $('#rese_box').hide();
        }
    });

    $("#bt3").bind('click',function(){
		 let rest_name = $('#hidden_rest_name').val();
        let rese_date = $('#rese_date').val();
        let rese_time = $('#rese_time').val();
        let rese_notes = $('#rese_notes').val();
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8081/hw4/ReservationsServlet',
            data : {
				name: rest_name,
                date: rese_date,
                time: rese_time,
                notes: rese_notes,
            },
            success: function(res){
                console.log(res);
                //let res_json = JSON.parse(res);
                if(res.code != 0){
                    alert(res.errmsg);
                }
                else{
                    //success
                    alert("success");
                    //$('#bt1').prop('disabled', true);
                    //let res_data = JSON.parse(res.data);
                    //console.log(res_data);

                    //window.location.href='index.html';
                }
            }
        });

    });

});

function get_star_html(rating){
    var acss = '<i class="fa fa-star"></i>';
    var bcss = '<i class="fa fa-star-half-o"></i>';
    var ccss = '<i class="fa fa-star-o"></i>';
    var data = rating;
    var aint = parseInt(data);
    var bfloat = parseFloat(data-aint)==0?0:1;
    var cint = 5 - aint -bfloat;
    var html = '';
    for(var i=0;i<aint;i++){
        html = html+acss;
    }
    for(var i=0;i<bfloat;i++){
        html = html+bcss;
    }
    for(var i=0;i<cint;i++){
        html = html+ccss;
    }
    return html;
}