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

    $("#signin").bind('click',function(){
        let username = $('#login_username').val();
        let password = $('#login_password').val();
        $.ajax({
            // xhrFields: {
            //     withCredentials: true
            // },
            // crossDomain: true,
            type: 'GET',
            url: 'http://localhost:8081/hw4/LoginServlet',
            data : {
                type: 'login',
                username: username,
                password: password,
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
                    alert("welcome,"+res_data.email);
                    window.location.href='home.html';
                }
            }
        });
    });

    $("#signup").bind('click',function(){
        let email = $('#signup_email').val();
        let username = $('#signup_username').val();
        let password = $('#signup_password').val();
        let password2 = $('#signup_password2').val();
        let agree = $('#agree').prop('checked');
        //console.log(agree);
        if(!agree){
            alert('Terms and Conditions not checked');
            return;
        }
        if(password != password2){
            alert('Mismatching passwords');
            return
        }
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8081/hw4/SignUpServlet',
            data : {
                email: email,
                username: username,
                password: password,
            },
            success: function(res){
                console.log(res);
                //let res_json = JSON.parse(res);
                if(res.code != 0){
                    alert(res.errmsg);
                }
                else{ 
                    alert("Successfully Signed up");
                    //window.location.href='index.html';
                }
            }
        });
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