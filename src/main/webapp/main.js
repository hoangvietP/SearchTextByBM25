$( document ).ready(function() {
     sendInfoAs();
});
var countKQ=1;
var countTR=1;
var request;
 var count = 0;
 var url="";
 var a="";
var dt;
var data="";
var i=0;
var i1=0;
function sendInfo(){
countKQ=1;
countTR=1;
            i=0;
            var term =document.getElementById("term").value;
            var pt = document.getElementById("ve4");
            var  dn= document.getElementById("ve5");
            var ti = document.getElementById("ve3");
            var bl = document.getElementById("ve6");
            var  nq= document.getElementById("ve7");
            var tt = document.getElementById("ve8");
            var json = JSON.stringify({term:term,tdn:a,ti:ti.checked,pt:pt.checked,dn:dn.checked,bl:bl.checked,nq:nq.checked,tt:tt.checked});
                $.ajax({
                    type:'POST',
                    url:url,
                    data:'data='+json,
                    success: function (dtt) {
                    console.log(dtt);
                    dt=dtt;
                    countKQ=(dtt.length)/8;
                    if(countKQ<1){
                        countKQ=1;
                    }
                    document.getElementById("countTK").innerHTML="<h8 id='countTK' style='color:#11CCFF;padding-top:20px;'>"+dtt.length+" kết quả"+"</h8>";
                    setresult();
                    }
                })
            };

     function setCountRS(){
        countTR+=1;
        setresult();



     }
  function dowsetCountRS(){
     if(i>=16){
        i-=16;
        countTR-=1;
        setresult();

     }

          }
function sendInfoAs(){
    gettdn();
    url="/Aurora/search";
}
function setresult(){
         if(countKQ>=2 && countTR<parseInt(countKQ)){
            document.getElementById("next").innerHTML="<button style='background-color:#11CCFF;color:white;position:relative;left:250px;' onclick='setCountRS()'>Xem Tiếp >>></button>";
         }else {
            document.getElementById("next").innerHTML="";
         }

         if(countKQ>=2 && countTR>=2){
                     document.getElementById("pre").innerHTML="<button style='background-color:#11CCFF;color:white;position:relative;top:53px;left:50px;' onclick='dowsetCountRS()'><<< Quay Lại</button>";
          }else{
          document.getElementById("pre").innerHTML="";

          }

        document.getElementById("kq").innerHTML="<h8 style='color:#11CCFF;position:relative;top:30px;left:200px;'>"+countTR+"/"+parseInt(countKQ)+"</h8>";

        i1=i;
        data="";
            for (i;i< dt.length-1; i++) {
                            if(dt.length>8){
                                if(i==i1+8){
                                    break;
                                }
                            }
                            var nd= dt[i].nd.split(" ");
                            var nnd="";
                            if(dt[i].loaiTerm!="termPT"){
                            var countCi = 0;
                            if(nd.length>50){
                                countCi=50;
                            }else if(nd.length<=50){
                                countCi=nd.length;
                            }
                            var nndbm = dt[i].nd.split(dt[i].term);
                            if(nndbm.length==1){
                                nndbm = dt[i].nd.split(dt[i].term+",");
                                if(nndbm.length==1){
                                   nndbm = dt[i].nd.split(dt[i].term+".");
                                }
                            }
                            nnd=nndbm[0];
                            for(l=1;l<=nndbm.length-1;l++){
                                nnd=nnd+" "+"<h8 style='color:red'>"+dt[i].term+"</h8>"+nndbm[l];
                            }

                            var tdd= dt[i].td.split(dt[i].term);
                            var ndtd =tdd[0];
                            for(l=1;l<=tdd.length-1;l++){
                                ndtd=ndtd+" "+"<h8 style='color:red'>"+dt[i].term+"</h8>"+tdd[l];
                            }

                data=data+" <div style='padding-bottom:20px;'><a href='/Aurora/doc?url="+dt[i].path+"' style='text-decoration: none;'><h4 style='color:#11CCFF;display:block;text-overflow: ellipsis;overflow: hidden; white-space: nowrap;'>"+dt[i].loai+"</h4></a><h8>"+ndtd+"</h8><br><h8 style=''>"+nnd+"</h8></br><h8 style='position:relative;top:-5px;color: #666'>"+dt[i].date+"</h8></div>";
            }
            if(dt[i].loaiTerm=="termPT"){
            var nd1= dt[i].nd.split(" ");
                        var sty =0;
                        var countC=0;
                        if(nd1.length>30){
                            countC=30;
                        }else if(nd1.length<=30){
                            countC=nd1.length;
                        }
                        for(k = 0; k <=  dt[i].termPT.length-1; k++){
                            for(m = 0; m <= countC-1 ; m++){
                                if(nd1[m]==dt[i].termPT[k] || nd1[m]==dt[i].termPT[k]+"," || nd1[m]==dt[i].termPT[k]+"." ){
                                    nd1[m]=" "+"<h8 style='color:red'>"+dt[i].termPT[k]+"</h8>";
                                }
                            }
                       }
                       var tett="";
                        for(m=0;m<=nd1.length-1;m++){
                            tett=tett+" "+nd1[m];
                        }
                        console.log(tett);
                        nnd=tett;
          data=data+" <div style=''><a href='/Aurora/doc?url="+dt[i].path+"' style='text-decoration: none;'><h4 style='color:#11CCFF;display:block;text-overflow: ellipsis;overflow: hidden; white-space: nowrap;'>"+dt[i].loai+"</h4></a><h8 style='display:block;text-overflow: ellipsis;overflow: hidden; white-space: nowrap;''>"+dt[i].td+"</h8><br><h10 >"+nnd+"</h10></br><h8 style=';color: #666'>"+dt[i].date+"</h8></div>"
        }
            }
            document.getElementById("result").innerHTML=data;

}

function gettdn(){
            var varA = document.getElementById("term").value;
                $.ajax({
                    type:'GET',
                    url:"https://vi.wikipedia.org/w/api.php?action=query&&origin=*&format=json&generator=search&gsrnamespace=0&gsrlimit=3&gsrsearch=%27"+varA+"%27",
                    success: function (dt) {
                    console.log(dt+"");
                        a=JSON.stringify(dt);
                        sendInfo();
                    }
                })
            };