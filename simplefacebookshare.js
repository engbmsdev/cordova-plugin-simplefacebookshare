/*
    Plugin di prova per condivisione facebook via Facebook SDK Android
*/

var simpleFacebookShare = {
    share: function(title, text, url, imageUrl, successCallback, errorCallback) {
        if (typeof cordova === "undefined"){
            alert("SimpleFacebookShare JS: Cordova not loaded");
            return;
        }
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'SimpleFacebookSharePlugin', // mapped to our native Java class called "CalendarPlugin"
            'share', // with this action name
            [{                  // and this array of custom arguments to create our entry
                "title": title,
                "text": text,
                "url": url,
                "imageUrl": imageUrl
            }]
        ); 
    }
}