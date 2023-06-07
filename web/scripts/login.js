var userMajor = null;
var userId = null;
var userName = null;
var userPwd = null;
var userSchool = null;
var userSex = null;
var userStuNum = null;

const baseUrl = "http://localhost:8080"


function login() {
  var t = document.getElementById("id_bu").value
  var p = document.getElementById("pwd_bu").value
  
  
  localStorage.removeItem('token')
  var token = localStorage.getItem('token')
  console.log(token)


  let check = 0;
  if (token) {
    check = 1;
  }

  if (t.toString().length >= 5 && p.toString().length >= 5) {
    userId = t;
    userPwd = p;
   
    if (check) {
      axios.defaults.headers.common["auth"] = token;
    }
    axios.post(`${baseUrl}/api/user/auth?check=${check}`, {
      uId: userId,
      uMajor: userMajor,
      uName: userName,
      uPwd: userPwd,
      uSchool: userSchool,
      uSex: userSex,
      uStuNum: userStuNum
    })
      .then(function (response) {
        console.log(response);
        let token = response['headers']['auth'];
        if (token) {
          localStorage.setItem('token', token)
          localStorage.removeItem('userID');
          localStorage.setItem('userID',userId)
        }
        window.location.href = 'person.html'

      })
      .catch(function (error) {
        console.log(error);
      });


  }
  else {
    alert("id和密码必须都大于5位")
  }
}






