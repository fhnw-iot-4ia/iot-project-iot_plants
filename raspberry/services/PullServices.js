var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;

var data = null;

var xhr = new XMLHttpRequest();
xhr.withCredentials = true;
let jsonData = null;

pullService = () => {
    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            return jsonData = JSON.parse(this.responseText)
        }
    });
}

xhr.open("GET", "https://api.thingspeak.com/channels/753425/status.json?api_key=UK7G4LQ8P9CYLH1A");
xhr.setRequestHeader("authorization", "Bearer UK7G4LQ8P9CYLH1A");

xhr.send(data);
pullService()
console.log(jsonData)