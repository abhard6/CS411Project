
<!DOCTYPE html>
<html>
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
  <!-- Latest compiled and minified CSS -->
  
  <style>
    .col-lg-1, .col-lg-10, .col-lg-11, .col-lg-12, .col-lg-2, .col-lg-3, .col-lg-4, .col-lg-5, .col-lg-6, .col-lg-7, .col-lg-8, .col-lg-9, .col-md-1, .col-md-10, .col-md-11, .col-md-12, .col-md-2, .col-md-3, .col-md-4, .col-md-5, .col-md-6, .col-md-7, .col-md-8, .col-md-9, .col-sm-1, .col-sm-10, .col-sm-11, .col-sm-12, .col-sm-2, .col-sm-3, .col-sm-4, .col-sm-5, .col-sm-6, .col-sm-7, .col-sm-8, .col-sm-9, .col-xs-1, .col-xs-10, .col-xs-11, .col-xs-12, .col-xs-2, .col-xs-3, .col-xs-4, .col-xs-5, .col-xs-6, .col-xs-7, .col-xs-8, .col-xs-9 {
    padding-right: 0px;
    padding-left: 0px;
    }

    </style>

  <body>
    <div class="container">
      <div class="row">
        <div class="pull-left col-xs-6">
            <h3>Admin Panel</h3>
        </div>

        <div style="margin-top:10px" class="pull-right col-xs-2">
          <button class="btn btn-large btn-success">Get Data</button>
          <button class="btn btn-large btn-danger">Stop Data</button>
        </div>
      </div>
      <div class="row" style="padding-top:20px">
        <div class="col-xs-1">
          <label>id</label>
        </div>

        <div class="col-xs-2">
          <label>timestamp</label>
        </div>
        <div class="col-xs-4">
          <label>content</label>
        </div>
        <div class="col-xs-1">
          <label>sentiment</label>
        </div>
        <div class="col-xs-1">
          <label>latitude</label>
        </div>
        <div class="col-xs-1">
          <label>longitude</label>
        </div>
        <div class="col-xs-1">
          <label>source</label>
        </div>
        <div class="text-center col-xs-1">
          <label>controls</label>
        </div>
      </div>
      <div class="row">
        <form role="form">
          <div class="form-group">
            <div class="col-xs-1">
              <input class="form-control" placeholder="id" type="text">
            </div>

            <div class="col-xs-2">
              <input class="form-control" type="text" placeholder="timestamp">
            </div>
            <div class="col-xs-4">
              <input class="form-control" type="text" placeholder="content">
            </div>
            <div class="col-xs-1">
              <input class="form-control" type="text" placeholder="sent">
            </div>
            <div class="col-xs-1">
              <input class="form-control" type="text" placeholder="lat">
            </div>
            <div class="col-xs-1">
              <input class="form-control" type="text" placeholder="long">
            </div>
            <div class="col-xs-1">
              <input class="form-control" type="text" placeholder="source">
            </div>
            <div class="col-xs-1">
              <button style="margin-left:10px" class="pull-right btn btn-danger btn-sm glyphicon glyphicon-remove"></button>
              <button class="pull-right btn btn-warning btn-sm glyphicon glyphicon-refresh"></button>
            </div>
        </form>
        </div>
      </div>
      <div class="row">
  </div></body>
</html>
