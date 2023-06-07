const baseUrl = "http://localhost:8080"
window.onload = auth


var token=null;
var actId=null;
var teamCount=null;
var teamintro=null;
var teamId=null;
var teamLeaderId=null;
var teamName=null;
var teamTotal=null;

function auth(){
    actId=localStorage.getItem('activeIdnow')
    token = localStorage.getItem('token')
    teamId=localStorage.getItem('teamIdnow')

    axios({
        method: 'get',
        url: `${baseUrl}/api/team/get/id?tId=${teamId}`,
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        console.log(response.data);
        var teaminfo=response.data.obj.team
       
        teamCount=teaminfo.tCount
        teamintro=teaminfo.tDesc
        teamLeaderId=teaminfo.tLeaderId
        teamName=teaminfo.tName
        teamTotal=teaminfo.tTotal
      //赋值
      document.getElementById("teamtitle").value=teamName
      document.getElementById("teamintro").value=teamintro
      document.getElementById("teamtotal").value=teamTotal
    })
      .catch(function (error) {
        console.log(error);
      });

}

function submit(){
    teamName=document.getElementById("teamtitle").value
    teamintro=document.getElementById("teamintro").value
    teamTotal=document.getElementById("teamtotal").value
    
    axios({
        method: 'post',
        url: `${baseUrl}/api/team/update`,
        data:{
            aId: actId,
            tCount: teamCount,
            tDesc: teamintro,
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
function backk(){
  window.history.back(); 
  return false;
}