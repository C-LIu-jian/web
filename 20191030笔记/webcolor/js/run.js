/**
 * 网页色彩演示脚本
 * @SparkSystemStudio 2019
 * tonylinx@qq.com
 */

var $body = document.querySelector("body"),
  $one = document.querySelector(".one"),
  $title = document.querySelector(".title"),
  $info = document.querySelector(".info"),
  $ins = document.querySelector(".ins"),
  $hex = document.querySelector(".hex"),
  $sht = document.querySelector(".sht"),
  $cvs = document.querySelectorAll(".cv"),
  $red = document.querySelector("#red"),
  $green = document.querySelector("#green"),
  $blue = document.querySelector("#blue"),
  show = false;
function d2h(s) {
  if (isNaN(s * 1)) {
    return "00";
  }
  return ("0" + (s * 1).toString(16)).slice(-2);
}
function set() {
  var r = $red.value * 1;
  var g = $green.value * 1;
  var b = $blue.value * 1;
  var color = "rgb(" + r + ", " + g + ", " + b + ")";
  var rr = d2h(r);
  var gg = d2h(g);
  var bb = d2h(b);
  var hex = "#" + d2h(r) + d2h(g) + d2h(b);
  var sht = "";
  if (rr[0] == rr[1] && gg[0] == gg[1] && bb[0] == bb[1]) {
    sht = "#" + rr[0] + "" + gg[0] + "" + bb[0];
  }
  $body.style.backgroundColor = color;
  $hex.innerHTML = hex;
  $sht.innerHTML = sht;
}
$one.onclick = function() {
  show = !show;
  if (show) {
    $title.style.display = "none";
    $info.style.display = "inline-block";
    $ins.style.display = "inline-block";
  } else {
    $red.value = "255";
    $green.value = "255";
    $blue.value = "255";
    set();
    $title.style.display = "inline-block";
    $info.style.display = "none";
    $ins.style.display = "none";
  }
};
for (var i = 0; i < $cvs.length; i++) {
  $cvs[i].onclick = function() {
    if (show) {
      var val = this.innerText;
      var r = parseInt(val.substr(1, 2), 16);
      var g = parseInt(val.substr(3, 2), 16);
      var b = parseInt(val.substr(5, 2), 16);
      console.log(r, g, b);
      $red.value = r;
      $green.value = g;
      $blue.value = b;
      set();
    }
  };
}
$red.onkeyup = $green.onkeyup = $blue.onkeyup = set;
set();
