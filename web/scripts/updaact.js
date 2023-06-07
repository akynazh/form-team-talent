const baseUrl = "http://localhost:8080"
window.onload = auth
var actId=null;
var token=null;
var actHolderId=null;
var acttitle=null;
var actintro=null;
var acttime=null;
var actpro=null;

var actQrcodePath=null;

function auth(){
    actId=localStorage.getItem('activeIdnow')
    token = localStorage.getItem('token')
    console.log(actId)
    axios({
        method: 'get',
        url: `${baseUrl}/api/activity/get/id?aId=${actId}`,
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        console.log(response.data);
        var actdetail=response.data.obj
        if(actdetail==null){
            alert("活动异常")
            window.location.href='joinact.html'
        }
        console.log(actdetail.activity)
        document.getElementById("acttitle").value=actdetail.activity.aName
        document.getElementById("actintro").value=actdetail.activity.aDesc
        if(actdetail.activity.aIsPublic==1){
            actdetail.activity.aIsPublic="公开"
        }else{
            actdetail.activity.aIsPublic="私有"
        }
        document.getElementById("actpro").value=actdetail.activity.aIsPublic
        
        ttt=parseInt(actdetail.activity.aEndDate)
        //console.log(ttt)
        var date = new Date(ttt); 
        Y = date.getFullYear() + '-';
        M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        D = (date.getDay()  < 10 ? '0'+(date.getDay()) : date.getDay())+ 'T';
        h = (date.getHours() < 10 ? '0'+(date.getHours()) : date.getHours())+ ':';
        m = (date.getMinutes()  < 10 ? '0'+(date.getMinutes()) : date.getMinutes())+ ':';
        s = (date.getSeconds() < 10 ? '0'+(date.getSeconds()) : date.getSeconds());
        var time=Y+M+D+h+m+s
        document.getElementById("acttime").value=time;
    })
      .catch(function (error) {
        console.log(error);
      });
}


function submit(){
    actHolderId=localStorage.getItem('userID')
    acttitle= document.getElementById("acttitle").value;
    console.log(acttitle)
    actintro= document.getElementById("actintro").value;
    acttime=document.getElementById("acttime").value;
    acttime=new Date(acttime).getTime();
    console.log(acttime)
    actpro=document.getElementById("actpro").value;
    if(actpro=="公开"){
        actpro=1;
    }else{
        actpro=0;
    }
    console.log(token)
    axios({
        method: 'post',
        url: `${baseUrl}/api/activity/update`,
        data:{
            aDesc: actintro,
            aEndDate: acttime,
            aHolderId: actHolderId,
            aId: actId,
            aIsPublic: actpro,
            aName: acttitle,
            aQrcodePath: actQrcodePath,
            status: 1
        },
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        console.log(response.data);
        backk();

      })
      .catch(function (error) {
        console.log(error);
      });
}

function backk(){
    window.history.back(); 
    return false;
  }