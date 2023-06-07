const baseUrl = "http://localhost:8080"
window.onload = auth
var actId=null;
var token=null;


function backk(){
  window.history.back(); 
  return false;
}

function auth(){
    actId=localStorage.getItem('activeIdnow')
    
    token = localStorage.getItem('token')



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
        //console.log(actdetail.activity)
        document.getElementById("acttitle").innerHTML=actdetail.activity.aName
        document.getElementById("actintro").innerHTML=actdetail.activity.aDesc
        if(actdetail.activity.aIsPublic==1){
            actdetail.activity.aIsPublic="公开"
        }else{
            actdetail.activity.aIsPublic="私有"
        }
        document.getElementById("actpro").innerHTML=actdetail.activity.aIsPublic
        
        
        ttt=parseInt(actdetail.activity.aEndDate)
        //console.log(ttt)
        let date1 = new Date(parseInt(ttt));
        let time=date1.toLocaleDateString().replace(/\//g, "-") + " " + date1.toTimeString();
        time=time.slice(0,time.length-21)
        document.getElementById("acttime").innerHTML=time;



        if(actdetail.owner!=true){
          document.getElementById("updaaact").style.display="none"
          document.getElementById("deletact").style.display="none"
          //登录其他账号验证（未验证）
        }
    })
      .catch(function (error) {
        console.log(error);
      });




      axios({
        method: 'get',
        url: `${baseUrl}/api/team/get/byAId?aId=${actId}`,
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        //console.log(response.data);
        var grouplist=response.data.obj
        console.log(grouplist)
        var listunit = "";
      for(let i=0;i<grouplist.length;i++){
        listunit+= '<a class="list" onclick="gogroupdetail('+'\''+grouplist[i].tId+'\''+')">'+grouplist[i].tName+'</a>';
      }
      document.getElementById("gglist").innerHTML = listunit;
    })
      .catch(function (error) {
        console.log(error);
      });


      
}

function gogroupdetail(ttId){
  localStorage.removeItem('teamIdnow');
  localStorage.setItem('teamIdnow',ttId);
  console.log(ttId)
  window.location.href='groupdetail.html';
}


function deleteact(){
    axios({
        method: 'post',
        url: `${baseUrl}/api/activity/remove?aId=${actId}`,
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        console.log(response.data);
        hidePopup();
        backk();
    })
      .catch(function (error) {
        console.log(error);
      });
}
function showPopup(){
    var overlay = document.getElementById("overlay");
    overlay.style.display = "block";
  }
  function hidePopup(){
    var overlay = document.getElementById("overlay");
    overlay.style.display = "none";
  }



  function upaaact(){
    localStorage.removeItem('activeIdnow');
    localStorage.setItem('activeIdnow',actId);
    window.location.href='updaact.html'
  }