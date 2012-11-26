#MBTiles Server

[![Build Status](https://travis-ci.org/EugeneBorshch/mbtiles-server.png)](https://travis-ci.org/EugeneBorshch/mbtiles-server)

Minimalistic and "easy to use" server for streaming [MBTiles](https://github.com/mapbox/mbtiles-spec) maps.

This app allows you to quickly deploy and stream maps created by [TileMill](http://mapbox.com/tilemill/) (or similiar) on your web site.

###Use case
This application should help in situations when custom looking maps are required
and Google, Bingo etc... maps can't help you with this.
So the suggested approach would be:

 * find the geographic data source (e.g. Open Street Maps)
 * style and create MBTiles database using [TileMill](http://mapbox.com/tilemill/docs/crashcourse/introduction/)
 * stream the database with **MBTiles Server**


##Live Demo

Live demo can be found here [here](http://mbtilesserver-eugeneborshch.rhcloud.com/example/html/browse.html)

Sample database that is used in the demo can be downloaded from [here](https://github.com/downloads/EugeneBorshch/mbtiles-server/Odessa.mbtiles)


##Features
 * Powered by [Play](http://www.playframework.org) and [Akka](http://akka.io) frameworks
 * Tile requests are handled asynchronously
 * Small code base
 * Can be easily integrated with map viewer libraries like [Leaflet.js](http://leafletjs.com)


##Installation & Usage

### Build 

* Download and install [Play framework](http://www.playframework.org/download) or [SBT](http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html)
* Clone this repository
* If you have SBT installed:   ``$ sbt clean compile stage`` 
* If you have Play installed:  ``$ play clean compile stage``

### Download
You can download compiled version from [here](https://github.com/downloads/EugeneBorshch/mbtiles-server/mbtiles-server-1.0.zip)

### Run
* If you've built from the sources: `$ cd target/`
* If you've downloaded already compiled version: unzip it and `$ cd mbtiles-server-1.0/`
* Run server `$ ./start -Ddb.default.url=jdbc:sqlite:<path to the mbtiles database>` 


By default server will start on **localhost** and **9000** port.
You can change this with the following params:

      -Dhttp.port=1234 -Dhttp.address=127.0.0.1

And the complete example:

      $ ./start -Ddb.default.url=jdbc:sqlite:/Users/eugene/maps/MyTestMap.mbtiles -Dhttp.port=80

###Usage
As soon as you have your server running it will listen for requests like:

     http://yourservename/tile/{col}/{row}/{level}

Where :

* `{col}` - tile column number
* `{row}` - tile row number
* `{level}` - zoom level

###Integration
You can integrate your map into the web page using [Leaflet.js](http://leafletjs.com) or similar libraries:
```html
<script type="text/javascript">

    // set up the map
    var map = new L.Map('map');

    // url to the tile server
    var url = 'http://localhost/tile/{x}/{y}/{z}';

    //tms:true is required for the TileMill databases
    var layer = new L.TileLayer(url, {minZoom:12, maxZoom:16, tms:true});

    // set the viewport
    map.setView(new L.LatLng(46.4741, 30.7405), 12);

    //add layer to the map
    map.addLayer(layer);

</script>
```

