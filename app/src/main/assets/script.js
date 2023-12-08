function takePicture() {
    navigator.camera.getPicture(onSuccess, onFail, {
        quality: 50,
        destinationType: Camera.DestinationType.DATA_URL
    });
}

function onSuccess(imageData) {
    var img = document.getElementById('myImage');
    img.src = "data:image/jpeg;base64," + imageData;
}

function onFail(message) {
    alert('Failed to take picture: ' message);
}