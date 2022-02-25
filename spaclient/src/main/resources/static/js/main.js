function generateState(length) {
    let stateValue = "";
    let alphaNumericCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    let alphaNumericCharactersLength = alphaNumericCharacters.length;
    for (let i = 0; i < length; i++) {
        stateValue += alphaNumericCharacters.charAt(Math.floor(Math.random() * alphaNumericCharactersLength));
    }

    document.getElementById("stateValue").innerHTML = stateValue;
}

function generateCodeVerifier() {
    let returnValue = "";
    let randomByteArray = new Uint8Array(32);
    window.crypto.getRandomValues(randomByteArray);

    returnValue = base64urlencode(randomByteArray);

    document.getElementById("codeVerifierValue").innerHTML = returnValue;
}

async function generateCodeChallenge() {
    let codeChallengeValue = "";

    let codeVerifier = document.getElementById("codeVerifierValue").innerHTML;

    let textEncoder = new TextEncoder('US-ASCII');
    let encodedValue = textEncoder.encode(codeVerifier);
    let digest = await window.crypto.subtle.digest("SHA-256", encodedValue);

    codeChallengeValue = base64urlencode(Array.from(new Uint8Array(digest)));

    document.getElementById("codeChallengeValue").innerHTML = codeChallengeValue;
}

function base64urlencode(sourceValue) {
    let stringValue = String.fromCharCode.apply(null, sourceValue);
    let base64Encoded = btoa(stringValue);
    let base64urlEncoded = base64Encoded.replace(/\+/g, '-').replace(/\//g, '_').replace(/=/g, '');
    return base64urlEncoded;
}

function getAuthCode() {
    let state = document.getElementById("stateValue").innerHTML;
    let codeChallenge = document.getElementById("codeChallengeValue").innerHTML;

    let authorizationURL = "http://localhost:8080/realms/appsdeveloperblog/protocol/openid-connect/auth";
    authorizationURL += "?client_id=photo-app-PKCE-client";
    authorizationURL += "&response_type=code";
    authorizationURL += "&scope=openid";
    authorizationURL += "&redirect_uri=http://localhost:8181/authcodeReader.html";
    authorizationURL += "&state=" + state;
    authorizationURL += "&code_challenge=" + codeChallenge;
    authorizationURL += "&code_challenge_method=S256";

    window.open(authorizationURL, 'authorizationRequestWindow', 'width=800,height=600,left=200,top=200');
}

function postAuthorize(state, authCode) {
    let originalStateValue = document.getElementById("stateValue").innerHTML;

    if(state === originalStateValue) {
        requestTokens(authCode);
    } else {
        alert("Invalid state value received");
    }
}

function requestTokens(authCode) {
    let codeVerifier = document.getElementById("codeVerifierValue").innerHTML;
    let data = {
        "grant_type": "authorization_code",
        "client_id": "photo-app-PKCE-client",
        "code": authCode,
        "code_verifier": codeVerifier,
        "redirect_uri":"http://localhost:8181/authcodeReader.html"
    };

    $.ajax({
        beforeSend: function (request) {
            request.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        },
        type: "POST",
        url: "http://localhost:8080/realms/appsdeveloperblog/protocol/openid-connect/token",
        data: data,
        success: postRequestAccessToken,
        dataType: "json"
    });
}

function postRequestAccessToken(data, status, jqXHR) {
    document.getElementById("accessToken").innerHTML = data["access_token"];
}

function getInfoFromResourceServer(){

    let accessToken = document.getElementById("accessToken").innerHTML;

    $.ajax({
        beforeSend: function (request) {
            request.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
            request.setRequestHeader("Authorization", "Bearer " + accessToken);
        },
        type: "GET",
        url: "http://localhost:8089/users/status/check",
        success: postInfoFromAccessToken,
        dataType: "text"
    });
}

function postInfoFromAccessToken(data, status, jqXHR) {
    alert(data);
}
