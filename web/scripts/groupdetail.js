const baseUrl = "http://localhost:8080"
window.onload = auth
var teamId=null;
var token=null;
var member=null;
var teaminfo=null;
var LeaderName=null;
var userId=null;
var useName=null;

var owner=null;

function auth(){
 
    teamId=localStorage.getItem('teamIdnow')
    userId=localStorage.getItem('userID')
    token = localStorage.getItem('token')
    //console.log(token)
    axios({
        method: 'get',
        url: `${baseUrl}/api/team/get/id?tId=${teamId}`,
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        console.log(response.data);
        owner=response.data.obj.owner
        member=response.data.obj.members
        teaminfo=response.data.obj.team
        LeaderName=response.data.obj.teamLeaderName

        console.log(teaminfo)
      //赋值
      document.getElementById("teamtitle").innerHTML=teaminfo.tName
      document.getElementById("teamintro").innerHTML=teaminfo.tDesc
      document.getElementById("teamcount").innerHTML=teaminfo.tCount
      document.getElementById("teamtotal").innerHTML=teaminfo.tTotal

      var myid=localStorage.getItem('userID')
      var flag=false;
      var listunit = "";
      for(let i=0;i<member.length;i++){
        listunit+= '<div class="uinthoder"><div class="list">'+member[i].uName+'\ ' +'\ '+'\ '+"学号:"+member[i].uStuNum+'</div></div>';
        if(member[i].uId==myid){
          flag=true;
        }
      }
      document.getElementById("memlist").innerHTML= listunit;


        if(flag!=true){//不在组里 不能退出 
          document.getElementById("exit").style.display="none"
         
        }else{//在组里 不能加入
          document.getElementById("joing").style.display="none"
        }

        if(owner!=true){//不是创建者 不能修改和解散小组
          document.getElementById("upgroup").style.display="none"
          document.getElementById("delegroup").style.display="none"
        }else{//是创建者 不能退出小组
          document.getElementById("exit").style.display="none"
        }
        if(teaminfo.tCount>=teaminfo.tTotal){//人满不能加入
          document.getElementById("joing").style.display="none"
        }//登录其他账号验证（未验证）权限
    })
      .catch(function (error) {
        console.log(error);
      });





      axios({
        method: 'get',
        url: `${baseUrl}/api/user/get/info`,
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        console.log(response.data.obj);
        useName=response.data.obj.uName;
      })
      .catch(function (error) {
        console.log(error);
      });

     
}



function submitreq(){//入组申请
  var req=document.getElementById("requ").value
  
  var timestamp=Date.parse(new Date()); 
  console.log(timestamp)

  axios({
    method: 'post',
    url: `${baseUrl}/api/req/send`,
    data:{
        aId: teaminfo.aId,
        agree: 0,
        content: req,
        fromId: userId,
        id: null,
        sendDate: timestamp,
        status: 0,
        tId: teaminfo.tId,
        tName: teaminfo.tName,
        toId: teaminfo.tLeaderId,
        uName: useName
    },
    headers: {
        "auth": token,
    }
}).then(function (response) {
    console.log(response.data);
    hidePopup1();
    if(response.data.msg!="ok"){
      alert("发送请求失败")
    }else{
      alert("成功发送请求")
    }
  })
  .catch(function (error) {
    console.log(error);
  });

}


function upgggr(){
  localStorage.removeItem('teamIdnow');
  localStorage.setItem('teamIdnow',teamId);
  window.location.href='updagroup.html'
}


function showPopup1(){
    var overlay1 = document.getElementById("overlay1");
    overlay1.style.display = "block";
  }
  function hidePopup1(){
    var overlay1 = document.getElementById("overlay1");
    overlay1.style.display = "none";
  }

  function showPopup(){
    var overlay1 = document.getElementById("overlay");
    overlay1.style.display = "block";
  }
  function hidePopup(){
    var overlay = document.getElementById("overlay");
    overlay.style.display = "none";
  }

  
function deletegroup(){
  axios({
    method: 'post',
    url: `${baseUrl}/api/team/remove?tId=${teamId}`,
    headers: {
        "auth": token,
    }
}).then(function (response) {
    console.log(response.data);
    hidePopup();
    backk()
})
  .catch(function (error) {
    console.log(error);
  });
}

function deleteme(){//请求处理写完后再来验证
  axios({
    method: 'post',
    url: `${baseUrl}/api/user/leave?tId=${teamId}`,
    headers: {
        "auth": token,
    }
}).then(function (response) {
    console.log(response.data);
    window.location.href='groupdetail.html'
})
  .catch(function (error) {
    console.log(error);
  });
}
function backk(){
  window.history.back(); 
  return false;
}