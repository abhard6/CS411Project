$("#generate-wordmap").click(function(e) {
    $.ajax({
        type: "POST",
        url: "/generate-wordmap",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        dataType: 'json',
        data: JSON.stringify({
            "latitudeTop": 45.0,
            "latitudeBottom": 35.0,
            "longitudeLeft": 110.0,
            "longitudeRight": 80.0,
            "trends": ["Raptors"]
        }),
        success: function(response) {
            console.log(response);
        }
    });
});