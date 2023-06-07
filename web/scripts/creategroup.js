
const baseUrl = "http://localhost:8080"
window.onload=load;

var actId=null;
var teamCount=null;
var teamDesc=null;
var teamId=null;
var teamLeaderId=null;
var teamName=null;
var teamTotal=null;


function load(){
    teamLeaderId=localStorage.getItem('userID');
    actId=localStorage.getItem('activeIdnow');
    if(teamLeaderId==null){
        alert("登录信息异常")
        window.location.href='login.html'
    }
    if(actId==null){
        alert("活动状态异常")
        window.location.href='joinact.html'
    }
}



function submit(){
    teamCount=1;
    teamDesc=document.getElementById("teamintro").value
    teamName=document.getElementById("teamtitle").value
    teamTotal=document.getElementById("teampnum").value
    //console.log(teamTotal)

    if(teamDesc=="请描述小组"){
        teamDesc=null;
    }
    if(teamName=="请输入小组名称"){
        teamName=null;
    }
    if(teamDesc==null||teamName==null){
        alert("表单未正确填写")
    }else{
        var token = localStorage.getItem('token')
        console.log(token)
        axios({
            method: 'post',
            url: `${baseUrl}/api/team/add`,
            data:{
                aId: actId,
                tCount: teamCount,
                tDesc: teamDesc,
                tId: teamId,
                tLeaderId: teamLeaderId,
                tName: teamName,
                tTotal: teamTotal
            },
            headers: {
                "auth": token,
            }
        }).then(function (response) {
            console.log(response.data);
      
            backk()
    
          })
          .catch(function (error) {
            console.log(error);
          });
    }

    

    
}

function backk(){
    window.history.back(); 
    return false;
  }

