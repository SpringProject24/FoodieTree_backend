var stompClient = null;

function setConnected(connected) {
  document.getElementById("connect").disabled = connected;
  document.getElementById("disconnect").disabled = !connected;
  if (connected) {
    document.getElementById("conversation").style.display = 'block';
  } else {
    document.getElementById("conversation").style.display = 'none';
  }
  document.getElementById("greetings").innerHTML = "";
}

function connect() {
  var socket = new SockJS('/gs-guide-websocket');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', function (greeting) {
      showGreeting(JSON.parse(greeting.body).content);
    });
  });
}

function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log("Disconnected");
}

function sendName() {
  var name = document.getElementById("name").value;
  stompClient.send("/app/hello", {}, JSON.stringify({'name': name}));
}

function showGreeting(message) {
  var greetings = document.getElementById("greetings");
  var newRow = document.createElement("tr");
  var newCell = document.createElement("td");
  newCell.textContent = message;
  newRow.appendChild(newCell);
  greetings.appendChild(newRow);
}

document.addEventListener("DOMContentLoaded", function() {
  document.querySelector("form").addEventListener('submit', function (e) {
    e.preventDefault();
  });
  document.getElementById("connect").addEventListener("click", function() { connect(); });
  document.getElementById("disconnect").addEventListener("click", function() { disconnect(); });
  document.getElementById("send").addEventListener("click", function() { sendName(); });
});
