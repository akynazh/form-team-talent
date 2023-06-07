
const baseUrl = "http://localhost:8080"
window.onload=load;
var actDesc=null;
var actEndDate=null;
var actId=null;
var actHolderId=null;
var actIsPublic=null;
var actName=null;
var actstatus=null;
var actQrcodePath=null;

function load(){
    actHolderId=localStorage.getItem('userID');
    //console.log(actHolderId)
    if(actHolderId==null){
        window.location.href='login.html'
    }
    const dateControl = document.querySelector('input[type="datetime-local"]');
   
    var date = new Date(); 
    Y = date.getFullYear() + '-';
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    D = (date.getDay()  < 10 ? '0'+(date.getDay()) : date.getDay())+ 'T';
    h = (date.getHours() < 10 ? '0'+(date.getHours()) : date.getHours())+ ':';
    m = (date.getMinutes()  < 10 ? '0'+(date.getMinutes()) : date.getMinutes())+ ':';
    s = (date.getSeconds() < 10 ? '0'+(date.getSeconds()) : date.getSeconds());
    var time=Y+M+D+h+m+s
    dateControl.value =time;
}

function submit(){

    actHolderId=localStorage.getItem('userID');
    console.log(actHolderId)
    actDesc=document.getElementById("actintro").value
    actEndDate=document.getElementById("acttime").value
    actIsPublic=document.getElementById("actpro").value//公开/私有
    actName=document.getElementById("acttitle").value
    
    if(actDesc=="请输入活动简介"){
        actDesc=null;
    }
      //console.log(actDesc)
    if(actName=="例如：思政小组展示"){
        actName=null;
    }
    if(actDesc==null||actName==null){
        alert("表单未正确填写")
    }else{
        if(actIsPublic=="公开"){
            actIsPublic=1;
        }else{
            actIsPublic=0;
        }
        //console.log(actIsPublic)
        actEndDate=new Date(actEndDate).getTime();
        console.log(actEndDate)
    
    
      
        var token = localStorage.getItem('token')
        console.log(token)
        axios({
            method: 'post',
            url: `${baseUrl}/api/activity/add`,
            data:{
                aDesc: actDesc,
                aEndDate: actEndDate,
                aHolderId: actHolderId,
                aId: actId,
                aIsPublic: actIsPublic,
                aName: actName,
                aQrcodePath: actQrcodePath,
                status: 1
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
  

    
}