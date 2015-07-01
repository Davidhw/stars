<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/html5bp.css">
    <link rel="stylesheet" href="css/main.css">
  </head>
  <body>
     ${content}
     <!-- Again, we're serving up the unminified source for clarity. -->

     <FORM>
		<INPUT TYPE="button" onClick="history.go(0)" VALUE="Play again!">
	</FORM>
        
    <script> var canvasWidth = 320;
    		var canvasHeight = 320; </script>
	<canvas id="myCanvas" width="320" height="320" style="border:1px solid #d3d3d3;"> </canvas> 
	    
	<script src="js/jquery-2.1.1.js"></script>
	<!-- <script> console.log('${board}'); -->
	 <script> console.log("reloading board") </script>
	 <script> var board = JSON.parse('${board}') </script> 
     <script src="js/main.js"></script>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>
