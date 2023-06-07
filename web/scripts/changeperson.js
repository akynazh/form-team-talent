
const baseUrl = "http://localhost:8080"
window.onload = auth
var myinfo=null;

function auth() {
   
    var token = localStorage.getItem('token')
    console.log(token)
   
    axios({
        method: 'get',
        url: `${baseUrl}/api/user/get/info`,
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        console.log(response.data.obj);
        myinfo=response.data.obj
        
        if(myinfo==null){
            window.location.href='login.html'
        }
        document.getElementById("userid").value=myinfo.uId;

        //myinfo.uStuNum=123
        if(myinfo.uMajor!=null){
            document.getElementById("usermajorchange").value=myinfo.uMajor;
        }
        if(myinfo.uName!=null){
            document.getElementById("usernamechange").value=myinfo.uName;
        }
        if(myinfo.uSchool!=null){
            document.getElementById("userschoolchange").value=myinfo.uSchool;
        }
        if(myinfo.uSex!=null){
            document.getElementById("usersexchange").value=myinfo.uSex;
        }
        if(myinfo.uStuNum!=null){
            document.getElementById("userstunumchange").value=myinfo.uStuNum;
        }

      })
      .catch(function (error) {
        console.log(error);
      });
}

function submit(){
    var uuMajor=document.getElementById("usermajorchange").value
    var uuName=document.getElementById("usernamechange").value
    var uuSchool=document.getElementById("userschoolchange").value
    var uuSex=document.getElementById("usersexchange").value
    var uuStuNum=document.getElementById("userstunumchange").value

    if(uuMajor=="请输入专业"){
        uuMajor=null;
    }
    if(uuName=="请输入姓名"){
        uuName=null;
    }
    if(uuSchool=="请输入学校"){
        uuSchool=null;
    }
    if(uuSex=="请输入性别（男/女/其他）"){
        uuSex=null;
    }
    if(uuStuNum=="请输入学号"){
        uuStuNum=null;
    }
    //console.log(uuName)


    var token = localStorage.getItem('token')
    console.log(token)
    axios({
        method: 'post',
        url: `${baseUrl}/api/user/update`,
        data:{
            uId: myinfo.uId,
            uMajor: uuMajor,
            uName: uuName,
            uPwd: myinfo.uPwd,
            uSchool: uuSchool,
            uSex: uuSex,
            uStuNum: uuStuNum
        },
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        console.log(response.data);
        window.location.href='person.html'
        

      })
      .catch(function (error) {
        console.log(error);
      });

    
}