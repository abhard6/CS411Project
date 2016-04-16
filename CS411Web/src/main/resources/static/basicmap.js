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


window.onload = function() {
  var baseLayer = L.tileLayer(
    'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',{
      attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://cloudmade.com">CloudMade</a>',
      maxZoom: 18
    }
  );

    var objs = [];
    var newData = {
      max: 8,
      data: {"p1":{"lat": 24.6408, "lng":46.7728, "count": 3},
             "p2": {"lat": 50.75, "lng":-1.55, "count": 3}}
    };

  var cfg = {
    // radius should be small ONLY if scaleRadius is true (or small radius is intended)
    "radius": 2,
    "maxOpacity": .8,
    // scales the radius based on map zoom
    "scaleRadius": true,
    // if set to false the heatmap uses the global maximum for colorization
    // if activated: uses the data maximum within the current map boundaries
    //   (there will always be a red spot with useLocalExtremas true)
    "useLocalExtrema": true,
    // which field name in your data represents the latitude - default "lat"
    latField: 'lat',
    // which field name in your data represents the longitude - default "lng"
    lngField: 'lng',
    // which field name in your data represents the data value - default "value"
    valueField: 'count'
  };


  var heatmapLayer = new HeatmapOverlay(cfg);

  var map = new L.Map('map', {
    center: new L.LatLng(25.6586, -80.3568),
    zoom: 4,
    layers: [baseLayer, heatmapLayer]
  });

  heatmapLayer.setData(newData);
  layer = heatmapLayer; // Make available on the window.

  // TODO: Yinrui, map is not really working. 
  $("#search").click(function(e) {
      // TODO: Put actual results of search query as data
      data = {"p1":{"lat": 24.6408, "lng":46.7728, "count": 3},
                          "p2": {"lat": 50.75, "lng":-1.55, "count": 3}};
      layer.setData(data);
  });
};
