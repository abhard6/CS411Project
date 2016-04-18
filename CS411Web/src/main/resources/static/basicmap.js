 var fill = d3.scale.category20();

function draw(words) {
  d3.select("#wordmap").append("svg")
    .attr("width", 500)
    .attr("height", 500)
    .append("g")
    .attr("transform", "translate(150,150)")
    .selectAll("text")
    .data(words)
    .enter().append("text")
    .style("font-size", function(d) { return d.size + "px"; })
    .style("font-family", "Impact")
    .style("fill", function(d, i) { return fill(i); })
    .attr("text-anchor", "middle")
    .attr("transform", function(d) {
      return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
    })
    .text(function(d) { return d.text; });
}

$("#dropdown-timespan").on('change',function(){
    var id=$(this).val();
    console.log(id);
});


$("#dropdown").on('change',function(){
    var id=$(this).val();

    $.ajax({
        url: "/timespan",
        method: 'GET',
        data: {"trend_chosen":id},
        //dataType: 'json',
        contentType: 'application/json',
        mimeType: 'application/json',
        success: function (response) {
            var select = document.getElementById("timespan");
            select.innerHTML = '';
            var days = new Array();
            for (var i=0; i<response.length; i++)
            	{
            		var r= response[i];
            		var d = (new Date(r.year, r.monthOfYear, r.dayOfMonth, r.hourOfDay, r.minuteOfHour, 0, 0));
                    days.push(d);
            		var el = document.createElement("option");
                    el.textContent = d;
                    el.value = d;
                    select.appendChild(el);
	
            	}
        }
	});

    
    //id="Amara";
	  $.ajax({
	        url: "/select",
	        method: 'GET',
	        data: {"trend_chosen":id},
	        //dataType: 'json',
	        contentType: 'application/json',
	        mimeType: 'application/json',
	        success: function (response) {
	            console.log(response);
	                        heatmapLayer.setData({max: 4,
                            data: response});
	            }
		});

  });


function drawUpdate(words){
    var total = 0;
  $.each(words, function(index, entry) {
    total+=entry['size'];
   });

   var avg = total/(words.length);

  //delete svg
  d3.selectAll('svg > g > *').remove();
  d3.layout.cloud().size([500, 500])
    .words(words)
    .padding(12)
    .rotate(function() { return ~~(Math.random() * 2) * 90; })
    .font("Impact")
    .fontSize(function(d) { return d.size; })
    .start();

  d3.select("svg")
    .selectAll("g")
    .attr("transform", "translate(150,150)")
    .selectAll("text")
    .data(words).enter().append("text")
    .style("font-size", function(d) { return 12 * Math.round(d.size/avg) + "px"; }) // scaled around 12px
    .style("font-family", "Impact")
    .style("fill", function(d, i) { return fill(i); })
    .attr("transform", function(d) {
      return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
    })
    .text(function(d) { return d.text; });
}

function showWordmapTab() {
    // show the wordmap tab
    $("#wordmap-tab").removeClass("hidden");
    $(".nav-tabs li").removeClass("active");
    $("#wordmap-tab").addClass("active");
    $("#main").addClass("hidden");
    $("#wordmap").removeClass("hidden");
}

function showMapTab() {
    // show the map tab
    $(".nav-tabs li").removeClass("active");
    $("#main-tab").addClass("active");
    $("#main").removeClass("hidden");
    $("#wordmap").addClass("hidden");
}

$("#wordmap-tab").click(function(e) {
    showWordmapTab();
});

$("#main-tab").click(function(e) {
    showMapTab();
});

$("#generate-wordmap").click(function(e) {

  bounds = map.getBounds();

  $.ajax({
    type: "POST",
    url: "/generate-wordmap",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    dataType: 'json',
    data: JSON.stringify({
      "latitudeTop": bounds.getNorth(),
      "latitudeBottom": bounds.getSouth(),
      "longitudeLeft": bounds.getWest(),
      "longitudeRight": bounds.getEast(),
      "trends": ["Raptors"]
    }),
    success: function(response) {
      d3.layout.cloud().size([500, 500])
        .words(response)
        .rotate(function() { return ~~(Math.random() * 2) * 90; })
        .font("Impact")
        .fontSize(function(d) { return d.size; })
        .on("end", drawUpdate)
        .start();

        showWordmapTab();
    }
  });
});

$(".select").change(function(e) {
	  if ( this.checked)
	  {
		  var id = $(e.target).parent().parent().find(".id")[0].value;
		  console.log(id);
		  $.ajax({
		        url: "/select",
		        method: 'GET',
		        data: {"trend_chosen":id},
		        //dataType: 'json',
		        contentType: 'application/json',
		        mimeType: 'application/json',
		        success: function (response) {
		            console.log(response);
		                        heatmapLayer.setData({max: 4,
                                data: response});
		            }
    		});
	  }
});

window.onload = function() {
	
	
  var baseLayer = L.tileLayer(
    'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',{
      attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://cloudmade.com">CloudMade</a>',
      maxZoom: 18
    }
  );

  var objs = [];

  var cfg = {
    // radius should be small ONLY if scaleRadius is true (or small radius is intended)
    "radius": .5,
    "scaleRadius": true,
    "minOpacity": .6,
    "maxOpacity": .6,
    // scales the radius based on map zoom
    "scaleRadius": true,
    // if set to false the heatmap uses the global maximum for colorization
    // if activated: uses the data maximum within the current map boundaries
    //   (there will always be a red spot with useLocalExtremas true)
    "useLocalExtrema": false,
    // which field name in your data represents the latitude - default "lat"
    latField: 'latitude',
    // which field name in your data represents the longitude - default "lng"
    lngField: 'longitude',
    // which field name in your data represents the data value - default "value"
    valueField: 'sentiment',

    gradient: {
      // enter n keys between 0 and 1 here
      // for gradient color customization
      '0': 'green',
      '0.5': 'blue',
      '1': 'red'
    },
    blur: 0
  };


  heatmapLayer = new HeatmapOverlay(cfg);

  map = new L.Map('map', {
    center: new L.LatLng(25.6586, -80.3568),
    zoom: 4,
    layers: [baseLayer, heatmapLayer]
  });

  draw([]);
};
