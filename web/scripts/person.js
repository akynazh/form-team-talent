
const baseUrl = "http://localhost:8080"
window.onload = auth
var myinfo=null;
var actlist=null;
var token=null;

var reqfromme=null;
var reqtome=null;

function auth() {
   //token获取
    token = localStorage.getItem('token')
    console.log(token)
    //获取个人信息
    axios({
        method: 'get',
        url: `${baseUrl}/api/user/get/info`,
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        //console.log(response.data);
        var  state=response.data.obj
        if(state==null){
            window.location.href='login.html'
        }else{
            myinfo=response.data.obj;
            //console.log(myinfo);
            if(myinfo.uSex=="男"){
                document.getElementById("femaletouxiang").style.display="none";
                document.getElementById("othertouxiang").style.display="none";
            }else if(myinfo.uSex=="女"){
                document.getElementById("maletouxiang").style.display="none";
                document.getElementById("othertouxiang").style.display="none";
            }else{
                document.getElementById("femaletouxiang").style.display="none";
                document.getElementById("maletouxiang").style.display="none";
            }
            document.getElementById("username").innerHTML=myinfo.uName;
            document.getElementById("userid").innerHTML=myinfo.uStuNum;
        }

      })
      .catch(function (error) {
        console.log(error);
      });


      //获取活动列表 和请求什么的 没写完
      axios({
        method: 'get',
        url: `${baseUrl}/api/activity/get/my`,
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        //console.log(response.data);
        actlist=response.data.obj

        var listunit1="";
        for(let i=0;i<actlist.length;i++){
            listunit1+= '<div class="list1"><a class="act-text" onclick="gotoactd('+'\''+actlist[i].aId+'\''+')">'+actlist[i].aName+'</a></div>'
        }
        
        document.getElementById("acthod").innerHTML=listunit1
      })
      .catch(function (error) {
        console.log(error);
      });

      axios({
        method: 'get',
        url: `${baseUrl}/api/team/get/my`,
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        //console.log(response.data);
        teamlist=response.data.obj

        var listunit2="";
        for(let i=0;i<teamlist.length;i++){
            listunit2+= '<div class="list1"><a class="act-text" onclick="gototeamd('+'\''+teamlist[i].tId+'\''+','+'\''+teamlist[i].aId+'\''+')">'+teamlist[i].tName+'</a></div>'
        }
        document.getElementById("gglist").innerHTML=listunit2
      })
      .catch(function (error) {
        console.log(error);
      });


      axios({
        method: 'get',
        url: `${baseUrl}/api/req/get/?type=${1}&status=${1}`,
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        console.log(response.data);
        reqtome=response.data.obj
        
        var listunit3="";
        for(let i=0;i<reqtome.length;i++){
            listunit3+= '<div class="list"><div class="act-text"><div>'+reqtome[i].uName+"&nbsp;"+'</div><div>请求加入</div><div>'+reqtome[i].tName+"&nbsp;"+'</div></div> <button class="bbbbtun" onclick="agreereq('+'\''+reqtome[i].id+'\''+')">同意</button><button class="bbbbtun" onclick="rejectreq('+'\''+reqtome[i].id+'\''+')">拒绝</button><details class="xiangqing"><summary></summary>  <div class="detainfohoder"><div class="ttext">请求时间：'+"&nbsp;"+ gedate(reqtome[i].sendDate)+'</div><div class="ttext">详细信息：'+"&nbsp;" +reqtome[i].content+'</div><div class="tttext">状态：'+"&nbsp;"+toS(reqtome[i].status,reqtome[i].agree)+'</div></div> </details></div>'  
        }
        document.getElementById("reqtomm").innerHTML=listunit3
      })
      .catch(function (error) {
        console.log(error);
      });


      axios({
        method: 'get',
        url: `${baseUrl}/api/req/get/?type=${0}&status=${1}`,
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        console.log(response.data);
        reqfromme=response.data.obj
        
        var listunit4="";
        for(let i=0;i<reqfromme.length;i++){
            listunit4+= '<div class="list"><div class="act-text"><div>'+reqfromme[i].uName+"&nbsp;"+'</div><div>请求加入</div><div>'+reqfromme[i].tName+"&nbsp;"+'</div></div><button class="bbbbtun" onclick="quash('+'\''+reqfromme[i].id+'\''+')">撤销</button><details class="xiangqing"><summary></summary>  <div class="detainfohoder"><div class="ttext">请求时间：'+"&nbsp;"+ gedate(reqfromme[i].sendDate)+'</div><div class="ttext">详细信息：'+"&nbsp;" +reqfromme[i].content+'</div><div class="tttext">状态：'+"&nbsp;"+toS(reqfromme[i].status,reqfromme[i].agree)+'</div></div> </details></div>'  
        }
        document.getElementById("reqfrommm").innerHTML=listunit4
      })
      .catch(function (error) {
        console.log(error);
      });
      
  
}

function gotoactd(acttid){
    localStorage.removeItem('activeIdnow');
    localStorage.setItem('activeIdnow',acttid);
    window.location.href='actdetail.html';
}
function gototeamd(teamid,actid){
    localStorage.removeItem('activeIdnow');
    localStorage.setItem('activeIdnow',actid);
    localStorage.removeItem('teamIdnow');
    localStorage.setItem('teamIdnow',teamid);
    window.location.href='groupdetail.html';
}
function toS(status,agree){
    var zhuangtai=null;
    if(status==1){
       zhuangtai="未处理"
    }else{
        if(agree==1){
            zhuangtai="同意"
        }else{
            zhuangtai="拒绝"
        }
}
return zhuangtai;
}

function gedate(t){
    let date1 = new Date(parseInt(t));
    let time=date1.toLocaleDateString().replace(/\//g, "-") + " " + date1.toTimeString();
    time=time.slice(0,time.length-21)
    return time;
}

function quash(reqid){
    axios({
        method: 'post',
        url: `${baseUrl}/api/req/remove/${reqid}`,
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        //console.log(response.data)
        window.location.href='person.html'
      })
      .catch(function (error) {
        console.log(error);
      });
}
function agreereq(reqid){
    axios({
        method: 'post',
        url: `${baseUrl}/api/req/handle/?id=${reqid}&agree=${1}`,
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        console.log(response.data)
        window.location.href='person.html'
      })
      .catch(function (error) {
        console.log(error);
      });
}

function rejectreq(reqid){
    axios({
        method: 'post',
        url: `${baseUrl}/api/req/handle/?id=${reqid}&agree=${0}`,
        headers: {
            "auth": token,
        }
    }).then(function (response) {
        console.log(response.data)
        window.location.href='person.html'
      })
      .catch(function (error) {
        console.log(error);
      });
}