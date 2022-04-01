var stompClient = null;

//소켓 연결되면 index에 있는 대화창 보여줌
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");    // 형진 sub/1  지민 sub/5
}

// 소켓 connect. 여기선 index에서 connect 버튼 누를 시 수동으로 연결됨. 우리는 아마 로그인 처리 이뤄지면 커넥트하는 방식으로?
function connect() {
    var socket = new SockJS("/api/ws");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        // /sub/chat을 구독하기 시작함. 이후 여기에 메세지 보내면 /sub/chat을 구독한 모든 사람들에게 보임
        // 우리는 /sub/{userId}로 구독하고, 댓글달면 해당 유저 id로 메세지 보내는 방식
        stompClient.subscribe('/sub/chat', function (chat) {
            showChat(JSON.parse(chat.body));
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
// id  보낸사람 받는사람 메세지, 읽음&안읽음
function sendChat() {
    stompClient.send("/pub/chat", {}, JSON.stringify({'name': $("#name").val(), 'message': $("#chatMessage").val()}));
}
// 채팅 보내면 여기서 비동기적으로 계속 받아오는 것 같음
function showChat(chat) {
    $("#greetings").append("<tr><td>" + chat.name + " : " + chat.message + "</td></tr>");
}

function sendName() {
    stompClient.send("/pub/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}
// 항상 비동기?로 대기중인 함수. 버튼 클릭이 일어나면 바로 js 함수들 실행시킴
$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#chatSend" ).click(function(){ sendChat(); });
});