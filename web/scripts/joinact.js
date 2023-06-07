const baseUrl = "http://localhost:8080"
window.onload = auth
var seacontent=null;
var publicactivelist=null;
function auth() {
   
    axios({
        method: 'get',
        url: `${baseUrl}/api/activity/get/pub`,
    }).then(function (response) {
       console.log(response);
       publicactivelist=response.data.obj
       var listunit = "";
       for (let i = 0; i < publicactivelist.length; i++) {
        if(publicactivelist[i].status==1&&publicactivelist[i].aIsPublic==1){
          listunit +='<div class="publist"><a onclick="goactdetail('+'\''+publicactivelist[i].aId+'\''+')">'+publicactivelist[i].aName+'</a></div>';
        }
     }
       //console.log(listunit)
       document.getElementById("publicactlist").innerHTML = listunit;
      })
      .catch(function (error) {
        console.log(error);
      });
}

function goactdetail(actId){
  localStorage.removeItem('activeIdnow');
  localStorage.setItem('activeIdnow',actId);
  window.location.href='actdetail.html';
}

function seaact(){
  document.getElementById("rhoder").style.display="block"
  seacontent=document.getElementById("searchact").value
  //console.log(seacontent)
  
  axios({
    method: 'get',
    url: `${baseUrl}/api/activity/search/${seacontent}`,
  }).then(function (response) {
   console.log(response);
   sealist=response.data.obj
   
   if(sealist.length!=0){
    document.getElementById("noneres").style.display="none"
    var listunit1 = "";
   for (let i = 0; i < sealist.length; i++) {
      listunit1 +='<div class="publist"><a onclick="goactdetail('+'\''+sealist[i].aId+'\''+')">'+sealist[i].aName+'</a></div>';
  }
   //console.log(listunit)
   document.getElementById("resulist").innerHTML = listunit1;
   }
  })
  .catch(function (error) {
    console.log(error);
  });
}